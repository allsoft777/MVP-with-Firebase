package com.seongil.mvplife.sample.common.firebase.auth;

import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class FirebaseRevokeAccessFromGoogleOnSubscribe implements SingleOnSubscribe<Status> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    @NonNull
    private FirebaseAuth mAuth;
    @NonNull
    private GoogleApiClient mGoogleApiClient;

    // ========================================================================
    // constructors
    // ========================================================================
    public FirebaseRevokeAccessFromGoogleOnSubscribe(@NonNull FirebaseAuth auth, @NonNull GoogleApiClient client) {
        mAuth = auth;
        mGoogleApiClient = client;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void subscribe(SingleEmitter<Status> e) throws Exception {
        mAuth.signOut();
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(status -> {
            if (e.isDisposed()) {
                return;
            }
            e.onSuccess(status);
        });
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
