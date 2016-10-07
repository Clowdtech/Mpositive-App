package clowdtech.mpositive.easydata;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by jrgos on 07/06/2016.
 */
public class AppPreferences {

    private static String preferencesStr = "clowdtech.mpositive.easydata.preferences";

    public static String CURRENT_KEY = "clowdtech.mpositive.easydata.preferences.CURRENT_KEY";
    public static String CURRENT_SECRET = "clowdtech.mpositive.easydata.preferences.CURRENT_SECRET";
    public static String CURRENT_UID = "clowdtech.mpositive.easydata.preferences.CURRENT_UID";
    public static String CURRENT_TOKEN = "clowdtech.mpositive.easydata.preferences.CURRENT_TOKEN";
    public static String LAST_REGISTRATION_ERROR = "clowdtech.mpositive.easydata.preferences.LAST_REGISTRATION_ERROR";

    public static String IS_POLL_RUNNING = "clowdtech.mpositive.easydata.preferences.IS_POLL_RUNNING";
    public static String LAST_AUTH_TIME = "clowdtech.mpositive.easydata.preferences.LAST_AUTH_TIME";
    public static String LAST_UPLOAD_TIME = "clowdtech.mpositive.easydata.preferences.LAST_UPLOAD_TIME";
    public static String LAST_DOWNLOAD_TIME = "clowdtech.mpositive.easydata.preferences.LAST_DOWNLOAD_TIME";
    public static String LAST_TRANSACTION_TIME = "clowdtech.mpositive.easydata.preferences.LAST_TRANSACTION_TIME";

    public static boolean has(Context c, String name) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        return prefs.contains(name);
    }

    public static String get(Context c, String name, String defaultValue) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        return prefs.getString(name, defaultValue);
    }
    public static Boolean get(Context c, String name, Boolean defaultValue) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        return prefs.getBoolean(name, defaultValue);
    }
    public static int get(Context c, String name, int defaultValue) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        return prefs.getInt(name, defaultValue);
    }
    public static Long get(Context c, String name, Long defaultValue) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        return prefs.getLong(name, defaultValue);
    }
    public static Float get(Context c, String name, Float defaultValue) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        return prefs.getFloat(name, defaultValue);
    }
    public static ArrayList<String> get(Context c, String name, ArrayList<String> defaultValue) {
        ArrayList<String> vals = new ArrayList<String>();

        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        int size = prefs.getInt(name + "_Size", 0);

        if (size > 0)
            for(int ix = 0; ix < size; ix++) {
                vals.add(prefs.getString(name + "_" + String.valueOf(ix), ""));
            }
        else
            return defaultValue;

        return vals;
    }

    public static void set(Context c, String name, String value) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        prefs.edit().putString(name, value).commit();
    }
    public static void set(Context c, String name, Boolean value) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(name, value).commit();
    }
    public static void set(Context c, String name, Long value) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        prefs.edit().putLong(name, value).commit();
    }
    public static void set(Context c, String name, Float value) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        prefs.edit().putFloat(name, value).commit();
    }
    public static void set(Context c, String name, int value) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        prefs.edit().putInt(name, value).commit();
    }
    public static void set(Context c, String name, ArrayList<String> value) {
        SharedPreferences prefs = c.getSharedPreferences(preferencesStr, Context.MODE_PRIVATE);
        prefs.edit().putInt(name + "_Size", value.size()).commit();

        for(int ix = 0; ix < value.size(); ix++) {
            prefs.edit().putString(name + "_" + String.valueOf(ix), value.get(ix)).commit();
        }
    }
}
