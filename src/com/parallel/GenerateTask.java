package com.parallel;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

import com.indoorobject.movingobject.MovingObj;
import com.indoorobject.movingobject.MultiDestinationMovement;

public class GenerateTask implements Runnable, Serializable {

    private final MovingObj movingObj;

    public GenerateTask(MovingObj movingObj) {
        this.movingObj = movingObj;
    }

    @Override
    public void run() {
    	// for test purpose
    	System.out.println(movingObj.getId());
		if (!movingObj.isActive()) {
			if (movingObj instanceof MultiDestinationMovement) {
				MultiDestinationMovement multiDestCustomer = (MultiDestinationMovement) movingObj;
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						multiDestCustomer.genMultiDestinations();
						System.out.println("new " + multiDestCustomer.getId() + " is generated");
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
						System.out.println("new " + movingObj.getId() + " is generated");
						movingObj.setActive(true);
						Thread thread = new Thread(movingObj);
						thread.start();
					}
				}, Math.max(0, movingObj.getInitMovingTime() - System.currentTimeMillis()));
			}
		}
    }
}
