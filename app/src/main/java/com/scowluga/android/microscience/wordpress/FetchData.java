package com.scowluga.android.microscience.wordpress;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class FetchData {

    public interface AsyncResponse {
        void processFinish(String output);
    }
//    https://stackoverflow.com/questions/12575068/how-to-get-the-result-of-onpostexecute-to-main-activity-because-asynctask-is-a

    public static class getData extends AsyncTask<String, Void, String> {

        private Context context;
        private Activity activity;
        private ProgressDialog progressDialog;

        private HttpURLConnection urlConnection;
        private String PARSE_URL = "https://microscience.on.ca/api/?json=get_news&post_id=1";

        public getData (Context c, Activity act, String url) {
            this.PARSE_URL = url;
            this.context = c;
            this.activity = act;

            progressDialog = new ProgressDialog(this.activity);

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
            //Do something with the JSON string
//            parseJSON(result);
//            return result;
//            return result;

        }

    }

    private void parseJSON(String data){

        try{

            JSONObject jsonResponse = new JSONObject(data);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("posts");
            int postCount = Integer.parseInt(jsonResponse.getString("count"));

            int jsonArrLength = jsonMainNode.length();

            for(int i=0; i < jsonArrLength; i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String postTitle = jsonChildNode.getString("slug");
                String postUrl = jsonChildNode.getString("url");

//                tvPostCount.setText("Number of posts:" +postCount);
//                tvPostTitle.setText("Page title:" +postTitle);
//                tvPostUrl.setText("Page PARSE_URL:" +postUrl);
            }

        }catch(Exception e){
            Log.i("App", "Error parsing data" +e.getMessage());

        }
    }
}