package io.skulltah.splatapp;

/**
 * Created by max on 10/21/15.
 */
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class Utils
{
    private static int sTheme;
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_BLACK = 1;
    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_DEFAULT:
                activity.setTheme(R.style.AppThemeLight);
                break;
            case THEME_BLACK:
                activity.setTheme(R.style.AppThemeDark);
                break;
//            case THEME_BLUE:
//                activity.setTheme(R.style.Thirdheme);
//                break;
        }
    }
}