package com.seongil.mvplife.sample.common.auth.firebase;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposables;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class FirebaseAuthStateChangedSubscribe implements ObservableOnSubscribe<FirebaseAuth> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    @NonNull
    private final FirebaseAuth mFirebaseAuth;

    // ========================================================================
    // constructors
    // ========================================================================
    public FirebaseAuthStateChangedSubscribe(@NonNull FirebaseAuth firebaseAuth) {
        mFirebaseAuth = firebaseAuth;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void subscribe(ObservableEmitter<FirebaseAuth> e) throws Exception {
        final FirebaseAuth.AuthStateListener listener = firebaseAuth -> authStateChanged(e, firebaseAuth);
        mFirebaseAuth.addAuthStateListener(listener);
        e.setDisposable(Disposables.fromAction(() -> mFirebaseAuth.removeAuthStateListener(listener)));
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void authStateChanged(ObservableEmitter<FirebaseAuth> emitter, @NonNull FirebaseAuth firebaseAuth) {
        if (emitter.isDisposed()) {
            return;
        }

        emitter.onNext(firebaseAuth);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
