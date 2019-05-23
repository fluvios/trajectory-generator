package cn.edu.zju.db.datagen.algorithm;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.transform.TransformProcess;
import org.datavec.api.transform.schema.Schema;
import org.datavec.api.writable.Writable;
import org.datavec.hadoop.records.reader.mapfile.MapFileSequenceRecordReader;
import org.datavec.spark.storage.SparkStorageUtils;
import org.datavec.spark.transform.SparkTransformExecutor;
import org.datavec.spark.transform.misc.StringToWritablesFunction;
import org.deeplearning4j.datasets.datavec.RecordReaderMultiDataSetIterator;
import org.deeplearning4j.nn.conf.BackpropType;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration.GraphBuilder;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.graph.rnn.DuplicateToTimeSeriesVertex;
import org.deeplearning4j.nn.conf.graph.rnn.LastTimeStepVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.EmbeddingLayer;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.joda.time.DateTimeZone;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import cn.edu.zju.db.datagen.trajectory.TrajectoryIterator;

public class NeuralNetwork {
	/**
	 * Dictionary that maps words into numbers.
	 */
	private final Map<String, Double> dict = new HashMap<>();

	/**
	 * Reverse map of {@link #dict}.
	 */
	private final Map<Double, String> revDict = new HashMap<>();

	/**
	 * The contents of the corpus. This is a list of sentences (each word of the
	 * sentence is denoted by a {@link java.lang.Double}).
	 */
	private final List<List<Double>> corpus = new ArrayList<>();

	private static final int HIDDEN_LAYER_WIDTH = 512;
	private static final int EMBEDDING_WIDTH = 128;
	private static final String MODEL_FILENAME = "/Kerja/trajectory-generator/data/rnn_train.zip";
	private static final String BACKUP_MODEL_FILENAME = "/Kerja/trajectory-generator/data/rnn_train.bak.zip";
	private static final int MINIBATCH_SIZE = 32;
	private static final Random rnd = new Random(new Date().getTime());
	private static final long SAVE_EACH_MS = TimeUnit.MINUTES.toMillis(5);
	private static final long TEST_EACH_MS = TimeUnit.MINUTES.toMillis(1);
	private static final int MAX_DICT = 20000;
	private static final int TBPTT_SIZE = 25;
	private static final double LEARNING_RATE = 1e-1;
	private static final double RMS_DECAY = 0.95;
	private static final int ROW_SIZE = 40;

	/**
	 * The delay between invocations of {@link java.lang.System#gc()} in
	 * milliseconds. If VRAM is being exhausted, reduce this value. Increase
	 * this value to yield better performance.
	 */
	private static final int GC_WINDOW = 2000;

	private static final int MACROBATCH_SIZE = 20; // see CorpusIterator

	/**
	 * The computation graph model.
	 */
	private ComputationGraph net;
	private JavaRDD<List<List<Writable>>> trainSequence;
	private JavaRDD<List<List<Writable>>> testSequence;
	private File trainFiles = new File("/Kerja/trajectory-generator/data/spark-training/");
	private File testFiles = new File("/Kerja/trajectory-generator/data/spark-testing/");;
	
