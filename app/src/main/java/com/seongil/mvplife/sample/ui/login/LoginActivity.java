package com.seongil.mvplife.sample.ui.login;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.ui.base.BaseActivity;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class LoginActivity extends BaseActivity {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        if (savedInstanceState == null) {
            attachFragment();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void attachFragment() {
        LoginFragment fragment = LoginFragment.newInstnace();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_main, fragment, "fragment");
        ft.commit();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
