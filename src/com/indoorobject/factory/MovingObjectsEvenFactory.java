package com.indoorobject.factory;

import java.util.ArrayList;
import java.util.List;

import com.database.spatialobject.Partition;
import com.indoorobject.movingobject.MovingObj;

/**
 * Created by alex9 on 2016/8/2.
 */
public class MovingObjectsEvenFactory extends MovingObjectFactory {

    @Override
    List<MovingObj> createMovingObjsOnPartition(String type, Partition partition, int movingObjectNumber) {
        List<MovingObj> tmpMovingObjects = new ArrayList<>();
        for (int i = 0; i < movingObjectNumber; i++) {
            tmpMovingObjects.add(createMovingObject(type, partition));
        }
        return tmpMovingObjects;
    }

    private MovingObj createMovingObject(String type, Partition partition) {
        MovingObj movingObj = (MovingObj)createIndoorObject(type);

        movingObj.setCurrentFloor(partition.getFloor());
        movingObj.setCurrentPartition(partition);
        movingObj.setLocation(partition.calRandomPointInMBR());
        movingObj.setId(++IndoorObjectFactory.movingObjID);
        return movingObj;
    }


}
