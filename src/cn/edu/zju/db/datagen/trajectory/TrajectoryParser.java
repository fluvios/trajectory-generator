package cn.edu.zju.db.datagen.trajectory;

public class TrajectoryParser {
	private String floor;
	private String room;
	private Double x;
	private Double y;
	
	public TrajectoryParser(String floor, String room, Double x, Double y) {
		super();
		this.floor = floor;
		this.room = room;
		this.x = x;
		this.y = y;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}
}
