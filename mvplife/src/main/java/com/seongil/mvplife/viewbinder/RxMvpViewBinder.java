package com.seongil.mvplife.viewbinder;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author seong-il, kim
 * @since 17. 4. 25
 */
public class RxMvpViewBinder implements MvpViewBinder {

    private CompositeDisposable mCompositeDisposable;

    @Override
    public void initializeLayout(@NonNull View layout) {
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroyView() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.add(disposable);
        }
    }

    @VisibleForTesting
    protected boolean isDisposed() {
        return mCompositeDisposable.size() == 0;
    }
}
