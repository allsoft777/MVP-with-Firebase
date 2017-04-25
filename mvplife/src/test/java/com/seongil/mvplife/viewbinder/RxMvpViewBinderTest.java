package com.seongil.mvplife.viewbinder;

import android.test.mock.MockContext;
import android.view.View;

import org.junit.Assert;
import org.junit.Test;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * @author seong-il, kim
 * @since 17. 4. 25
 */
public class RxMvpViewBinderTest {

    @Test
    public void compositeDisposableTest() throws Exception {
        RxMvpViewBinder vb = new RxMvpViewBinder();
        MockContext context = new MockContext();
        vb.initializeLayout(new View(context));

        vb.addDisposable(Single.create(new SingleOnSubscribe<Boolean>() {
            @Override
            public void subscribe(SingleEmitter<Boolean> e) throws Exception {
                e.onSuccess(true);
            }
        }).subscribe());

        Assert.assertFalse(vb.isDisposed());
        vb.onDestroyView();
        Assert.assertTrue(vb.isDisposed());
    }
}
