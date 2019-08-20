package com.trajectory;

import java.awt.Color;
import java.util.ArrayList;

public class VisualTrajectory {
	private Integer userId;
	private Color color;
	private ArrayList<Trajectory> trajectories = new ArrayList<Trajectory>();
	
	public VisualTrajectory(Integer userId, Color color, ArrayList<Trajectory> trajectories) {
		super();
		this.userId = userId;
		this.color = color;
		this.trajectories = trajectories;
	}
	
	public VisualTrajectory(Color color, ArrayList<Trajectory> trajectories) {
		super();
		this.color = color;
		this.trajectories = trajectories;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ArrayList<Trajectory> getTrajectories() {
		return trajectories;
	}

	public void setTrajectories(ArrayList<Trajectory> trajectories) {
		this.trajectories = trajectories;
	}
}
