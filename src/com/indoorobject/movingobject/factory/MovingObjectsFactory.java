package com.indoorobject.movingobject.factory;

import java.util.ArrayList;

import com.database.spatialobject.Partition;
import com.indoorobject.movingobject.MovingObj;

public abstract class MovingObjectsFactory implements SimpleMovingObjCreateInterface {

    public MovingObjectsFactory() {
        // TODO Auto-generated constructor stub
    }

    public abstract void generateMovingObjsInPart(Partition partition, ArrayList<MovingObj> movingObjs, int pointNum,
                                                  String movingObjType);

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
