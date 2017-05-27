package com.scowluga.android.microscience.wordpress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.scowluga.android.microscience.news.NewsProvider;
import com.scowluga.android.microscience.news.Post;
import com.scowluga.android.microscience.news.StorageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by scowluga on 5/24/2017.
 */

public class DataFetchAsyncTask extends AsyncTask<String, Void, Integer> {

    // RETURN KEYS
    public static final int KEY_NEW = 2;
    public static final int KEY_SAME = 1;
    public static final int KEY_EMPTY = 0;
    private static final int KEY_ERROR = -1;

    // INTERFACE
    public interface AsyncResponse {
        void processFinish(Integer output);
    }
    private AsyncResponse delegate = null;

    private ProgressDialog progressDialog;

    private HttpURLConnection urlConnection;
    private String PARSE_URL;
    private boolean showProgress;
    private List<Post> posts;
    private Activity activity;

    public DataFetchAsyncTask(List<Post> info, Activity a, boolean show, String url, AsyncResponse delegate){
        this.posts = info;
        this.PARSE_URL = url;
        this.delegate = delegate;
        this.showProgress = show;
        this.activity = a;

        if (this.showProgress) {
            progressDialog = new ProgressDialog(a);
        }
    }

    @Override
    protected void onPreExecute() {
        if (this.showProgress) {
            progressDialog.setMessage("Connecting to Database...");
            progressDialog.show();
        }
    }

    @Override
    protected Integer doInBackground(String... args) {
        StringBuilder result = new StringBuilder();
        int key;
        try {
            URL url = new URL(PARSE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            List<Post> temp = parseJSON(result.toString(), posts, this.activity.getApplicationContext());
            key = checkNew(posts, temp);
            return key;
        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return KEY_ERROR;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if (this.showProgress) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        delegate.processFinish(result);
    }

    private static final String MEDIA_URL = "https://microscience.on.ca/wp-json/wp/v2/media?parent=";

    public static List<Post> parseJSON(String output, List<Post> posts, Context context) {
        List<Post> info = new ArrayList<>();

        SimpleDateFormat inFmt = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outFmt = new SimpleDateFormat("MMM dd, yyyy");

        try {
            JSONArray jsonArray = new JSONArray(output);

            for (int i = 0; i < jsonArray.length(); i++) { // FOR EACH OBJECT
                JSONObject childObj = jsonArray.getJSONObject(i);
                String id = childObj.getString("id");
                String title = new JSONObject(childObj.getString("title")).getString("rendered");
                String content = (new JSONObject(childObj.getString("content")).getString("rendered")).replace("\\/", "/");
                String date = outFmt.format(inFmt.parse(childObj.getString("date").substring(0, 11)));
                String link = childObj.getString("link").replace("\\/", "/");

                if (posts.contains(new Post(id))) { // if it already exists, skip
                    info.add(new Post(id, title, content, date, link, "featured_image" + id));
                } else { // It's new!
                    String featured = childObj.getString("featured_media");
                    boolean noImage = featured.equals("0");
                    if (noImage) { // doesn't have an image, set tag
                        info.add(new Post(id, title, content, date, link, Post.NO_IMAGE));
                    } else { // HAS AN IMAGE
                        // READING IN
                        String name = "featured_image" + id;
                        URL url = new URL(MEDIA_URL + id + "?fields=guid");
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                        StringBuilder result = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);
                        }
                        String res = result.toString();

                        // GETTING NEW URL
                        JSONArray array = new JSONArray(res);
                        JSONObject imageObj = array.getJSONObject(0);
                        String imageLink = new JSONObject(imageObj.getString("guid")).getString("rendered").replace("\\/", "/");
                        Bitmap b = getBitmapFromURL(imageLink); // GETTING BITMAP

                        StorageManager.saveToInternalStorage(b, name, context); // saving

                        info.add(new Post(id, title, content, date, link, name));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    private Integer checkNew(List<Post> posts, List<Post> temp) {
        if (temp.size() == 0) {
            return KEY_EMPTY;
        } else if (posts.size() == temp.size()){
            return KEY_SAME;
        }
        NewsProvider.rewriteContacts(activity.getApplicationContext(), temp);
        return KEY_NEW;
    }

    public static Bitmap getBitmapFromURL(String src) throws IOException {
        URL url = new URL(src);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        Bitmap myBitmap = BitmapFactory.decodeStream(input);
        return myBitmap;
    }
}
