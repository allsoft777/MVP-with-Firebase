package com.seongil.mvplife.sample.ui.base;

import android.support.annotation.NonNull;

import com.seongil.mvplife.base.MvpView;

/**
 * @author seong-il, kim
 * @since 2017. 5. 11.
 */
public interface BaseView extends MvpView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void showProgressDialog(@NonNull String msg);

    void dismissProgressDialog();
}
