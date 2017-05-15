package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.seongil.mvplife.sample.common.exception.NetworkConException;
import com.seongil.mvplife.sample.common.firebase.analytics.AnalyticsReporter;
import com.seongil.mvplife.sample.common.share.ShareClipItemManager;
import com.seongil.mvplife.sample.common.utils.NetworkUtils;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.common.utils.ToastUtil;
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
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

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
        Disposable disposable = Single.zip(
              SummaryTableRef.getInstance().updateFavouritesItemState(itemKey, isFavouritesItem),
              DetailTableRef.getInstance().updateFavouritesItemState(itemKey, isFavouritesItem),
              (result1, result2) -> result1 && result2)
              .compose(RxTransformer.asyncSingleStream())
              .subscribe(
                    result -> getView().notifyUpdatedFavouritesItem(itemKey, isFavouritesItem),
                    t -> getView().renderError(t)
              );
        addDisposable(disposable);
    }

    public void fetchClipListFromRepository(@Nullable String lastLoadedItemKey, final boolean filterFavouritesItem) {
        if (!NetworkUtils.isInternetOn(MainApplication.getAppContext())) {
            getView().renderError(new NetworkConException("Network is not connected."));
            return;
        }

        Disposable disposable = RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> SummaryTableRef.getInstance().getSummaryPostsDatabaseRef(user))
              .flatMap(ref -> fetchDataFromRepository(ref, lastLoadedItemKey, filterFavouritesItem))
              .compose(RxTransformer.asyncObservableStream())
              .subscribe();
        addDisposable(disposable);
    }

    public void removeClipItemsFromRepository(@NonNull List<String> itemKeys) {
        getView().showProgressDialog(MainApplication.getRes().getString(R.string.msg_deleting));
        Disposable disposable = Single.zip(
              SummaryTableRef.getInstance().removeClipItems(itemKeys),
              DetailTableRef.getInstance().removeClipItems(itemKeys),
              (result1, result2) -> result1 && result2)
              .compose(RxTransformer.asyncSingleStream())
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
                          ToastUtil.showToast("error");
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

    public void shareItems(@NonNull List<ClipDomainViewModel> list) {
        final ShareClipItemManager mgr = new ShareClipItemManager();
        final String str = mgr.buildShareText(list);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, str);
        sendIntent.setType("text/plain");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getAppContext().startActivity(sendIntent);
        reportAnalyticsForSharingItems(list.size());
    }

    private void reportAnalyticsForSharingItems(int size) {
        Bundle analyticsBundle = new Bundle();
        analyticsBundle.putInt(AnalyticsReporter.Param.ITEM_CNT, size);
        analyticsBundle.putString(AnalyticsReporter.Param.TRIGGER_VIEW, AnalyticsReporter.Param.LIST_VIEW);
        AnalyticsReporter.shareClipItem(analyticsBundle);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    private class ConvertDataSnapshotToDomainListOnSubscribe
          implements ObservableOnSubscribe<List<ClipDomainViewModel>> {

        private DataSnapshot mDataSnapshot;

        private ConvertDataSnapshotToDomainListOnSubscribe(DataSnapshot dataSnapshot) {
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