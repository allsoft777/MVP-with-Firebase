package com.seongil.mvplife.sample.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.seongil.mvplife.sample.common.auth.firebase.exception.GoogleApiClientConnectionFailThrowable;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

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
    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private ImageView mUserThumbnail;
    private SignInButton mSignInBtn;
    private View mSignOutAndDisconnectContainer;

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

        mStatusTextView = (TextView) view.findViewById(R.id.status);
        mDetailTextView = (TextView) view.findViewById(R.id.detail);
        mUserThumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        mSignOutAndDisconnectContainer = view.findViewById(R.id.sign_out_and_disconnect);

        mSignInBtn = (SignInButton) view.findViewById(R.id.sign_in_button);
        RxView.clicks(mSignInBtn)
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(v -> signIn());

        Button signOutbnt = (Button) view.findViewById(R.id.sign_out_button);
        RxView.clicks(signOutbnt)
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(v -> signOut());

        Button disconnectBtn = (Button) view.findViewById(R.id.disconnect_button);
        RxView.clicks(disconnectBtn)
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(v -> revokeAccess());

        Button enjoyBtn = (Button) view.findViewById(R.id.goto_main_button);
        RxView.clicks(enjoyBtn)
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(v -> launchMainView());

        getPresenter().signInGoogle(getActivity());
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().checkFirebaseAuthState(mFirebaseAuth);
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
                renderSignedOutUser(null);
            }
        }
    }

    @Override
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
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
        mStatusTextView.setText(getString(R.string.google_status_fmt, firebaseUser.getEmail()));
        mDetailTextView.setText(getString(R.string.firebase_status_fmt, firebaseUser.getUid()));
        mSignInBtn.setVisibility(View.GONE);
        mSignOutAndDisconnectContainer.setVisibility(View.VISIBLE);

        Glide
              .with(this)
              .fromUri()
              .load(firebaseUser.getPhotoUrl())
              .bitmapTransform(new CropCircleTransformation(getActivity().getApplicationContext()))
              .placeholder(R.drawable.default_user_profile)
              .into(mUserThumbnail);
    }

    @Override
    public void renderSignedOutUser(@Nullable FirebaseUser firebaseUser) {
        mStatusTextView.setText(R.string.signed_out);
        mDetailTextView.setText(null);
        mSignInBtn.setVisibility(View.VISIBLE);
        mSignOutAndDisconnectContainer.setVisibility(View.GONE);

        Glide
              .with(this)
              .fromResource()
              .load(R.drawable.default_user_profile)
              .bitmapTransform(new CropCircleTransformation(getActivity().getApplicationContext()))
              .into(mUserThumbnail);
    }

    @Override
    public void onGoogleSignOut(Status status) {
        renderSignedOutUser(null);
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
        getPresenter().firebaseAuthWithGoogle(mFirebaseAuth, acct);
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void signOut() {
        getPresenter().signOut(mFirebaseAuth, mGoogleApiClient);
    }

    private void revokeAccess() {
        getPresenter().revokeAccessFromGoogleApiClient(mFirebaseAuth, mGoogleApiClient);
    }

    private void launchMainView() {
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
