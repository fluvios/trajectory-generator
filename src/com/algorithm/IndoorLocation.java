package com.algorithm;

import java.awt.geom.Point2D;

import com.database.spatialobject.Floor;
import com.database.spatialobject.Partition;
import com.indoorobject.utility.IdrObjsUtility;

public class IndoorLocation {

    private double x;
    private double y;
    private Floor floor;
    private Partition partition;

    public IndoorLocation() {
        super();
    }

    public IndoorLocation(Point2D.Double point) {
        super();
        this.x = point.getX();
        this.y = point.getY();
        this.floor = null;
        this.partition = null;
    }

    public IndoorLocation(Point2D.Double point, Floor floor) {
        super();
        this.x = point.getX();
        this.y = point.getY();
        this.setFloor(floor);
        this.partition = IdrObjsUtility.findPartitionForPoint(floor, point);
    }

    public IndoorLocation(double x, double y) {
        super();
        this.x = x;
        this.y = y;
        this.floor = null;
        this.partition = null;
    }


    public IndoorLocation(double x, double y, Floor floor) {
        super();
        this.x = x;
        this.y = y;
        this.setFloor(floor);
        this.partition = IdrObjsUtility.findPartitionForPoint(floor, new Point2D.Double(x, y));
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Floor getFloor() {
        return floor;
    }

    public void setFloor(Floor floor) {
        this.floor = floor;
    }

    public Partition getPartition() {
        return partition;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }

    @Override
    public String toString() {

        String tmp = "";

        if (this.getFloor() == null) {
            tmp = "NULL" + "\t" + "NULL" + "\t";
        } else {
            tmp = this.getFloor().getItemID() + "\t";
            if (this.getPartition() == null) {
                tmp = tmp + "NULL" + "\t";
            } else {
                tmp = tmp + this.getPartition().getItemID() + "\t";
            }
        }

        tmp = tmp + this.getX() + "\t" + this.getY() + "\t";

        return tmp;
    }


}
