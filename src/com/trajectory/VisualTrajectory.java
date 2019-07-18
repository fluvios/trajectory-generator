package com.trajectory;

import java.awt.Color;
import java.util.ArrayList;

public class VisualTrajectory {
	private Color color;
	private ArrayList<Trajectory> trajectories = new ArrayList<Trajectory>();
	
	public VisualTrajectory(Color color, ArrayList<Trajectory> trajectories) {
		super();
		this.color = color;
		this.trajectories = trajectories;
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
