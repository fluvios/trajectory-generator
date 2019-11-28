package com.parallel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.indoorobject.movingobject.MovingObj;
import com.indoorobject.movingobject.MultiDestinationMovement;

public class GenerateTask implements Runnable, Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<MovingObj> movingObjs;

    public GenerateTask(ArrayList<MovingObj> movingObjs) {
        this.movingObjs = movingObjs;
    }

    @Override
    public void run() {
    	for (MovingObj m : movingObjs) {
			System.out.println(m.getInitialDistribution());
		}
    }
}
