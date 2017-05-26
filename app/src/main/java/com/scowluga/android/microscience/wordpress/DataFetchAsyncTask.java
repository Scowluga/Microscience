package com.scowluga.android.microscience.wordpress;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by scowluga on 5/24/2017.
 */

public class DataFetchAsyncTask extends AsyncTask<String, Void, String> {

    // INTERFACE
    public interface AsyncResponse {
        void processFinish(String output);
    }
    private AsyncResponse delegate = null;

    private ProgressDialog progressDialog;

    private HttpURLConnection urlConnection;
    private String PARSE_URL;
    private boolean showProgress;


    public DataFetchAsyncTask(Activity a, boolean show, String url, AsyncResponse delegate){
        this.PARSE_URL = url;
        this.delegate = delegate;
        this.showProgress = show;

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
    protected String doInBackground(String... args) {

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(PARSE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        if (this.showProgress) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        // result is JSON String
        delegate.processFinish(result);

    }
}
