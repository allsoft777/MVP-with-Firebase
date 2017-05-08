package com.seongil.mvplife.sample.repository.detailpost;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
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
    public Observable<DatabaseReference> getDetailPostItemDatabaseRef(@NonNull final String itemKey) {
        return Observable.create(e -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (e.isDisposed()) {
                return;
            }

            if (user == null) {
                e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
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

    public Observable<ClipDomain> insertNewItemToRepository(
          @NonNull DatabaseReference summaryItemKeyRef, @NonNull ClipDomain domain) {
        return Observable.create(e -> {
            try {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (e.isDisposed()) {
                    return;
                }
                if (user == null) {
                    e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
                    return;
                }

                final String summaryItemKey = summaryItemKeyRef.getKey();
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref =
                      db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(summaryItemKey);
                ref.runTransaction(new Transaction.Handler() {

                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Map<String, Object> dataSet = new HashMap<>(4);
                        dataSet.put(RepoTableContracts.COL_CREATED_AT, domain.getCreatedAt());
                        dataSet.put(RepoTableContracts.COL_DATA, domain.getTextData());
                        dataSet.put(RepoTableContracts.COL_SOURCE, domain.getSource());
                        dataSet.put(RepoTableContracts.COL_FAVORITE_ITEM, domain.isFavouritesItem());
                        mutableData.setValue(dataSet);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        ClipDomain domain = dataSnapshot.getValue(ClipDomain.class);
                        if (databaseError != null) {
                            e.onError(new Throwable(databaseError.getMessage()));
                            return;
                        }
                        domain.setKey(dataSnapshot.getKey());
                        e.onNext(domain);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                e.onError(ex);
            }
        });
    }

    public Observable<Boolean> deleteClipItem(@NonNull String itemKey) {
        return Observable.create(e -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (e.isDisposed()) {
                return;
            }

            if (user == null) {
                e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
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

    public Observable<ClipDomain> updateClipItemToRepository(@NonNull ClipDomain domain) {
        return Observable.create(e -> {
            try {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (e.isDisposed()) {
                    return;
                }
                if (user == null) {
                    e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
                    return;
                }

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference ref =
                      db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(domain.getKey());
                ref.runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Map<String, Object> dataSet = new HashMap<>(4);
                        dataSet.put(RepoTableContracts.COL_CREATED_AT, domain.getCreatedAt());
                        dataSet.put(RepoTableContracts.COL_DATA, domain.getTextData());
                        dataSet.put(RepoTableContracts.COL_SOURCE, domain.getSource());
                        dataSet.put(RepoTableContracts.COL_FAVORITE_ITEM, domain.isFavouritesItem());
                        mutableData.setValue(dataSet);
                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                        ClipDomain domain = dataSnapshot.getValue(ClipDomain.class);
                        if (databaseError != null) {
                            e.onError(new Throwable(databaseError.getMessage()));
                            return;
                        }
                        domain.setKey(dataSnapshot.getKey());
                        e.onNext(domain);
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                e.onError(ex);
            }
        });
    }

    public Observable<Boolean> updateFavouritesItemState(@NonNull String itemKey, boolean isFavouritesItem) {
        return Observable.create(e -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (e.isDisposed()) {
                return;
            }
            if (user == null) {
                e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
                return;
            }

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref =
                  db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(itemKey);

            ref.runTransaction(new Transaction.Handler() {
                @Override
                public Transaction.Result doTransaction(MutableData mutableData) {
                    ClipDomain domain = mutableData.getValue(ClipDomain.class);
                    if (domain == null) {
                        return Transaction.success(mutableData);
                    }
                    domain.setFavouritesItem(isFavouritesItem);
                    mutableData.setValue(domain);
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        e.onError(new Throwable(databaseError.getMessage()));
                        return;
                    }
                    e.onNext(true);
                }
            });
        });
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
