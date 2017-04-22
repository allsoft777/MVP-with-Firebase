package com.seongil.mvplife.sample.common.utils;

import io.reactivex.CompletableTransformer;
import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class RxUtil {

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
    public static <T> FlowableTransformer<T, T> asyncFlowableStream() {
        return o -> o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> asyncSingleStream() {
        return o -> o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static CompletableTransformer asyncCompletableStream() {
        return o -> o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableTransformer<T, T> asyncObservableStream() {
        return o -> o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
