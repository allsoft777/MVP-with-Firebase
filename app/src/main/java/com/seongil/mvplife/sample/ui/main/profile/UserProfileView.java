package com.seongil.mvplife.sample.ui.main.profile;

import android.content.Intent;

import com.seongil.mvplife.base.MvpView;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public interface UserProfileView extends MvpView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void renderUserThumbnail(String url);

    void renderError(Throwable t);

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
