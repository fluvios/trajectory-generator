package com.trajectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.dataset.api.MultiDataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

@SuppressWarnings("serial")
public class TrajectoryIterator implements MultiDataSetIterator {

	private MultiDataSetPreProcessor preProcessor;
	private Random randnumG;
	private final int seed;
	private final int batchSize;
	private final int totalBatches;

	private static int numDigits = 3; // Maximum number of room code
	public static int SEQ_VECTOR_DIM = FloorIterator.getFloorTotal() + FloorIterator.getRoomTotal()+1; // Plus separator char
	
    public static final Map<String, Integer> oneHotMap = new IdentityHashMap<String, Integer>();
    public static final String[] oneHotOrder = new String[SEQ_VECTOR_DIM];

//	public static Map<Integer, String> oneHotMap = new HashMap<Integer, String>();
//	public static String[] oneHotOrder = new String[FloorIterator.getFloorTotal()+FloorIterator.getRoomTotal()];
//	public static List<Integer[]> oneHotBinary;
	    
	private Set<String> seenSequences = new HashSet<String>();
	private boolean toTestSet = false;
	private int currentBatch = 0;

	private static final String COMMA_DELIMITER = ",";

	public TrajectoryIterator(int seed, int batchSize, int totalBatches) {

		this.seed = seed;
		this.randnumG = new Random(seed);

		this.batchSize = batchSize;
		this.totalBatches = totalBatches;
		
		// Need to fix
		oneHotEncoding();
	}

	public MultiDataSet generateTest(int testSize) {
		toTestSet = true;
		MultiDataSet testData = next(testSize);
		reset();
		return testData;
	}

	@Override
	public MultiDataSet next(int sampleSize) {
		INDArray encoderSeq, decoderSeq, outputSeq;

		List<INDArray> encoderSeqList = new ArrayList<>();
		List<INDArray> decoderSeqList = new ArrayList<>();
		List<INDArray> outputSeqList = new ArrayList<>();
		
		for (Entry<String, Integer> o : oneHotMap.entrySet()) {
			// Push the array into list
			encoderSeqList.add(mapToOneHot(o.getKey()));
			decoderSeqList.add(mapToOneHot(o.getKey()));
			outputSeqList.add(mapToOneHot(o.getKey()));
		}

		encoderSeq = Nd4j.vstack(encoderSeqList);
		decoderSeq = Nd4j.vstack(decoderSeqList);
		outputSeq = Nd4j.vstack(outputSeqList);
		
		INDArray[] inputs = new INDArray[] { encoderSeq, decoderSeq };
		INDArray[] inputMasks = new INDArray[] { Nd4j.ones(1, 3),
				Nd4j.ones(1, 3) };
		INDArray[] labels = new INDArray[] { outputSeq };
		INDArray[] labelMasks = new INDArray[] { Nd4j.ones(1, 3) };
		
		return new org.nd4j.linalg.dataset.MultiDataSet(inputs, labels, inputMasks, labelMasks);
	}

	@Override
	public void reset() {
		currentBatch = 0;
		toTestSet = false;
		seenSequences = new HashSet<String>();
		randnumG = new Random(seed);
	}

	@Override
	public boolean resetSupported() {
		return true;
	}

	@Override
	public boolean asyncSupported() {
		return false;
	}

	@Override
	public boolean hasNext() {
		return currentBatch < totalBatches;
	}

	@Override
	public MultiDataSet next() {
		return next(batchSize);
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported");
	}

	@Override
	public MultiDataSetPreProcessor getPreProcessor() {
		return null;
	}

	/*
	 * Takes in an array of strings and return a one hot encoded array of size 1 x
	 * (Floor+Room) x length of code Each element in the array indicates a time step Length of one
	 * hot vector = Floor+Room
	 */
    private static INDArray mapToOneHot(String toEncode) {
        INDArray ret = Nd4j.zeros(1, SEQ_VECTOR_DIM, 3);
        String [] arrOfVal = toEncode.split(":", 3); 
        
        for (int i = 0; i < SEQ_VECTOR_DIM; i++) {
        	for (int j = 0; j < arrOfVal.length; j++) {
        		// check if its floor
        		if(i == Integer.parseInt(arrOfVal[0])) {
                	ret.putScalar(0, i, 0, 1);
        		}
        		
        		// check if its room
        		if(i == Integer.parseInt(arrOfVal[0])) {
                	ret.putScalar(0, i, 2, 1);
        		}        		
			}
		}
        
    	ret.putScalar(0, SEQ_VECTOR_DIM-1, 1, 1);
        
        return ret;
    }

	public static String mapToString(INDArray encodeSeq, INDArray decodeSeq) {
		return mapToString(encodeSeq, decodeSeq, " --> ");
	}

	public static String mapToString(INDArray encodeSeq, INDArray decodeSeq, String sep) {
		String ret = "";
		String[] encodeSeqS = oneHotDecode(encodeSeq);
		String[] decodeSeqS = oneHotDecode(decodeSeq);
		for (int i = 0; i < encodeSeqS.length; i++) {
			ret += "\t" + encodeSeqS[i] + sep + decodeSeqS[i] + "\n";
		}
		return ret;
	}

	/*
	 * Helper method that takes in a one hot encoded INDArray and returns an
	 * interpreted array of strings toInterpret size batchSize x
	 * one_hot_vector_size(14) x time_steps
	 */
	public static String[] oneHotDecode(INDArray toInterpret) {

		String[] decodedString = new String[(int) toInterpret.size(0)];
		INDArray oneHotIndices = Nd4j.argMax(toInterpret, 1); // drops a dimension, so now a two dim array of shape
																// batchSize x time_steps
		for (int i = 0; i < oneHotIndices.size(0); i++) {
			int[] currentSlice = oneHotIndices.slice(i).dup().data().asInt(); // each slice is a batch
			decodedString[i] = mapFromOneHot(currentSlice);
		}
		return decodedString;
	}

	private static String mapFromOneHot(int[] toMap) {
		String ret = "";
		for (int i = 0; i < toMap.length; i++) {
			ret += oneHotOrder[toMap[i]];
		}
		// encoder sequence, needs to be reversed
		if (toMap.length > numDigits + 1 + 1) {
			return new StringBuilder(ret).reverse().toString();
		}
		return ret;
	}

	/*
	 * One hot encoding map
	 */
	private static void oneHotEncoding() {
		BufferedReader br = null;
		List<TrajectoryParser> trajectories = new ArrayList<TrajectoryParser>();
		try {
			// Reading the csv file
			br = new BufferedReader(new FileReader("/Kerja/trajectory-generator/data/dataset/Dest_Traj_186.csv"));

			String line = "";
			// Read to skip the header
			br.readLine();
			// Reading from the second line
			while ((line = br.readLine()) != null) {
				String[] trajectDetails = line.split(COMMA_DELIMITER);
				trajectories.add(new TrajectoryParser(trajectDetails[0], trajectDetails[1],
						Double.parseDouble(trajectDetails[2]), Double.parseDouble(trajectDetails[3])));
			}

			// integer encode input data
			for (int i = 0; i < trajectories.size(); i++) {
				oneHotMap.put(FloorIterator.getBinary(trajectories.get(i).getFloor(), trajectories.get(i).getRoom()),i);
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setPreProcessor(MultiDataSetPreProcessor preProcessor) {
		this.preProcessor = preProcessor;
	}

	public static int[] combineInt(int[] a, int[] b) {
		int length = a.length + b.length;
		int[] result = new int[length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
}
