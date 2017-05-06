package com.seongil.mvplife.sample.ui.cliplist.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.ui.base.BaseActivity;
import com.seongil.mvplife.sample.ui.cliplist.fragment.ClipListFragment;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipItemListActivity extends BaseActivity {

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

    // ========================================================================
    // methods
    // ========================================================================
    private void configureActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void attachFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ClipListFragment fragment = (ClipListFragment) fm.findFragmentByTag("clip_list");
        if (fragment == null) {
            fragment = ClipListFragment.newInstance();
        }
        fm.beginTransaction().replace(R.id.content_main, fragment, "clip_list").commit();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}