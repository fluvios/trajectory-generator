package com.gui;

import java.util.List;

import com.database.spatialobject.AccessPoint;
import com.database.spatialobject.Partition;
import com.indoorobject.utility.SpatialHandler;

/**
 * Created by alex9 on 2016/7/19.
 */
public class PrintIsolatedObject {

    public static void printAllIsolation(List<Partition> partitions, List<AccessPoint> accessPoints) {
        System.out.println("Partition with no doors: ");
        for (Partition partition : partitions) {
            if (partition.getAPs().size() == 0) {
                System.out.println(partition);
            }
        }

        System.out.println("\nPartition with no connected partitions");
        for (Partition partition : partitions) {
            if (partition.getConParts().size() == 0) {
                System.out.println(partition);
            }
        }

        System.out.println("\nPartition both empty");
        for (Partition partition : partitions) {
            if (partition.getAPs().size() == 0 && partition.getConParts().size() == 0) {
                System.out.println(partition);
            }
        }

        System.out.println("\nAccess Point with no partitions: ");
        for (AccessPoint accessPoint : accessPoints) {
            if (accessPoint.getPartitions().size() == 0) {
                System.out.println(accessPoint);
            }
        }

        for (Partition partition : partitions) {
            if (SpatialHandler.isPolygonSelfIntersection(partition.getPolygon2D())) {
                System.out.println(partition);
            }
        }

    }

}