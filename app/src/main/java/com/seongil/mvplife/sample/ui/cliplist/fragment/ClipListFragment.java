package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.sharedprefs.DefaultSharedPrefWrapper;
import com.seongil.mvplife.sample.common.sharedprefs.SharedPrefKeys;
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
        mClipListViewBinder = new ClipListViewBinder(getActivity().getLayoutInflater(), this);
        mInputContainerViewBinder = new InputContainerViewBinder();

        mClipListViewBinder.initializeLayout(view.findViewById(R.id.list_view_container));
        mInputContainerViewBinder.initializeLayout(view.findViewById(R.id.input_container));

        mClipListViewBinder.renderLoadingView();
        mInputContainerViewBinder.hideContainer();

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
        mInputContainerViewBinder.onDestroyView();
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

            return true;
        } else if (item.getItemId() == R.id.action_star) {
            final boolean favouritesItemFilterMode = !isFavouritesItemFilterMode();
            updateFavouritesState(item, favouritesItemFilterMode);
            mClipListViewBinder.renderLoadingView();
            mClipListViewBinder.initializeListView();
            getPresenter().fetchClipListFromRepository("", favouritesItemFilterMode);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        mActionMode.setTitle(" " + count);
        mActionMode.invalidate();
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

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    private class ContextActionModeCallback implements android.view.ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_clip_list_context_action, menu);
            mActionMode = mode;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            final int count = mClipListViewBinder.getSelectedItemCount();
            mActionMode.setTitle("  " + count);
            MenuItem done = menu.findItem(R.id.action_delete);
            done.setEnabled(count > 0);
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    break;
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