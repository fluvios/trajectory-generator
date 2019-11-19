package com.indoorobject.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.indoorobject.movingobject.MovingObj;
import com.indoorobject.movingobject.MultiDestinationMovement;

public class MovingObjectTask implements Runnable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<MovingObj> movingObjs;

	public MovingObjectTask(ArrayList<MovingObj> movingObjs) {
		// TODO Auto-generated constructor stub
		this.movingObjs = movingObjs;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		// Start the calculation time

		// check if moving object is zero
		if(movingObjs.size() <= 0) {
			System.out.println("no visitor in building!");
		}

		for (MovingObj movingObj : movingObjs) {
			if (movingObj instanceof MultiDestinationMovement) {
				MultiDestinationMovement multiDestCustomer = (MultiDestinationMovement) movingObj;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						multiDestCustomer.genMultiDestinations();
						System.out.println("Multi Destination moving object "+ multiDestCustomer.getId() + " is activated");
						multiDestCustomer.setActive(true);
						Thread thread = new Thread(multiDestCustomer);
						thread.start();
					}
				}, Math.max(0, multiDestCustomer.getInitMovingTime() - System.currentTimeMillis()));
			} else {
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						System.out.println( "Single Destination moving object " + movingObj.getId() + " is activated");
						movingObj.setActive(true);
						Thread thread = new Thread(movingObj);
						thread.start();
					}
				}, Math.max(0, movingObj.getInitMovingTime() - System.currentTimeMillis()));
			}
		}
	}
}
