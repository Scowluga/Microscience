package com.scowluga.android.microscience;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.scowluga.android.microscience.news.NewsAdapter;
import com.scowluga.android.microscience.news.NewsFragment;
import com.scowluga.android.microscience.news.NewsProvider;
import com.scowluga.android.microscience.news.Post;
import com.scowluga.android.microscience.wordpress.DataFetchAsyncTask;

import java.io.File;
import java.util.List;

import static com.scowluga.android.microscience.news.NewsFragment.fetchURL;
import static com.scowluga.android.microscience.news.NewsFragment.parseNews;

public class FirstRun extends AppCompatActivity {
    public static boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        // If this is the first run, then 'first' will not exist. Therefore defaulted to true
        first = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirst", true);

        if (wifiOn(getApplicationContext())) { // WIFI
            runSetUp();
        } else { // No Wifi
            if (first) {
                // Display + Finish;
                displayError("Error", "Please connect to Wifi for first run.");
            } else {
                beginMain();
            }
        }
    }

    private void runSetUp() {
        DataFetchAsyncTask asyncTask = (DataFetchAsyncTask) new DataFetchAsyncTask(FirstRun.this, true, fetchURL, new DataFetchAsyncTask.AsyncResponse(){
            @Override
            public void processFinish(String output){
                List<Post> temp = parseNews(output, NewsProvider.getPosts(getApplicationContext()));
                if (temp.size() > 0) {
                    NewsProvider.rewriteContacts(getApplicationContext(), temp);
                    if (first) {
                        // Set 'first' as false so the setup doesn't happen every time
                        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                                .putBoolean("isFirst", false).apply();
                    }
                    beginMain();
                } else { // empty list
                    if (first) {
                        displayError("Error", "Please connect to Wifi for first run.");
                    } else {
                        beginMain();
                    }
                }
            }
        }).execute();
    }

    private void displayError(String title, String message) { // Quits application. Cannot load data
        AlertDialog alertDialog = new AlertDialog.Builder(FirstRun.this).create();
        alertDialog.setTitle(title);
        alertDialog.setIcon(R.drawable.icon_exclamation);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(0); // Closes it
                    }
                });
        alertDialog.show();
    }

    private void beginMain() {
        Intent intent = new Intent(FirstRun.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public static boolean wifiOn(Context context) { // FROM NOW ON if (wifiOn) is checking if there is wifi
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifi.isConnected();

//        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//
//        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON
//
//            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//
//            if( wifiInfo.getNetworkId() == -1 ){
//                return false; // Not connected to an access point
//            }
//            return true; // Connected to an access point
//        }
//        else {
//            return false; // Wi-Fi adapter is OFF
//        }
    } // boolean for if there is wifi
}
