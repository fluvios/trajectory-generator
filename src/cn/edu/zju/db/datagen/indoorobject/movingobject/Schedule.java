package cn.edu.zju.db.datagen.indoorobject.movingobject;

import java.util.Date;

public class Schedule {
	private String day;
    private Date startTime;
    private Date finishTime;
    private float probs;
    
	public String getDay() {
		return day;
	}
	
	public void setDay(String day) {
		this.day = day;
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
