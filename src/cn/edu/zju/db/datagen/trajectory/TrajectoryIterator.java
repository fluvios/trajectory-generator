package cn.edu.zju.db.datagen.trajectory;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.MultiDataSet;
import org.nd4j.linalg.dataset.api.MultiDataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.MultiDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import cn.edu.zju.db.datagen.algorithm.NeuralNetwork;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

@SuppressWarnings("serial")
public class TrajectoryIterator implements MultiDataSetIterator {

	private MultiDataSetPreProcessor preProcessor;
	private Random randnumG;
	private final int seed;
	private final int batchSize;
	private final int totalBatches;

	private static int numDigits = NeuralNetwork.featureNumbers;
	public static int SEQ_VECTOR_DIM = 0;
	public static Map<Integer, String> oneHotMap = new HashMap<Integer, String>();
	public static List<Integer[]> oneHotBinary;
	public static String[] oneHotOrder = new String[SEQ_VECTOR_DIM];

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
		return null;
//		INDArray encoderSeq, decoderSeq, outputSeq;
//		int currentCount = 0;
//		int num1, num2;
//		List<INDArray> encoderSeqList = new ArrayList<>();
//		List<INDArray> decoderSeqList = new ArrayList<>();
//		List<INDArray> outputSeqList = new ArrayList<>();
//		while (currentCount < sampleSize) {
//			while (true) {
//				num1 = randnumG.nextInt((int) Math.pow(10, numDigits));
//				num2 = randnumG.nextInt((int) Math.pow(10, numDigits));
//				String forSum = String.valueOf(num1) + "+" + String.valueOf(num2);
//				if (seenSequences.add(forSum)) {
//					break;
//				}
//			}
//			String[] encoderInput = prepToString(num1, num2);
//			encoderSeqList.add(mapToOneHot(encoderInput));
//
//			String[] decoderInput = prepToString(num1 + num2, true);
//			if (toTestSet) {
//				// wipe out everything after "go"; not necessary since we do not use these at
//				// test time but here for clarity
//				int i = 1;
//				while (i < decoderInput.length) {
//					decoderInput[i] = " ";
//					i++;
//				}
//			}
//			decoderSeqList.add(mapToOneHot(decoderInput));
//
//			String[] decoderOutput = prepToString(num1 + num2, false);
//			outputSeqList.add(mapToOneHot(decoderOutput));
//			currentCount++;
//		}
//
//		encoderSeq = Nd4j.vstack(encoderSeqList);
//		decoderSeq = Nd4j.vstack(decoderSeqList);
//		outputSeq = Nd4j.vstack(outputSeqList);
//
//		INDArray[] inputs = new INDArray[] { encoderSeq, decoderSeq };
//		INDArray[] inputMasks = new INDArray[] { Nd4j.ones(sampleSize, numDigits * 2 + 1),
//				Nd4j.ones(sampleSize, numDigits + 1 + 1) };
//		INDArray[] labels = new INDArray[] { outputSeq };
//		INDArray[] labelMasks = new INDArray[] { Nd4j.ones(sampleSize, numDigits + 1 + 1) };
//		currentBatch++;
//		return new org.nd4j.linalg.dataset.MultiDataSet(inputs, labels, inputMasks, labelMasks);
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
	 * Helper method for encoder input Given two numbers, num1 and num, returns a
	 * string array which represents the input to the encoder RNN Note that the
	 * string is padded to the correct length and reversed Eg. num1 = 7, num 2 = 13
	 * will return {"3","1","+","7"," "}
	 */
	public String[] prepToString(int num1, int num2) {

		String[] encoded = new String[numDigits * 2 + 1];
		String num1S = String.valueOf(num1);
		String num2S = String.valueOf(num2);
		// padding
		while (num1S.length() < numDigits) {
			num1S = " " + num1S;
		}
		while (num2S.length() < numDigits) {
			num2S = " " + num2S;
		}

		String sumString = num1S + "+" + num2S;

		for (int i = 0; i < encoded.length; i++) {
			encoded[(encoded.length - 1) - i] = Character.toString(sumString.charAt(i));
		}

		return encoded;

	}

