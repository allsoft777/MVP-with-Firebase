package com.seongil.mvplife.sample.ui.main.navdrawer;

import android.support.annotation.NonNull;

import com.seongil.mvplife.base.MvpView;

/**
 * @author seong-il, kim
 * @since 17. 3. 20
 */
public interface NavDrawerView extends MvpView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void renderUserThumbnail(@NonNull String url);

    void renderError(@NonNull Throwable t);
}
