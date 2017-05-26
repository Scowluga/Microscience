package com.scowluga.android.microscience.news;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.scowluga.android.microscience.MainActivity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_APPEND;

/**
 * Created by scowluga on 5/25/2017.
 */

public class NewsProvider {

    public static List<Post> getPosts(Context context) {
        // Getting the entire string of the file
        String file = getString(context, MainActivity.NEWS_FILENAME);
        if (TextUtils.isEmpty(file)) { // If there is no text in the file (empty)
            return new ArrayList<>();
        }
        return Post.grandDecode(file);
    };

    public static void rewriteContacts(Context context, List<Post> posts) { // rewriting the file
        // Clears file
        clearFile(context, MainActivity.NEWS_FILENAME);

        String write = Post.grandEncode(posts);

        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(MainActivity.NEWS_FILENAME, MODE_APPEND);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write(write);
            writer.close();
            outputStream.close();
        } catch (Exception e) {
            Toast.makeText(context, "Error saving contacts.", Toast.LENGTH_SHORT).show();
        }
    }

    public static void clearFile(Context context, String fileName) { // Clears file
        // Writes empty string to file
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write("");
            writer.close();
            outputStream.close();
        } catch (Exception e) {
            Toast.makeText(context, "Error writing to file.", Toast.LENGTH_SHORT).show();
        }
    }
    public static String getString(Context context, String fileName) {
        // Get entire string of the file
        FileInputStream inputStream;
        try {
            inputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

            char[] inputBuffer = new char[3000];
            String s="";
            int charRead;

            while ((charRead=inputStreamReader.read(inputBuffer))>0) {
                String readstring = String.copyValueOf(inputBuffer, 0, charRead);
                s += readstring;
            }
            inputStream.close();
            inputStreamReader.close();
            return s;
        } catch (Exception e) {
            Toast.makeText(context, "Error reading from file.", Toast.LENGTH_SHORT).show();
        }
        return "";
    }

}
