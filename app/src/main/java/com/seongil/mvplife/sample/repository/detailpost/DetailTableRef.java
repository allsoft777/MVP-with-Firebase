package com.seongil.mvplife.sample.repository.detailpost;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.repository.common.RepoTableContracts;
import com.seongil.mvplife.sample.repository.exception.InvalidFirebaseUser;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @author seong-il, kim
 * @since 17. 5. 1
 */
public class DetailTableRef {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private static DetailTableRef sInstance;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized DetailTableRef getInstance() {
        if (sInstance == null) {
            sInstance = new DetailTableRef();
        }
        return sInstance;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    public Observable<DatabaseReference> getDetailPostItemDatabaseRef(String itemKey) {
        return Observable.create(e -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (e.isDisposed()) {
                return;
            }

            if (user == null) {
                e.onError(new InvalidFirebaseUser("There is no user."));
                return;
            }

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            if (db == null) {
                e.onError(new Throwable("FirebaseDatabase is invalid."));
                return;
            }
            e.onNext(db.getReference(RepoTableContracts.TABLE_DETAIL_POST)
                  .child(user.getUid()).child(itemKey));
        });
    }

    public Observable<Boolean> insertNewItemToRepository(
          @NonNull DatabaseReference summaryItemKeyRef, @NonNull ClipDomain domain) {
        return Observable.create(e -> {
            try {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (e.isDisposed()) {
                    return;
                }

                if (user == null) {
                    e.onError(new InvalidFirebaseUser("There is no user."));
                    return;
                }

                final String summaryItemKey = summaryItemKeyRef.getKey();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref =
                      db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(summaryItemKey);

                Map<String, Object> dataSet = new HashMap<>(4);
                dataSet.put(RepoTableContracts.COL_CREATED_AT, domain.getCreatedAt());
                dataSet.put(RepoTableContracts.COL_DATA, domain.getTextData());
                dataSet.put(RepoTableContracts.COL_SOURCE, domain.getSource());
                dataSet.put(RepoTableContracts.COL_FAVORITE_ITEM, domain.isFavouritesItem());

                ref.setValue(dataSet).addOnCompleteListener(task -> e.onNext(true));
            } catch (Exception ex) {
                ex.printStackTrace();
                e.onError(ex);
            }
        });
    }

    public Observable<Boolean> deleteClipItem(String itemKey) {
        return Observable.create(e -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (e.isDisposed()) {
                return;
            }

            if (user == null) {
                e.onError(new InvalidFirebaseUser("There is no user."));
                return;
            }

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref =
                  db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(itemKey);
            Task<Void> task = ref.removeValue();
            task.addOnCompleteListener(taskArgs -> e.onNext(true));
            task.addOnFailureListener(taskArgs -> e.onError(taskArgs.getCause()));
        });
    }

    public Observable<Boolean> updateClipItemToRepository(ClipDomain domain) {
        return Observable.create(e -> {
            try {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (e.isDisposed()) {
                    return;
                }

                if (user == null) {
                    e.onError(new InvalidFirebaseUser("There is no user."));
                    return;
                }

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref =
                      db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(domain.getKey());

                Map<String, Object> dataSet = new HashMap<>(4);
                dataSet.put(RepoTableContracts.COL_CREATED_AT, domain.getCreatedAt());
                dataSet.put(RepoTableContracts.COL_DATA, domain.getTextData());
                dataSet.put(RepoTableContracts.COL_SOURCE, domain.getSource());
                dataSet.put(RepoTableContracts.COL_FAVORITE_ITEM, domain.isFavouritesItem());

                Task<Void> task = ref.setValue(dataSet);
                task.addOnCompleteListener(___ -> e.onNext(true));
                task.addOnFailureListener(t -> e.onError(t.getCause()));
            } catch (Exception ex) {
                ex.printStackTrace();
                e.onError(ex);
            }
        });
    }

    public Observable<Boolean> updateFavouritesItemState(String itemKey, boolean isFavoriteItem) {
        return Observable.create(e -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (e.isDisposed()) {
                return;
            }

            if (user == null) {
                e.onError(new InvalidFirebaseUser("There is no user."));
                return;
            }

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref =
                  db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(itemKey);

            try {
                Map<String, Object> dataSet = new HashMap<>(1);
                dataSet.put(RepoTableContracts.COL_FAVORITE_ITEM, isFavoriteItem);

                Task<Void> task = ref.updateChildren(dataSet);
                task.addOnCompleteListener(___ -> e.onNext(true));
                task.addOnFailureListener(t -> e.onError(t.getCause()));
            } catch (Exception ex) {
                ex.printStackTrace();
                e.onError(ex);
            }
        });
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
