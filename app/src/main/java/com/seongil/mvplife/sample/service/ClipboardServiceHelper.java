package com.seongil.mvplife.sample.service;

import android.content.Intent;

import com.seongil.mvplife.sample.application.MainApplication;

/**
 * @author seong-il, kim
 * @since 17. 5. 3
 */
public class ClipboardServiceHelper {

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
    public static void stopService() {
        Intent intent = new Intent(MainApplication.getAppContext(), ClipboardService.class);
        MainApplication.getAppContext().stopService(intent);
    }

    public static void startService() {
        Intent intent = new Intent(MainApplication.getAppContext(), ClipboardService.class);
        MainApplication.getAppContext().startService(intent);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
