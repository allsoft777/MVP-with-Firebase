package com.seongil.mvplife.sample.ui.detailview.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.ExtraKey;
import com.seongil.mvplife.sample.common.sharedprefs.DefaultSharedPrefWrapper;
import com.seongil.mvplife.sample.common.sharedprefs.SharedPrefKeys;
import com.seongil.mvplife.sample.common.utils.ClipboardManagerUtil;
import com.seongil.mvplife.sample.common.utils.LogUtil;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.ui.base.BaseFragment;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRail;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRailEvents;
import com.seongil.mvplife.sample.ui.detailview.fragmentviewbinder.DetailViewBodyContainerFvb;
import com.seongil.mvplife.sample.ui.detailview.fragmentviewbinder.DetailViewFvbListener;
import com.seongil.mvplife.sample.ui.detailview.fragmentviewbinder.DetailViewMenuContainerFvb;

/**
 * @author seong-il, kim
 * @since 17. 5. 1
 */
public class DetailClipItemFragment
      extends BaseFragment<DetailClipItemView, DetailClipItemPresenter> implements DetailClipItemView,
      DetailViewFvbListener {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "DetailClipItemFragment";

    // ========================================================================
    // fields
    // ========================================================================
    private DetailViewBodyContainerFvb mDetailViewBodyContainerFvb;
    private DetailViewMenuContainerFvb mSoftButtonsViewBinder;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized DetailClipItemFragment newInstance(@NonNull Bundle args) {
        DetailClipItemFragment fragment = new DetailClipItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @NonNull
    @Override
    public DetailClipItemPresenter createPresenter() {
        return new DetailClipItemPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_detailview;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().registerRxSkyRail();

        mDetailViewBodyContainerFvb = new DetailViewBodyContainerFvb();
        mSoftButtonsViewBinder = new DetailViewMenuContainerFvb(this);

        mDetailViewBodyContainerFvb.initializeLayout(view.findViewById(R.id.content_container));
        mSoftButtonsViewBinder.initializeLayout(view.findViewById(R.id.soft_menu_container));

        final String clipItemKey = retrieveClipItemKeyFromArgument(getArguments());
        if (TextUtils.isEmpty(clipItemKey)) {
            mDetailViewBodyContainerFvb.renderEditMode();
            getActionBar().setTitle(getString(R.string.edit_view));
        } else {
            mDetailViewBodyContainerFvb.renderLoadingView();
            getActionBar().setTitle(getString(R.string.detail_view));
            getPresenter().fetchClipDataFromRepository(getResources(), clipItemKey);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDetailViewBodyContainerFvb.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mDetailViewBodyContainerFvb.hideSoftInput();
    }

    @Override
    public void renderClipDomain(@NonNull ClipDomain domain) {
        mDetailViewBodyContainerFvb.renderClipItemDomain(domain);
        mSoftButtonsViewBinder.renderFavouritesItemState(domain.isFavouritesItem());
    }

    @Override
    public void renderError(@NonNull Throwable t) {
        renderToastMsg(t.getMessage());
    }

    @Override
    public void notifyRemovedItem(@NonNull String itemKey) {
        ClipListViewSkyRail.getInstance().getSkyRail().send(new ClipListViewSkyRailEvents.DeletedItem(itemKey));
        renderToastMsg(getString(R.string.msg_remove_successfully));
        getActivity().finish();
    }

    @Override
    public void deleteClipItem() {
        final String key = mDetailViewBodyContainerFvb.getItemKey();
        getPresenter().deleteClipItem(getResources(), key);
    }

    @Override
    public void toggleFavoriteItem() {
        final boolean isFavouritesItem = mDetailViewBodyContainerFvb.isFavouritesItem();
        final boolean updatedState = !isFavouritesItem;
        if (mDetailViewBodyContainerFvb.isNewItemInsertionMode()) {
            mDetailViewBodyContainerFvb.updateFavouritesState(updatedState);
            mSoftButtonsViewBinder.renderFavouritesItemState(updatedState);
            return;
        }
        final String itemKey = mDetailViewBodyContainerFvb.getItemKey();
        getPresenter().updateFavouritesStateToRepository(itemKey, updatedState);
    }

    @Override
    public void shareClipItem() {
        final ClipDomain domain = mDetailViewBodyContainerFvb.getOriginalDomain();
        getPresenter().shareClipItem(domain);
    }

    @Override
    public void copyClipItem() {
        if (!mDetailViewBodyContainerFvb.isNewItemInsertionMode()) {
            ClipListViewSkyRail.getInstance().getSkyRail().send(
                  new ClipListViewSkyRailEvents.DeletedItem(mDetailViewBodyContainerFvb.getItemKey()));
        }
        ClipboardManagerUtil.copyText(mDetailViewBodyContainerFvb.getDomainWithInputData().getTextData());
        finishActivity(true);
    }

    @Override
    public void setEditMode() {
        getActionBar().setTitle(getString(R.string.edit_view));
        mDetailViewBodyContainerFvb.renderEditMode();
    }

    @Override
    public void setReadOnlyMode() {
        getActionBar().setTitle(getString(R.string.detail_view));
        mDetailViewBodyContainerFvb.renderReadOnlyMode();
    }

    @Override
    public void notifyUpdatedFavoriteState(boolean isFavouritesItem) {
        if (isFavouritesItem) {
            renderToastMsg(getString(R.string.msg_success_to_add_favourites_item));
        } else {
            renderToastMsg(getString(R.string.msg_success_to_remove_favourites_item));
        }

        if (!mDetailViewBodyContainerFvb.isNewItemInsertionMode()) {
            ClipListViewSkyRail.getInstance().getSkyRail().send(
                  new ClipListViewSkyRailEvents.UpdateFavouritesState(mDetailViewBodyContainerFvb.getItemKey(), isFavouritesItem));
        }
        mDetailViewBodyContainerFvb.updateFavouritesState(isFavouritesItem);
        mSoftButtonsViewBinder.renderFavouritesItemState(isFavouritesItem);

        final boolean isFavouritesFilterOn = DefaultSharedPrefWrapper.getInstance().getBoolean(
              MainApplication.getAppContext(), SharedPrefKeys.KEY_CLIP_LIST_FAVOURITES_ITEM_SWITCH_ON);
        if (isFavouritesFilterOn && !isFavouritesItem) {
            getActivity().finish();
        }
    }

    @Override
    public void notifyUpdatedClipItem(@NonNull final ClipDomain domain) {
        ClipListViewSkyRail.getInstance().getSkyRail().send(new ClipListViewSkyRailEvents.UpdatedItem(domain));
        renderToastMsg(getString(R.string.msg_success_to_update_clip_item));
        finishActivity(true);
    }

    @Override
    public void finishActivity(boolean force) {
        if (force) {
            getActivity().finish();
            return;
        }
        if (mDetailViewBodyContainerFvb.isNewItemInsertionMode()) {
            handleExitWithNewItemInsertionMode();
            return;
        }
        handleExitWithEditItemMode();
    }

    @Override
    public void onBackPressed() {
        finishActivity(false);
    }

    @Override
    public void notifyInsertionSuccess(@NonNull ClipDomain domain) {
        ClipListViewSkyRail.getInstance().getSkyRail().send(new ClipListViewSkyRailEvents.InsertedNewItem(domain));
        finishActivity(true);
    }

    // ========================================================================
    // methods
    // ========================================================================
    private String retrieveClipItemKeyFromArgument(Bundle arguments) {
        if (arguments == null) {
            LogUtil.i(TAG, "Insertion mode.");
            return "";
        }
        return arguments.getString(ExtraKey.KEY_CLIP_ITEM_KEY);
    }

    @NonNull
    private ActionBar getActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar == null) {
            throw new NullPointerException("ActionBar is null.");
        }
        return actionBar;
    }

    private void handleExitWithNewItemInsertionMode() {
        if (!mDetailViewBodyContainerFvb.validateInputField()) {
            getActivity().finish();
            return;
        }
        insertOrUpdateDataToRepository();
    }

    private void handleExitWithEditItemMode() {
        final boolean existModifiedData = mDetailViewBodyContainerFvb.existModifiedData();
        if (!existModifiedData) {
            getActivity().finish();
            return;
        }
        insertOrUpdateDataToRepository();
    }

    private void insertOrUpdateDataToRepository() {
        if (mDetailViewBodyContainerFvb.isNewItemInsertionMode()) {
            getPresenter().insertNewItemToRepository(getResources(),
                  mDetailViewBodyContainerFvb.buildDomainForInsertionToRepository());
            return;
        }
        getPresenter().updateClipDataToRepository(getResources(), mDetailViewBodyContainerFvb.getDomainWithInputData());
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
