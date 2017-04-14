package com.seongil.mvplife.sample.common.auth.google;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.auth.firebase.exception.GoogleApiClientConnectionFailThrowable;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class GoogleApiClientBuilderOnSubscribe implements SingleOnSubscribe<GoogleApiClient> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private FragmentActivity mFragmentActivity;

    // ========================================================================
    // constructors
    // ========================================================================
    public GoogleApiClientBuilderOnSubscribe(@NonNull FragmentActivity activity) {
        mFragmentActivity = activity;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void subscribe(SingleEmitter<GoogleApiClient> e) throws Exception {
        if (e.isDisposed()) {
            return;
        }

        final GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestIdToken(mFragmentActivity.getString(R.string.default_web_client_id))
              .requestEmail()
              .requestProfile()
              .build();

        GoogleApiClient client = new GoogleApiClient.Builder(mFragmentActivity)
              .enableAutoManage(mFragmentActivity,
                    (result) -> e.onError(new GoogleApiClientConnectionFailThrowable(result)))
              .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
              .build();

        if (e.isDisposed()) {
            return;
        }

        e.onSuccess(client);
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}