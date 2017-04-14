package com.seongil.mvplife.sample.common.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import static com.google.common.base.Preconditions.checkNotNull;

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
          @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        fragmentManager.beginTransaction()
              .add(frameId, fragment)
              .commit();
    }

    public static void replaceFragmentToActivity(@NonNull FragmentManager fragmentManager,
          @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        fragmentManager.beginTransaction()
              .replace(frameId, fragment)
              .commit();
    }

    public static void replaceFragmentToStack(@NonNull FragmentManager fragmentManager,
          @NonNull Fragment fragment, int frameId) {
        fragmentManager.beginTransaction()
              .replace(frameId, fragment)
              .addToBackStack(null)
              .commit();
    }

    public static void startActivity(Context context, Intent intent) {
        checkNotNull(context);
        checkNotNull(intent);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            LogUtils.e(TAG, e.toString());
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}