package com.algorithm;

import java.io.File;
import java.io.IOException;

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
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import com.trajectory.FloorIterator;
import com.trajectory.TrajectoryIterator;
import com.trajectory.TrajectoryProcessor;

public class NeuralNetwork {

	public static final int featureNumbers = 5;
	// Random number generator seed, for reproducability
	public static final int seed = 1234;
	
	// filename of the model
    private static final String MODEL_FILENAME = "/Kerja/trajectory-generator/data/rnn_train.zip";
    
    // filename of the previous version of the model (backup)
    private static final String BACKUP_MODEL_FILENAME = "/Kerja/trajectory-generator/data/rnn_train.bak.zip";

	// Tweak these to tune the dataset size = batchSize * totalBatches
	public static int batchSize = 10;
	public static int totalBatches = 100;
	public static int nEpochs = 20;

	// Tweak the number of hidden nodes
	public static final int numHiddenNodes = 128;

	// This is the size of the one hot vector
	public static int FEATURE_VEC_SIZE;
	private static ComputationGraph net;

	public static void main(String[] args) throws Exception {
		// Create List for holding Floor objects
		FloorIterator.encodeFloor();
		FloorIterator.encodeRoom();
		FEATURE_VEC_SIZE = FloorIterator.getFloorTotal()+FloorIterator.getRoomTotal()+1;
		
		// This is a custom iterator that returns MultiDataSets on each call of next -
		// More details in comments in the class
		TrajectoryIterator iterator = new TrajectoryIterator(seed, batchSize, totalBatches);

		File networkFile = new File(MODEL_FILENAME);
		if (networkFile.exists()) {
            System.out.println("Loading the existing network...");
            net = ComputationGraph.load(networkFile, true);
		} else {
			System.out.println("Create Computational Graph");
			createComputationalGraph();
		}

		// Train model:
		int iEpoch = 0;
		int testSize = 1;
		TrajectoryProcessor predictor = new TrajectoryProcessor(net);
		net.fit(iterator);
		while (iEpoch < nEpochs) {
			System.out.printf(
					"* = * = * = * = * = * = * = * = * = ** EPOCH %d ** = * = * = * = * = * = * = * = * = * = * = * = * = * =\n",
					iEpoch);
			MultiDataSet testData = iterator.generateTest(testSize);
			INDArray predictions = predictor.output(testData);
			evaluation(predictions, testData.getFeatures()[0], testData.getLabels()[0]);
			/*
			 * (Comment/Uncomment) the following block of code to (see/or not see) how the
			 * output of the decoder is fed back into the input during test time
			 */
			System.out.println("Printing stepping through the decoder for a minibatch of size three:");
			testData = iterator.generateTest(testSize);
			predictor.output(testData, true);
			System.out.println("\n* = * = * = * = * = * = * = * = * = ** EPOCH " + iEpoch
					+ " COMPLETE ** = * = * = * = * = * = * = * = * = * = * = * = * = * =");
			iEpoch++;
		}
	}

	public static void createComputationalGraph() {
		ComputationGraphConfiguration configuration = new NeuralNetConfiguration.Builder().weightInit(WeightInit.XAVIER)
				.updater(new Adam(0.25)).seed(seed).graphBuilder()
				// These are the two inputs to the computation graph
				.addInputs("additionIn", "sumOut")
				.setInputTypes(InputType.recurrent(FEATURE_VEC_SIZE), InputType.recurrent(FEATURE_VEC_SIZE))
				// The inputs to the encoder will have size = minibatch x featuresize x timesteps
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

		net = new ComputationGraph(configuration);
		net.init();
		net.setListeners(new ScoreIterationListener(1));
	}
	
    private void saveModel(File networkFile) throws IOException {
        System.out.println("Saving the model...");
        File backup = new File(BACKUP_MODEL_FILENAME);
        if (networkFile.exists()) {
            if (backup.exists()) {
                backup.delete();
            }
            networkFile.renameTo(backup);
        }
        ModelSerializer.writeModel(net, networkFile, true);
        System.out.println("Done.");
    }

	private static void evaluation(INDArray predictions, INDArray questions, INDArray answers) {

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
