package com.seongil.mvplife.sample.ui.detailview.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.ExtraKey;
import com.seongil.mvplife.sample.ui.base.BaseActivity;
import com.seongil.mvplife.sample.ui.detailview.fragment.DetailClipItemFragment;
import com.seongil.mvplife.sample.ui.detailview.skyrail.DetailViewSkyRail;
import com.seongil.mvplife.sample.ui.detailview.skyrail.SkyRailClipItemDetailViewEvent;

/**
 * @author seong-il, kim
 * @since 2017. 4. 30.
 */
public class DetailClipItemActivity extends BaseActivity {

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar_with_container);
        if (savedInstanceState == null) {
            configureActionBar();
            attachFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DetailViewSkyRail.getInstance().getSkyRail().send(new SkyRailClipItemDetailViewEvent.OnBackPressedEvent());
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void configureActionBar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void attachFragment() {
        final FragmentManager fm = getSupportFragmentManager();
        DetailClipItemFragment fragment = (DetailClipItemFragment) fm.findFragmentByTag("fragment");
        if (fragment == null) {
            final Bundle args = getIntent().getBundleExtra(ExtraKey.KEY_EXTRA_BUNDLE);
            fragment = DetailClipItemFragment.newInstance(args);
        }
        fm.beginTransaction().replace(R.id.content_main, fragment, "fragment").commit();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
