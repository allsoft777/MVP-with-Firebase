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

package com.seongil.mvplife.delegate;

import com.seongil.mvplife.base.MvpPresenter;
import com.seongil.mvplife.base.MvpView;

/**
 * Implements the delegation layer for the fragment using by the MVP architecture.
 *
 * @param <V> The type of {@link MvpView}
 * @param <P> The type of {@link MvpPresenter}
 *
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public class MvpDelegateImpl<V extends MvpView, P extends MvpPresenter<V>> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private final MvpDelegateCallback<V, P> mDelegateCallback;

    // ========================================================================
    // constructors
    // ========================================================================
    public MvpDelegateImpl(MvpDelegateCallback<V, P> delegateCallback) {
        if (delegateCallback == null) {
            throw new NullPointerException("MvpDelegateCallback is null");
        }
        mDelegateCallback = delegateCallback;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    public void createPresenter() {
        P presenter = mDelegateCallback.getPresenter();
        if (presenter == null) {
            presenter = mDelegateCallback.createPresenter();
        }
        if (presenter == null) {
            throw new NullPointerException("Presenter is null! " +
                  "You have to return the valid presenter.");
        }
        mDelegateCallback.setPresenter(presenter);
    }

    public void attachView() {
        final P presenter = mDelegateCallback.getPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter is null from getPresenter()");
        }
        presenter.attachView(mDelegateCallback.getMvpView());
    }

    public void detachView() {
        final P presenter = mDelegateCallback.getPresenter();
        if (presenter == null) {
            throw new NullPointerException("Presenter is null from getPresenter()");
        }
        presenter.detachView();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
