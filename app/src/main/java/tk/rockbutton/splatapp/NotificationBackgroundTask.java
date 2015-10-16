package tk.rockbutton.splatapp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Debug;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by max on 10/16/15.
 */
public class NotificationBackgroundTask extends Service {

    private final int UPDATE_INTERVAL = 60 * 1000;
    private Timer timer = new Timer();
    private static final int NOTIFICATION_EX = 1;
    private NotificationManager notificationManager;

    public NotificationBackgroundTask() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // code to execute when the service is first created
        super.onCreate();
        updateNotification();
    }

    public void updateNotification()
    {
        MainActivity.updateNotification();
        Log.i("Service", "updating notification...");
//        final Calendar cld = Calendar.getInstance();
//
//        int time = cld.get(Calendar.HOUR_OF_DAY);
//        if(time>12)
//        {
//            createNotification(null);
//        }
//        else
//        {
//            alert.setMessage("Not yet");
//        }
    }

    @Override
    public void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid)
    {
        return START_STICKY;
    }

    private void stopService() {
        if (timer != null) timer.cancel();
    }
}