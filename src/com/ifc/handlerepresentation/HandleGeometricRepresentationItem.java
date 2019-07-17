package com.ifc.handlerepresentation;

import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcExtrudedAreaSolid;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcGeometricRepresentationItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcRepresentationItem;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcShapeRepresentation;
import openifctools.com.openifcjavatoolbox.ifc2x3tc1.IfcSolidModel;

/**
 * Created by alex9 on 2016/8/12.
 */
public class HandleGeometricRepresentationItem {

    static void positionFromIfcGeomRepresItem(IfcGeometricRepresentationItem geometricRepresentationItem) {
        if (geometricRepresentationItem instanceof IfcSolidModel) {

        }

    }


    public static void positionFromIfcShapeRepresentation(IfcShapeRepresentation shapeRepresentation) {
        if (shapeRepresentation.getRepresentationIdentifier().toString().equals("Body") && shapeRepresentation.getRepresentationType().toString().equals("SweptSolid")) {
            for (IfcRepresentationItem ifcRepresentationItem : shapeRepresentation.getItems()) {
                IfcExtrudedAreaSolid ifcExtrudedAreaSolid = (IfcExtrudedAreaSolid) ifcRepresentationItem;
                positionFromIfcExtrudedAreaSolid(ifcExtrudedAreaSolid);
            }
        }
    }


    private static void positionFromIfcExtrudedAreaSolid(IfcExtrudedAreaSolid extrudedAreaSolid) {

    }

}
