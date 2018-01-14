package nwh2018.jttpsoft.soundbomb.Utilities;

import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import nwh2018.jttpsoft.soundbomb.Activities.SourceActivity;
import nwh2018.jttpsoft.soundbomb.Activities.TitleActivity;

public class Utilities {
    private static final String TAG = "soundbomb.Utilities";
    public static byte[] getFileAsByteArray(String fileName){
        File file = new File(fileName);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "Could not find file.");
        }





        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum); //no doubt here is 0
                //Writes len bytes from the specified byte array starting at offset off to this byte array output stream.
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e(TAG, "Could not write file stream data.");
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    public static void parseByteArrayAsFile(byte[] bytes){
        //below is the different part
        File someFile = new File("bufferedSong.mp3");
        try {
            FileOutputStream fos = new FileOutputStream(someFile);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getFileName(String filePath){
        String[] chunks = filePath.split("/");
        String[] nibbles = chunks[chunks.length - 1].split("\\.");
        return nibbles[nibbles.length - 2];
    }
}