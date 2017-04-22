package com.seongil.mvplife.sample.ui.login;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.seongil.mvplife.base.MvpBasePresenter;
import com.seongil.mvplife.sample.common.firebase.auth.RxFirebaseAuth;
import com.seongil.mvplife.sample.common.google.RxGoogleApiClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .filter(client -> isViewAttached())
              .subscribe(
                    client -> getView().setGoogleApiClient(client),
                    t -> getView().onGoogleApiClientConnectionFailed(t)
              );
    }

    public void checkFirebaseAuthState(@NonNull FirebaseAuth auth) {
        getView().showProgressDialog();

        RxFirebaseAuth.getInstance()
              .authStateChanged(auth)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .filter(fireBaseAuth -> isViewAttached())
              .doOnNext(fireBaseAuth -> getView().hideProgressDialog())
              .subscribe(
                    this::handleSucceedAuth,
                    t -> getView().renderErrorMsg(t.getMessage())
              );
    }

    public void signOut(@NonNull FirebaseAuth firebaseAuth, @NonNull GoogleApiClient googleApiClient) {
        getView().showProgressDialog();

        RxFirebaseAuth.getInstance()
              .signOut(firebaseAuth, googleApiClient)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .filter(status -> isViewAttached())
              .subscribe(status -> {
                  getView().hideProgressDialog();
                  getView().onGoogleSignOut(status);
              });
    }

    public void revokeAccessFromGoogleApiClient(@NonNull FirebaseAuth firebaseAuth, @NonNull
          GoogleApiClient googleApiClient) {
        getView().showProgressDialog();

        RxFirebaseAuth.getInstance()
              .revokeAccessFromGoogleApiClient(firebaseAuth, googleApiClient)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .filter(status -> isViewAttached())
              .subscribe(status -> {
                  getView().hideProgressDialog();
                  getView().onGoogleSignOut(status);
              });
    }

    public void firebaseAuthWithGoogle(@NonNull FirebaseAuth firebaseAuth, @NonNull GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        RxFirebaseAuth.getInstance()
              .signInWithCredential(firebaseAuth, credential)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .filter(task -> isViewAttached())
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
