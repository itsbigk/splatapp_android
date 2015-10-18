package tk.rockbutton.splatapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by max on 10/16/15.
 */
public class NotificationBackgroundTask extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        updateWidget();
        updateNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWidget() {
        //String lastUpdated = DateFormat.format("hh:mm:ss", new Date()).toString();

        // Create an intent to launce something
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pendingintent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        RemoteViews view = new RemoteViews(getPackageName(), R.layout.appwidget);
        MapRotation dataset = JsonHelper.getMapRotations();

        view.setOnClickPendingIntent(R.id.widget1_main, pendingintent);

        //Regular
        view.setTextViewText(R.id.rotcard_map1_mapName_regular, dataset.schedule.get(0).regular[0].nameEn);
        view.setTextViewText(R.id.rotcard_map2_mapName_regular, dataset.schedule.get(0).regular[1].nameEn);
        view.setImageViewResource(R.id.rotcard_map1_img_regular, RotationAdapter.getMapImage(dataset.schedule.get(0).regular[0].nameEn));
        view.setImageViewResource(R.id.rotcard_map2_img_regular, RotationAdapter.getMapImage(dataset.schedule.get(0).regular[1].nameEn));

        //Ranked
        view.setTextViewText(R.id.rotcard_header_rules, dataset.schedule.get(0).rankedRulesEn);
        view.setTextViewText(R.id.rotcard_map1_mapName_ranked, dataset.schedule.get(0).ranked[0].nameEn);
        view.setTextViewText(R.id.rotcard_map2_mapName_ranked, dataset.schedule.get(0).ranked[1].nameEn);
        view.setImageViewResource(R.id.rotcard_map1_img_ranked, RotationAdapter.getMapImage(dataset.schedule.get(0).ranked[0].nameEn));
        view.setImageViewResource(R.id.rotcard_map2_img_ranked, RotationAdapter.getMapImage(dataset.schedule.get(0).ranked[1].nameEn));

        ComponentName thisWidget = new ComponentName(this, AppWidgetProvider1.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, view);
    }



    public void updateNotification() {
        SharedPreferences prefs = getSharedPreferences(
                "tk.rockbutton.splatapp", Context.MODE_PRIVATE);

        boolean isNotificationActive = prefs.getBoolean("isNotificationActive", false);

        if (!isNotificationActive) return;

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

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        Notification.Builder builder = new Notification.Builder(this);
        builder
                .setContentIntent(resultPendingIntent)
                .setContentTitle("")
                .setContentText("")
                .setSmallIcon(R.drawable.ic_stat_name_regular)
                .setPriority(Notification.PRIORITY_MIN)
                .setOngoing(true);
        Notification notification = new Notification.BigTextStyle(builder)
                .build();
        notification.contentView = contentView;
        notification.bigContentView = expandedView;

        notificationManager.notify(0, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}