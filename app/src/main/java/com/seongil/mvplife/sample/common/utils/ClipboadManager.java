package com.seongil.mvplife.sample.common.utils;

import android.content.Context;
import android.text.ClipboardManager;

import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.firebase.reporter.CrashReporter;

/**
 * @author seong-il, kim
 * @since 17. 5. 7
 */
public class ClipboadManager {

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
    public static void copyText(String text) {
        if (VersionChecker.isLowerThanHoneyComb()) {
            ClipboardManager clipboard = (ClipboardManager) MainApplication.getAppContext()
                  .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard =
                  (android.content.ClipboardManager) MainApplication.getAppContext()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = null;
            try {
                clip = android.content.ClipData.newPlainText(StringUtil.subString(text, 0, 20), text);
            } catch (Exception e) {
                CrashReporter.getInstance().report(e);
                e.printStackTrace();
            }
            clipboard.setPrimaryClip(clip);
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
