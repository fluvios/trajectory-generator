package cn.edu.zju.db.datagen.trajectory;

import java.util.Date;

public class Trajectory {
	private int userId;
	private int floorId;
	private int partitionId;
	private double axis;
	private double oordinat;
	private Date timestamp;
	
	public Trajectory(int floorId, int partitionId, double axis, double oordinat, Date timestamp) {
		super();
		this.floorId = floorId;
		this.partitionId = partitionId;
		this.axis = axis;
		this.oordinat = oordinat;
		this.timestamp = timestamp;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFloorId() {
		return floorId;
	}

	public void setFloorId(int floorId) {
		this.floorId = floorId;
	}

	public int getRoomId() {
		return partitionId;
	}

	public void setRoomId(int roomId) {
		partitionId = roomId;
	}

	public double getAxis() {
		return axis;
	}

	public void setAxis(double axis) {
		this.axis = axis;
	}

	public double getOordinat() {
		return oordinat;
	}

	public void setOordinat(double oordinat) {
		this.oordinat = oordinat;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}