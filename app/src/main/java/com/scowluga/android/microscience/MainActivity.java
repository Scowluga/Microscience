package com.scowluga.android.microscience;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.google.zxing.Result;
import com.scowluga.android.microscience.contact.ContactFragment;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , ZXingScannerView.ResultHandler {

    private static final String TAGFRAGMENT = "TAGFRAGMENT";

    public DrawerLayout drawer;
    public Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();

        // INITIALIZE HOME
        Fragment frag = ContactFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                .addToBackStack(TAGFRAGMENT)
                .commit();
    }

    public void setup() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // ----------------------------------------------------------------------
    // ------------------ GENERAL LIFE CYCLE FUNCTIONS ----------------------
    // ----------------------------------------------------------------------

    @Override
    protected void onPause() {
        super.onPause();

        if (zXingScannerView != null) { // Scanner
            zXingScannerView.stopCamera();
        }
    }

    // ----------------------------------------------------------------------
    // ------------------ NAVIGATION DRAWER FUNCTIONS -----------------------
    // ----------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (zXingScannerView != null) {
                zXingScannerView.removeAllViews();
                zXingScannerView.stopCamera();
                zXingScannerView = null;
                setContentView(R.layout.activity_main);
                setup();
                Fragment frag = getSupportFragmentManager().findFragmentByTag(TAGFRAGMENT);
//                if (frag instanceof ContactFragment) {
//                    Toast.makeText(this, "Wehe", Toast.LENGTH_SHORT).show();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.frag_layout, ContactFragment.newInstance(), TAGFRAGMENT)
//                            .addToBackStack(TAGFRAGMENT)
//                            .commit();
//                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                        .addToBackStack(TAGFRAGMENT)
                        .commit();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_qr) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) { // NOT GRANTED
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            } else {
                launchQR();
            };
        } else if (id == R.id.nav_contact) {
            Fragment frag = ContactFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .addToBackStack(TAGFRAGMENT)
                    .commit();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // ----------------------------------------------------------------------
    // ------------------ QR CODE SCANNER FUNCTIONS -------------------------
    // ----------------------------------------------------------------------

    private ZXingScannerView zXingScannerView;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    public void handleResult(Result result) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("QR Scanning Results");
//        builder.setMessage(result.getText());
//        AlertDialog alert = builder.create();
//        alert.show();

        if (URLUtil.isValidUrl(result.getText())) { // if it's a url. go to


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(result.getText()));
            startActivity(intent);
            onBackPressed();
        }
        // Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
        zXingScannerView.resumeCameraPreview(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    launchQR();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please enable camera access", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void launchQR() {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

}
