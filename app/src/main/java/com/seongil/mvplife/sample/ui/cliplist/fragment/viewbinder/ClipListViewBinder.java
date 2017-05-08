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
import com.seongil.mvplife.sample.common.utils.ClipboadManager;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.ui.cliplist.adapter.ClipListAdapter;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRail;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.SkyRailClipListEvent;
import com.seongil.mvplife.sample.ui.detailview.activity.DetailClipItemActivity;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;
import com.seongil.mvplife.viewbinder.RxMvpViewBinder;
import com.seongil.recyclerviewlife.decor.RecyclerViewDividerItemDecor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipListViewBinder extends RxMvpViewBinder {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int STATE_LIST_VIEW = 0;
    private static final int STATE_EMPTY = 1;
    private static final int STATE_LOADING = 2;
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
    private List<ClipDomainViewModel> mViewModelList;

    // ========================================================================
    // constructors
    // ========================================================================
    public ClipListViewBinder(@NonNull LayoutInflater layoutInflater,
                              @NonNull ClipListFragmentViewBinderListener fragmentViewBinderListener) {
        mLayoutInflater = layoutInflater;
        mFragmentListener = fragmentViewBinderListener;
        mViewModelList = new ArrayList<>();
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
        mAdapter = new ClipListAdapter(mLayoutInflater);
        mAdapter.setData(mViewModelList);

        final LinearLayoutManager llm = new LinearLayoutManager(layout.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mListView.setLayoutManager(llm);
        mListView.setAdapter(mAdapter);
        mListView.setHasFixedSize(true);
        mListView.setFocusable(false);

        RecyclerViewDividerItemDecor decor = new RecyclerViewDividerItemDecor(
              layout.getContext(), RecyclerView.VERTICAL, R.drawable.common_recyclerview_list_item_divider);
        mListView.addItemDecoration(decor);

        mLoadingView = (ProgressBar) layout.findViewById(R.id.loading_bar);
        mEmptyContainer = layout.findViewById(R.id.empty_container);
        ImageView emptyIcon = (ImageView) mEmptyContainer.findViewById(R.id.empty_icon);
        TextView emptyText = (TextView) mEmptyContainer.findViewById(R.id.empty_text);
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
        mViewModelList.clear();
        mViewModelList = null;
        mListView = null;
    }

    // ========================================================================
    // methods
    // ========================================================================
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
        } else {
            throw new AssertionError("There is no defined event : " + o.getClass().getSimpleName());
        }
    }

    private void handleClickedFavoriteItem(SkyRailClipListEvent.FavoriteItemEvent o) {
        final int pos = getPositionByKey(o.getKey());
        if (pos == INVALID_KEY_POSITION) {
            return;
        }
        mFragmentListener.updateFavouritesItemToRepository(o.getKey(), o.isFavourites());
    }

    private void handleClickedItem(SkyRailClipListEvent.ClickItemEvent o) {
        Intent intent = new Intent(MainApplication.getAppContext(), DetailClipItemActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle args = new Bundle();
        args.putString(ExtraKey.KEY_CLIP_ITEM_KEY, o.getKey());
        intent.putExtra(ExtraKey.KEY_EXTRA_BUNDLE, args);

        MainApplication.getAppContext().startActivity(intent);
    }

    private void copyDataToClipboard(SkyRailClipListEvent.CopyItemToClipboard o) {
        int pos = getPositionByKey(o.getKey());
        if (pos == INVALID_KEY_POSITION) {
            return;
        }

        final String targetData = mAdapter.getDataSet().get(pos).getDomain().getTextData();
        removeViewModel(o.getKey());
        ClipboadManager.copyText(targetData);
    }

    // TODO selection mode
    private void handleLongClickedItem(SkyRailClipListEvent.LongClickItemEvent o) {

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
        mListView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mEmptyContainer.setVisibility(View.GONE);
        mState = STATE_LIST_VIEW;
    }

    public void renderEmptyView() {
        mLoadingView.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mEmptyContainer.setVisibility(View.VISIBLE);
        mState = STATE_EMPTY;
    }

    public void renderLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);
        mEmptyContainer.setVisibility(View.GONE);
        mState = STATE_LOADING;
    }

    public void renderListItems(@NonNull List<ClipDomain> list) {
        if (mState != STATE_LIST_VIEW) {
            renderListView();
        }
        for (ClipDomain domain : list) {
            if (existItem(domain.getKey())) {
                continue;
            }
            mAdapter.addItemToFirstPosition(new ClipDomainViewModel(domain));
            mListView.scrollToPosition(0);
        }
    }

    public void insertViewModel(@NonNull ClipDomain domain) {
        if (mState != STATE_LIST_VIEW) {
            renderListView();
        }
        if (existItem(domain.getKey())) {
            return;
        }
        mListView.scrollToPosition(0);
        mAdapter.addItemToFirstPosition(new ClipDomainViewModel(domain));
    }

    private boolean existItem(@NonNull String itemKey) {
        for (ClipDomainViewModel viewModel : mAdapter.getDataSet()) {
            if (viewModel.getDomain().getKey().equals(itemKey)) {
                return true;
            }
        }
        return false;
    }

    public void updateViewModel(@NonNull ClipDomain domain) {
        final int position = getPositionByKey(domain.getKey());
        if (position == INVALID_KEY_POSITION) {
            return;
        }
        mAdapter.replaceItem(new ClipDomainViewModel(domain), position);
    }

    public void removeViewModel(@NonNull String itemKey) {
        final int position = getPositionByKey(itemKey);
        if (position == INVALID_KEY_POSITION) {
            return;
        }
        mAdapter.removePosition(position, false);
        mAdapter.notifyDataSetChanged();

        if (mAdapter.getItemCount() == 0) {
            renderEmptyView();
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}