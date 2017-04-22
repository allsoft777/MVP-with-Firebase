package com.seongil.mvplife.sample.common.ui.dialog.picturechooser.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;

import com.seongil.mvplife.sample.common.ui.recyclerview.SingleTextViewTypeViewBinder;
import com.seongil.recyclerviewlife.single.RecyclerListViewAdapter;
import com.seongil.recyclerviewlife.single.viewbinder.AbstractViewBinder.RecyclerViewItemClickListener;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public class PictureChooserMenuAdapter extends RecyclerListViewAdapter {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int VIEW_TYPE_BASIC = 1;

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================
    public PictureChooserMenuAdapter(
          @NonNull LayoutInflater layoutInflater,
          @Nullable RecyclerViewItemClickListener clickListener) {
        super(layoutInflater);
        addViewBinder(new SingleTextViewTypeViewBinder(VIEW_TYPE_BASIC, layoutInflater, clickListener));
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
