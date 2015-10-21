package io.skulltah.splatapp;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity context;

    View fragment_container;
    ImageView miiIcon;
    TextView miiName;
    NavigationView navigationView;

    RotationFragment rotationFragment;
    RankFragment rankFragment;
    FriendListFragment friendListFragment;
    EquipmentFragment equipmentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        fragment_container = findViewById(R.id.main_fragment_container);

        rotationFragment = new RotationFragment();
        rankFragment = new RankFragment();
        friendListFragment = new FriendListFragment();
        equipmentFragment = new EquipmentFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, rotationFragment)
                .commit();

        Intent intent = new Intent(this, NotificationBackgroundTask.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                1800000,
                1800000, pi);
        startService(intent);
    }

    public static void goToDefaultFragment(){
        context.getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, context.rotationFragment)
                        .commit();
        context.navigationView.getMenu().getItem(0).setChecked(true);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
//            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("Exception", e.getMessage());
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        miiIcon = (ImageView) findViewById(R.id.mii_icon);
        miiName = (TextView) findViewById(R.id.mii_name);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.context);
        if (!prefs.getString("mii_name", "").equals("")) {
            miiIcon.setImageBitmap(getBitmapFromURL(prefs.getString("mii_icon", "")));
            miiName.setText(prefs.getString("mii_name", ""));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_about:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("About");
                alertDialog.setMessage(getString(R.string.copyright));
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                return true;
            case R.id.action_settings:
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(myIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //Fragment fragment;
        Fragment fragment = null;

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_rotaton:
                fragment = rotationFragment;
                break;
            case R.id.nav_rank:
                fragment = rankFragment;
                break;
            case R.id.nav_friend_list:
                fragment = friendListFragment;
                break;
            case R.id.nav_equipment:
                fragment = equipmentFragment;
                break;
            case R.id.nav_website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://skulltah.github.io/splatapp"));
                startActivity(browserIntent);
                break;
        }

        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_fragment_container, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
