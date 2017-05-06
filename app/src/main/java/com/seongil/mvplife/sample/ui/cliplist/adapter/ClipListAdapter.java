package com.seongil.mvplife.sample.ui.cliplist.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;
import com.seongil.recyclerviewlife.single.RecyclerListViewAdapter;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipListAdapter extends RecyclerListViewAdapter<ClipDomainViewModel> {

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
    public ClipListAdapter(@NonNull LayoutInflater layoutInflater) {
        super(layoutInflater);
        addViewBinder(new ClipItemBasicViewBinder(VIEW_TYPE_DEFAULT, layoutInflater));
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