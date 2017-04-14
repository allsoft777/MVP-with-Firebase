/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seongil.mvplife.fragment;

import android.os.Bundle;
import android.view.View;

import com.seongil.mvplife.base.MvpPresenter;
import com.seongil.mvplife.base.MvpView;
import com.seongil.mvplife.delegate.MvpDelegateCallback;
import com.seongil.mvplife.delegate.fragment.MvpFragmentDelegate;
import com.seongil.mvplife.delegate.fragment.MvpFragmentDelegateImpl;

/**
 * Abstract class for the fragment which is holding a reference of the {@link MvpPresenter}
 * Also, holding a {@link MvpFragmentDelegate} which is handling the lifecycle of the fragment.
 *
 * @param <V> The type of {@link MvpView}
 * @param <P> The type of {@link MvpPresenter}
 *
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>>
      extends CoreFragment implements MvpView, MvpDelegateCallback<V, P> {

    // ========================================================================
    // Constants
    // ========================================================================

    // ========================================================================
    // Fields
    // ========================================================================
    private MvpFragmentDelegate mFragmentDelegate;
    private P mPresenter;

    // ========================================================================
    // Constructors
    // ========================================================================

    // ========================================================================
    // Getter & Setter
    // ========================================================================

    // ========================================================================
    // Methods for/from SuperClass/Interfaces
    // ========================================================================
    @Override
    public abstract P createPresenter();

    @Override
    public P getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(P presenter) {
        mPresenter = presenter;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V getMvpView() {
        return (V) this;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMvpDelegate().onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        getMvpDelegate().onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMvpDelegate().onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getMvpDelegate().onDestroy();
    }

    // ========================================================================
    // Methods
    // ========================================================================
    protected MvpFragmentDelegate getMvpDelegate() {
        if (mFragmentDelegate == null) {
            mFragmentDelegate = new MvpFragmentDelegateImpl<>(this);
        }

        return mFragmentDelegate;
    }

    // ========================================================================
    // Inner and Anonymous Classes
    // ========================================================================
}