package cn.edu.zju.db.datagen.trajectory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class FloorIterator {
	private static Map<String, Integer> binaryFloor = new IdentityHashMap<String, Integer>();
	private static Map<String, Integer> binaryRoom = new IdentityHashMap<String, Integer>();

	private static List<String> floorList = new ArrayList<String>();
	private static Map<String, String> roomList = new IdentityHashMap<String, String>();

	private static final String COMMA_DELIMITER = ",";
	
	private static int floorTotal;
	private static int roomTotal;

	public static void encodeFloor() {
		BufferedReader br = null;
		try {
			// Reading the csv file
			br = new BufferedReader(new FileReader("/Kerja/trajectory-generator/data/dataset/Floors.csv"));

			// Create List for holding Floor objects

			String line = "";
			// Read to skip the header
			br.readLine();
			// Reading from the second line
			while ((line = br.readLine()) != null) {
				String[] floorDetails = line.split(COMMA_DELIMITER);
				floorList.add(floorDetails[2]);
			}

			// Convert into binary
			for (int i = 0; i < floorList.size(); i++) {
				binaryFloor.put(floorList.get(i), i);
			}

			floorTotal = binaryFloor.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void encodeRoom() {
		BufferedReader br = null;
		String[] roomDetails = null;

		try {
			// Reading the csv file
			br = new BufferedReader(new FileReader("/Kerja/trajectory-generator/data/dataset/Partitions.csv"));

			String line = "";
			// Read to skip the header
			br.readLine();
			// Reading from the second line
			while ((line = br.readLine()) != null) {
				roomDetails = line.split(COMMA_DELIMITER);
				if (!roomDetails[1].equals("OUTDOOR")) {
					roomList.put(roomDetails[0], roomDetails[1]);
				}
			}

			// Convert into binary
			int i = 0;
			for (String floor : floorList) {
				for (Map.Entry<String, String> entry : roomList.entrySet()) {
					if (entry.getKey().equals(floor)) {
						binaryRoom.put(entry.getValue(), i);
						i++;
					}
				}
			}

//			for (Object obj : binaryRoom.entrySet()) {
//				Map.Entry<String, Integer> entry = (Map.Entry) obj;
//				System.out.print("Key: " + entry.getKey());
//				System.out.println(", Value: " + entry.getValue());
//			}
			
			roomTotal = binaryRoom.size();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void decodeFloor() {

	}

	public static void decodeRoom() {

	}
	
	public static void main(String[] args) {
		encodeFloor();
		encodeRoom();
	}

	public static String getBinary(String floor, String room) {
		String temp = "";

		for (Map.Entry<String, Integer> f : binaryFloor.entrySet()) {
			if (f.getKey().equals(floor)) {
				temp += f.getValue();
				break;
			}
		}
		
		// make one char as partition
		temp += ":";

		for (Map.Entry<String, Integer> r : binaryRoom.entrySet()) {
			if (r.getKey().equals(room)) {
				temp += r.getValue();
				break;
			}
		}
		
		return temp;
	}

	public static int getFloorTotal() {
		return floorTotal;
	}

	public static void setFloorTotal(int floorTotal) {
		FloorIterator.floorTotal = floorTotal;
	}

	public static int getRoomTotal() {
		return roomTotal;
	}

	public static void setRoomTotal(int roomTotal) {
		FloorIterator.roomTotal = roomTotal;
	}
}