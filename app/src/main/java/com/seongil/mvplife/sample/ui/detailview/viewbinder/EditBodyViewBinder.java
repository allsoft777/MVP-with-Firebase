package com.seongil.mvplife.sample.ui.detailview.viewbinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.repository.common.RepoTableContracts;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;
import com.seongil.mvplife.viewbinder.RxMvpViewBinder;

/**
 * @author seong-il, kim
 * @since 17. 5. 1
 */
public class EditBodyViewBinder extends RxMvpViewBinder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private EditText mEditText;
    private ClipDomainViewModel mViewModel;

    // ========================================================================
    // constructors
    // ========================================================================
    public EditBodyViewBinder() {
        mViewModel = new ClipDomainViewModel(new ClipDomain());
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
        mEditText = (EditText) layout.findViewById(R.id.edit_text);
        mEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                toggleSoftInput();
            } else {
                hideSoftInput();
            }
        });
        renderReadOnlyMode();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideSoftInput();
    }

    // ========================================================================
    // methods
    // ========================================================================
    public ClipDomain getOriginalDomain() {
        return mViewModel.getDomain();
    }

    public boolean isNewItemInsertionMode() {
        return mViewModel.isInvalidDomain();
    }

    public ClipDomain getDomainWithInputData() {
        final ClipDomain domain = new ClipDomain(mViewModel.getDomain());
        fillInputDataToViewModel(domain);
        return domain;
    }

    public ClipDomain buildDomainForInsertionToRepository() {
        mViewModel.getDomain().setCreatedAt(System.currentTimeMillis());
        mViewModel.getDomain().setTextData(mEditText.getText().toString());
        mViewModel.getDomain().setSource(RepoTableContracts.DATA_SOURCE);
        return mViewModel.getDomain();
    }

    public boolean validateInputField() {
        final String inputData = mEditText.getText().toString();
        return !TextUtils.isEmpty(inputData) && !inputData.trim().isEmpty();
    }

    private void fillInputDataToViewModel(@NonNull ClipDomain domain) {
        domain.setTextData(mEditText.getText().toString());
    }

    public String getItemKey() {
        return mViewModel.getDomain().getKey();
    }

    public boolean isFavouritesItem() {
        return mViewModel.getDomain().isFavouritesItem();
    }

    public void renderClipItemDomain(@NonNull ClipDomain domain) {
        mViewModel = new ClipDomainViewModel(domain);
        mEditText.setText(mViewModel.getDomain().getTextData());
        renderReadOnlyMode();
    }

    public void updateFavouritesState(final boolean isFavouritesItem) {
        mViewModel.getDomain().setFavouritesItem(isFavouritesItem);
    }

    public void renderEditMode() {
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
    }

    public void renderReadOnlyMode() {
        mEditText.setFocusable(false);
        mEditText.setFocusableInTouchMode(false);
        mEditText.clearFocus();
        hideSoftInput();
    }

    public boolean existModifiedData() {
        return !mViewModel.getDomain().getTextData().equals(mEditText.getText().toString());
    }

    public void hideSoftInput() {
        InputMethodManager imm =
              (InputMethodManager) MainApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    private void toggleSoftInput() {
        InputMethodManager imm =
              (InputMethodManager) MainApplication.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
