package com.seongil.mvplife.sample.ui.main.navdrawer.viewbinder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.firebase.info.RxFirebaseUser;
import com.seongil.mvplife.sample.common.utils.RxUtil;
import com.seongil.mvplife.sample.common.utils.ToastUtil;
import com.seongil.mvplife.viewbinder.MvpViewBinder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author seong-il, kim
 * @since 17. 3. 30
 */
public class NavDrawerHeaderViewBinder implements MvpViewBinder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private NavDrawerFragmentListener mFragmentInteraction;
    private ImageView mProfileThumbnail;
    private TextView mNameTextView;
    private TextView mEmailAddress;

    // ========================================================================
    // constructors
    // ========================================================================
    public NavDrawerHeaderViewBinder(@NonNull NavDrawerFragmentListener fragmentInteraction) {
        mFragmentInteraction = fragmentInteraction;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void initializeLayout(@NonNull View layout) {
        mProfileThumbnail = (ImageView) layout.findViewById(R.id.user_profile_thumbnail);
        mNameTextView = (TextView) layout.findViewById(R.id.name);
        mEmailAddress = (TextView) layout.findViewById(R.id.email_address);
        layout.setOnClickListener(___ -> mFragmentInteraction.onReplaceToUserInfoView());
        renderUserInfo();
    }

    @Override
    public void onDestroyView() {
        mFragmentInteraction = null;
        mProfileThumbnail = null;
        mNameTextView = null;
    }

    // ========================================================================
    // methods
    // ========================================================================
    public void renderUserThumbnail(String url) {
        Glide
              .with(mProfileThumbnail.getContext())
              .load(url)
              .fitCenter()
              .bitmapTransform(new CropCircleTransformation(mProfileThumbnail.getContext()))
              .placeholder(R.drawable.user_profile_placeholder)
              .into(mProfileThumbnail);
    }

    private void renderUserInfo() {
        RxFirebaseUser.getInstance().getCurrentUser()
              .compose(RxUtil.asyncObservableStream())
              .subscribe(fireBaseUser -> {
                  mNameTextView.setText(fireBaseUser.getDisplayName());
                  mEmailAddress.setText(fireBaseUser.getEmail());
              }, throwable -> ToastUtil.showToast(throwable.getMessage()));
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
