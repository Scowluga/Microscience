package com.scowluga.android.microscience;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class FirstRun extends AppCompatActivity {
    private static boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        // If this is the first run, then 'first' will not exist. Therefore defaulted to true
        first = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirst", true);

        if (first) { // First run
            if (wifiOn(getApplicationContext())) { // WIFI
                AsyncTask task = new ProgressTask(getApplicationContext(), FirstRun.this).execute();

            } else { // NO WIFI + FIRST RUN
                // Display + Finish;
                displayError("Error", "Please connect to Wifi for first run.");
            }
        } else { // Not first run --> Just begin Main
            beginMain();
        }
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

    // Setting up the application
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {

        private Context context;
        private ProgressDialog dialog;

        public ProgressTask(Context ctx, Activity act) {
            context = ctx;
            dialog = new ProgressDialog(act);
        }
        protected void onPreExecute() {
            dialog.setMessage("Setting up Application...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if (success) {
                // Start activity because successfuly set up
                if (first) {
                    // Set 'first' as false so the setup doesn't happen every time
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                            .putBoolean("isFirst", false).apply();
                }
                beginMain();
            } else {
                if (first) {
                    displayError("Error", "Unsuccessful setup. Please check Wifi connection.");
                } else {
                    Toast.makeText(context, "Error setting up. Please check Wifi connection.", Toast.LENGTH_LONG).show();
                }
            }
        }

        protected Boolean doInBackground(final String... args) {

            File f;
            if (first) {
                f = new File(getApplicationContext().getFilesDir(), MainActivity.NEWS_FILENAME);
            } else {
                // pass
            }


            return true;
        }
    }

    public static boolean wifiOn(Context context) { // FROM NOW ON if (wifiOn) is checking if there is wifi
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    } // boolean for if there is wifi
}
