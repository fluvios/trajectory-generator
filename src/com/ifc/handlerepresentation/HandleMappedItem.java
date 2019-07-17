package com.ifc.handlerepresentation;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcAxis2Placement2D;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCartesianPoint;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcCircle;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcGeometricSet;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcGeometricSetSelect;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcLengthMeasure;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcMappedItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcPolyline;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentationItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcShapeRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcTrimmedCurve;

/**
 * Created by alex9 on 2016/8/12.
 */
public class HandleMappedItem {
    static void positionFromIfcRepresentationMap(IfcMappedItem ifcMappedItem) {
        IfcShapeRepresentation shapeRepresentation = (IfcShapeRepresentation) ifcMappedItem.getMappingSource().getMappedRepresentation();
        for (IfcRepresentationItem ifcRepresentationItem : shapeRepresentation.getItems()) {
            if (ifcRepresentationItem instanceof IfcExtrudedAreaSolid) {
                positionFromIfcExtrudedAreaSolid((IfcExtrudedAreaSolid) ifcRepresentationItem);
            }
            if (ifcRepresentationItem instanceof IfcGeometricSet) {
                positionFromIfcGeometricSet((IfcGeometricSet) ifcRepresentationItem);
            }
        }
        System.out.println(shapeRepresentation + "\n");
    }

    private static void positionFromIfcExtrudedAreaSolid(IfcExtrudedAreaSolid extrudedAreaSolid) {
        System.out.println(extrudedAreaSolid.getPosition().getLocation().getCoordinates());
    }

    private static void positionFromIfcGeometricSet(IfcGeometricSet geometricSet) {
        for (IfcGeometricSetSelect geometricSetSelect : geometricSet.getElements()) {
            if (geometricSetSelect instanceof IfcPolyline) {
                positionFromIfcPolyline((IfcPolyline) geometricSetSelect);
            }
            if (geometricSetSelect instanceof IfcTrimmedCurve) {
                positionFromIfcTrimmedCurve((IfcTrimmedCurve) geometricSetSelect);
            }
        }
    }

    private static void positionFromIfcPolyline(IfcPolyline polyline) {
        for (IfcCartesianPoint cartesianPoint : polyline.getPoints()) {
            positionFromIfcCartesianPoint(cartesianPoint);
        }
    }

    private static void positionFromIfcTrimmedCurve(IfcTrimmedCurve trimmedCurve) {
        IfcAxis2Placement2D position = (IfcAxis2Placement2D)((IfcCircle) trimmedCurve.getBasisCurve()).getPosition();
        positionFromIfcCartesianPoint(position.getLocation());
    }

    private static void positionFromIfcCartesianPoint(IfcCartesianPoint cartesianPoint) {
        System.out.println("Point: ");
        for (IfcLengthMeasure ifcLengthMeasure : cartesianPoint.getCoordinates()) {
            System.out.println(ifcLengthMeasure + " ");
        }
        System.out.println("");
    }

}
