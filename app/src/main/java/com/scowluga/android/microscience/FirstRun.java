package com.scowluga.android.microscience;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class FirstRun extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);

        // If this is the first run, then 'first' will not exist. Therefore defaulted to true
        boolean first = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirst", true);

        if (first) { // First run
            // Executing the set up of the application
            AsyncTask task = new ProgressTask(getApplicationContext(), FirstRun.this).execute();

            // Set 'first' as false so the setup doesn't happen every time
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirst", false).apply();

        } else {
            // Just start the activity
            Intent intent = new Intent(FirstRun.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

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
                Intent intent = new Intent(FirstRun.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(context, "Error setting up", Toast.LENGTH_LONG).show();
            }
        }

        protected Boolean doInBackground(final String... args) {

            return true;
        }
    }

}
