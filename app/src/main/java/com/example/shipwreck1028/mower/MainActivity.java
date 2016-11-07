package com.example.shipwreck1028.mower;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String className = this.getClass().getSimpleName();
    String qr_value = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AdMob
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-5640232104571176~1643007644");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Get the extra text with key "key" from the QRScannerActivity
        // and set the TextView to that text, will be implemented later.
        Log.w(className, "Getting QR Code");
        Intent startingIntent = getIntent();
        qr_value = startingIntent.getStringExtra("key");
        TextView t = (TextView) findViewById(R.id.qr_code);
        t.setTextColor(Color.parseColor("#FF0000"));
        t.setText(qr_value);

        // Setup the Toolbar
        Log.w(className, "Adding Toolbar to MainActivity.java");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Add the Drawer the the side and a listener to handle the intent.
        // You can find the action called from the drawer in the onNavigationItemSelected method.
        Log.w(className, "Adding Navigation Drawer to MainActivity.java");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Add a navigation View to the Main activity.
        Log.w(className, "Adding a navigation View to MainActivity.java");
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        // Disallowing them to press the back button here ensures that
        // when a user is first logged in, they cannot hit a virtual back button
        // to accidentally log themselves out. If they currently have the
        // navigation bar open, back button will collapse it.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.w(className, "Back Button Pressed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This simply creates the Options Menu.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This selects and option and adds flags for it to not
        // show in your history and gets the application context.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            final Intent i = new Intent();
            Context context = getApplicationContext();
            i.setData(Uri.parse("package:" + context.getPackageName()));
            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id;
        id = item.getItemId();
        if (id == R.id.nav_camera) {
            // Scan a QR Code
            Log.w(className, "Opening QR Code Scanner");
            Intent newRegisterWindow = new Intent(this, QRScannerActivity.class);
            startActivity(newRegisterWindow);
        } else if (id == R.id.nav_gallery) {
            // View the current status of the mower
            // This could possibly just become the landing page for the app
            // so we will see how that goes.
            Log.w(className, "Opening Mower Status");
        } else if (id == R.id.nav_manage) {
            // Submit A New Mower to the database
            Log.w(className, "Opening Mower Submission form");
            Intent sendStuff = new Intent(this, RegisterMowerActivity.class);
            startActivity(sendStuff);
        } else if (id == R.id.nav_share) {
            // Activate the sharing button feature
            Log.w(className, "Triggered share button");
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, R.string.share_subject);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, R.string.share_text);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_profile) {
            // Open the Profile Page for the current user
            Log.w(className, "Opening profile page");
        } else if (id == R.id.nav_logout) {
            // Logout button which actually just sends you back to the LoginScreen
            // Fun fact, this whole app kinda fakes the whole validation process ;)
            Log.w(className, "Logging out...");
            Intent sendStuff = new Intent(this, LoginActivity.class);
            startActivity(sendStuff);
        } else if (id == R.id.nav_send) {
            // Send application feedback to the developed (me) via email.
            Log.w(className, "Sending feedback via e-mail");
            Intent feedbackEmail = new Intent(Intent.ACTION_SEND);
            feedbackEmail.setType("text/email");
            feedbackEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"admin@aaronvigal.com"});
            feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            int STATUS = 0;
            startActivityForResult(Intent.createChooser(feedbackEmail, "Send Feedback:"), STATUS);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.w(className, "Feedback Success");
            Toast.makeText(this, R.string.feedback_ok, Toast.LENGTH_SHORT).show();
        } else {
            Log.w(className, "Feedback Failed");
            Toast.makeText(this, R.string.feedback_failed, Toast.LENGTH_SHORT).show();
        }
    }

}
