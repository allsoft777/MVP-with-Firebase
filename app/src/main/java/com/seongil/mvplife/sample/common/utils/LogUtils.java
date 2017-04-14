package com.seongil.mvplife.sample.common.utils;

import android.util.Log;

import java.util.Locale;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class LogUtils {

    // ========================================================================
    // constants
    // ========================================================================
    private final static String LOG_TAG = "FirebasePlayground";
    private final static String MATCH = "[ %s %dL ] ";
    private final static boolean SWITCH = true;

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

    private static String buildHeader(String classTag) {
        StackTraceElement stack = Thread.currentThread().getStackTrace()[4];
        return stack == null ? "UNKNOWN" : String.format(Locale.getDefault(),
              MATCH, classTag, stack.getLineNumber());
    }

    public static void v(String classTag, Object msg) {
        if (SWITCH) {
            Log.v(LOG_TAG, buildHeader(classTag) + msg.toString());
        }
    }

    public static void d(String classTag, Object msg) {
        if (SWITCH) {
            Log.d(LOG_TAG, buildHeader(classTag) + msg.toString());
        }
    }

    public static void i(String classTag, Object msg) {
        if (SWITCH) {
            Log.i(LOG_TAG, buildHeader(classTag) + msg.toString());
        }
    }

    public static void w(String classTag, Object msg) {
        if (SWITCH) {
            Log.w(LOG_TAG, buildHeader(classTag) + msg.toString());
        }
    }

    public static void e(String classTag, Object msg) {
        if (SWITCH) {
            Log.e(LOG_TAG, buildHeader(classTag) + msg.toString());
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}