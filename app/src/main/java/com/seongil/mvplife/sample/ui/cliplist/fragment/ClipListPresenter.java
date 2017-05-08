package com.seongil.mvplife.sample.ui.cliplist.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.seongil.mvplife.base.RxMvpPresenter;
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

    public void fetchClipListFromRepository(@Nullable String lastLoadedItemKey) {
        Disposable disposable = RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> SummaryTableRef.getInstance().getSummaryPostsDatabaseRef(user))
              .flatMap(ref -> fetchDataFromRepository(ref, lastLoadedItemKey))
              .compose(RxTransformer.asyncObservableStream())
              .subscribe();
        addDisposable(disposable);
    }

    private Observable<Boolean> fetchDataFromRepository(DatabaseReference ref, @Nullable String lastLoadedItemKey) {
        if (TextUtils.isEmpty(lastLoadedItemKey)) {
            return fetchDataForFirstTimeFromRepository(ref);
        } else {
            return fetchNextDataFromRepository(ref, lastLoadedItemKey);
        }
    }

    private Observable<Boolean> fetchDataForFirstTimeFromRepository(@NonNull DatabaseReference ref) {
        return Observable.create(e -> ref
              .orderByKey()
              .limitToLast(LOAD_CLIP_ITEM_PER_CYCLE)
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
                          list.add(0, element);
                      }
                      getView().renderClipDataList(list, list.size() == LOAD_CLIP_ITEM_PER_CYCLE);
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              })
        );
    }

    private Observable<Boolean> fetchNextDataFromRepository(
          @NonNull DatabaseReference ref,
          @NonNull String lastLoadedItemKey) {

        return Observable.create(e -> ref
              .orderByKey()
              .endAt(lastLoadedItemKey)
              .limitToLast(LOAD_CLIP_ITEM_PER_CYCLE + 1)
              .addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
                      if (dataSnapshot.getValue() == null) {
                          getView().renderClipDataList(new ArrayList<>(), false);
                          return;
                      }

                      ClipDomain element;
                      List<ClipDomain> list = new ArrayList<>();
                      for (DataSnapshot item : dataSnapshot.getChildren()) {
                          if (item.getKey().equals(lastLoadedItemKey)) {
                              continue;
                          }

                          element = item.getValue(ClipDomain.class);
                          element.setKey(item.getKey());
                          list.add(0, element);
                      }
                      getView().renderClipDataList(list, list.size() == LOAD_CLIP_ITEM_PER_CYCLE);
                  }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {

                  }
              }));
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}