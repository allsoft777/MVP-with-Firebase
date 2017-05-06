package com.seongil.mvplife.sample.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.seongil.mvplife.sample.common.utils.LogUtil;
import com.seongil.mvplife.sample.service.ClipboardService;

/**
 * @author seong-il, kim
 * @since 17. 5. 2
 */
public class ClipDiaryMainReceiver extends BroadcastReceiver {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "ClipDiaryMainReceiver";

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
    @Override
    public void onReceive(Context context, Intent intent) {
        handleAction(context, intent);
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void handleAction(Context context, Intent intent) {
        final String action = intent.getAction();
        if (TextUtils.isEmpty(action)) {
            LogUtil.e(TAG, "action is empty");
            return;
        }

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
            launchClipboardObserver(context);
        }
    }

    private void launchClipboardObserver(Context context) {
        Intent intent = new Intent(context, ClipboardService.class);
        context.startService(intent);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
