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

import java.lang.ref.WeakReference;

/**
 * @param <V> The type of {@link MvpView}
 *
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public class MvpBasePresenter<V extends MvpView> implements MvpPresenter<V> {

    // ========================================================================
    // Constants
    // ========================================================================

    // ========================================================================
    // Fields
    // ========================================================================
    private WeakReference<MvpView> mViewRef;

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
    public void attachView(V view) {
        mViewRef = new WeakReference<MvpView>(view);
    }

    @Override
    public void detachView() {
        if (mViewRef == null) {
            return;
        }
        mViewRef.clear();
        mViewRef = null;
    }

    // ========================================================================
    // Methods
    // ========================================================================
    @SuppressWarnings("unchecked")
    protected V getView() {
        return (V) mViewRef.get();
    }

    protected boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    // ========================================================================
    // Inner and Anonymous Classes
    // ========================================================================
}
