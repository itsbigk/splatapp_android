package tk.rockbutton.splatapp;

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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity context;

    View fragment_container;

    RotationFragment rotationFragment;

    private AlarmManager alarmMgr;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        fragment_container = findViewById(R.id.main_fragment_container);

        rotationFragment = new RotationFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, rotationFragment)
                .commit();

        Intent intent = new Intent(this, NotificationBackgroundTask.class);
        PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                1000L,
                1000L, pi);
        //INTERVAL_FIFTEEN_MINUTES
        startService(intent);

        checkForUpdates();
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_notification) {
            toggleNotification();
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
            case R.id.nav_website:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.rockbutton.tk/splatapp"));
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

    private void toggleNotification() {
        SharedPreferences prefs = this.getSharedPreferences(
                "tk.rockbutton.splatapp", Context.MODE_PRIVATE);

        boolean isNotificationActive = prefs.getBoolean("isNotificationActive", false);

        prefs.edit().putBoolean("isNotificationActive", !isNotificationActive).apply();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (isNotificationActive)
            notificationManager.cancel(0);
        else {
            updateNotification();
        }
    }

    public void updateNotification() {
        SharedPreferences prefs = getSharedPreferences(
                "tk.rockbutton.splatapp", Context.MODE_PRIVATE);

        boolean isNotificationActive = prefs.getBoolean("isNotificationActive", false);

        if (!isNotificationActive) return;

        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);

        MapRotation dataset = JsonHelper.getMapRotations();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);

        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.big_notification_layout);
        // Rotation 1
        expandedView.setTextViewText(R.id.rotnot_rot1_ranked_rules, dataset.schedule.get(0).rankedRulesEn);
        expandedView.setTextViewText(R.id.rotnot_rot1_map1_name_regular, dataset.schedule.get(0).regular[0].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot1_map2_name_regular, dataset.schedule.get(0).regular[1].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot1_map1_name_ranked, dataset.schedule.get(0).ranked[0].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot1_map2_name_ranked, dataset.schedule.get(0).ranked[1].nameEn);
        // Rotation 2
        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String starttime2 = dateFormatter.format(new Date(dataset.schedule.get(1).startTime));
        String endtime2 = dateFormatter.format(new Date(dataset.schedule.get(1).endTime));
        expandedView.setTextViewText(R.id.rotnot_rot2_time, starttime2 + "\r\n" + endtime2);
        expandedView.setTextViewText(R.id.rotnot_rot2_ranked_rules, dataset.schedule.get(1).rankedRulesEn);
        expandedView.setTextViewText(R.id.rotnot_rot2_map1_name_regular, dataset.schedule.get(1).regular[0].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot2_map2_name_regular, dataset.schedule.get(1).regular[1].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot2_map1_name_ranked, dataset.schedule.get(1).ranked[0].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot2_map2_name_ranked, dataset.schedule.get(1).ranked[1].nameEn);
        // Rotation 3
        String starttime3 = dateFormatter.format(new Date(dataset.schedule.get(2).startTime));
        String endtime3 = dateFormatter.format(new Date(dataset.schedule.get(2).endTime));
        expandedView.setTextViewText(R.id.rotnot_rot3_time, starttime3 + "\r\n" + endtime3);
        expandedView.setTextViewText(R.id.rotnot_rot3_ranked_rules, dataset.schedule.get(2).rankedRulesEn);
        expandedView.setTextViewText(R.id.rotnot_rot3_map1_name_regular, dataset.schedule.get(2).regular[0].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot3_map2_name_regular, dataset.schedule.get(2).regular[1].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot3_map1_name_ranked, dataset.schedule.get(2).ranked[0].nameEn);
        expandedView.setTextViewText(R.id.rotnot_rot3_map2_name_ranked, dataset.schedule.get(2).ranked[1].nameEn);

        Notification.Builder builder = new Notification.Builder(this);
        builder
                .setContentIntent(intent)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.ic_stat_name_regular)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MIN);
        Notification notification = new Notification.BigTextStyle(builder)
                .build();
        notification.contentView = contentView;
        notification.bigContentView = expandedView;

        notificationManager.notify(0, notification);
    }

    public static void checkForUpdates() {
        // Load items
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject json = null;
                try {
                    json = new JSONObject(JsonHelper.getJsonFromUrl("http://rockbutton.tk/splatapp/checkforupdates.php"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (json == null) return;

                final SplatappUpdate splatappUpdate = new SplatappUpdate();
                try {
                    splatappUpdate.lastUpdated = json.getLong("lastUpdated");
                    splatappUpdate.changelog = json.getString("changelog");
                    splatappUpdate.updateUrl = json.getString("updateUrl");
                    splatappUpdate.latestVersion = json.getString("latestVersion");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    PackageInfo pInfo = MainActivity.context.getPackageManager().getPackageInfo(MainActivity.context.getPackageName(), 0);
                    if (!pInfo.versionName.equals(splatappUpdate.latestVersion)) {
                        MainActivity.context.runOnUiThread(new Runnable() {
                            public void run() {
                                // Need to update!
                                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.context);
                                dlgAlert.setMessage(splatappUpdate.changelog);
                                dlgAlert.setTitle("Update available!");
                                dlgAlert.setPositiveButton("Update",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(splatappUpdate.updateUrl));
                                                MainActivity.context.startActivity(browserIntent);
                                            }
                                        });
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();
                            }
                        });
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
