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

package com.seongil.mvplife.delegate.fragment;

import android.os.Bundle;
import android.view.View;

import com.seongil.mvplife.base.MvpPresenter;
import com.seongil.mvplife.base.MvpView;
import com.seongil.mvplife.delegate.MvpDelegateCallback;
import com.seongil.mvplife.delegate.MvpDelegateImpl;

/**
 * @param <V> The type of {@link MvpView}
 * @param <P> The type of {@link MvpPresenter}
 *
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public class MvpFragmentDelegateImpl<V extends MvpView, P extends MvpPresenter<V>>
      implements MvpFragmentDelegate {

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
    public MvpFragmentDelegateImpl(MvpDelegateCallback<V, P> delegateCallback) {
        if (delegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null!");
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
    public void onCreate(Bundle saved) {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getDelegateImpl().createPresenter();
        getDelegateImpl().attachView();
    }

    @Override
    public void onDestroyView() {
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
