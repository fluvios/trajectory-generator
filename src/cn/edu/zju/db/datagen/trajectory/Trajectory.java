package cn.edu.zju.db.datagen.trajectory;

import java.util.Date;

public class Trajectory {
	private int userId;
	private int floorId;
	private int RoomId;
	private double axis;
	private double oordinat;
	private Date timestamp;

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
		return RoomId;
	}

	public void setRoomId(int roomId) {
		RoomId = roomId;
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