package com.seongil.mvplife.sample.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.seongil.mvplife.activity.BaseMvpV7CompatActivity;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.model.SkyRailEvents;
import com.seongil.mvplife.sample.common.utils.ActivityUtils;
import com.seongil.mvplife.sample.common.utils.ToastUtil;
import com.seongil.mvplife.sample.ui.main.navdrawer.NavDrawerFragment;
import com.seongil.mvplife.sample.ui.main.profile.UserProfileFragment;

/**
 * @author seong-il, kim
 * @since 17. 3. 20
 */
public class MainActivity extends BaseMvpV7CompatActivity<MainView, MainPresenter>
      implements MainView, OnNavDrawerListener {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG_USER_PROFILE = "main.view.user_profile";

    // ========================================================================
    // fields
    // ========================================================================
    private NavDrawerFragment mNavDrawerFragment;

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
    public MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initializeLayout();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MainApplication.getSkyRail().send(
              new SkyRailEvents.OnActivityResultEvent(requestCode, resultCode, data));
    }

    @Override
    public void onBackPressed() {
        if (mNavDrawerFragment.isDrawerOpened()) {
            mNavDrawerFragment.closeDrawer();
            return;
        }
        finish();
    }

    @Override
    public void renderError(@NonNull Throwable t) {
        ToastUtil.showToast("MainActivity : " + t.getMessage());
    }

    @Override
    public void renderUserProfileView() {
        replaceFragmentWithUserProfileFragment();
    }

    @Override
    public void renderDashboardView() {
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void initializeLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavDrawerFragment = (NavDrawerFragment) getSupportFragmentManager()
              .findFragmentById(R.id.fragment_navigation);
        mNavDrawerFragment.initialize(toolbar, drawer, this);

        replaceFragmentWithUserProfileFragment();
    }

    private void replaceFragmentWithUserProfileFragment() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG_USER_PROFILE);
        if (fragment == null) {
            fragment = UserProfileFragment.newInstance();
        }
        ActivityUtils.replaceFragmentToActivity(fm, fragment, R.id.content_main, TAG_USER_PROFILE);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
