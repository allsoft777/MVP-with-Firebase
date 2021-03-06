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
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.exception.NetworkConException;
import com.seongil.mvplife.sample.common.firebase.reporter.CrashReporter;
import com.seongil.mvplife.sample.common.utils.NetworkUtils;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.repository.common.RepoTableContracts;
import com.seongil.mvplife.sample.repository.exception.InvalidFirebaseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;

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
            if (!NetworkUtils.isInternetOn(MainApplication.getAppContext())) {
                e.onError(new NetworkConException(
                      MainApplication.getRes().getString(R.string.err_network_connection_failed)));
                return;
            }
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

    public Single<String> insertNewItemToRepository(String newItemKey, @NonNull ClipDomain domain) {
        return Single.create(e -> {
            if (!NetworkUtils.isInternetOn(MainApplication.getAppContext())) {
                e.onError(new NetworkConException(
                      MainApplication.getRes().getString(R.string.err_network_connection_failed)));
                return;
            }
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
                return;
            }

            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref =
                  db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(newItemKey);

            try {
                Map<String, Object> dataSet = new HashMap<>(4);
                dataSet.put(RepoTableContracts.COL_CREATED_AT, domain.getCreatedAt());
                dataSet.put(RepoTableContracts.COL_DATA, domain.getTextData());
                dataSet.put(RepoTableContracts.COL_SOURCE, domain.getSource());
                dataSet.put(RepoTableContracts.COL_FAVORITE_ITEM, domain.isFavouritesItem());

                ref.setValue(dataSet).addOnCompleteListener(task -> e.onSuccess(newItemKey));
            } catch (Exception ex) {
                e.onError(ex);
                CrashReporter.getInstance().report(ex);
            }
        });
    }

    public Single<Boolean> deleteClipItem(@NonNull String itemKey) {
        return Single.create(e -> {
            if (!NetworkUtils.isInternetOn(MainApplication.getAppContext())) {
                e.onError(new NetworkConException(
                      MainApplication.getRes().getString(R.string.err_network_connection_failed)));
                return;
            }
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
                return;
            }

            final FirebaseDatabase db = FirebaseDatabase.getInstance();
            final DatabaseReference ref =
                  db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid()).child(itemKey);
            final Task<Void> task = ref.removeValue();
            task.addOnCompleteListener(taskArgs -> e.onSuccess(true));
            task.addOnFailureListener(taskArgs -> e.onError(taskArgs.getCause()));
        });
    }

    public Single<ClipDomain> updateClipItemToRepository(@NonNull ClipDomain domain) {
        return Single.create(e -> {
            if (!NetworkUtils.isInternetOn(MainApplication.getAppContext())) {
                e.onError(new NetworkConException(
                      MainApplication.getRes().getString(R.string.err_network_connection_failed)));
                return;
            }
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
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
            final Task<Void> task = ref.setValue(dataSet);
            task.addOnCompleteListener(taskArgs -> e.onSuccess(domain));
            task.addOnFailureListener(taskArgs -> e.onError(taskArgs.getCause()));
        });
    }

    public Single<Boolean> updateFavouritesItemState(@NonNull String itemKey, boolean isFavouritesItem) {
        return Single.create(e -> {
            if (!NetworkUtils.isInternetOn(MainApplication.getAppContext())) {
                e.onError(new NetworkConException(
                      MainApplication.getRes().getString(R.string.err_network_connection_failed)));
                return;
            }
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
                    e.onSuccess(true);
                }
            });
        });
    }

    public Single<Boolean> removeClipItems(@NonNull List<String> itemKeys) {
        return Single.create(e -> {
            if (!NetworkUtils.isInternetOn(MainApplication.getAppContext())) {
                e.onError(new NetworkConException(
                      MainApplication.getRes().getString(R.string.err_network_connection_failed)));
                return;
            }
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user == null) {
                e.onError(new InvalidFirebaseUser("Current FireBase User is invalid."));
                return;
            }

            Map<String, Object> childValues = new HashMap<>();
            for (String itemKey : itemKeys) {
                childValues.put(itemKey, null);
            }
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref =
                  db.getReference(RepoTableContracts.TABLE_DETAIL_POST).child(user.getUid());
            ref.updateChildren(childValues, (databaseError, databaseReference) -> {
                if (databaseError != null) {
                    e.onError(new Throwable(databaseError.getMessage()));
                } else {
                    e.onSuccess(true);
                }
            });
        });
    }
    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
