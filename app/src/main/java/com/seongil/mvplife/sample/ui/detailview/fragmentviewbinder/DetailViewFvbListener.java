package com.seongil.mvplife.sample.ui.detailview.fragmentviewbinder;

/**
 * @author seong-il, kim
 * @since 2017. 4. 30.
 */
public interface DetailViewFvbListener {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void setEditMode();

    void setReadOnlyMode();

    void shareClipItem();

    void deleteClipItem();

    void copyClipItem();

    void toggleFavoriteItem();

    void finishActivity(boolean force);
}
