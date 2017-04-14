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

/**
 * @param <V> The type of {@link MvpView}
 *
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public interface MvpPresenter<V extends MvpView> {

    // ========================================================================
    // Methods
    // ========================================================================

    /**
     * Attach the mvp view to this mPresenter
     *
     * @param view MVP view
     */
    void attachView(V view);

    /**
     * Detach the attached view from this mPresenter
     */
    void detachView();
}