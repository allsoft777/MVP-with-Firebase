package com.seongil.mvplife.sample.common.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class ActivityUtils {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "ActivityUtils";

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
          @NonNull Fragment fragment, int frameId, @Nullable String tag) {
        fragmentManager.beginTransaction()
              .add(frameId, fragment, tag)
              .commit();
    }

    public static void replaceFragmentToActivity(@NonNull FragmentManager fragmentManager,
          @NonNull Fragment fragment, int frameId, @Nullable String tag) {
        fragmentManager.beginTransaction()
              .replace(frameId, fragment, tag)
              .commit();
    }

    public static void replaceFragmentToStack(@NonNull FragmentManager fragmentManager,
          @NonNull Fragment fragment, int frameId, @Nullable String tag) {
        fragmentManager.beginTransaction()
              .replace(frameId, fragment, tag)
              .addToBackStack(null)
              .commit();
    }

    public static void startActivity(@NonNull Context context, @NonNull Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtils.e(TAG, e.toString());
        }
    }

    public static void startActivityForResult(@NonNull Activity activity, @NonNull Intent intent, int requestCode) {
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            LogUtils.e(TAG, e.toString());
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}