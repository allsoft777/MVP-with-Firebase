package com.seongil.mvplife.sample.ui.main.navdrawer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.ui.base.BaseFragment;
import com.seongil.mvplife.sample.ui.main.OnNavDrawerListener;
import com.seongil.mvplife.sample.ui.main.navdrawer.viewbinder.NavDrawerFragmentListener;
import com.seongil.mvplife.sample.ui.main.navdrawer.viewbinder.NavDrawerHeaderViewBinder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author seong-il, kim
 * @since 17. 3. 20
 */
public class NavDrawerFragment extends BaseFragment<NavDrawerView, NavDrawerPresenter>
      implements NavDrawerView, NavDrawerFragmentListener {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private DrawerLayout mDrawerLayout;
    private OnNavDrawerListener mNavDrawerListener;
    private NavDrawerHeaderViewBinder mHeaderViewBinder;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @NonNull
    @Override
    public NavDrawerPresenter createPresenter() {
        return new NavDrawerPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_main_navigation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().registerSkyRail();

        mHeaderViewBinder = new NavDrawerHeaderViewBinder(this);
        mHeaderViewBinder.initializeLayout(view.findViewById(R.id.nav_profile_container));

        getPresenter().fetchUserThumbnailFromRepository();
    }

    @Override
    public void onDestroyView() {
        mHeaderViewBinder.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void renderUserThumbnail(@NonNull String url) {
        mHeaderViewBinder.renderUserThumbnail(url);
    }

    @Override
    public void renderError(@NonNull Throwable t) {
        renderToastMsg(t.getMessage());
    }

    @Override
    public void onReplaceToUserInfoView() {
        closeDrawer();
        getClosingDrawerObservable().subscribe(time -> mNavDrawerListener.renderUserProfileView());
    }

    @Override
    public void onRenderDashBoardView() {
        closeDrawer();
        getClosingDrawerObservable().subscribe(time -> mNavDrawerListener.renderDashboardView());
    }

    // ========================================================================
    // methods
    // ========================================================================
    public void initialize(@NonNull Toolbar toolbar,
          @NonNull DrawerLayout drawerLayout,
          @NonNull OnNavDrawerListener navListener) {
        mDrawerLayout = drawerLayout;
        mNavDrawerListener = navListener;

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
              toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public boolean isDrawerOpened() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private Observable<Long> getClosingDrawerObservable() {
        return Observable.timer(200, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
