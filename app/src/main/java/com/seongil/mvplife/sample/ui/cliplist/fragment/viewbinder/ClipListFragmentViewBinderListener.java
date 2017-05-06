package com.seongil.mvplife.sample.ui.cliplist.fragment.viewbinder;

import android.support.annotation.NonNull;

/**
 * @author seong-il, kim
 * @since 17. 5. 3
 */
public interface ClipListFragmentViewBinderListener {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void updateFavouritesItemToRepository(@NonNull String itemKey, boolean isFavouritesItem);
}
