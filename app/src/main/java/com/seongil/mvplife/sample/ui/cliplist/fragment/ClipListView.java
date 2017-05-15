package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.support.annotation.NonNull;

import com.seongil.mvplife.sample.ui.base.BaseView;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;

import java.util.List;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public interface ClipListView extends BaseView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    void renderClipDataList(@NonNull List<ClipDomainViewModel> list, final boolean existNextItemMore);

    void renderError(@NonNull Throwable t);

    void renderEmptyView();

    void notifyUpdatedFavouritesItem(@NonNull String itemKey, final boolean isFavouritesItem);

    void notifyRemovedItems(@NonNull List<String> itemKeys);
}