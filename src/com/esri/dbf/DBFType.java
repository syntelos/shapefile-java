package com.esri.dbf;

/**
 */
public final class DBFType
{

    public final static byte END = 0x1A;
    public final static byte DELETED = 0x2A;

    public final static byte NUMBER = 'N';
    public final static byte FLOAT = 'F';
    public final static byte CHARACTER = 'C';
    public final static byte DATE = 'D';

    public enum Data {
        Short,
        Long,
        Float,
        Double,
        String,
        Date;

        public final static Data For(DBFField field){
            switch(field.dataType){
            case NUMBER:
                if (0 == field.decimalCount){

                    if (9 > field.fieldLength){

                        return Data.Short;
                    }
                    else {
                        return Data.Long;
                    }
                }
                else {
                    return Data.Double;
                }

            case FLOAT:
                return Data.Float;
            case CHARACTER:
                return Data.String;
            case DATE:
                return Data.Date;
            default:
                throw new IllegalArgumentException(field.toString());
            }
        }
    }

    private DBFType()
    {
    }

}
