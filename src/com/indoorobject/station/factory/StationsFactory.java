package com.indoorobject.station.factory;

import java.util.ArrayList;

import com.database.spatialobject.Partition;
import com.indoorobject.station.Station;

public abstract class StationsFactory implements SimpleStationCreateInterface {

    public StationsFactory() {
        // TODO Auto-generated constructor stub
    }


    public abstract void generateStationsInPart(Partition partition, ArrayList<Station> stations, int stationNum, String stationType);

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
