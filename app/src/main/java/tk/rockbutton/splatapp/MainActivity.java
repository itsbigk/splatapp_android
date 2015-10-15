package tk.rockbutton.splatapp;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static MainActivity context;

    View fragment_container;

    RotationFragment rotationFragment;

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

        fragment_container = findViewById(R.id.main_fragment_container);

        rotationFragment = new RotationFragment();

        getFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, rotationFragment)
                .commit();
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

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (isNotificationActive)
            mNotificationManager.cancel(1);
        else {
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
//            contentView.setImageViewResource(R.id.image, R.drawable.match_regular);
//            contentView.setTextViewText(R.id.title, "Custom notification");
//            contentView.setTextViewText(R.id.text, "This is a custom layout");

            RemoteViews bigContentView = new RemoteViews(getPackageName(), R.layout.big_notification_layout);

            Notification mNotifyBuilder = new Notification.Builder(this)
                    .setContentTitle("New Message")
                    .setContentText("You've received new messages.")
                    .setSmallIcon(R.drawable.match_ranked)
                    .setLargeIcon(BitmapFactory.decodeResource(
                            getResources(), R.drawable.match_regular))
                    .setContent(contentView)
                    .setStyle(new Notification.BigTextStyle().bigText("sesegsegesge"))
                    .build();

            mNotifyBuilder.bigContentView = bigContentView;

            mNotificationManager.notify(
                    1,
                    mNotifyBuilder);
        }
    }
}
