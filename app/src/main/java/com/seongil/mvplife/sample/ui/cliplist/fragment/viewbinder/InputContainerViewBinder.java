package com.seongil.mvplife.sample.ui.cliplist.fragment.viewbinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.ui.detailview.activity.DetailClipItemActivity;
import com.seongil.mvplife.viewbinder.MvpViewBinder;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author seong-il, kim
 * @since 17. 5. 6
 */
public class InputContainerViewBinder implements MvpViewBinder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private View mContainerView;

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
    public void initializeLayout(@NonNull View layout) {
        mContainerView = layout.findViewById(R.id.input_container);
        RxView.clicks(layout.findViewById(R.id.make_notes))
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(___ -> launchEditView());
    }

    @Override
    public void onDestroyView() {

    }

    // ========================================================================
    // methods
    // ========================================================================
    private void launchEditView() {
        Intent intent = new Intent(MainApplication.getAppContext(), DetailClipItemActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getAppContext().startActivity(intent);
    }

    public void hideContainer() {
        mContainerView.setVisibility(View.GONE);
    }

    public void showContainer() {
        if (mContainerView.getVisibility() == View.VISIBLE) {
            return;
        }
        mContainerView.setVisibility(View.VISIBLE);
    }

    public void renderErrorView(@NonNull Throwable t) {
        // Nothing
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
