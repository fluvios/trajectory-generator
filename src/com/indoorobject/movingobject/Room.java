package com.indoorobject.movingobject;

import java.util.ArrayList;

public class Room {
	private String name;
	private ArrayList<Schedule> schedules = new ArrayList<Schedule>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Schedule> getSchedules() {
		return schedules;
	}
	public void setSchedules(ArrayList<Schedule> schedules) {
		this.schedules = schedules;
	}
	
	
}
