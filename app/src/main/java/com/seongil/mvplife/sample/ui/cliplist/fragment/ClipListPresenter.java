package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.support.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.seongil.mvplife.base.RxMvpPresenter;
import com.seongil.mvplife.sample.common.firebase.reporter.CrashReporter;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.repository.detailpost.DetailTableRef;
import com.seongil.mvplife.sample.repository.summarypost.SummaryTableRef;
import com.seongil.mvplife.sample.repository.user.RxFirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipListPresenter extends RxMvpPresenter<ClipListView> {

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
    public void insertNewClipItemToRepository(ClipDomain domain) {
        RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> SummaryTableRef.getInstance().insertNewItemToRepository(domain))
              .flatMap(summaryRefKey -> DetailTableRef.getInstance().insertNewItemToRepository(summaryRefKey, domain))
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(result -> getView().notifyInsertionSuccess(), t -> getView().renderError(t));
    }

    public void removeClipItemFromRepository(String itemKey) {
        Disposable disposable = Observable.zip(
              SummaryTableRef.getInstance().deleteClipItem(itemKey).subscribeOn(Schedulers.io()),
              DetailTableRef.getInstance().deleteClipItem(itemKey).subscribeOn(Schedulers.io()),
              (result1, result2) -> result1 && result2)
              .observeOn(AndroidSchedulers.mainThread()).subscribe();
        addDisposable(disposable);
    }

    public void updateFavouritesItemToRepository(@NonNull String itemKey, boolean isFavouritesItem) {
        Disposable disposable = Observable.zip(
              SummaryTableRef.getInstance()
                    .updateFavouritesItemState(itemKey, isFavouritesItem).subscribeOn(Schedulers.io()),
              DetailTableRef.getInstance()
                    .updateFavouritesItemState(itemKey, isFavouritesItem).subscribeOn(Schedulers.io()),
              (result1, result2) -> result1 && result2)
              .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    result -> getView().notifyUpdatedFavouritesItem(isFavouritesItem),
                    t -> getView().renderError(t)
              );
        addDisposable(disposable);
    }

    public void monitorChildEvents() {
        Disposable disposable = RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> SummaryTableRef.getInstance().getSummaryPostsDatabaseRef(user))
              .flatMap(this::monitorChildEvents)
              .compose(RxTransformer.asyncObservableStream())
              .subscribe();
        addDisposable(disposable);
    }

    public void monitorSingleValueEvent() {
        Disposable disposable = RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> SummaryTableRef.getInstance().getSummaryPostsDatabaseRef(user))
              .flatMap(this::addListenerForSingleValueEvent)
              .compose(RxTransformer.asyncObservableStream())
              .subscribe();
        addDisposable(disposable);
    }

    private Observable<Boolean> addListenerForSingleValueEvent(DatabaseReference ref) {
        return Observable.create(e -> ref.orderByKey()
              .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      if (dataSnapshot.getValue() == null) {
                          getView().renderEmptyView();
                          return;
                      }

                      ClipDomain element;
                      List<ClipDomain> list = new ArrayList<>();
                      for (DataSnapshot item : dataSnapshot.getChildren()) {
                          element = item.getValue(ClipDomain.class);
                          element.setKey(item.getKey());
                          list.add(element);
                      }
                      getView().renderClipDataList(list);
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                  }
              })
        );
    }

    private Observable<Boolean> monitorChildEvents(DatabaseReference ref) {
        return Observable.create(e -> ref.orderByKey()
              .addChildEventListener(new ChildEventListener() {
                  @Override
                  public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                      handleChildAddedItem(dataSnapshot, s);
                  }

                  @Override
                  public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                      handleChildChangedItem(dataSnapshot, s);
                  }

                  @Override
                  public void onChildRemoved(DataSnapshot dataSnapshot) {
                      handleChildRemovedItem(dataSnapshot);
                  }

                  @Override
                  public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {
                  }
              })
        );
    }

    private void handleChildAddedItem(DataSnapshot dataSnapshot, String s) {
        if (!isViewAttached()) {
            return;
        }
        try {
            final ClipDomain domain = dataSnapshot.getValue(ClipDomain.class);
            domain.setKey(dataSnapshot.getKey());
            getView().addNewClipData(domain);
        } catch (Exception e) {
            e.printStackTrace();
            CrashReporter.getInstance().report(e);
        }
    }

    private void handleChildChangedItem(DataSnapshot dataSnapshot, String s) {
        if (!isViewAttached()) {
            return;
        }

        try {
            final ClipDomain domain = dataSnapshot.getValue(ClipDomain.class);
            domain.setKey(dataSnapshot.getKey());
            getView().updateClipData(domain);
        } catch (Exception e) {
            e.printStackTrace();
            CrashReporter.getInstance().report(e);
        }
    }

    private void handleChildRemovedItem(DataSnapshot dataSnapshot) {
        if (!isViewAttached()) {
            return;
        }
        final String key = dataSnapshot.getKey();
        getView().removeClipData(key);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}