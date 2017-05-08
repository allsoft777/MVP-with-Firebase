package com.seongil.mvplife.sample.ui.detailview.fragment;

import android.support.annotation.NonNull;

import com.seongil.mvplife.base.MvpView;
import com.seongil.mvplife.sample.domain.ClipDomain;

/**
 * @author seong-il, kim
 * @since 2017. 4. 30.
 */
public interface DetailClipItemView extends MvpView {

    void renderClipDomain(@NonNull ClipDomain domain);

    void notifyRemovedItem(@NonNull String itemKey);

    void notifyUpdatedFavoriteState(boolean isFavouritesItem);

    void notifyUpdatedClipItem(@NonNull final ClipDomain domain);

    void renderError(@NonNull Throwable t);

    void showProgressDialog(@NonNull String msg);

    void dismissProgressDialog();

    void onBackPressed();

    void notifyInsertionSuccess(@NonNull ClipDomain domain);
}
