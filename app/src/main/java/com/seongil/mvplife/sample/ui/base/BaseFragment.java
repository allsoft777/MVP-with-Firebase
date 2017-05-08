package com.seongil.mvplife.sample.ui.base;

import com.seongil.mvplife.base.MvpPresenter;
import com.seongil.mvplife.base.MvpView;
import com.seongil.mvplife.fragment.BaseMvpFragmentV4;
import com.seongil.mvplife.sample.common.utils.ToastUtil;

/**
 * @author seong-il, kim
 * @since 17. 3. 20
 */
public abstract class BaseFragment<V extends MvpView, P extends MvpPresenter<V>>
      extends BaseMvpFragmentV4<V, P> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
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
