package com.seongil.mvplife.sample.repository.user;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.seongil.mvplife.sample.repository.exception.InvalidFirebaseUser;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * @author seong-il, kim
 * @since 17. 4. 22
 */
public class GetFirebaseUserOnSubscribe implements ObservableOnSubscribe<FirebaseUser> {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void subscribe(ObservableEmitter<FirebaseUser> e) throws Exception {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (e.isDisposed()) {
            return;
        }
        if (user == null) {
            e.onError(new InvalidFirebaseUser("Can't find a firebase user"));
            return;
        }
        e.onNext(user);
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
