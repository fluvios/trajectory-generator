package com.indoorobject.factory;

import java.util.ArrayList;
import java.util.List;

import com.database.spatialobject.Partition;
import com.indoorobject.station.Station;

/**
 * Created by alex9 on 2016/8/3.
 */
public class StationsEvenFactory extends StationFactory {

    @Override
    protected List<Station> createStationsOnPartition(String type, Partition partition, int stationNumber) {
        List<Station> tmpStations = new ArrayList<>();
        for (int i = 0; i < stationNumber; i++) {
            tmpStations.add(createStation(type, partition));
        }
        return tmpStations;
    }

    private Station createStation(String type, Partition partition) {
        Station station = (Station)createIndoorObject(type);
        station.setLocation(partition.calRandomPointInMBR());
        station.setCurrentFloor(partition.getFloor());
        station.setCurrentFloor(partition.getFloor());
        station.setId(IndoorObjectFactory.stationID++);
        return station;
    }
}
