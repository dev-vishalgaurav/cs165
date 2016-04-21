package edu.cs.dartmouth.cs165.myruns.vishal.storage.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import edu.cs.dartmouth.cs165.myruns.vishal.R;
import edu.cs.dartmouth.cs165.myruns.vishal.global.MyRunsApp;

/**
 * Helper class to wrap the operations on shared preferences.
 */
public class PreferenceUtils {
    public static final String TAG = "PrefUtils";
    public static final String EXTRA_NAME = "extra_name";
    public static final String EXTRA_EMAIL = "extra_email";
    public static final String EXTRA_PHONE = "extra_phone";
    public static final String EXTRA_CLASS = "extra_class";
    public static final String EXTRA_MAJOR = "extra_major";
    public static final String EXTRA_GENDER_SELECTED = "extra_gender_selected";
    public static final String EXTRA_GENDER = "extra_gender";
    public static final String EXTRA_IMAGE = "extra_image";
    public static final String EXTRA_PROFILE_EXISTS = "extra_profile_exists";

    /**
     * Save all the preferences of profile settings to Shared preferences
     * @param context
     * @param bundle
     */
    public static void saveProfileSettings(Context context, Bundle bundle) {
        putBoolean(context, EXTRA_PROFILE_EXISTS, true);
        putString(context, EXTRA_NAME, bundle.getString(EXTRA_NAME));
        putString(context, EXTRA_EMAIL, bundle.getString(EXTRA_EMAIL));
        putString(context, EXTRA_PHONE, bundle.getString(EXTRA_PHONE));
        putString(context, EXTRA_CLASS, bundle.getString(EXTRA_CLASS));
        putString(context, EXTRA_MAJOR, bundle.getString(EXTRA_MAJOR));
        putBoolean(context, EXTRA_GENDER_SELECTED, true);
        putBoolean(context, EXTRA_GENDER, bundle.getBoolean(EXTRA_GENDER));
        Uri image = bundle.getParcelable(EXTRA_IMAGE);
        if(image!=null){
            MyRunsApp.printTrace(TAG,"Image uri = " + image.toString());
            putString(context, EXTRA_IMAGE, image.toString());
        }
    }

    /**
     * get all the preferences for profile settings in Bundle
     * @param context
     * @return
     */
    public static Bundle getProfileSettings(Context context) {
        Bundle bundle = null;
        if (getBoolean(context, EXTRA_PROFILE_EXISTS, false)) {
            bundle = new Bundle();
            bundle.putString(EXTRA_NAME, getString(context, EXTRA_NAME, ""));
            bundle.putString(EXTRA_EMAIL, getString(context, EXTRA_EMAIL, ""));
            bundle.putString(EXTRA_PHONE, getString(context, EXTRA_PHONE, ""));
            bundle.putString(EXTRA_CLASS, getString(context, EXTRA_CLASS, ""));
            bundle.putString(EXTRA_MAJOR, getString(context, EXTRA_MAJOR, ""));
            bundle.putBoolean(EXTRA_GENDER_SELECTED, getBoolean(context, EXTRA_GENDER_SELECTED, true));
            bundle.putBoolean(EXTRA_GENDER, getBoolean(context, EXTRA_GENDER, false));
            String image = getString(context,EXTRA_IMAGE,null);
            MyRunsApp.printTrace(TAG, "Image uri = " + image);
            if (image != null && !image.isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(image);
                    bundle.putParcelable(EXTRA_IMAGE, imageUri);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return bundle;
    }

    /**
     * put a string in preferences
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * get a string from prefernce
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defaultValue);
    }

    /**
     * put boolean into shared preferences
     * @param context
     * @param key
     * @param defaultValue
     * @return
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defaultValue);
    }

    /**
     * get boolean from shared preferences
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }
    public static int getUnitType(Context context){
        try{
            String unit = getString(context,context.getString(R.string.pref_unit_key),"1");
            return Integer.parseInt(unit);
        }catch (NumberFormatException ex){
            return 1;
        }
    }

    public static String getDistanceUnit(Context context) {
        return (getUnitType(context) == 1) ? context.getString(R.string.kms) : context.getString(R.string.miles);
    }
}
