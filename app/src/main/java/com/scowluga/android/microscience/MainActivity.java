package com.scowluga.android.microscience;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.scowluga.android.microscience.nav2_about.AboutFragment;
import com.scowluga.android.microscience.nav7_qr.QrReader;
import com.scowluga.android.microscience.nav8_contact.ContactFragment;
import com.scowluga.android.microscience.nav4_featured.FeaturedFragment;
import com.scowluga.android.microscience.nav1_home.HomeFragment;
import com.scowluga.android.microscience.nav3_news.NewsFragment;
import com.scowluga.android.microscience.nav5_categories.ProductFragment;
import com.scowluga.android.microscience.nav6_training.TrainingFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAGFRAGMENT = "TAGFRAGMENT";
    public static final String NEWS_FILENAME = "news.txt";

    public static DrawerLayout drawer;
    public static Toolbar toolbar;
    public static MenuItem action_refresh;

    // ----------------------------------------------------------------------
    // ------------------ GENERAL LIFE CYCLE FUNCTIONS ----------------------
    // ----------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        setup();

        // INITIALIZE HOME
        if (FirstRun.open) {
            Fragment frag = HomeFragment.newInstance();
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .commit();
            FirstRun.open = false;
        }
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


    @Override
    protected void onPause() {
        super.onPause();
    }

    // ----------------------------------------------------------------------
    // ------------------ NAVIGATION DRAWER FUNCTIONS -----------------------
    // ----------------------------------------------------------------------

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        action_refresh = menu.findItem(R.id.action_refresh);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Fragment f = getSupportFragmentManager().findFragmentByTag(TAGFRAGMENT);
            if (f instanceof NewsFragment) { // IF NEWS FRAGMENT IS RUNNING ON UPDATE
                NewsFragment.refreshStorage(getApplicationContext(), this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            Fragment frag = HomeFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .addToBackStack(TAGFRAGMENT)
                    .commit();

        } else if (id == R.id.nav_about) {
            Fragment frag = AboutFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .addToBackStack(TAGFRAGMENT)
                    .commit();
        } else if (id == R.id.nav_news) {
            Fragment frag = NewsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .addToBackStack(TAGFRAGMENT)
                    .commit();
        } else if (id == R.id.nav_featured) {
            Fragment frag = FeaturedFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .addToBackStack(TAGFRAGMENT)
                    .commit();
        } else if (id == R.id.nav_products) {
            Fragment frag = ProductFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .addToBackStack(TAGFRAGMENT)
                    .commit();
        } else if (id == R.id.nav_training) {
            Fragment frag = TrainingFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frag_layout, frag, TAGFRAGMENT)
                    .addToBackStack(TAGFRAGMENT)
                    .commit();
        } else if (id == R.id.nav_qr) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) { // NOT GRANTED
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

            } else {
                launchQR();
            }
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
    // ------------------ GENERAL ACCESS FUNCTIONS --------------------------
    // ----------------------------------------------------------------------

    public static void websiteLaunch(View view) {
        String uri = "https://microscience.on.ca/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        view.getContext().startActivity(intent);
    }

    public static void contactLaunch(View view) {
        String uri = "https://microscience.on.ca/contact-us/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        view.getContext().startActivity(intent);
    }

    public static void rateLaunch(View view) {
        Context c = view.getContext();
        Uri uri = Uri.parse("market://details?id=" + c.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            c.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            c.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + c.getPackageName())));
        }
    }

    public static void remoteLaunch(View view) {
        String uri = "https://microscience.on.ca/remote-assistance-webinar/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        view.getContext().startActivity(intent);
    }

    // ----------------------------------------------------------------------
    // ------------------ QR CODE SCANNER FUNCTIONS -------------------------
    // ----------------------------------------------------------------------

    public static final String PRIVACY_URL = "https://microscience.on.ca/privacy-policy/";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Privacy Policy");
                    alertDialog.setMessage("Would you like to view the privacy policy of Microscience?");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_URL));
                                    startActivity(intent);
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            launchQR();
                        }
                    });
                    alertDialog.show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Please enable camera access", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void launchQR() {
        Intent intent = new Intent(MainActivity.this, QrReader.class);
        startActivity(intent);
    }
}
