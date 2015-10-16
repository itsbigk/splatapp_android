package tk.rockbutton.splatapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by max on 10/16/15.
 */
public class AppWidgetProvider1 extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        MapRotation dataset = JsonHelper.getMapRotations();

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            //Intent intent = new Intent(context, MainActivity.class);
            //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget);
            //views.setOnClickPendingIntent(R.id.button, pendingIntent);

            //Regular
            views.setTextViewText(R.id.rotcard_map1_mapName_regular, dataset.schedule.get(0).regular[0].nameEn);
            views.setTextViewText(R.id.rotcard_map2_mapName_regular, dataset.schedule.get(0).regular[1].nameEn);
            views.setImageViewResource(R.id.rotcard_map1_img_regular, RotationAdapter.getMapImage(dataset.schedule.get(0).regular[0].nameEn));
            views.setImageViewResource(R.id.rotcard_map2_img_regular, RotationAdapter.getMapImage(dataset.schedule.get(0).regular[1].nameEn));

            //Ranked
            views.setTextViewText(R.id.rotcard_header_rules, dataset.schedule.get(0).rankedRulesEn);
            views.setTextViewText(R.id.rotcard_map1_mapName_ranked, dataset.schedule.get(0).ranked[0].nameEn);
            views.setTextViewText(R.id.rotcard_map2_mapName_ranked, dataset.schedule.get(0).ranked[1].nameEn);
            views.setImageViewResource(R.id.rotcard_map1_img_ranked, RotationAdapter.getMapImage(dataset.schedule.get(0).ranked[0].nameEn));
            views.setImageViewResource(R.id.rotcard_map2_img_ranked, RotationAdapter.getMapImage(dataset.schedule.get(0).ranked[1].nameEn));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}