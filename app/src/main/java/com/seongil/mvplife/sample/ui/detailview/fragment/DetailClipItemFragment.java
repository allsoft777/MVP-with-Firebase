package com.seongil.mvplife.sample.ui.detailview.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import com.seongil.mvplife.sample.ui.cliplist.skyrail.SkyRailClipListEvent;
import com.seongil.mvplife.sample.ui.detailview.viewbinder.EditBodyViewBinder;
import com.seongil.mvplife.sample.ui.detailview.viewbinder.EditClipItemFragmentViewBinderListener;
import com.seongil.mvplife.sample.ui.detailview.viewbinder.EditSoftButtonsViewBinder;

/**
 * @author seong-il, kim
 * @since 17. 5. 1
 */
public class DetailClipItemFragment
      extends BaseFragment<DetailClipItemView, DetailClipItemPresenter> implements DetailClipItemView,
      EditClipItemFragmentViewBinderListener {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "DetailClipItemFragment";

    // ========================================================================
    // fields
    // ========================================================================
    private EditBodyViewBinder mEditBodyViewBinder;
    private EditSoftButtonsViewBinder mSoftButtonsViewBinder;
    private ProgressDialog mProgressDialog;

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

        mEditBodyViewBinder = new EditBodyViewBinder();
        mSoftButtonsViewBinder = new EditSoftButtonsViewBinder(this);

        mEditBodyViewBinder.initializeLayout(view.findViewById(R.id.content_container));
        mSoftButtonsViewBinder.initializeLayout(view.findViewById(R.id.soft_menu_container));

        final String clipItemKey = retrieveClipItemKeyFromArgument(getArguments());
        if (TextUtils.isEmpty(clipItemKey)) {
            mEditBodyViewBinder.renderEditMode();
            getActionBar().setTitle(getString(R.string.edit_view));
        } else {
            mEditBodyViewBinder.renderLoadingView();
            getActionBar().setTitle(getString(R.string.detail_view));
            getPresenter().fetchClipDataFromRepository(getResources(), clipItemKey);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mEditBodyViewBinder.onDestroyView();
        dismissProgressDialog();
        mProgressDialog = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        mEditBodyViewBinder.hideSoftInput();
    }

    @Override
    public void renderClipDomain(@NonNull ClipDomain domain) {
        mEditBodyViewBinder.renderClipItemDomain(domain);
        mSoftButtonsViewBinder.renderFavouritesItemState(domain.isFavouritesItem());
    }

    @Override
    public void renderError(@NonNull Throwable t) {
        renderToastMsg(t.getMessage());
    }

    @Override
    public void notifyRemovedItem(@NonNull String itemKey) {
        ClipListViewSkyRail.getInstance().getSkyRail().send(new SkyRailClipListEvent.DeletedItem(itemKey));
        renderToastMsg(getString(R.string.msg_remove_successfully));
        getActivity().finish();
    }

    @Override
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showProgressDialog(@NonNull String msg) {
        if (mProgressDialog == null) {
            createProgressDialog();
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    @Override
    public void deleteClipItem() {
        final String key = mEditBodyViewBinder.getItemKey();
        getPresenter().deleteClipItem(getResources(), key);
    }

    @Override
    public void toggleFavoriteItem() {
        final boolean isFavouritesItem = mEditBodyViewBinder.isFavouritesItem();
        final boolean updatedState = !isFavouritesItem;
        if (mEditBodyViewBinder.isNewItemInsertionMode()) {
            mEditBodyViewBinder.updateFavouritesState(updatedState);
            mSoftButtonsViewBinder.renderFavouritesItemState(updatedState);
            return;
        }
        final String itemKey = mEditBodyViewBinder.getItemKey();
        getPresenter().updateFavouritesStateToRepository(itemKey, updatedState);
    }

    @Override
    public void shareClipItem() {
        final ClipDomain domain = mEditBodyViewBinder.getOriginalDomain();
        getPresenter().shareClipItem(domain);
    }

    @Override
    public void copyClipItem() {
        if (!mEditBodyViewBinder.isNewItemInsertionMode()) {
            ClipListViewSkyRail.getInstance().getSkyRail().send(
                  new SkyRailClipListEvent.DeletedItem(mEditBodyViewBinder.getItemKey()));
        }
        ClipboardManagerUtil.copyText(mEditBodyViewBinder.getDomainWithInputData().getTextData());
        finishActivity(true);
    }

    @Override
    public void setEditMode() {
        getActionBar().setTitle(getString(R.string.edit_view));
        mEditBodyViewBinder.renderEditMode();
    }

    @Override
    public void setReadOnlyMode() {
        getActionBar().setTitle(getString(R.string.detail_view));
        mEditBodyViewBinder.renderReadOnlyMode();
    }

    @Override
    public void notifyUpdatedFavoriteState(boolean isFavouritesItem) {
        if (isFavouritesItem) {
            renderToastMsg(getString(R.string.msg_success_to_add_favourites_item));
        } else {
            renderToastMsg(getString(R.string.msg_success_to_remove_favourites_item));
        }

        if (!mEditBodyViewBinder.isNewItemInsertionMode()) {
            ClipListViewSkyRail.getInstance().getSkyRail().send(
                  new SkyRailClipListEvent.UpdateFavouritesState(mEditBodyViewBinder.getItemKey(), isFavouritesItem));
        }
        mEditBodyViewBinder.updateFavouritesState(isFavouritesItem);
        mSoftButtonsViewBinder.renderFavouritesItemState(isFavouritesItem);

        final boolean isFavouritesFilterOn = DefaultSharedPrefWrapper.getInstance().getBoolean(
              MainApplication.getAppContext(), SharedPrefKeys.KEY_CLIP_LIST_FAVOURITES_ITEM_SWITCH_ON);
        if (isFavouritesFilterOn && !isFavouritesItem) {
            getActivity().finish();
        }
    }

    @Override
    public void notifyUpdatedClipItem(@NonNull final ClipDomain domain) {
        ClipListViewSkyRail.getInstance().getSkyRail().send(new SkyRailClipListEvent.UpdatedItem(domain));
        renderToastMsg(getString(R.string.msg_success_to_update_clip_item));
        finishActivity(true);
    }

    @Override
    public void finishActivity(boolean force) {
        if (force) {
            getActivity().finish();
            return;
        }
        if (mEditBodyViewBinder.isNewItemInsertionMode()) {
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
        ClipListViewSkyRail.getInstance().getSkyRail().send(new SkyRailClipListEvent.InsertedNewItem(domain));
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
        if (!mEditBodyViewBinder.validateInputField()) {
            getActivity().finish();
            return;
        }
        renderDialogToAskUpdateDataToRepository();
    }

    private void handleExitWithEditItemMode() {
        final boolean existModifiedData = mEditBodyViewBinder.existModifiedData();
        if (!existModifiedData) {
            getActivity().finish();
            return;
        }
        renderDialogToAskUpdateDataToRepository();
    }

    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void renderDialogToAskUpdateDataToRepository() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.exit_confirm_dialog))
              .setMessage(exitDialogMessage())
              .setCancelable(false)
              .setPositiveButton(getString(R.string.confirm), (dialog, whichButton) -> {
                  if (mEditBodyViewBinder.isNewItemInsertionMode()) {
                      getPresenter().insertNewItemToRepository(getResources(),
                            mEditBodyViewBinder.buildDomainForInsertionToRepository());
                      return;
                  }
                  getPresenter().updateClipDataToRepository(getResources(),
                        mEditBodyViewBinder.getDomainWithInputData());
              })
              .setNegativeButton(getString(R.string.no), (dialog, whichButton) -> {
                  finishActivity(true);
                  dialog.dismiss();
              });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String exitDialogMessage() {
        if (mEditBodyViewBinder.isNewItemInsertionMode()) {
            return getString(R.string.msg_insert_new_item);
        }
        return getString(R.string.msg_exit_with_saving_item);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
