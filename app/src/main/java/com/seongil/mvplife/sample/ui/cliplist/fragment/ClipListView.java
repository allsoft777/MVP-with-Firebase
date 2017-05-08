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
    void renderClipDataList(@NonNull List<ClipDomain> list, final boolean existNextItemMore);

    void renderError(@NonNull Throwable t);

    void renderEmptyView();

    void notifyUpdatedFavouritesItem(@NonNull String itemKey, final boolean isFavouritesItem);
}