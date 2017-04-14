package com.seongil.mvplife.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.seongil.mvplife.base.MvpBasePresenter;
import com.seongil.mvplife.base.MvpView;
import com.seongil.mvplife.delegate.MvpDelegateCallback;
import com.seongil.mvplife.delegate.activity.MvpActivityDelegate;
import com.seongil.mvplife.delegate.activity.MvpActivityDelegateImpl;

/**
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public abstract class BaseMvpV7CompatActivity<V extends MvpView, P extends MvpBasePresenter<V>>
      extends AppCompatActivity implements MvpDelegateCallback<V, P>, MvpView {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    protected P mPresenter;
    protected MvpActivityDelegate mMvpDelegate;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    public abstract P createPresenter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(P presenter) {
        this.mPresenter = presenter;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getMvpView() {
        return (V) this;
    }

    @SuppressWarnings("unchecked")
    public MvpActivityDelegate getMvpDelegate() {
        if (mMvpDelegate == null) {
            mMvpDelegate = new MvpActivityDelegateImpl<>(this);
        }
        return mMvpDelegate;
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
