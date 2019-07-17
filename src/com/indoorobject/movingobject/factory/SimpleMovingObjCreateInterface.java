package com.indoorobject.movingobject.factory;

import java.util.ArrayList;

import com.database.spatialobject.Partition;
import com.indoorobject.movingobject.MovingObj;

public interface SimpleMovingObjCreateInterface {

    public abstract void generateMovingObjsInPart(Partition partition,
                                                  ArrayList<MovingObj> movingObjs, int pointNum, String movingObjType);

}
