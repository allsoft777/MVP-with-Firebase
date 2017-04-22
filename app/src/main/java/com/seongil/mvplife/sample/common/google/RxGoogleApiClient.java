package com.seongil.mvplife.sample.common.google;

import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;

import io.reactivex.Single;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class RxGoogleApiClient {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private static RxGoogleApiClient sInstance;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized RxGoogleApiClient getInstance() {
        if (sInstance == null) {
            sInstance = new RxGoogleApiClient();
        }
        return sInstance;
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
    public Single<GoogleApiClient> getGoogleApiClient(final FragmentActivity activity) {
        return Single.create(new GoogleApiClientBuilderOnSubscribe(activity));
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
