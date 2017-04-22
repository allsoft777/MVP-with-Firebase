package com.seongil.mvplife.sample.common.model;

import android.support.annotation.DrawableRes;

import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class SingleTextAndDrawerViewData implements RecyclerViewItem {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private String text;
    @DrawableRes
    private int drawerResId;

    // ========================================================================
    // constructors
    // ========================================================================
    public SingleTextAndDrawerViewData(String text, @DrawableRes int drawerResId) {
        this.text = text;
        this.drawerResId = drawerResId;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @DrawableRes
    public int getDrawerResId() {
        return drawerResId;
    }

    public void setDrawerResId(@DrawableRes int drawerResId) {
        this.drawerResId = drawerResId;
    }

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
