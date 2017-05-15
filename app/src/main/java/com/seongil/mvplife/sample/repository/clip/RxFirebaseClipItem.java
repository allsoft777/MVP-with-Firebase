package com.seongil.mvplife.sample.repository.clip;

import io.reactivex.Single;

/**
 * @author seong-il, kim
 * @since 2017. 5. 13.
 */
public class RxFirebaseClipItem {

    private static RxFirebaseClipItem sInstance;

    public static synchronized RxFirebaseClipItem getInstance() {
        if (sInstance == null) {
            sInstance = new RxFirebaseClipItem();
        }
        return sInstance;
    }

    public Single<String> genNewKey() {
        return Single.create(new GenClipKeyOnSubscribe());
    }
}
