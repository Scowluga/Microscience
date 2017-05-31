package com.scowluga.android.microscience.nav3_news;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by robertlu on 2017-05-26.
 */

public class StorageManager {

    public static void saveToInternalStorage(Bitmap bitmapImage, String fileName, Context context) {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap loadImageFromStorage(String fileName, Context context) {
        try {
            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f=new File(directory, fileName);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }

}
