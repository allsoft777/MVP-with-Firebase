package com.seongil.mvplife.sample.ui.main;

import android.content.ContentResolver;
import android.support.annotation.NonNull;

import com.seongil.mvplife.base.MvpView;

/**
 * @author seong-il, kim
 * @since 17. 3. 30
 */
public interface MainView extends MvpView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    ContentResolver getContentResolver();

    void renderError(@NonNull Throwable t);
}
