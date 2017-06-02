package com.seongil.mvplife.sample.ui.login;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.seongil.mvplife.base.MvpBasePresenter;
import com.seongil.mvplife.sample.common.firebase.auth.RxFirebaseAuth;
import com.seongil.mvplife.sample.common.google.RxGoogleApiClient;
import com.seongil.mvplife.sample.common.utils.RxTransformer;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class LoginPresenter extends MvpBasePresenter<LoginView> {

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

    // ========================================================================
    // methods
    // ========================================================================
    public void signInGoogle(@NonNull FragmentActivity activity) {
        RxGoogleApiClient.getInstance()
              .getGoogleApiClient(activity)
              .compose(RxTransformer.asyncSingleStream())
              .filter(___ -> isViewAttached())
              .subscribe(
                    client -> getView().setGoogleApiClient(client),
                    t -> getView().onGoogleApiClientConnectionFailed(t)
              );
    }

    public void checkFirebaseAuthState(@NonNull FirebaseAuth auth) {
        RxFirebaseAuth.getInstance()
              .authStateChanged(auth)
              .compose(RxTransformer.asyncObservableStream())
              .filter(___ -> isViewAttached())
              .subscribe(this::handleSucceedAuth, t -> getView().renderErrorMsg(t.getMessage()));
    }

    public void firebaseAuthWithGoogle(@NonNull FirebaseAuth firebaseAuth, @NonNull GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        RxFirebaseAuth.getInstance()
              .signInWithCredential(firebaseAuth, credential)
              .compose(RxTransformer.asyncSingleStream())
              .filter(___ -> isViewAttached())
              .subscribe(
                    result -> getView().onFirebaseAuthWithGoogleCompleted(result),
                    t -> getView().onFirebaseAuthWithGoogleFailed(t)
              );
    }

    private void handleSucceedAuth(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            getView().renderSignedInUser(user);
        } else {
            getView().renderSignedOutUser();
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
