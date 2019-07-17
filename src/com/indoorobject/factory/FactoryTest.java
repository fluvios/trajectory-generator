package com.indoorobject.factory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.database.DB_Connection;
import com.database.DB_WrapperLoad;
import com.indoorobject.movingobject.MovingObj;
import com.indoorobject.station.Station;
import com.indoorobject.utility.IdrObjsUtility;
import com.indoorobject.utility.PropLoader;

/**
 * Created by alex9 on 2016/8/4.
 */
public class FactoryTest {


    private static void setMovingObjsInitTime(List<MovingObj> movingObjList) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 30);
        IdrObjsUtility.objectGenerateStartTime = calendar.getTime();

        Calendar calendar1 = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 40);
        IdrObjsUtility.objectGenerateEndTime = calendar1.getTime();
        movingObjList.forEach(movingObj -> {
            movingObj.setInitMovingTime(calGaussianTime());
        });
    }

    private static long calGaussianTime() {
        Random random = new Random();
        long startTime = IdrObjsUtility.objectGenerateStartTime.getTime();
        long endTime = IdrObjsUtility.objectGenerateEndTime.getTime();
        long middle = (long)((endTime - startTime) / 2.0 + startTime);
        long error = (long)((endTime - startTime) * 0.5);
        long gaussianError = (long)(random.nextGaussian() * error);
        System.out.println(gaussianError);
        long gaussianTime = (long)(middle + gaussianError);
        if (gaussianTime < startTime) {
            return startTime;
        } else if(gaussianTime > endTime) {
            return endTime;
        } else {
            return gaussianTime;
        }
    }

    public static void main(String[] args) throws SQLException {
        Connection con = DB_Connection.connectToDatabase("conf/moovework.properties");
        DB_WrapperLoad.loadALL(con, 25);
        PropLoader propLoader = new PropLoader();
        propLoader.loadProp("conf/factory.properties");
        List<Station> stations = new ArrayList<>();
        DB_WrapperLoad.floorT.forEach(floor -> stations.addAll(IndoorObjectFactory.createStationsOnFloor(floor, propLoader.getStationDistributerType())));
        System.out.println(stations);

        List<MovingObj> movingObjs =
                DB_WrapperLoad.floorT
                        .stream()
                        .map(floor -> IndoorObjectFactory.createMovingObjectsOnFloor(floor, propLoader.getMovingObjDistributerType()))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
        System.out.println(movingObjs);

        setMovingObjsInitTime(movingObjs);

        movingObjs.forEach(movingObj -> System.out.println(new Date(movingObj.getInitMovingTime())));
    }
}
