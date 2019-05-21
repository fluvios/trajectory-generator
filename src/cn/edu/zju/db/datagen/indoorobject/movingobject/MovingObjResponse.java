package cn.edu.zju.db.datagen.indoorobject.movingobject;

public class MovingObjResponse {
	private String startTime;
	private String endTime;
	private MovingObject[] movingObject;

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public MovingObject[] getMovingObject() {
		return movingObject;
	}
	public void setMovingObject(MovingObject[] movingObject) {
		this.movingObject = movingObject;
	}

	
}
