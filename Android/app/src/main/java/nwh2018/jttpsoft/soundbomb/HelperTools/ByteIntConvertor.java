package nwh2018.jttpsoft.soundbomb.HelperTools;

/**
 * Created by tjm on 1/14/18.
 */

public class ByteIntConvertor {
    private ByteIntConvertor(){}

    public static long bytesToLong(byte[] bytes){
        long s=0;
        for(int i=0; i<8;i++){
            s = (s<<8);
            byte currentByte = bytes[i];
            long intermedianByte = (long) currentByte;
            long intermedianLong = intermedianByte & 0xff;
            s = (s | intermedianLong);
        }
        return s;
    }

    public static byte[] longToBytes(long integer){
        byte[] bytes = new byte[8];
        for(int i=0;i<8;i++){
            long intermedianByte = (integer & 0xff);
            bytes[7-i] = (byte) intermedianByte;
            integer = (integer >> 8);
        }
        return bytes;
    }
}
