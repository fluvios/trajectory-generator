package com.indoorobject.factory;

import java.util.List;

import com.database.spatialobject.Floor;
import com.indoorobject.IndoorObject;
import com.indoorobject.movingobject.MovingObj;
import com.indoorobject.station.Station;

/**
 * Created by alex9 on 2016/8/3.
 */
public abstract class IndoorObjectFactory {
    public static int movingObjID = 1;
    public static int stationID = 1;

    IndoorObject createIndoorObject(String type) {
        Class<?> reflaction;
        IndoorObject indoorObj = null;
        try {
            reflaction = Class.forName(type);
            indoorObj = (IndoorObject) reflaction.newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return indoorObj;
    }

    public static List<Station> createStationsOnFloor(Floor floor, String stationFactoryType) {
        Class<?> stationReflection = null;
        StationFactory stationFactory = null;

        try {
            stationReflection = Class.forName(stationFactoryType);
            stationFactory = (StationFactory) stationReflection.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert stationFactory != null;
        return stationFactory.createIndoorObjectsOnFloor(floor);
    }

    public static List<MovingObj> createMovingObjectsOnFloor(Floor floor, String movingObjectFactoryType) {
        Class<?> movingObjectReflection = null;
        MovingObjectFactory movingObjectFactory = null;

        try {
            movingObjectReflection = Class.forName(movingObjectFactoryType);
            movingObjectFactory = (MovingObjectFactory) movingObjectReflection.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assert movingObjectFactory != null;
        return movingObjectFactory.createIndoorObjectsOnFloor(floor);
    }

    abstract List<? extends IndoorObject> createIndoorObjectsOnFloor(Floor floor);
}
