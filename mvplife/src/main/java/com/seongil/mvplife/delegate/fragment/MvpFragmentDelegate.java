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

/**
 * Interface definition for a fragment delegation using by MVP architecture.
 * This interface is defined to attach the MvpView and MvpPresenter to the Fragment
 * and remove the boilerplate codes in creating an architecture of MVP base codes.
 * <p/>
 * All of methods defined this class MUST follow the fragment lifecycle.
 *
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public interface MvpFragmentDelegate {

    // ========================================================================
    // Methods
    // ========================================================================
    void onCreate(Bundle saved);

    void onDestroy();

    void onViewCreated(View view, Bundle savedInstanceState);

    void onDestroyView();
}
