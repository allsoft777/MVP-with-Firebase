package com.seongil.mvplife.sample.common.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * @author seong-il, kim
 * @since 17. 5. 9
 */
public class DefaultSharedPrefWrapper {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private static DefaultSharedPrefWrapper sInstance;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================
    public static synchronized DefaultSharedPrefWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new DefaultSharedPrefWrapper();
        }
        return sInstance;
    }

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    private static SharedPreferences getPreference(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putBoolean(final Context context, final String key, final boolean data) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editors = pref.edit();
        editors.putBoolean(key, data);
        editors.commit();
    }

    public void putString(final Context context, final String key, final String data) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editors = pref.edit();
        editors.putString(key, data);
        editors.commit();
    }

    public void putInt(final Context context, final String key, final int data) {
        SharedPreferences pref = getPreference(context);
        SharedPreferences.Editor editors = pref.edit();
        editors.putInt(key, data);
        editors.commit();
    }

    public boolean getBoolean(@NonNull final Context context, final String key) {
        SharedPreferences pref = getPreference(context);
        return pref != null && pref.getBoolean(key, false);
    }

    public String getString(@NonNull final Context context, final String key) {
        SharedPreferences pref = getPreference(context);
        return pref == null ? "" : pref.getString(key, "");
    }

    public String getString(@NonNull final Context context, final String key, final String defVale) {
        SharedPreferences pref = getPreference(context);
        return pref == null ? defVale : pref.getString(key, defVale);
    }

    public int getInt(@NonNull final Context context, final String key) {
        SharedPreferences pref = getPreference(context);
        return pref == null ? -1 : pref.getInt(key, -1);
    }

    public int getInt(@NonNull final Context context, final String key, final int defValue) {
        SharedPreferences pref = getPreference(context);
        return pref == null ? defValue : pref.getInt(key, defValue);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
