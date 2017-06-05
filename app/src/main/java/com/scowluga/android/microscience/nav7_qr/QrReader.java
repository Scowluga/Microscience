package com.scowluga.android.microscience.nav7_qr;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;

import com.google.zxing.Result;
import com.scowluga.android.microscience.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.scowluga.android.microscience.MainActivity.PRIVACY_URL;

public class QrReader extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    public ZXingScannerView zXingScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment7_qr_reader);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Qr Reader");

        zXingScannerView = new ZXingScannerView(getApplicationContext());
        zXingScannerView.setResultHandler(this);

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.scanner_layout);
        layout.addView(zXingScannerView);

        zXingScannerView.startCamera();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        if (zXingScannerView != null) { // Scanner
            zXingScannerView.stopCamera();
        }
        super.onPause();
    }

    @Override
    public void handleResult(Result result) {
        final ZXingScannerView.ResultHandler handler = this;

        if (URLUtil.isValidUrl(result.getText())) { // if it's a url. go to
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
            startActivity(intent);
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("QR Scanning Results");
            builder.setMessage(result.getText());
            AlertDialog alert = builder.create();
            alert.show();
            alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    zXingScannerView.resumeCameraPreview(handler);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Privacy Policy");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL));
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }
}
