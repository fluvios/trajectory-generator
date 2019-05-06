package cn.edu.zju.db.datagen.indoorobject.movingobject;

import java.util.Date;

public class Schedule {
	private int partitionId;
	private String roomId;
    private Date startTime;
    private Date finishTime;
    private float probs;
    
	public int getPartitionId() {
		return partitionId;
	}
	public void setPartitionId(int partitionId) {
		this.partitionId = partitionId;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public float getProbs() {
		return probs;
	}
	public void setProbs(float probs) {
		this.probs = probs;
	}
    
    
}
