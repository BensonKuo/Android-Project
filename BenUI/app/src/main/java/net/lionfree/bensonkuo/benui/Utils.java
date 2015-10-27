package net.lionfree.bensonkuo.benui;

/**
 * Created by bensonkuo on 2015/10/23.
 */

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;


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

    public static Uri getPhotoURI() {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, "123.png");

        return Uri.fromFile(file);
    }

    // 把檔案路徑位置變成 bytes
    public static byte[] uriToBytes(Context context, Uri uri) {
        try {
            // getContentResolver()是啥？
            // Return a ContentResolver instance for your application's package.
            InputStream is = context.getContentResolver().openInputStream(uri);
            //Open a stream on to the content associated with a content URI.

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            // 讀is內的東西到buffer?
            while ((len = is.read(buffer)) != -1) {
                // Reads a single byte from this stream and returns it as an integer in the range from 0 to 255. Returns -1 if the end of the stream has been reached. Blocks until one byte has been read, the end of the source stream is detected or an exception is thrown.
                baos.write(buffer, 0, len); //從buffer 寫到 baos?
            }
            // Returns the contents of this ByteArrayOutputStream as a byte array.
            return baos.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.toString();
        }
        return null;
    }

    // 把輸入的網址變成byte array
    public static byte[] urlToBytes(String urlStr) {
        try {
            URL url = new URL(urlStr);
            URLConnection connection = url.openConnection();
            // Returns a new connection to the resource referred to by this URL.
            InputStream is = connection.getInputStream();
            // Returns an input stream to read data from this socket.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1){
                baos.write(buffer,0,len);
            }
            return baos.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}