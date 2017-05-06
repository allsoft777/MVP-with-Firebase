package com.seongil.mvplife.sample.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jakewharton.rxbinding2.view.RxView;
import com.seongil.mvplife.fragment.BaseMvpFragmentV4;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.firebase.exception.GoogleApiClientConnectionFailThrowable;
import com.seongil.mvplife.sample.ui.cliplist.activity.ClipItemListActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class LoginFragment extends BaseMvpFragmentV4<LoginView, LoginPresenter> implements LoginView {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "LoginFragment";
    private static final int RC_SIGN_IN = 9001;

    // ========================================================================
    // fields
    // ========================================================================
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFireBaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInBtn;
    private boolean mLaunchedMainView;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized LoginFragment newInstnace() {
        return new LoginFragment();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_login;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSignInBtn = (SignInButton) view.findViewById(R.id.sign_in_button);
        RxView.clicks(mSignInBtn)
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(v -> signIn());

        getPresenter().signInGoogle(getActivity());
        mFireBaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().checkFirebaseAuthState(mFireBaseAuth);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideProgressDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                renderSignedOutUser();
            }
        }
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.msg_loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void renderErrorMsg(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setGoogleApiClient(GoogleApiClient gac) {
        mGoogleApiClient = gac;
    }

    @Override
    public <T extends Throwable> void onGoogleApiClientConnectionFailed(T t) {
        if (!(t instanceof GoogleApiClientConnectionFailThrowable)) {
            return;
        }
        GoogleApiClientConnectionFailThrowable throwable = (GoogleApiClientConnectionFailThrowable) t;
        renderErrorMsg(throwable.getMessage() + " code : " + throwable.getConnectionResult().getErrorCode());
    }

    @Override
    public void renderSignedInUser(FirebaseUser firebaseUser) {
        mSignInBtn.setVisibility(View.GONE);
        launchMainView();
    }

    @Override
    public void renderSignedOutUser() {
        mSignInBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGoogleSignOut(Status status) {
        renderSignedOutUser();
    }

    @Override
    public void onFirebaseAuthWithGoogleCompleted(Task<AuthResult> authResultTask) {
        Log.i(TAG, "onFirebaseAuthWithGoogleCompleted");
    }

    @Override
    public void onFirebaseAuthWithGoogleFailed(Throwable t) {
        if (t instanceof FirebaseNetworkException) { // explicit exception
            renderErrorMsg("Network Connection Error.");
        }
        renderErrorMsg("onFirebaseAuthWithGoogleFailed : " + t.getMessage());
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        getPresenter().firebaseAuthWithGoogle(mFireBaseAuth, acct);
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void launchMainView() {
        if (mLaunchedMainView) {
            return;
        }
        mLaunchedMainView = true;
        Intent intent = new Intent(getActivity(), ClipItemListActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
