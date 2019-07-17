package com.indoorobject;

import java.awt.geom.Point2D;

import com.database.spatialobject.AccessPoint;
import com.database.spatialobject.Floor;
import com.database.spatialobject.Partition;
import com.indoorobject.utility.IdrObjsUtility;
import com.spatialgraph.D2DGraph;

public abstract class IndoorObject {

    protected Point2D.Double currentLocation;
    protected Partition currentPartition;
    protected Floor currentFloor;
    protected int id;

    public Point2D.Double getCurrentLocation() {
        return currentLocation;
    }

    public void setLocation(Point2D.Double currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Partition getCurrentPartition() {
        return currentPartition;
    }

    public void setCurrentPartition(Partition currentPartition) {
        this.currentPartition = currentPartition;
    }

    public Floor getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(Floor currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public double generateMIWD(Floor destFloor, Point2D.Double destLocation, AccessPoint[] aps) {
        double minDistance;
        double tmpDistance = D2DGraph.MAX_DISTANCE;
        Partition par1 = this.getCurrentPartition();
        Partition par2 = IdrObjsUtility.findPartitionForPoint(destFloor, destLocation);

        if (par1 == par2) {
            minDistance = this.currentLocation.distance(destLocation);
            return minDistance;
        } else {
            minDistance = D2DGraph.MAX_DISTANCE;
        }
        if (par1 == null || par2 == null) {
            return D2DGraph.MAX_DISTANCE;
        }

        for (AccessPoint ap1 : par1.getAPConnectors()) {
            double toAP1 = this.getCurrentLocation().distance(ap1.getLocation2D());
            for (AccessPoint ap2 : par2.getAPConnectors()) {
                double toAP2 = destLocation.distance(ap2.getLocation2D());
                tmpDistance = toAP1 + toAP2 + this.getCurrentFloor().getD2dGraph().D2DDistance(ap1, ap2);
                if (tmpDistance < minDistance) {
                    aps[0] = ap1;
                    aps[1] = ap2;
                    minDistance = tmpDistance;
                }
            }
        }
        return minDistance;
    }

}
