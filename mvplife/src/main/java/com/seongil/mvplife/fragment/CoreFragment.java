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

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author seong-il, kim
 * @since 17. 1. 6
 */
public class CoreFragment extends Fragment {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int INVALID_LAYOUT_ID = 0;

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================
    protected int getLayoutRes() {
        return INVALID_LAYOUT_ID;
    }

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = getLayoutRes();
        if (layoutRes == INVALID_LAYOUT_ID) {
            throw new IllegalArgumentException("Layout id is 0. You must override the getLayoutRes method");
        }

        return inflater.inflate(layoutRes, container, false);
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
