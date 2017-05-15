package com.seongil.mvplife.sample.ui.cliplist.fragment.fragmentviewbinder;

import android.support.annotation.NonNull;

/**
 * @author seong-il, kim
 * @since 17. 5. 3
 */
public interface ClipListFvbListener {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void fetchNextItemMoreFromRepository(@NonNull String lastLoadedItemKey);

    void updateFavouritesItemToRepository(@NonNull String itemKey, boolean isFavouritesItem);

    boolean isFavouritesItemFilterMode();

    void startContextActionBar();

    void renderCountOfSelectedItems(int count);

    void finishActivity();

    void removeSelectedItems();
}
