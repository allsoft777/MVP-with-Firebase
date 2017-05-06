package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.support.annotation.NonNull;

import com.seongil.mvplife.base.MvpView;
import com.seongil.mvplife.sample.domain.ClipDomain;

import java.util.List;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public interface ClipListView extends MvpView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void addNewClipData(@NonNull ClipDomain domain);

    void updateClipData(@NonNull ClipDomain domain);

    void renderClipDataList(@NonNull List<ClipDomain> list);

    void removeClipData(@NonNull String itemKey);

    void notifyInsertionSuccess();

    void renderError(@NonNull Throwable t);

    void renderEmptyView();

    void notifyUpdatedFavouritesItem(boolean isFavouritesItem);
}