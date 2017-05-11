package com.seongil.mvplife.sample.ui.detailview.fragment;

import android.support.annotation.NonNull;

import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.ui.base.BaseView;

/**
 * @author seong-il, kim
 * @since 2017. 4. 30.
 */
public interface DetailClipItemView extends BaseView {

    void renderClipDomain(@NonNull ClipDomain domain);

    void notifyRemovedItem(@NonNull String itemKey);

    void notifyUpdatedFavoriteState(boolean isFavouritesItem);

    void notifyUpdatedClipItem(@NonNull final ClipDomain domain);

    void renderError(@NonNull Throwable t);

    void onBackPressed();

    void notifyInsertionSuccess(@NonNull ClipDomain domain);
}
