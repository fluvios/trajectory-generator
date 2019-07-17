package com.gui;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.database.DB_Connection;
import com.database.DB_WrapperInsert;
import com.database.spatialobject.AccessPoint;
import com.database.spatialobject.Partition;
import com.indoorobject.utility.IdrObjsUtility;
import com.indoorobject.utility.SpatialHandler;

/**
 * Created by alex9 on 2016/7/19.
 */
public class ClearIllegal {

    public static List<Partition> calIntersectionPartitions(List<Partition> partitions) {
        List<Partition> result = new ArrayList<>();
        for (Partition partition : partitions) {
            if (SpatialHandler.isPolygonSelfIntersection(partition.getPolygon2D())) {
                result.add(partition);
            }
        }
        return result;
    }

    public static List<AccessPoint> calIsolatedAccessPoints(List<AccessPoint> accessPoints) {
        List<AccessPoint> result = new ArrayList<>();
        for (AccessPoint accessPoint : accessPoints) {
            if (accessPoint.getPartitions().size() == 0) {
                result.add(accessPoint);
            }
        }
        return result;
    }

    public static List<Partition> calIsolatedPartitions(List<Partition> partitions) {
        List<Partition> result = new ArrayList<>();
        for (Partition partition : partitions) {
            if (partition.getConParts().size() == 0) {
                result.add(partition);
            }
        }
        return result;
    }

    public static void connectPossiblePartitons(Partition partition) {
        Connection connection = DB_Connection.connectToDatabase("conf/moovework.properties");
        List<Partition> closePartitions = IdrObjsUtility.findClosePartitions(partition.getFloor(), partition);
        System.out.println(partition + " may connect");
        try {
            for (Partition closePartition : closePartitions) {
                if (partition.getConParts().contains(closePartition)) {
                    continue;
                }
                System.out.println(closePartition);
                if (SpatialHandler.polygonIntersectionLines(closePartition.getPolygon2D(), partition.getPolygon2D()).size() > 0) {
                    DB_WrapperInsert.insertDoorBetweenPart(connection, partition, closePartition);
                    System.out.println(closePartition);
                }
            }
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}