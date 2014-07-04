package model;

import com.esri.dbf.DBFField;
import com.esri.dbf.DBFHeader;
import com.esri.dbf.DBFReader;
import com.esri.dbf.DBFType;

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
import java.util.GregorianCalendar;
import java.util.List;

/**
 */
public class DBFRead
{

    public static void main(String[] argv){
        if (1 == argv.length){
            File file = new File(argv[0]);
            DataInputStream inputStream = null;
            try {
                inputStream = new DataInputStream(new FileInputStream(file));

                final DBFReader dbfReader = new DBFReader(new DataInputStream(inputStream));

                final List<DBFField> fields = dbfReader.getFields();

                while (DBFType.END != dbfReader.nextDataType()){
                    int field = 0;
                    for (DBFField meta: fields){
                        Writable actual = dbfReader.readFieldWritable(field++);
                        switch(DBFType.Data.For(meta)){
                        case Short:
                            {
                                IntWritable ovalue = (IntWritable)actual;
                                int value = ovalue.get();
                                System.out.printf("name '%s', type 'SHORT', value '%d'%n",meta.fieldName,value);
                            }
                            break;
                        case Long:
                            {
                                LongWritable ovalue = (LongWritable)actual;
                                long value = ovalue.get();
                                System.out.printf("name '%s', type 'LONG', value '%d'%n",meta.fieldName,value);
                            }
                            break;
                        case Float:
                            {
                                FloatWritable ovalue = (FloatWritable)actual;
                                float value = ovalue.get();
                                System.out.printf("name '%s', type 'FLOAT', value '%f'%n",meta.fieldName,value);
                            }
                            break;
                        case Double:
                            {
                                DoubleWritable ovalue = (DoubleWritable)actual;
                                double value = ovalue.get();
                                System.out.printf("name '%s', type 'DOUBLE', value '%f'%n",meta.fieldName,value);
                            }
                            break;
                        case String:
                            {
                                Text ovalue = (Text)actual;
                                String value = ovalue.toString().trim();
                                System.out.printf("name '%s', type 'STRING', value '%s'%n",meta.fieldName,value);
                            }
                            break;
                        case Date:
                            {
                                LongWritable ovalue = (LongWritable)actual;
                                long value = ovalue.get();
                                System.out.printf("name '%s', type 'DATE', value '%d'%n",meta.fieldName,value);
                            }
                            break;
                        default:
                            System.out.printf("Unrecognized DBF record name '%s', type '%c', class '%s'%n",meta.fieldName,meta.dataType,actual.getClass().getName());
                            break;
                        }
                    }

                    break;//
                }
            }
            catch (IOException exc){
                System.err.println("usage: model.DBFRead <file.dbf>");
                System.exit(1);
            }
            finally {
                if (null != inputStream){
                    try {
                        inputStream.close();
                    }
                    catch (IOException ignore){
                    }
                }
            }
        }
        else {
            System.err.println("usage: model.DBFRead <file.dbf>");
            System.exit(1);
        }
    }
}
