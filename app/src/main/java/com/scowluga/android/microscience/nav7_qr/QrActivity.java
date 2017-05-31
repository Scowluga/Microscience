package com.scowluga.android.microscience.nav7_qr;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.URLUtil;

import com.google.zxing.Result;
import com.scowluga.android.microscience.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {


    public ZXingScannerView zXingScannerView;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment7_activity_qr);

        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
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
}
