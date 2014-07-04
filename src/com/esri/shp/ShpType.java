package com.esri.shp;

/**
 * 
 */
public enum ShpType {
    Null(0),
    Point(1),
    PolyLine(3),
    Polygon(5),
    MultiPoint(8),
    PointZ(11),
    PolyLineZ(13),
    PolygonZ(15),
    MultiPointZ(18),
    PointM(21),
    PolyLineM(23),
    PolygonM(25),
    MultiPointM(28),
    MultiPatch(31);


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

    private ShpType(int value){
        this.value = value;
    }
}
