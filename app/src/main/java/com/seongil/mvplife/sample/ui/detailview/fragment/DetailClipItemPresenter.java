package com.seongil.mvplife.sample.ui.detailview.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.seongil.mvplife.base.RxMvpPresenter;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.firebase.reporter.CrashReporter;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.repository.clip.RxFirebaseClipItem;
import com.seongil.mvplife.sample.repository.detailpost.DetailTableRef;
import com.seongil.mvplife.sample.repository.summarypost.SummaryTableRef;
import com.seongil.mvplife.sample.ui.detailview.skyrail.DetailViewSkyRail;
import com.seongil.mvplife.sample.ui.detailview.skyrail.DetailViewSkyRailEvents;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author seong-il, kim
 * @since 17. 5. 1
 */
public class DetailClipItemPresenter extends RxMvpPresenter<DetailClipItemView> {

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
    protected void registerRxSkyRail() {
        final Disposable disposable = DetailViewSkyRail.getInstance().getSkyRail().toObservable()
              .compose(RxTransformer.asyncObservableStream())
              .filter(o -> isViewAttached())
              .subscribe(this::handleRailEvent);
        addDisposable(disposable);
    }

    private void handleRailEvent(Object o) {
        if (o instanceof DetailViewSkyRailEvents.OnBackPressedEvent) {
            getView().onBackPressed();
        }
    }

    public void deleteClipItem(@NonNull Resources res, @NonNull String itemKey) {
        getView().showProgressDialog(res.getString(R.string.msg_deleting));
        Disposable disposable = Single.zip(
              SummaryTableRef.getInstance().deleteClipItem(itemKey),
              DetailTableRef.getInstance().deleteClipItem(itemKey),
              (result1, result2) -> result1 && result2)
              .compose(RxTransformer.asyncSingleStream())
              .subscribe(result -> getView().notifyRemovedItem(itemKey), t -> getView().renderError(t));
        addDisposable(disposable);
    }

    public void fetchClipDataFromRepository(@NonNull Resources res, @NonNull String itemKey) {
        Disposable disposable = DetailTableRef.getInstance().getDetailPostItemDatabaseRef(itemKey)
              .flatMap(this::addListenerForSingleValueEvent)
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(___ -> {}, t -> getView().renderError(t));
        addDisposable(disposable);
    }

    public void insertNewItemToRepository(@NonNull Resources res, @NonNull ClipDomain domain) {
        getView().showProgressDialog(res.getString(R.string.msg_inserting));
        RxFirebaseClipItem.getInstance().genNewKey()
              .flatMap(newKey -> SummaryTableRef.getInstance().insertNewItemToRepository(newKey, domain))
              .flatMap(newKey -> DetailTableRef.getInstance().insertNewItemToRepository(newKey, domain))
              .compose(RxTransformer.asyncSingleStream())
              .subscribe(itemKey -> {
                  domain.setKey(itemKey);
                  getView().notifyInsertionSuccess(domain);
              }, t -> getView().renderError(t));
    }

    public void updateClipDataToRepository(@NonNull Resources res, @NonNull ClipDomain domain) {
        getView().showProgressDialog(res.getString(R.string.msg_updating));
        Disposable disposable = Observable.zip(
              SummaryTableRef.getInstance().updateClipItemToRepository(domain),
              DetailTableRef.getInstance().updateClipItemToRepository(domain),
              (result1, result2) -> result2)
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(result -> getView().notifyUpdatedClipItem(result), t -> getView().renderError(t));
        addDisposable(disposable);
    }

    public void updateFavouritesStateToRepository(@NonNull String key, final boolean favouritesItem) {
        Disposable disposable = Observable.zip(
              SummaryTableRef.getInstance().updateFavouritesItemState(key, favouritesItem).subscribeOn(Schedulers.io()),
              DetailTableRef.getInstance().updateFavouritesItemState(key, favouritesItem).subscribeOn(Schedulers.io()),
              (result1, result2) -> result1 && result2)
              .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    result -> getView().notifyUpdatedFavoriteState(favouritesItem),
                    t -> getView().renderError(t)
              );
        addDisposable(disposable);
    }

    public void shareClipItem(@NonNull ClipDomain clipDomain) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, clipDomain.getTextData());
        sendIntent.setType("text/plain");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getAppContext().startActivity(sendIntent);
    }

    private Observable<Boolean> addListenerForSingleValueEvent(@NonNull Query ref) {
        return Observable.create(e -> ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!isViewAttached()) {
                    return;
                }
                try {
                    final ClipDomain domain = dataSnapshot.getValue(ClipDomain.class);
                    domain.setKey(dataSnapshot.getKey());
                    getView().renderClipDomain(domain);
                } catch (Exception e) {
                    e.printStackTrace();
                    CrashReporter.getInstance().report(e);
                }
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
