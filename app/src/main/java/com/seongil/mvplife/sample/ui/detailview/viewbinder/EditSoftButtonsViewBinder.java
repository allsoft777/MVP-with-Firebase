package com.seongil.mvplife.sample.ui.detailview.viewbinder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.viewbinder.MvpViewBinder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author seong-il, kim
 * @since 17. 5. 2
 */
public class EditSoftButtonsViewBinder implements MvpViewBinder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private EditClipItemFragmentViewBinderListener mFragmentListener;
    private boolean mIsEditMode;
    private Button mFavoriteBtn;
    private Button mEditModeBtn;

    // ========================================================================
    // constructors
    // ========================================================================
    public EditSoftButtonsViewBinder(EditClipItemFragmentViewBinderListener fragmentViewBinderListener) {
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
        mEditModeBtn = (Button) layout.findViewById(R.id.btn_toggle_edit);
        RxView.clicks(mEditModeBtn)
              .compose(this::applyButtonClickThrottle)
              .subscribe(___ -> toggleEditModel());

        mFavoriteBtn = (Button) layout.findViewById(R.id.btn_favourites);
        RxView.clicks(mFavoriteBtn)
              .compose(this::applyButtonClickThrottle)
              .subscribe(___ -> mFragmentListener.toggleFavoriteItem());

        RxView.clicks(layout.findViewById(R.id.btn_delete))
              .compose(this::applyButtonClickThrottle)
              .subscribe(___ -> mFragmentListener.deleteClipItem());

        RxView.clicks(layout.findViewById(R.id.btn_copy))
              .compose(this::applyButtonClickThrottle)
              .subscribe(___ -> mFragmentListener.copyClipItem());

        RxView.clicks(layout.findViewById(R.id.btn_share))
              .compose(this::applyButtonClickThrottle)
              .subscribe(___ -> mFragmentListener.shareClipItem());

        RxView.clicks(layout.findViewById(R.id.btn_close))
              .compose(this::applyButtonClickThrottle)
              .subscribe(___ -> mFragmentListener.finishActivity(false));
    }

    @Override
    public void onDestroyView() {
        mFragmentListener = null;
    }

    // ========================================================================
    // methods
    // ========================================================================
    private Observable<Object> applyButtonClickThrottle(Observable<Object> obj) {
        return obj.throttleFirst(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread());
    }

    private void toggleEditModel() {
        if (mIsEditMode) {
            mEditModeBtn.setBackgroundResource(R.drawable.selector_ic_edit);
            mFragmentListener.setReadOnlyMode();
            mIsEditMode = false;
            return;
        }

        mEditModeBtn.setBackgroundResource(R.drawable.selector_ic_read_only);
        mFragmentListener.setEditMode();
        mIsEditMode = true;
    }

    public void renderFavouritesItemState(boolean isFavouritesItem) {
        mFavoriteBtn.setBackgroundResource(
              isFavouritesItem ? R.drawable.selector_ic_favorite_true : R.drawable.selector_ic_favorite_false);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
