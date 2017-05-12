package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.seongil.mvplife.base.RxMvpPresenter;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.repository.common.RepoTableContracts;
import com.seongil.mvplife.sample.repository.detailpost.DetailTableRef;
import com.seongil.mvplife.sample.repository.summarypost.SummaryTableRef;
import com.seongil.mvplife.sample.repository.user.RxFirebaseUser;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.seongil.mvplife.sample.ui.cliplist.fragment.ClipListFragment.LOAD_CLIP_ITEM_PER_CYCLE;

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
    public void updateFavouritesItemToRepository(@NonNull String itemKey, final boolean isFavouritesItem) {
        Disposable disposable = Observable.zip(
              SummaryTableRef.getInstance()
                    .updateFavouritesItemState(itemKey, isFavouritesItem).subscribeOn(Schedulers.io()),
              DetailTableRef.getInstance()
                    .updateFavouritesItemState(itemKey, isFavouritesItem).subscribeOn(Schedulers.io()),
              (result1, result2) -> result1 && result2)
              .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    result -> getView().notifyUpdatedFavouritesItem(itemKey, isFavouritesItem),
                    t -> getView().renderError(t)
              );
        addDisposable(disposable);
    }

    public void fetchClipListFromRepository(@Nullable String lastLoadedItemKey, final boolean filterFavouritesItem) {
        Disposable disposable = RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> SummaryTableRef.getInstance().getSummaryPostsDatabaseRef(user))
              .flatMap(ref -> fetchDataFromRepository(ref, lastLoadedItemKey, filterFavouritesItem))
              .compose(RxTransformer.asyncObservableStream())
              .subscribe();
        addDisposable(disposable);
    }

    public void removeClipItemsFromRepository(@NonNull List<String> itemKeys) {
        getView().showProgressDialog(MainApplication.getRes().getString(R.string.msg_deleting));
        Disposable disposable = Observable.zip(
              SummaryTableRef.getInstance().removeClipItems(itemKeys),
              DetailTableRef.getInstance().removeClipItems(itemKeys),
              (result1, result2) -> result1 && result2)
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(result -> getView().notifyRemovedItems(itemKeys), t -> getView().renderError(t));
        addDisposable(disposable);
    }

    private Observable<Boolean> fetchDataFromRepository(
          @NonNull DatabaseReference ref, @Nullable String lastLoadedItemKey, final boolean filterFavouritesItem) {
        if (TextUtils.isEmpty(lastLoadedItemKey)) {
            return fetchDataForFirstTimeFromRepository(ref, filterFavouritesItem);
        } else {
            return fetchNextDataFromRepository(ref, lastLoadedItemKey, filterFavouritesItem);
        }
    }

    private Observable<Boolean> fetchDataForFirstTimeFromRepository(
          @NonNull DatabaseReference ref, final boolean filterFavouritesItem) {
        return Observable.create(e -> {
                  Query query;
                  if (filterFavouritesItem) {
                      query = filterFavouritesItem(ref);
                  } else {
                      query = ref.orderByKey();
                  }
                  query = query.limitToLast(LOAD_CLIP_ITEM_PER_CYCLE);
                  query.addListenerForSingleValueEvent(new ValueEventListener() {
                      @Override
                      public void onDataChange(DataSnapshot dataSnapshot) {
                          if (dataSnapshot.getValue() == null) {
                              getView().renderEmptyView();
                              return;
                          }
                          handleReceivedDataSnapshot(dataSnapshot);
                      }

                      @Override
                      public void onCancelled(DatabaseError databaseError) {
                      }
                  });
              }
        );
    }

    private Observable<Boolean> fetchNextDataFromRepository(
          @NonNull DatabaseReference ref, @NonNull String lastLoadedItemKey, boolean filterFavouritesItem) {
        return Observable.create(e -> {
            Query query;
            if (filterFavouritesItem) {
                query = filterFavouritesItem(ref);
            } else {
                query = ref.orderByKey();
            }
            query = query.endAt(lastLoadedItemKey).limitToLast(LOAD_CLIP_ITEM_PER_CYCLE + 1);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        getView().renderClipDataList(new ArrayList<>(), false);
                        return;
                    }
                    handleReceivedDataSnapshot(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        });
    }

    private void handleReceivedDataSnapshot(DataSnapshot dataSnapshot) {
        Observable.create(new ConvertDataSnapshotToDomainListOnSubscribe(dataSnapshot))
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(list -> getView().renderClipDataList(list, list.size() == LOAD_CLIP_ITEM_PER_CYCLE));
    }

    private Query filterFavouritesItem(Query query) {
        return query.orderByChild(RepoTableContracts.COL_FAVORITE_ITEM).equalTo(true);
    }

    private List<ClipDomainViewModel> convertDataSnapshotToDomainViewModel(DataSnapshot dataSnapshot) {
        ClipDomain element;
        List<ClipDomainViewModel> list = new ArrayList<>();
        for (DataSnapshot item : dataSnapshot.getChildren()) {
            element = item.getValue(ClipDomain.class);
            element.setKey(item.getKey());
            list.add(0, new ClipDomainViewModel(element));
        }
        return list;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    private class ConvertDataSnapshotToDomainListOnSubscribe
          implements ObservableOnSubscribe<List<ClipDomainViewModel>> {

        private DataSnapshot mDataSnapshot;

        public ConvertDataSnapshotToDomainListOnSubscribe(DataSnapshot dataSnapshot) {
            mDataSnapshot = dataSnapshot;
        }

        @Override
        public void subscribe(ObservableEmitter<List<ClipDomainViewModel>> e) throws Exception {
            try {
                List<ClipDomainViewModel> list = convertDataSnapshotToDomainViewModel(mDataSnapshot);
                e.onNext(list);
            } catch (Exception ex) {
                e.onError(ex);
            }
        }
    }
}