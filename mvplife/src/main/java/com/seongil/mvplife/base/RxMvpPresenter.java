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

package com.seongil.mvplife.base;

import android.support.annotation.NonNull;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public class RxMvpPresenter<V extends MvpView> extends MvpBasePresenter<V> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private CompositeDisposable mCompositeDisposables;

    // ========================================================================
    // constructors
    // ========================================================================
    public RxMvpPresenter() {
        mCompositeDisposables = new CompositeDisposable();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void detachView() {
        super.detachView();
        mCompositeDisposables.clear();
        mCompositeDisposables = null;
    }

    // ========================================================================
    // methods
    // ========================================================================
    protected void addDisposable(@NonNull Disposable s) {
        if (mCompositeDisposables != null) {
            mCompositeDisposables.add(s);
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}