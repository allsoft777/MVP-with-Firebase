package com.seongil.mvplife.sample.ui.login;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.seongil.mvplife.base.MvpView;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public interface LoginView extends MvpView {

    // ========================================================================
    // Constants
    // ========================================================================

    // ========================================================================
    // Methods
    // ========================================================================
    void showProgressDialog();

    void hideProgressDialog();

    void renderErrorMsg(String msg);

    void setGoogleApiClient(GoogleApiClient gac);

    <T extends Throwable> void onGoogleApiClientConnectionFailed(T t);

    void renderSignedInUser(FirebaseUser firebaseUser);

    void renderSignedOutUser();

    void onGoogleSignOut(Status status);

    void onFirebaseAuthWithGoogleCompleted(Task<AuthResult> authResultTask);

    void onFirebaseAuthWithGoogleFailed(Throwable t);
}
