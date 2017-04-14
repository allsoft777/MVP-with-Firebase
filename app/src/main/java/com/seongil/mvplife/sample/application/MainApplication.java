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

package com.seongil.mvplife.sample.application;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class MainApplication extends Application {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private RefWatcher mRefWatcher;
    private static MainApplication sInstance;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================
    public static RefWatcher getRefWatcher(Context context) {
        return ((MainApplication) context.getApplicationContext()).mRefWatcher;
    }

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }

        enabledStrictMode();
        mRefWatcher = LeakCanary.install(this);
    }

    // ========================================================================
    // methods
    // ========================================================================
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                  .detectAll()
                  .penaltyLog()
                  .penaltyDeath()
                  .build());
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
