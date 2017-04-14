package com.seongil.mvplife.sample.common.auth.firebase;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class FirebaseSignInWithCredentialOnSubscribe implements SingleOnSubscribe<Task<AuthResult>> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    @NonNull
    private FirebaseAuth mAuth;
    @NonNull
    private AuthCredential mCredential;

    // ========================================================================
    // constructors
    // ========================================================================
    public FirebaseSignInWithCredentialOnSubscribe(@NonNull FirebaseAuth auth, @NonNull AuthCredential credential) {
        mAuth = auth;
        mCredential = credential;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    @Override
    public void subscribe(SingleEmitter<Task<AuthResult>> e) throws Exception {
        final OnCompleteListener<AuthResult> listener = task -> handleResultForCheckedCredential(e, task);
        mAuth.signInWithCredential(mCredential).addOnCompleteListener(listener);
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void handleResultForCheckedCredential(SingleEmitter<Task<AuthResult>> e, @NonNull Task<AuthResult> task) {
        if (e.isDisposed()) {
            return;
        }
        if (!task.isSuccessful()) {
            e.onError(task.getException());
            return;
        }
        e.onSuccess(task);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
