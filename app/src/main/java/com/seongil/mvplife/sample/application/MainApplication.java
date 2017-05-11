package com.seongil.mvplife.sample.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.seongil.mvplife.sample.service.ClipboardServiceHelper;
import com.squareup.leakcanary.LeakCanary;

/**
 * @author seong-il, kim
 * @since 2017. 4. 30.
 */
public class MainApplication extends Application {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private static MainApplication sInstance;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        ClipboardServiceHelper.startService();
    }

    // ========================================================================
    // methods
    // ========================================================================
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static Resources getRes() {
        return getAppContext().getResources();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