	/*
	 * Helper method for decoder input when goFirst for decoder output when !goFirst
	 * Given a number, return a string array which represents the decoder input (or
	 * output) given goFirst (or !goFirst) eg. For numDigits = 2 and sum = 31 if
	 * goFirst will return {"go","3","1", " "} if !goFirst will return
	 * {"3","1"," ","eos"}
	 */
	public String[] prepToString(int sum, boolean goFirst) {
		int start, end;
		String[] decoded = new String[numDigits + 1 + 1];
		if (goFirst) {
			decoded[0] = "Go";
			start = 1;
			end = decoded.length - 1;
		} else {
			start = 0;
			end = decoded.length - 2;
			decoded[decoded.length - 1] = "End";
		}

		String sumString = String.valueOf(sum);
		int maxIndex = start;
		// add in digits
		for (int i = 0; i < sumString.length(); i++) {
			decoded[start + i] = Character.toString(sumString.charAt(i));
			maxIndex++;
		}

		// needed padding
		while (maxIndex <= end) {
			decoded[maxIndex] = " ";
			maxIndex++;
		}
		return decoded;

	}

	/*
	 * Takes in an array of strings and return a one hot encoded array of size 1 x
	 * 14 x timesteps Each element in the array indicates a time step Length of one
	 * hot vector = 14
	 */
	private static INDArray mapToOneHot(String[] toEncode) {

		INDArray ret = Nd4j.zeros(1, SEQ_VECTOR_DIM, toEncode.length);
		for (int i = 0; i < toEncode.length; i++) {
			ret.putScalar(0, oneHotMap.get(toEncode[i]), i, 1);
		}

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
			br = new BufferedReader(new FileReader("/Kerja/trajectory-generator/data/dataset/Dest_Traj_159.csv"));

			// Create List for holding Floor objects
			FloorIterator.encodeFloor();
			FloorIterator.encodeRoom();
			SEQ_VECTOR_DIM = FloorIterator.getFloorTotal() + FloorIterator.getRoomTotal();

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
				oneHotMap.put(i,
						FloorIterator.getBinary(trajectories.get(i).getFloor(), trajectories.get(i).getRoom()));
			}

			// one hot encode
			oneHotBinary = new ArrayList<Integer[]>();
			for (Map.Entry<Integer, String> o : oneHotMap.entrySet()) {
				Integer[] tempFloor = new Integer[FloorIterator.getFloorTotal()];
				Integer[] tempRoom = new Integer[FloorIterator.getRoomTotal()];				
				String [] arrOfVal = o.getValue().split(":", 2); 
				
				// First floor total digit use represent floor
				for (int i = 0; i < FloorIterator.getFloorTotal(); i++) {
					if (i == Integer.parseInt(arrOfVal[0])) {
						tempFloor[i] = 1;
					} else {
						tempFloor[i] = 0;
					}
				}

				// First floor total digit use represent floor
				for (int i = 0; i < FloorIterator.getRoomTotal(); i++) {
					if (i == Integer.parseInt(arrOfVal[1])) {
						tempRoom[i] = 1;
					} else {
						tempRoom[i] = 0;
					}
				}

				// Push the array into list
				Integer[] temp = combineInt(tempFloor, tempRoom);
				oneHotBinary.add(temp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setPreProcessor(MultiDataSetPreProcessor preProcessor) {
		this.preProcessor = preProcessor;
	}

	public static Integer[] combineInt(Integer[] a, Integer[] b) {
		int length = a.length + b.length;
		Integer[] result = new Integer[length];
		System.arraycopy(a, 0, result, 0, a.length);
		System.arraycopy(b, 0, result, a.length, b.length);
		return result;
	}
}
