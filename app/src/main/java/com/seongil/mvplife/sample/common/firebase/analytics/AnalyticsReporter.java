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
        return FirebaseAnalytics.getInstance(MainApplication.getAppContext());
    }

    public static void createNewClipManually() {
        getAnalytics().logEvent("cliplist_create_manually", new Bundle());
    }

    public static void shareClipItem(Bundle bundle) {
        getAnalytics().logEvent("cliplist_share_item", bundle);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public static class Param {

        public static final String ITEM_CNT = "item_count";
        public static final String TRIGGER_VIEW = "trigger_view";
        public static final String LIST_VIEW = "list_view";
        public static final String DETAIL_VIEW = "detail_view";
    }
}
