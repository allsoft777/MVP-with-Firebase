package com.seongil.mvplife.delegate.activity;

import android.os.Bundle;

/**
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public interface MvpActivityDelegate {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void onCreate(Bundle bundle);

    void onDestroy();
}