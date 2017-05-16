package com.seongil.mvplife.sample.common.firebase.analytics;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.seongil.mvplife.sample.application.MainApplication;

/**
 * @author seong-il, kim
 * @since 17. 5. 15
 */
public class AnalyticsReporter {

    // ========================================================================
    // constants
    // ========================================================================

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
    private static FirebaseAnalytics getAnalytics() {
        FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(MainApplication.getAppContext());
        analytics.setAnalyticsCollectionEnabled(true);
        analytics.setMinimumSessionDuration(5000);
        return analytics;
    }

    public static void createNewClipManually() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "create_new_item");
        getAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void shareClipItem(Bundle bundle) {
        getAnalytics().logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
