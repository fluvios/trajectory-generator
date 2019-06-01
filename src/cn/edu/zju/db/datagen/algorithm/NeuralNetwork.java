package cn.edu.zju.db.datagen.algorithm;

import java.io.File;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.rnn.DuplicateToTimeSeriesVertex;
import org.deeplearning4j.nn.conf.graph.rnn.LastTimeStepVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.eclipse.swt.internal.C;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.NDArrayIndex;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import cn.edu.zju.db.datagen.trajectory.TrajectoryIterator;
import cn.edu.zju.db.datagen.trajectory.TrajectoryProcessor;

public class NeuralNetwork {

	public static final int featureNumbers = 5;
	// Random number generator seed, for reproducability
	public static final int seed = 1234;

	// Tweak these to tune the dataset size = batchSize * totalBatches
	public static int batchSize = 10;
	public static int totalBatches = 500;
	public static int nEpochs = 20;

	// Tweak the number of hidden nodes
	public static final int numHiddenNodes = 128;

	// This is the size of the one hot vector
	public static final int FEATURE_VEC_SIZE = 14;

	public static void main(String[] args) throws Exception {

		// This is a custom iterator that returns MultiDataSets on each call of next -
		// More details in comments in the class
		TrajectoryIterator iterator = new TrajectoryIterator(seed, batchSize, totalBatches);

		ComputationGraphConfiguration configuration = new NeuralNetConfiguration.Builder().weightInit(WeightInit.XAVIER)
				.updater(new Adam(0.25)).seed(seed).graphBuilder()
				// These are the two inputs to the computation graph
				.addInputs("additionIn", "sumOut")
				.setInputTypes(InputType.recurrent(FEATURE_VEC_SIZE), InputType.recurrent(FEATURE_VEC_SIZE))
				// The inputs to the encoder will have size = minibatch x featuresize x
				// timesteps
				// Note that the network only knows of the feature vector size. It does not know
				// how many time steps unless it sees an instance of the data
				.addLayer("encoder",
						new LSTM.Builder().nIn(FEATURE_VEC_SIZE).nOut(numHiddenNodes).activation(Activation.TANH)
								.build(),
						"additionIn")
				// Create a vertex indicating the very last time step of the encoder layer needs
				// to be directed to other places in the comp graph
				.addVertex("lastTimeStep", new LastTimeStepVertex("additionIn"), "encoder")
				// Create a vertex that allows the duplication of 2d input to a 3d input
				// In this case the last time step of the encoder layer (viz. 2d) is duplicated
				// to the length of the timeseries "sumOut" which is an input to the comp graph
				// Refer to the javadoc for more detail
				.addVertex("duplicateTimeStep", new DuplicateToTimeSeriesVertex("sumOut"), "lastTimeStep")
				// The inputs to the decoder will have size = size of output of last timestep of
				// encoder (numHiddenNodes) + size of the other input to the comp graph,sumOut
				// (feature vector size)
				.addLayer("decoder",
						new LSTM.Builder().nIn(FEATURE_VEC_SIZE + numHiddenNodes).nOut(numHiddenNodes)
								.activation(Activation.SOFTSIGN).build(),
						"sumOut", "duplicateTimeStep")
				.addLayer("output",
						new RnnOutputLayer.Builder().nIn(numHiddenNodes).nOut(FEATURE_VEC_SIZE)
								.activation(Activation.SOFTMAX).lossFunction(LossFunctions.LossFunction.MCXENT).build(),
						"decoder")
				.setOutputs("output").build();

		ComputationGraph net = new ComputationGraph(configuration);
		net.init();
		net.setListeners(new ScoreIterationListener(1));

		// Train model:
		int iEpoch = 0;
		int testSize = 100;
		TrajectoryProcessor predictor = new TrajectoryProcessor(net);
		while (iEpoch < nEpochs) {
			net.fit(iterator);
			System.out.printf(
					"* = * = * = * = * = * = * = * = * = ** EPOCH %d ** = * = * = * = * = * = * = * = * = * = * = * = * = * =\n",
					iEpoch);
			MultiDataSet testData = iterator.generateTest(testSize);
			INDArray predictions = predictor.output(testData);
			encode_decode_eval(predictions, testData.getFeatures()[0], testData.getLabels()[0]);
			/*
			 * (Comment/Uncomment) the following block of code to (see/or not see) how the
			 * output of the decoder is fed back into the input during test time
			 */
			System.out.println("Printing stepping through the decoder for a minibatch of size three:");
			testData = iterator.generateTest(3);
			predictor.output(testData, true);
			System.out.println("\n* = * = * = * = * = * = * = * = * = ** EPOCH " + iEpoch
					+ " COMPLETE ** = * = * = * = * = * = * = * = * = * = * = * = * = * =");
			iEpoch++;
		}

	}

	private static void encode_decode_eval(INDArray predictions, INDArray questions, INDArray answers) {

		int nTests = (int) predictions.size(0);
		int wrong = 0;
		int correct = 0;
		String[] questionS = TrajectoryIterator.oneHotDecode(questions);
		String[] answersS = TrajectoryIterator.oneHotDecode(answers);
		String[] predictionS = TrajectoryIterator.oneHotDecode(predictions);
		for (int iTest = 0; iTest < nTests; iTest++) {
			if (!answersS[iTest].equals(predictionS[iTest])) {
				System.out.println(questionS[iTest] + " gives " + predictionS[iTest] + " != " + answersS[iTest]);
				wrong++;
			} else {
				System.out.println(questionS[iTest] + " gives " + predictionS[iTest] + " == " + answersS[iTest]);
				correct++;
			}
		}
		double randomAcc = Math.pow(10, -1 * (featureNumbers + 1)) * 100;
		System.out.println("WRONG: " + wrong);
		System.out.println("CORRECT: " + correct);
		System.out.println(
				"Note randomly guessing digits in succession gives lower than a accuracy of:" + randomAcc + "%");
		System.out.println("The digits along with the spaces have to be predicted\n");
	}
}
