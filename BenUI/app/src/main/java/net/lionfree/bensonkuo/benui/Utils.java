package net.lionfree.bensonkuo.benui;

/**
 * Created by bensonkuo on 2015/10/23.
 */
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class Utils {

    public static void writeFile(Context context, String fileName, String content) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(content.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (Exception e) {
            e.toString();
        }
    }


    public static String readFile(Context context, String fileName) {
        try {
            FileInputStream fis = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];

            fis.read(buffer, 0, buffer.length);
            //Reads up to byteCount bytes from this stream and stores them in the byte array buffer starting at byteOffset.
            fis.close();

            return new String(buffer);

        } catch (FileNotFoundException e) {
            e.toString();
        } catch (IOException e) {
            e.toString();
        } catch (Exception e) {
            e.toString();
        }
        // when no file exists
        return "";
    }

    public static Uri getPhotoURI(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (! dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir, "123.png");

        return Uri.fromFile(file) ;
    }
}