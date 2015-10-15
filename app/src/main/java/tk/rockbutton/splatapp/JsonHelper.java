package tk.rockbutton.splatapp;

import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 10/15/15.
 */
public class JsonHelper {
    private static final String mapRotationsUrl = "https://splatoon.ink/schedule.json";
    private static final int timeout = 5000;

    public static String getMapRotationsJson() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection c = null;
        try {
            URL u = new URL(mapRotationsUrl);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }

        } catch (IOException ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
//                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    public static MapRotation getMapRotations() {
        JSONObject json = null;
        try {
            json = new JSONObject(JsonHelper.getMapRotationsJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MapRotation mapRotation = new MapRotation();
        try {
            mapRotation.updateTime = json.getLong("updateTime");
            mapRotation.schedule = JsonHelper.getSchedule(json, mapRotation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mapRotation;
    }

    public static List<MapRotation.ScheduleItem> getSchedule(JSONObject json, MapRotation mapRotation) throws JSONException {
        List<MapRotation.ScheduleItem> schedule = new ArrayList<>();
        JSONArray scheduleArray = json.getJSONArray("schedule");
        for (int i = 0; i < scheduleArray.length(); i++) {
            JSONObject json_data = scheduleArray.getJSONObject(i);
            MapRotation.ScheduleItem scheduleItem = mapRotation.new ScheduleItem();

            scheduleItem.startTime = json_data.getLong("startTime");
            scheduleItem.endTime = json_data.getLong("endTime");
            scheduleItem.regular = JsonHelper.getMapMode(json_data, scheduleItem, "regular");
            scheduleItem.ranked = JsonHelper.getMapMode(json_data, scheduleItem, "ranked");
            scheduleItem.rankedRulesJp = json_data.getJSONObject("ranked").getString("rulesJP");
            scheduleItem.rankedRulesEn = json_data.getJSONObject("ranked").getString("rulesEN");
            schedule.add(scheduleItem);
        }

        return schedule;
    }

    public static MapRotation.ScheduleItem.MapItem[] getMapMode(JSONObject json, MapRotation.ScheduleItem scheduleItem, String mode) throws JSONException {
        List<MapRotation.ScheduleItem.MapItem> mapCollectionItems = new ArrayList<>();
        JSONArray modeArray = json.getJSONObject(mode).getJSONArray("maps");
        for (int i = 0; i < 2; i++) {
            JSONObject json_data = modeArray.getJSONObject(i);
            MapRotation.ScheduleItem.MapItem mapCollectionItem = scheduleItem.new MapItem();

            mapCollectionItem.nameJp = json_data.getString("nameJP");
            mapCollectionItem.nameEn = json_data.getString("nameEN");

            mapCollectionItems.add(mapCollectionItem);
        }

        return mapCollectionItems.toArray(new MapRotation.ScheduleItem.MapItem[mapCollectionItems.size()]);
    }
}
