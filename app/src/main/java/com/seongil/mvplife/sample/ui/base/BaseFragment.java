package com.seongil.mvplife.sample.ui.base;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.seongil.mvplife.base.MvpPresenter;
import com.seongil.mvplife.fragment.BaseMvpFragmentV4;
import com.seongil.mvplife.sample.common.utils.ToastUtil;

/**
 * @author seong-il, kim
 * @since 17. 3. 20
 */
public abstract class BaseFragment<V extends BaseView, P extends MvpPresenter<V>>
      extends BaseMvpFragmentV4<V, P> implements BaseView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private ProgressDialog mProgressDialog;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dismissProgressDialog();
        mProgressDialog = null;
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

    // ========================================================================
    // methods
    // ========================================================================
    private void createProgressDialog() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    protected void renderToastMsg(String msg) {
        ToastUtil.showToast(msg);
    }

    protected void renderToastMsg(String msg, int duration) {
        ToastUtil.showToast(msg, duration);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
