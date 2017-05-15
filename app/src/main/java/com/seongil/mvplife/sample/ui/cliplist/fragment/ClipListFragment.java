package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.sharedprefs.DefaultSharedPrefWrapper;
import com.seongil.mvplife.sample.common.sharedprefs.SharedPrefKeys;
import com.seongil.mvplife.sample.ui.base.BaseFragment;
import com.seongil.mvplife.sample.ui.cliplist.fragment.fragmentviewbinder.ClipListCreationFvb;
import com.seongil.mvplife.sample.ui.cliplist.fragment.fragmentviewbinder.ClipListFvbListener;
import com.seongil.mvplife.sample.ui.cliplist.fragment.fragmentviewbinder.ClipListViewFvb;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipListFragment extends BaseFragment<ClipListView, ClipListPresenter>
      implements ClipListView, ClipListFvbListener {

    // ========================================================================
    // constants
    // ========================================================================
    public static final int LOAD_CLIP_ITEM_PER_CYCLE = 20;

    // ========================================================================
    // fields
    // ========================================================================
    private ClipListViewFvb mClipListViewBinder;
    private ClipListCreationFvb mClipListMenuContainerFvb;
    private ActionMode mActionMode;

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
        mClipListViewBinder = new ClipListViewFvb(getActivity().getLayoutInflater(), this);
        mClipListMenuContainerFvb = new ClipListCreationFvb();

        mClipListViewBinder.initializeLayout(view.findViewById(R.id.list_view_container));
        mClipListMenuContainerFvb.initializeLayout(view.findViewById(R.id.bottom_container));

        mClipListViewBinder.renderLoadingView();
        mClipListMenuContainerFvb.hideContainer();

        getPresenter().fetchClipListFromRepository("", isFavouritesItemFilterMode());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
        mClipListViewBinder.onDestroyView();
        mClipListMenuContainerFvb.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clip_list, menu);
        final boolean favouritesSwitchOn = DefaultSharedPrefWrapper.getInstance().getBoolean(
              MainApplication.getAppContext(), SharedPrefKeys.KEY_CLIP_LIST_FAVOURITES_ITEM_SWITCH_ON);
        updateFavouritesState(menu.findItem(R.id.action_star), favouritesSwitchOn);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            // TODO
            return true;
        } else if (item.getItemId() == R.id.action_star) {
            handleClickedFavouritesItem(item);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void renderError(@NonNull Throwable t) {
        dismissProgressDialog();
        renderToastMsg(t.toString());
        mClipListViewBinder.renderErrorView(t);
        mClipListMenuContainerFvb.renderErrorView(t);
    }

    @Override
    public void renderClipDataList(@NonNull List<ClipDomainViewModel> list, final boolean existNextItemMore) {
        mClipListViewBinder.insertCollectionToLastPosition(list, existNextItemMore);
        mClipListMenuContainerFvb.showContainer();
    }

    @Override
    public void renderEmptyView() {
        mClipListViewBinder.renderEmptyView();
        mClipListMenuContainerFvb.showContainer();
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
        getPresenter().fetchClipListFromRepository(lastLoadedItemKey, isFavouritesItemFilterMode());
    }

    @Override
    public void updateFavouritesItemToRepository(@NonNull String itemKey, final boolean isFavouritesItem) {
        getPresenter().updateFavouritesItemToRepository(itemKey, isFavouritesItem);
    }

    @Override
    public boolean isFavouritesItemFilterMode() {
        return DefaultSharedPrefWrapper.getInstance().getBoolean(
              MainApplication.getAppContext(), SharedPrefKeys.KEY_CLIP_LIST_FAVOURITES_ITEM_SWITCH_ON);
    }

    @Override
    public void startContextActionBar() {
        mActionMode = getActivity().startActionMode(new ContextActionModeCallback());
    }

    @Override
    public void renderCountOfSelectedItems(int count) {
        if (mActionMode == null) {
            return;
        }
        mActionMode.setTitle(count);
        mActionMode.invalidate();
    }

    @Override
    public void notifyRemovedItems(@NonNull List<String> itemKeys) {
        dismissProgressDialog();
        mClipListViewBinder.removeItems(itemKeys);

        final int size = itemKeys.size();
        String msg = String.valueOf(size) + " ";
        msg += size > 1 ? getString(R.string.msg_multiple_items_removed)
              : getString(R.string.msg_an_item_removed);
        renderToastMsg(msg);
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    @Override
    public void removeSelectedItems() {
        getPresenter().removeClipItemsFromRepository(mClipListViewBinder.getSelectedItemKeys());
        mActionMode.finish();
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void updateFavouritesState(MenuItem menuItem, boolean switchOn) {
        if (menuItem == null) {
            return;
        }
        @DrawableRes int iconRes;
        if (switchOn) {
            iconRes = R.drawable.ic_action_favourites_true;
        } else {
            iconRes = R.drawable.ic_action_favourites_false;
        }
        menuItem.setIcon(iconRes);
        DefaultSharedPrefWrapper.getInstance().putBoolean(
              MainApplication.getAppContext(), SharedPrefKeys.KEY_CLIP_LIST_FAVOURITES_ITEM_SWITCH_ON, switchOn);
    }

    private void handleClickedFavouritesItem(MenuItem item) {
        if (mClipListViewBinder.isInitialLoadingState()) {
            return;
        }

        item.setEnabled(false);
        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()).subscribe(___ -> item.setEnabled(true));

        final boolean favouritesItemFilterMode = !isFavouritesItemFilterMode();
        updateFavouritesState(item, favouritesItemFilterMode);
        mClipListViewBinder.renderLoadingView();
        mClipListViewBinder.initializeListView();
        getPresenter().fetchClipListFromRepository("", favouritesItemFilterMode);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    private class ContextActionModeCallback implements Callback {

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_clip_list_context_action, menu);
            mActionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            final int count = mClipListViewBinder.getSelectedItemCount();
            mActionMode.setTitle(count);
            MenuItem done = menu.findItem(R.id.action_delete);
            done.setEnabled(count > 0);
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    removeSelectedItems();
                    return true;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            mClipListViewBinder.clearSelectionMode();
            mActionMode = null;
        }
    }
}