	public void read(String location) {
		Nd4j.getMemoryManager().setAutoGcWindow(GC_WINDOW);
		
		// read all trajectory data from folder
		File folder = new File(location);
		File[] listOfFiles = folder.listFiles();
		
		// We'll use Spark local to handle our data
		SparkConf conf = new SparkConf();
		conf.setMaster("local[*]");
		conf.setAppName("Trajectory-LSTM");

		JavaSparkContext sc = new JavaSparkContext(conf);

		for (File file : listOfFiles) {
			System.out.println("---" + file.getName() + "---");
			try {
				// read(file);
				transform(file, sc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Train the network
		try {
	        File networkFile = new File(toTempPath(MODEL_FILENAME));
	        if (networkFile.exists()) {
				train();	        		        	
	        }else {
	        	createComputationGraph();
				train();	        	
	        }
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void transform(File file, JavaSparkContext sc) throws Exception {

		// =====================================================================
		// Step 1: Define the input data schema
		// =====================================================================

		// Let's define the schema of the data that we want to import
		Schema inputDataSchema = new Schema.Builder().addColumnInteger("floorId").addColumnInteger("partitionId")
				.addColumnDouble("axisPos").addColumnDouble("ordinatePos").addColumnsString("Timestamp").build();

		// Print out the schema:
		System.out.println("Input data schema details:");
		System.out.println(inputDataSchema);

		System.out.println("\n\nOther information obtainable from schema:");
		System.out.println("Number of columns: " + inputDataSchema.numColumns());
		System.out.println("Column names: " + inputDataSchema.getColumnNames());
		System.out.println("Column types: " + inputDataSchema.getColumnTypes());

		// =====================================================================
		// Step 2: Define the operations we want to do
		// =====================================================================

		// Lets define some operations to execute on the data...
		TransformProcess tp = new TransformProcess.Builder(inputDataSchema)
				.removeAllColumnsExceptFor("Timestamp","axisPos","ordinatePos")
//				.stringToTimeTransform("Timestamp", "yy/MM/dd HH:mm:ss", DateTimeZone.UTC)
//				.renameColumn("Timestamp", "DateTime")
//				.removeColumns("DateTime")
				.convertToSequence().build();

		Schema outputSchema = tp.getFinalSchema();

		System.out.println("\n\n\nSchema after transforming data:");
		System.out.println(outputSchema);

		// Define the path to the data file. You could use a directory here if
		String path = file.getAbsolutePath();
		JavaRDD<String> data = sc.textFile(path);

		// We first need to parse this format. It's comma-delimited (CSV)
		// format, so let's parse it using CSVRecordReader:
		RecordReader rr = new CSVRecordReader();
		JavaRDD<List<Writable>> parsedInputData = data.map(new StringToWritablesFunction(rr));

		// Now, let's execute the transforms we defined earlier:
		JavaRDD<List<List<Writable>>> records = SparkTransformExecutor.executeToSequence(parsedInputData, tp);

		
		double[] weights = {0.8,0.2};
		JavaRDD<List<List<Writable>>>[] split = records.randomSplit(weights);	

		trainSequence = split[0];
		testSequence = split[1];
		
		// For the sake of this example, let's collect the data locally and
		// print it:
		// JavaRDD<String> processedAsString = records.map(new
		// WritablesToStringFunction(","));
		// records.saveAsTextFile("/Fachri/dvita/data/spark/" + System.currentTimeMillis());
		
		if(!trainFiles.exists()) {SparkStorageUtils.saveMapFileSequences(trainFiles.getAbsolutePath(), trainSequence);}
		if(!testFiles.exists()) {SparkStorageUtils.saveMapFileSequences(testFiles.getAbsolutePath(), testSequence);}
		
		System.out.println("\n\nDONE");
	}

	/**
	 * Configure and initialize the computation graph. This is done once in the
	 * beginning to prepare the {@link #net} for training.
	 */
	private void createComputationGraph() {
		final NeuralNetConfiguration.Builder builder = new NeuralNetConfiguration.Builder()
				.updater(new RmsProp(LEARNING_RATE)).weightInit(WeightInit.XAVIER)
				.gradientNormalization(GradientNormalization.RenormalizeL2PerLayer);

		final GraphBuilder graphBuilder = builder.graphBuilder().pretrain(false).backprop(true)
				.backpropType(BackpropType.Standard).tBPTTBackwardLength(TBPTT_SIZE).tBPTTForwardLength(TBPTT_SIZE)
				.addInputs("inputLine", "decoderInput")
				.setInputTypes(InputType.recurrent(2), InputType.recurrent(3))
				.addLayer("embeddingEncoder",
						new EmbeddingLayer.Builder().nIn(2).nOut(EMBEDDING_WIDTH).build(), "inputLine")
				.addLayer("encoder",
						new LSTM.Builder().nIn(EMBEDDING_WIDTH).nOut(HIDDEN_LAYER_WIDTH).activation(Activation.TANH)
								.build(),
						"embeddingEncoder")
				.addVertex("thoughtVector", new LastTimeStepVertex("inputLine"), "encoder")
				.addVertex("dup", new DuplicateToTimeSeriesVertex("decoderInput"), "thoughtVector")
				.addVertex("merge", new MergeVertex(), "decoderInput", "dup")
				.addLayer("decoder",
						new LSTM.Builder().nIn(3 + HIDDEN_LAYER_WIDTH).nOut(HIDDEN_LAYER_WIDTH)
								.activation(Activation.TANH).build(),
						"merge")
				.addLayer("output",
						new RnnOutputLayer.Builder().nIn(HIDDEN_LAYER_WIDTH).nOut(3)
								.activation(Activation.SOFTMAX).lossFunction(LossFunctions.LossFunction.MCXENT).build(),
						"decoder")
				.setOutputs("output");

		net = new ComputationGraph(graphBuilder.build());
		net.init();
	}

	private void train() throws IOException, InterruptedException {
		MapFileSequenceRecordReader trainRR = new MapFileSequenceRecordReader();
		trainRR.initialize(new FileSplit(trainFiles));
		RecordReaderMultiDataSetIterator trainIter = new RecordReaderMultiDataSetIterator
															.Builder(MINIBATCH_SIZE)
															.addSequenceReader("records", trainRR)
															.addInput("records")
												            .build();
		for (int epoch = 1; epoch < 10000; ++epoch) {
			System.out.println("Epoch " + epoch);
			net.fit(trainIter);
		}
	}

	private void test() {
		System.out.println("======================== TEST ========================");
		int selected = rnd.nextInt(corpus.size());
		List<Double> rowIn = new ArrayList<>(corpus.get(selected));
		System.out.print("In: ");
		for (Double idx : rowIn) {
			System.out.print(revDict.get(idx) + " ");
		}
		System.out.println("====================== TEST END ======================");
	}

	private void saveModel(File networkFile) throws IOException {
		System.out.println("Saving the model...");
		File backup = new File(toTempPath(BACKUP_MODEL_FILENAME));
		if (networkFile.exists()) {
			if (backup.exists()) {
				backup.delete();
			}
			networkFile.renameTo(backup);
		}
		ModelSerializer.writeModel(net, networkFile, true);
		System.out.println("Done.");
	}

	private String toTempPath(String path) {
		return System.getProperty("java.io.tmpdir") + "/" + path;
	}
}