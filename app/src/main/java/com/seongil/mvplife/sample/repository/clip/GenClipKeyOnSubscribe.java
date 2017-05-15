package com.seongil.mvplife.sample.repository.clip;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.seongil.mvplife.sample.repository.common.RepoTableContracts;
import com.seongil.mvplife.sample.repository.exception.InvalidFirebaseUser;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * @author seong-il, kim
 * @since 2017. 5. 13.
 */
public class GenClipKeyOnSubscribe implements SingleOnSubscribe<String> {

    public GenClipKeyOnSubscribe() {
    }

    @Override
    public void subscribe(SingleEmitter<String> e) throws Exception {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (e.isDisposed()) {
            return;
        }
        if (user == null) {
            e.onError(new InvalidFirebaseUser("Can't find a firebase user"));
            return;
        }

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        String key = db.getReference(RepoTableContracts.TABLE_SUMMARY_POSTS)
              .child(user.getUid()).push().getKey();
        if (e.isDisposed()) {
            return;
        }
        e.onSuccess(key);
    }
}
