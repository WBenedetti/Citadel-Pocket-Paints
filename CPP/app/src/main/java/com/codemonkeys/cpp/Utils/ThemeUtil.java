package com.codemonkeys.cpp.Utils;

/**
 * Created by eric on 2017-09-28.
 */

import android.app.Activity;
import android.content.Intent;

import com.codemonkeys.cpp.R;

public class ThemeUtil {
    private static int current_theme;
    public final static int LightTheme = 0;
    public final static int DarkTheme =1;
    public final static int RedTheme = 2;
    public final static int BlueTheme = 3;
    public final static int UserTheme = 4;

    public static void changeToTheme(Activity activity, int theme) {
        current_theme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void onActivityCreateSetTheme(Activity activity) {

        switch (current_theme) {
            default:
            case LightTheme:
                activity.setTheme(R.style.LightTheme);
                break;
            case DarkTheme:
                activity.setTheme(R.style.DarkTheme);
                break;
            case RedTheme:
                activity.setTheme(R.style.RedTheme);
                break;
            case BlueTheme:
                activity.setTheme(R.style.BlueTheme);
                break;
            case UserTheme:
                activity.setTheme(R.style.UserTheme);

                break;
        }
    }
}