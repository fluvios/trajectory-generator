package com.parallel;

import java.awt.geom.Point2D;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IExecutorService;

import diva.util.java2d.Polygon2D;

public class MasterMember {
	private static Point2D.Double polygon2D;

	public static void main(String[] args) throws Exception {
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
		IExecutorService executor = hazelcastInstance.getExecutorService("exec");
		for (int k = 1; k <= 1000; k++) {
	        double randX = Math.random();
	        double randY = Math.random();
	        polygon2D = new Point2D.Double(randX, randY);
			Thread.sleep(1000);
			System.out.println("Producing echo task: " + polygon2D.getX());
			executor.execute(new GenerateTask(polygon2D));
		}
		System.out.println("EchoTaskMain finished!");
	}
}