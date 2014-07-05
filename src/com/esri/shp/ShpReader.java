package com.esri.shp;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.MultiPoint;
import org.apache.commons.io.EndianUtils;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * http://www.esri.com/library/whitepapers/pdfs/shapefile.pdf
 */
public class ShpReader
{
    private DataInputStream m_dataInputStream;
    private ShpHeader m_shpHeader;

    private int m_parts[] = new int[4];

    public int recordNumber;
    public int contentLength;
    public int contentLengthInBytes;
    public int shapeType;
    public double xmin;
    public double ymin;
    public double xmax;
    public double ymax;
    public int numParts;
    public int numPoints;

    public ShpReader(final DataInputStream dataInputStream) throws IOException
    {
        m_dataInputStream = dataInputStream;
        m_shpHeader = new ShpHeader(dataInputStream);
    }

    public ShpHeader getHeader()
    {
        return m_shpHeader;
    }

    public boolean hasMore() throws IOException
    {
        return m_dataInputStream.available() > 0;
    }

    private void readRecordHeader() throws IOException
    {
        recordNumber = m_dataInputStream.readInt();
        contentLength = m_dataInputStream.readInt();
        contentLengthInBytes = contentLength + contentLength - 4;

        shapeType = EndianUtils.readSwappedInteger(m_dataInputStream);
    }

    public Point readPoint() throws IOException
    {
        if (ShpType.Null.value != this.shapeType){

            return queryPoint(new Point());
        }
        else {
            return null;
        }
    }

    public Polygon readPolygon() throws IOException
    {
        if (ShpType.Null.value != this.shapeType){

            return queryPolygon(new Polygon());
        }
        else {
            return null;
        }
    }

    public Point queryPoint(final Point point) throws IOException
    {
        readRecordHeader();
        if (ShpType.Null.value != this.shapeType){
            point.setX(EndianUtils.readSwappedDouble(m_dataInputStream));
            point.setY(EndianUtils.readSwappedDouble(m_dataInputStream));
        }
        return point;
    }

    public Polygon queryPolygon(final Polygon polygon) throws IOException
    {
        polygon.setEmpty();

        readRecordHeader();
        if (ShpType.Null.value != this.shapeType){
            xmin = EndianUtils.readSwappedDouble(m_dataInputStream);
            ymin = EndianUtils.readSwappedDouble(m_dataInputStream);
            xmax = EndianUtils.readSwappedDouble(m_dataInputStream);
            ymax = EndianUtils.readSwappedDouble(m_dataInputStream);

            numParts = EndianUtils.readSwappedInteger(m_dataInputStream);
            numPoints = EndianUtils.readSwappedInteger(m_dataInputStream);

            if ((numParts + 1) > m_parts.length){
                m_parts = new int[numParts + 1];
            }
            for (int p = 0; p < numParts; p++){
                m_parts[p] = EndianUtils.readSwappedInteger(m_dataInputStream);
            }
            m_parts[numParts] = numPoints;

            for (int i = 0, j = 1; i < numParts; ){
                final int count = m_parts[j++] - m_parts[i++];
                for (int c = 0; c < count; c++){
                    final double x = EndianUtils.readSwappedDouble(m_dataInputStream);
                    final double y = EndianUtils.readSwappedDouble(m_dataInputStream);
                    if (0 == c){
                        polygon.startPath(x, y);
                    }
                    else {
                        polygon.lineTo(x, y);
                    }
                }
            }
            polygon.closeAllPaths();
        }
        return polygon;
    }

    public Polyline queryPolyline(final Polyline polyline) throws IOException
    {
        polyline.setEmpty();

        readRecordHeader();
        if (ShpType.Null.value != this.shapeType){
            xmin = EndianUtils.readSwappedDouble(m_dataInputStream);
            ymin = EndianUtils.readSwappedDouble(m_dataInputStream);
            xmax = EndianUtils.readSwappedDouble(m_dataInputStream);
            ymax = EndianUtils.readSwappedDouble(m_dataInputStream);

            numParts = EndianUtils.readSwappedInteger(m_dataInputStream);
            numPoints = EndianUtils.readSwappedInteger(m_dataInputStream);

            if ((numParts + 1) > m_parts.length){
                m_parts = new int[numParts + 1];
            }
            for (int p = 0; p < numParts; p++){
                m_parts[p] = EndianUtils.readSwappedInteger(m_dataInputStream);
            }
            m_parts[numParts] = numPoints;

            for (int i = 0, j = 1; i < numParts; ){
                final int count = m_parts[j++] - m_parts[i++];

                double lx = 0.0, ly = 0.0;

                for (int c = 0; c < count; c++){
                    final double x = EndianUtils.readSwappedDouble(m_dataInputStream);
                    final double y = EndianUtils.readSwappedDouble(m_dataInputStream);
                    if (0 == c){
                        lx = x;
                        ly = y;
                        polyline.startPath(x, y);
                    }
                    else if (x != lx && y != ly){
                        polyline.lineTo(x, y);
                    }
                }
            }
        }
        return polyline;
    }

    public MultiPoint queryMultiPoint(final MultiPoint multiPoint) throws IOException
    {
        multiPoint.setEmpty();

        readRecordHeader();
        if (ShpType.Null.value != this.shapeType){
            xmin = EndianUtils.readSwappedDouble(m_dataInputStream);
            ymin = EndianUtils.readSwappedDouble(m_dataInputStream);
            xmax = EndianUtils.readSwappedDouble(m_dataInputStream);
            ymax = EndianUtils.readSwappedDouble(m_dataInputStream);

            numParts = EndianUtils.readSwappedInteger(m_dataInputStream);
            numPoints = EndianUtils.readSwappedInteger(m_dataInputStream);

            if ((numParts + 1) > m_parts.length){
                m_parts = new int[numParts + 1];
            }
            for (int p = 0; p < numParts; p++){
                m_parts[p] = EndianUtils.readSwappedInteger(m_dataInputStream);
            }
            m_parts[numParts] = numPoints;

            for (int i = 0, j = 1; i < numParts; ){
                final int count = m_parts[j++] - m_parts[i++];
                for (int c = 0; c < count; c++){
                    final double x = EndianUtils.readSwappedDouble(m_dataInputStream);
                    final double y = EndianUtils.readSwappedDouble(m_dataInputStream);

                    multiPoint.add(x, y);
                }
            }
        }
        return multiPoint;
    }

}
