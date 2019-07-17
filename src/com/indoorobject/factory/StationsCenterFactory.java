package com.indoorobject.factory;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Collectors;

import com.database.spatialobject.Partition;
import com.indoorobject.station.Station;
import com.indoorobject.utility.SpatialHandler;

/**
 * Created by alex9 on 2016/8/4.
 */
public class StationsCenterFactory extends StationFactory {
    @Override
    protected List<Station> createStationsOnPartition(String type, Partition partition, int stationNumber) {
        double radius = 3;
        List<Point2D.Double> randomPointsNearCenter = SpatialHandler.getRandomPointsNearCenter(partition.getPolygon2D(), stationNumber, radius);
        return randomPointsNearCenter
                       .stream()
                       .map(point -> createStation(type, partition, point))
                       .collect(Collectors.toList());
    }

    private Station createStation(String type, Partition partition, Point2D.Double location) {
        Station station = (Station)createIndoorObject(type);

        station.setLocation(location);
        station.setCurrentFloor(partition.getFloor());
        station.setCurrentPartition(partition);
        station.setId(IndoorObjectFactory.stationID++);
        return station;
    }
}