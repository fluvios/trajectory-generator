package com.parallel;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;

import diva.util.java2d.Polygon2D;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.concurrent.Callable;

public class GenerateTask implements Runnable, Serializable {

    private final Point2D.Double polygon2D;

    public GenerateTask( Point2D.Double polygon2D) {
        this.polygon2D = polygon2D;
    }

    @Override
    public void run() {
        try {
            Thread.sleep( 5000 );
        } catch ( InterruptedException e ) {
        }
        System.out.println( "echo:" + polygon2D.getX());
    }
}
