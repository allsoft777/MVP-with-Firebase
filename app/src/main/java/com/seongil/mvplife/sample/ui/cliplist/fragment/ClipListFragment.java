package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.utils.ToastUtil;
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

        getPresenter().monitorSingleValueEvent();
        getPresenter().monitorChildEvents();
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
    public void notifyInsertionSuccess() {
        renderToastMsg(getString(R.string.msg_added_new_item));
    }

    @Override
    public void renderError(@NonNull Throwable t) {
        ToastUtil.showToast(t.toString());
    }

    @Override
    public void addNewClipData(@NonNull ClipDomain viewModel) {
        mClipListViewBinder.insertViewModel(viewModel);
    }

    @Override
    public void updateClipData(@NonNull ClipDomain domain) {
        mClipListViewBinder.updateViewModel(domain);
    }

    @Override
    public void renderClipDataList(@NonNull List<ClipDomain> list) {
        mClipListViewBinder.renderListItems(list);
    }

    @Override
    public void removeClipData(@NonNull String itemKey) {
        mClipListViewBinder.removeViewModel(itemKey);
    }

    @Override
    public void renderEmptyView() {
        mClipListViewBinder.renderEmptyView();
    }

    @Override
    public void notifyUpdatedFavouritesItem(boolean isFavouritesItem) {
        if (isFavouritesItem) {
            renderToastMsg(getString(R.string.msg_success_to_add_favourites_item));
        } else {
            renderToastMsg(getString(R.string.msg_success_to_remove_favourites_item));
        }
    }

    @Override
    public void updateFavouritesItemToRepository(@NonNull String itemKey, boolean isFavouritesItem) {
        getPresenter().updateFavouritesItemToRepository(itemKey, isFavouritesItem);
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}