package com.indoorobject.station.factory;

import java.util.ArrayList;

import com.database.spatialobject.Partition;
import com.indoorobject.station.Station;

public interface SimpleStationCreateInterface {

    public abstract void generateStationsInPart(Partition partition,
                                                ArrayList<Station> stations, int stationNum, String stationType);


}
