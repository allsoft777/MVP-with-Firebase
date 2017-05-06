package com.seongil.mvplife.sample.repository.user;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Observable;

/**
 * @author seong-il, kim
 * @since 17. 4. 22
 */
public class RxFirebaseUser {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private static RxFirebaseUser sInstance;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized RxFirebaseUser getInstance() {
        if (sInstance == null) {
            sInstance = new RxFirebaseUser();
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
    public Observable<FirebaseUser> getCurrentUser() {
        return Observable.create(new GetFirebaseUserOnSubscribe());
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
