package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.ui.base.BaseFragment;
import com.seongil.mvplife.sample.ui.cliplist.fragment.viewbinder.ClipListFragmentViewBinderListener;
import com.seongil.mvplife.sample.ui.cliplist.fragment.viewbinder.ClipListViewBinder;
import com.seongil.mvplife.sample.ui.cliplist.fragment.viewbinder.InputContainerViewBinder;

import java.util.List;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipListFragment extends BaseFragment<ClipListView, ClipListPresenter>
      implements ClipListView, ClipListFragmentViewBinderListener {

    // ========================================================================
    // constants
    // ========================================================================
    public static final int LOAD_CLIP_ITEM_PER_CYCLE = 20;

    // ========================================================================
    // fields
    // ========================================================================
    private ClipListViewBinder mClipListViewBinder;
    private InputContainerViewBinder mInputContainerViewBinder;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized ClipListFragment newInstance() {
        return new ClipListFragment();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @NonNull
    @Override
    public ClipListPresenter createPresenter() {
        return new ClipListPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_clip_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClipListViewBinder = new ClipListViewBinder(getActivity().getLayoutInflater(), this);
        mInputContainerViewBinder = new InputContainerViewBinder();

        mClipListViewBinder.initializeLayout(view.findViewById(R.id.list_view_container));
        mInputContainerViewBinder.initializeLayout(view.findViewById(R.id.input_container));

        mClipListViewBinder.renderLoadingView();
        mInputContainerViewBinder.hideContainer();

        getPresenter().fetchClipListFromRepository("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mClipListViewBinder.onDestroyView();
        mInputContainerViewBinder.onDestroyView();
    }

    @Override
    public void renderError(@NonNull Throwable t) {
        renderToastMsg(t.toString());
    }

    @Override
    public void renderClipDataList(@NonNull List<ClipDomain> list, final boolean existNextItemMore) {
        mClipListViewBinder.insertCollectionToLastPosition(list, existNextItemMore);
        mInputContainerViewBinder.showContainer();
    }

    @Override
    public void renderEmptyView() {
        mClipListViewBinder.renderEmptyView();
        mInputContainerViewBinder.showContainer();
    }

    @Override
    public void notifyUpdatedFavouritesItem(@NonNull String itemKey, final boolean isFavouritesItem) {
        if (isFavouritesItem) {
            renderToastMsg(getString(R.string.msg_success_to_add_favourites_item));
        } else {
            renderToastMsg(getString(R.string.msg_success_to_remove_favourites_item));
        }
        mClipListViewBinder.renderFavouritesState(itemKey, isFavouritesItem);
    }

    @Override
    public void fetchNextItemMoreFromRepository(@NonNull String lastLoadedItemKey) {
        getPresenter().fetchClipListFromRepository(lastLoadedItemKey);
    }

    @Override
    public void updateFavouritesItemToRepository(@NonNull String itemKey, final boolean isFavouritesItem) {
        getPresenter().updateFavouritesItemToRepository(itemKey, isFavouritesItem);
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}