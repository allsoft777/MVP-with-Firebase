package com.seongil.mvplife.sample.ui.main.navdrawer.adapter.header;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.seongil.recyclerviewlife.single.RecyclerListViewAdapter;
import com.seongil.recyclerviewlife.single.viewbinder.AbstractViewBinder;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class NavigationMenuListAdapter extends RecyclerListViewAdapter {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int VIEW_TYPE_DEFAULT = 1;

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================
    public NavigationMenuListAdapter(
          @NonNull LayoutInflater layoutInflater, @NonNull
          AbstractViewBinder.RecyclerViewItemClickListener clickListener) {
        super(layoutInflater);
        addViewBinder(new NavHeaderListViewBinder(VIEW_TYPE_DEFAULT, layoutInflater, clickListener));
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
