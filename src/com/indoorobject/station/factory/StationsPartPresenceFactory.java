package com.indoorobject.station.factory;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.database.DB_WrapperLoad;
import com.database.spatialobject.AccessPoint;
import com.database.spatialobject.Partition;
import com.indoorobject.IndoorObjsFactory;
import com.indoorobject.station.Station;

public class StationsPartPresenceFactory extends StationsFactory {

    int[] APFlag = new int[DB_WrapperLoad.accessPointConnectorT.size() + 100];

    public StationsPartPresenceFactory() {

    }


    @Override
    public void generateStationsInPart(Partition partition, ArrayList<Station> stations, int stationNum, String stationType) {
        for (AccessPoint ap : partition.getAPs()) {
            //when there is alreay a rfid or random number is smaller than 0.2, pass
            if (APFlag[DB_WrapperLoad.accessPointConnectorT.indexOf(ap)] == 1 || Math.random() < 0.3) {
                continue;
            }
            //for showing station on the door
            Point2D.Double chkPoint = new Point2D.Double(ap.getLocation2D().getX() - 0.4, ap.getLocation2D().getY() - 0.4);
            Station station = createStation(stationType, partition, chkPoint);
            stations.add(station);
            APFlag[DB_WrapperLoad.accessPointConnectorT.indexOf(ap)] = 1;
        }

//		int stationSizeInPart;
//		double partArea = partition.calMBRArea();
//		if(partArea < 80){
// 			stationSizeInPart = (int)(Math.random()*2);
//		} else {
//			stationSizeInPart = (int)(1 + Math.random()*2);
//		}

        for (int i = 0; i < stationNum; i++) {
            Point2D.Double randomPoint = partition.calRandomPointInMBR();
            if (partition.getPolygon2D().contains(randomPoint) == false) {
                continue;
            }
            Station station = createStation(stationType, partition, randomPoint);
            stations.add(station);
        }

    }

    private Station createStation(String stationType, Partition partition, Point2D.Double point) {
        Station station = (Station) IndoorObjsFactory.createIndoorObject(stationType);
        station.setCurrentFloor(partition.getFloor());
        station.setCurrentPartition(partition);
        station.setLocation(point);
        station.setId(++IndoorObjsFactory.stationID);
        station.setType(2);        //means FRID
        return station;
    }

}
