package com.seongil.mvplife.delegate.activity;

import android.os.Bundle;

import com.seongil.mvplife.base.MvpBasePresenter;
import com.seongil.mvplife.base.MvpView;
import com.seongil.mvplife.delegate.MvpDelegateCallback;
import com.seongil.mvplife.delegate.MvpDelegateImpl;

/**
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public class MvpActivityDelegateImpl<V extends MvpView, P extends MvpBasePresenter<V>>
      implements MvpActivityDelegate {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private final MvpDelegateCallback<V, P> mCallback;
    private MvpDelegateImpl<V, P> mDelegateImpl;

    // ========================================================================
    // constructors
    // ========================================================================
    public MvpActivityDelegateImpl(MvpDelegateCallback<V, P> delegateCallback) {
        if (delegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null");
        }
        mCallback = delegateCallback;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void onCreate(Bundle bundle) {
        getDelegateImpl().createPresenter();
        getDelegateImpl().attachView();
    }

    @Override
    public void onDestroy() {
        getDelegateImpl().detachView();
    }

    // ========================================================================
    // methods
    // ========================================================================
    protected MvpDelegateImpl<V, P> getDelegateImpl() {
        if (mDelegateImpl == null) {
            mDelegateImpl = new MvpDelegateImpl<V, P>(mCallback);
        }

        return mDelegateImpl;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
