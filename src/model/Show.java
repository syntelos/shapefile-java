package model;

import com.esri.dbf.DBFField;
import com.esri.dbf.DBFHeader;
import com.esri.dbf.DBFReader;
import com.esri.dbf.DBFType;

import com.esri.core.geometry.Envelope2D;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.MultiPoint;
import com.esri.shp.ShpHeader;
import com.esri.shp.ShpReader;
import com.esri.shp.ShpType;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 */
public class Show
{

    public static void main(String[] argv){
        if (1 == argv.length){
            File fileDbf = new File(argv[0]+".dbf");
            File fileShp = new File(argv[0]+".shp");
            DataInputStream dbfStream = null;
            DataInputStream shpStream = null;
            try {
                dbfStream = new DataInputStream(new FileInputStream(fileDbf));
                shpStream = new DataInputStream(new FileInputStream(fileShp));

                final DBFReader dbfReader = new DBFReader(dbfStream);
                final ShpReader shpReader = new ShpReader(shpStream);

                final List<DBFField> fields = dbfReader.getFields();
                final ShpHeader header = shpReader.getHeader();
                final ShpType shape = ShpType.For(header);
                final Point point = new Point();
                final Polyline polyLine = new Polyline();
                final Polygon polygon = new Polygon();
                final MultiPoint multiPoint = new MultiPoint();

                while (DBFType.END != dbfReader.nextDataType()){
                    int field = 0;
                    for (DBFField meta: fields){
                        Writable actual = dbfReader.readFieldWritable(field++);
                        switch(DBFType.Data.For(meta)){
                        case Short:
                            {
                                IntWritable ovalue = (IntWritable)actual;
                                int value = ovalue.get();
                                System.out.printf("%s='%d' ",meta.fieldName,value);
                            }
                            break;
                        case Long:
                            {
                                LongWritable ovalue = (LongWritable)actual;
                                long value = ovalue.get();
                                System.out.printf("%s='%d' ",meta.fieldName,value);
                            }
                            break;
                        case Float:
                            {
                                FloatWritable ovalue = (FloatWritable)actual;
                                float value = ovalue.get();
                                System.out.printf("%s='%f' ",meta.fieldName,value);
                            }
                            break;
                        case Double:
                            {
                                DoubleWritable ovalue = (DoubleWritable)actual;
                                double value = ovalue.get();
                                System.out.printf("%s='%f' ",meta.fieldName,value);
                            }
                            break;
                        case String:
                            {
                                Text ovalue = (Text)actual;
                                String value = ovalue.toString().trim();
                                System.out.printf("%s='%s' ",meta.fieldName,value);
                            }
                            break;
                        case Date:
                            {
                                LongWritable ovalue = (LongWritable)actual;
                                long value = ovalue.get();
                                System.out.printf("%s='%d' ",meta.fieldName,value);
                            }
                            break;
                        default:
                            System.out.printf("Unrecognized DBF record name '%s', type '%c', class '%s'%n",meta.fieldName,meta.dataType,actual.getClass().getName());
                            break;
                        }
                    }

                    switch(shape){
                    case Point:
                        {
                            shpReader.queryPoint(point);
                            System.out.println("Point");
                        }
                        break;
                    case PolyLine:
                        {
                            shpReader.queryPolyline(polyLine);
                            System.out.printf("PolyLine[%d]%n",polyLine.getSegmentCount());
                        }
                        break;
                    case Polygon:
                        {
                            shpReader.queryPolygon(polygon);
                            System.out.printf("Polygon[%d]%n",polygon.getSegmentCount());
                        }
                        break;
                    case MultiPoint:
                        {
                            shpReader.queryMultiPoint(multiPoint);
                            System.out.printf("MultiPoint[%d]%n",multiPoint.getPointCount());
                        }
                        break;
                    default:
                        throw new IllegalStateException("TODO "+shape);
                    }
                }
            }
            catch (java.io.EOFException exc){
                return;
            }
            catch (Exception exc){
                exc.printStackTrace();
                System.exit(1);
            }
            finally {
                if (null != dbfStream){
                    try {
                        dbfStream.close();
                    }
                    catch (IOException ignore){
                    }
                }
                if (null != shpStream){
                    try {
                        shpStream.close();
                    }
                    catch (IOException ignore){
                    }
                }
            }
        }
        else {
            System.err.println("usage: model.Read <basename>");
            System.exit(1);
        }
    }
}
