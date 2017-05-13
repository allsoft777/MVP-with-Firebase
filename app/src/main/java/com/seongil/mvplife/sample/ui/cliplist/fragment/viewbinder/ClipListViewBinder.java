package com.seongil.mvplife.sample.ui.cliplist.fragment.viewbinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.ExtraKey;
import com.seongil.mvplife.sample.common.exception.NetworkConException;
import com.seongil.mvplife.sample.common.utils.ClipboardManagerUtil;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.ui.cliplist.adapter.ClipListAdapter;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRail;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.SkyRailClipListEvent;
import com.seongil.mvplife.sample.ui.detailview.activity.DetailClipItemActivity;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;
import com.seongil.mvplife.viewbinder.RxMvpViewBinder;
import com.seongil.recyclerviewlife.model.common.ViewStatus;
import com.seongil.recyclerviewlife.scroll.LinearRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipListViewBinder extends RxMvpViewBinder {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int STATE_LIST_VIEW = 0;
    private static final int STATE_EMPTY_VIEW = 1;
    private static final int STATE_ENTIRE_LOADING_VIEW = 2;
    private static final int STATE_MORE_ITEM_LOADING_VIEW = 3;
    private static final int STATE_ERROR_VIEW = 4;
    private static final int INVALID_KEY_POSITION = -1;

    // ========================================================================
    // fields
    // ========================================================================
    private ClipListFragmentViewBinderListener mFragmentListener;
    private int mState = STATE_LIST_VIEW;
    private LayoutInflater mLayoutInflater;
    private RecyclerView mListView;
    private ProgressBar mLoadingView;
    private View mEmptyContainer;
    private ClipListAdapter mAdapter;
    private boolean mExistNextItemMore = true;

    private View mErrorContainer;
    private ImageView mErrorIcon;
    private TextView mErrorText;

    // ========================================================================
    // constructors
    // ========================================================================
    public ClipListViewBinder(@NonNull LayoutInflater layoutInflater,
                              @NonNull ClipListFragmentViewBinderListener fragmentViewBinderListener) {
        mLayoutInflater = layoutInflater;
        mFragmentListener = fragmentViewBinderListener;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void initializeLayout(@NonNull View layout) {
        super.initializeLayout(layout);
        mListView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        initializeListView();

        mLoadingView = (ProgressBar) layout.findViewById(R.id.loading_bar);

        mEmptyContainer = layout.findViewById(R.id.empty_container);
        ImageView emptyIcon = (ImageView) mEmptyContainer.findViewById(R.id.empty_icon);
        TextView emptyText = (TextView) mEmptyContainer.findViewById(R.id.empty_text);

        mErrorContainer = layout.findViewById(R.id.error_container);
        mErrorIcon = (ImageView) mErrorContainer.findViewById(R.id.error_icon);
        mErrorText = (TextView) mErrorContainer.findViewById(R.id.error_msg);

        emptyIcon.setBackgroundResource(R.drawable.ic_in_group_add_item_guide);
        emptyText.setText(emptyText.getResources().getString(R.string.msg_touch_to_add_item));

        mEmptyContainer.setOnClickListener(v -> insertElement());
        registerSkyRail();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLayoutInflater = null;
        mFragmentListener = null;
        mAdapter = null;
        mListView = null;
    }

    // ========================================================================
    // methods
    // ========================================================================
    public void initializeListView() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
        mAdapter = new ClipListAdapter(mLayoutInflater);
        mAdapter.useFooterView();

        final LinearLayoutManager llm = new LinearLayoutManager(mListView.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(llm);
        mListView.setAdapter(mAdapter);
        mListView.setHasFixedSize(true);
        mListView.setFocusable(false);
        addScrollListenerToListView(llm);
    }

    private void addScrollListenerToListView(@NonNull LinearLayoutManager llm) {
        final LinearRecyclerViewScrollListener scrollListener = new LinearRecyclerViewScrollListener(llm, mAdapter) {
            @Override
            protected boolean isLoadingNextItems() {
                return mState == STATE_MORE_ITEM_LOADING_VIEW;
            }

            @Override
            protected void onLoadNextData() {
                super.onLoadNextData();
                if (mExistNextItemMore && mState != STATE_MORE_ITEM_LOADING_VIEW) {
                    mState = STATE_MORE_ITEM_LOADING_VIEW;
                    mFragmentListener.fetchNextItemMoreFromRepository(getLastItemKey());
                }
            }
        };
        mListView.addOnScrollListener(scrollListener);
    }

    @SuppressWarnings("unchecked")
    private String getLastItemKey() {
        if (mAdapter.getItemCount() > 1) {
            ClipDomainViewModel model = mAdapter.getDataSet().get(mAdapter.getItemCount() - 2);
            return model.getDomain().getKey();
        }
        return "";
    }

    private void insertElement() {
        Intent intent = new Intent(MainApplication.getAppContext(), DetailClipItemActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getAppContext().startActivity(intent);
    }

    private void registerSkyRail() {
        final Disposable disposable = ClipListViewSkyRail.getInstance().getSkyRail()
              .toObservable()
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(this::handleRailEvent);
        addDisposable(disposable);
    }

    private void handleRailEvent(Object o) {
        if (o instanceof SkyRailClipListEvent.FavoriteItemEvent) {
            handleClickedFavoriteItem((SkyRailClipListEvent.FavoriteItemEvent) o);
        } else if (o instanceof SkyRailClipListEvent.ClickItemEvent) {
            handleClickedItem((SkyRailClipListEvent.ClickItemEvent) o);
        } else if (o instanceof SkyRailClipListEvent.LongClickItemEvent) {
            handleLongClickedItem((SkyRailClipListEvent.LongClickItemEvent) o);
        } else if (o instanceof SkyRailClipListEvent.CopyItemToClipboard) {
            copyDataToClipboard((SkyRailClipListEvent.CopyItemToClipboard) o);
        } else if (o instanceof SkyRailClipListEvent.InsertedNewItem) {
            insertItemToFirstPosition(((SkyRailClipListEvent.InsertedNewItem) o).getDomain());
        } else if (o instanceof SkyRailClipListEvent.DeletedItem) {
            removeItemFromListView(((SkyRailClipListEvent.DeletedItem) o).getItemKey());
        } else if (o instanceof SkyRailClipListEvent.UpdatedItem) {
            updateViewModel(((SkyRailClipListEvent.UpdatedItem) o).getDomain());
        } else if (o instanceof SkyRailClipListEvent.UpdateFavouritesState) {
            SkyRailClipListEvent.UpdateFavouritesState event = (SkyRailClipListEvent.UpdateFavouritesState) o;
            renderFavouritesState(event.getItemKey(), event.isFavouritesItem());
        } else if (o instanceof SkyRailClipListEvent.SelectItem) {
            handleSelectItemEvent((SkyRailClipListEvent.SelectItem) o);
        } else {
            throw new AssertionError("There is no defined event : " + o.getClass().getSimpleName());
        }
    }

    private void handleSelectItemEvent(SkyRailClipListEvent.SelectItem o) {
        final int pos = getPositionByKey(o.getKey());
        if (pos == INVALID_KEY_POSITION) {
            return;
        }
        mAdapter.getDataSet().get(pos).setSelected(o.isSelected());
    }

    private void handleClickedFavoriteItem(SkyRailClipListEvent.FavoriteItemEvent o) {
        final int pos = getPositionByKey(o.getKey());
        if (pos == INVALID_KEY_POSITION) {
            return;
        }
        mFragmentListener.updateFavouritesItemToRepository(o.getKey(), o.isFavourites());
    }

    private void handleClickedItem(SkyRailClipListEvent.ClickItemEvent o) {
        if (mAdapter.isSelectionMode()) {
            final int pos = getPositionByKey(o.getKey());
            if (pos == INVALID_KEY_POSITION) {
                return;
            }
            final boolean curState = mAdapter.getDataSet().get(pos).isSelected();
            mAdapter.getDataSet().get(pos).setSelected(!curState);
            mAdapter.notifyItemChanged(pos);
            mFragmentListener.renderCountOfSelectedItems(mAdapter.getSelectedItemCount());
        } else {
            launchDetailView(o.getKey());
        }
    }

    private void launchDetailView(@NonNull String itemKey) {
        Intent intent = new Intent(MainApplication.getAppContext(), DetailClipItemActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle args = new Bundle();
        args.putString(ExtraKey.KEY_CLIP_ITEM_KEY, itemKey);
        intent.putExtra(ExtraKey.KEY_EXTRA_BUNDLE, args);

        MainApplication.getAppContext().startActivity(intent);
    }

    private void copyDataToClipboard(SkyRailClipListEvent.CopyItemToClipboard o) {
        int pos = getPositionByKey(o.getKey());
        if (pos == INVALID_KEY_POSITION) {
            return;
        }

        final String targetData = mAdapter.getDataSet().get(pos).getDomain().getTextData();
        removeItemFromListView(o.getKey());
        ClipboardManagerUtil.copyText(targetData);
        Disposable disposable = Single.timer(1, TimeUnit.SECONDS)
              .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
              .subscribe(___ -> mListView.scrollToPosition(0));
        addDisposable(disposable);
    }

    private void handleLongClickedItem(SkyRailClipListEvent.LongClickItemEvent o) {
        if (mAdapter.isSelectionMode()) {
            return;
        }
        int pos = getPositionByKey(o.getKey());
        if (pos == INVALID_KEY_POSITION) {
            return;
        }
        mAdapter.getDataSet().get(pos).setSelected(true);
        mAdapter.setSelectionMode(true);
        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount(true));
        mFragmentListener.startContextActionBar();
    }

    private int getPositionByKey(@NonNull String itemKey) {
        int size = mAdapter.getItemCount();
        for (int i = 0; i < size; i++) {
            if (mAdapter.getDataSet().get(i).getDomain().getKey().equals(itemKey)) {
                return i;
            }
        }
        return INVALID_KEY_POSITION;
    }

    public void renderListView() {
        mErrorContainer.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyContainer.setVisibility(View.GONE);
        mState = STATE_LIST_VIEW;
    }

    public void renderEmptyView() {
        mErrorContainer.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mEmptyContainer.setVisibility(View.VISIBLE);
        mState = STATE_EMPTY_VIEW;
    }

    public void renderLoadingView() {
        mErrorContainer.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mEmptyContainer.setVisibility(View.GONE);
        mState = STATE_ENTIRE_LOADING_VIEW;
    }

    public void renderErrorView(Throwable t) {
        mErrorContainer.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mEmptyContainer.setVisibility(View.GONE);
        mState = STATE_ERROR_VIEW;

        if (t instanceof NetworkConException) {
            mErrorText.setText(mErrorText.getResources().getString(R.string.err_network_connection_failed));
            mErrorIcon.setBackgroundResource(R.drawable.error_network_connection);
        } else {
            throw new IllegalArgumentException("Invalid exception code.");
        }
    }

    public void insertCollectionToLastPosition(@NonNull List<ClipDomain> list, final boolean existNextItemMore) {
        mExistNextItemMore = existNextItemMore;
        if (!mExistNextItemMore) {
            mAdapter.updateFooterViewStatus(ViewStatus.VISIBLE_LABEL_VIEW, false);
        }

        if (mState != STATE_LIST_VIEW) {
            renderListView();
        }

        List<ClipDomainViewModel> dataSet = new ArrayList<>();
        for (ClipDomain domain : list) {
            dataSet.add(new ClipDomainViewModel(domain));
        }
        mAdapter.addCollectionToLastPosition(dataSet);
    }

    private void insertItemToFirstPosition(@NonNull ClipDomain domain) {
        if (mFragmentListener.isFavouritesItemFilterMode() && !domain.isFavouritesItem()) {
            return;
        }

        if (mState != STATE_LIST_VIEW && mState != STATE_MORE_ITEM_LOADING_VIEW) {
            mExistNextItemMore = false;
            mAdapter.updateFooterViewStatus(ViewStatus.VISIBLE_LABEL_VIEW, false);
        }
        if (mState != STATE_LIST_VIEW) {
            renderListView();
        }

        mAdapter.addItemToFirstPosition(new ClipDomainViewModel(domain));
    }

    private void removeItemFromListView(@NonNull String itemKey) {
        final int pos = getPositionByKey(itemKey);
        if (pos == INVALID_KEY_POSITION) {
            return;
        }
        mAdapter.removePosition(pos, false);
        mAdapter.notifyItemRemoved(pos);

        if (mAdapter.getItemCount() == 1) {
            renderEmptyView();
        }
    }

    public void updateViewModel(@NonNull ClipDomain domain) {
        final int position = getPositionByKey(domain.getKey());
        if (position == INVALID_KEY_POSITION) {
            return;
        }
        mAdapter.replaceItem(new ClipDomainViewModel(domain), position);
    }

    public void renderFavouritesState(@NonNull String itemKey, boolean isFavouritesItem) {
        final int pos = getPositionByKey(itemKey);
        if (pos == INVALID_KEY_POSITION) {
            return;
        }
        if (!isFavouritesItem && mFragmentListener.isFavouritesItemFilterMode()) {
            removeItemFromListView(itemKey);
            return;
        }

        final ClipDomain domain = mAdapter.getItem(pos).getDomain();
        domain.setFavouritesItem(isFavouritesItem);
        mAdapter.replaceItem(new ClipDomainViewModel(domain), pos);
    }

    public int getSelectedItemCount() {
        return mAdapter.getSelectedItemCount();
    }

    public List<String> getSelectedItemKeys() {
        return mAdapter.retrieveSelectedItemKeys();
    }

    public void clearSelectionMode() {
        if (mAdapter.isSelectionMode()) {
            mAdapter.setSelectionMode(false);
        }
        mAdapter.clearSelectedItems();
        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
    }

    public void removeItems(@NonNull List<String> itemKeys) {
        for (String itemKey : itemKeys) {
            mAdapter.removeItem(itemKey);
        }
        mAdapter.notifyDataSetChanged();
    }

    public boolean isInitialLoadingState() {
        return mState == STATE_ENTIRE_LOADING_VIEW;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}