package com.esri.shp;

import com.esri.core.geometry.Geometry;

/**
 * 
 */
public enum ShpType {
    Null        ( 0, Geometry.Type.Unknown),
    Point       ( 1, Geometry.Type.Point),
    PolyLine    ( 3, Geometry.Type.Polyline),
    Polygon     ( 5, Geometry.Type.Polygon),
    MultiPoint  ( 8, Geometry.Type.MultiPoint),
    PointZ      (11, Geometry.Type.Point),
    PolyLineZ   (13, Geometry.Type.Polyline),
    PolygonZ    (15, Geometry.Type.Polygon),
    MultiPointZ (18, Geometry.Type.MultiPoint),
    PointM      (21, Geometry.Type.Point),
    PolyLineM   (23, Geometry.Type.Polyline),
    PolygonM    (25, Geometry.Type.Polygon),
    MultiPointM (28, Geometry.Type.MultiPoint),
    MultiPatch  (31, Geometry.Type.Unknown);


    public final static ShpType For(ShpHeader header){
        return For(header.shapeType);
    }
    public final static ShpType For(int value){
        switch(value){
        case 0:
            return Null;
        case 1:
            return Point;
        case 3:
            return PolyLine;
        case 5:
            return Polygon;
        case 8:
            return MultiPoint;
        case 11:
            return PointZ;
        case 13:
            return PolyLineZ;
        case 15:
            return PolygonZ;
        case 18:
            return MultiPointZ;
        case 21:
            return PointM;
        case 23:
            return PolyLineM;
        case 25:
            return PolygonM;
        case 28:
            return MultiPointM;
        case 31:
            return MultiPatch;
        default:
            throw new IllegalArgumentException(String.valueOf(value));
        }
    }


    public final int value;

    public final Geometry.Type geometry;


    private ShpType(int value, Geometry.Type geometry){
        this.value = value;
        this.geometry = geometry;
    }
}
