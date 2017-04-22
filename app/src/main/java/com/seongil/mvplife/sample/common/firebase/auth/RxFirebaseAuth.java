package com.seongil.mvplife.sample.common.firebase.auth;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class RxFirebaseAuth {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private static RxFirebaseAuth sInstance;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized RxFirebaseAuth getInstance() {
        if (sInstance == null) {
            sInstance = new RxFirebaseAuth();
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

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public Observable<FirebaseAuth> authStateChanged(@NonNull FirebaseAuth auth) {
        return Observable.create(new FirebaseAuthStateChangedSubscribe(auth));
    }

    public Single<Task<AuthResult>> signInWithCredential(@NonNull FirebaseAuth firebaseAuth,
          @NonNull AuthCredential credential) {
        return Single.create(new FirebaseSignInWithCredentialOnSubscribe(firebaseAuth, credential));
    }

    public Single<Status> signOut(@NonNull FirebaseAuth firebaseAuth, @NonNull GoogleApiClient googleApiClient) {
        return Single.create(new FirebaseSignOutSubscribe(firebaseAuth, googleApiClient));
    }

    public Single<Status> revokeAccessFromGoogleApiClient(@NonNull FirebaseAuth firebaseAuth,
          @NonNull GoogleApiClient googleApiClient) {
        return Single.create(new FirebaseRevokeAccessFromGoogleOnSubscribe(firebaseAuth, googleApiClient));
    }
}