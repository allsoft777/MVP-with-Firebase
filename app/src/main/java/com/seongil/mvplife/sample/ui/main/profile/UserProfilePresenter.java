package com.seongil.mvplife.sample.ui.main.profile;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.storage.StorageException;
import com.seongil.mvplife.base.RxMvpPresenter;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.firebase.info.RxFirebaseUser;
import com.seongil.mvplife.sample.common.model.SkyRailEvents;
import com.seongil.mvplife.sample.common.utils.RxUtil;
import com.seongil.mvplife.sample.controller.RxUserThumbnail;

import io.reactivex.disposables.Disposable;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public class UserProfilePresenter extends RxMvpPresenter<UserProfileView> {

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
    public void registerSkyRail() {
        Disposable disposable = MainApplication.getSkyRail()
              .toObservable()
              .compose(RxUtil.asyncObservableStream())
              .filter(o -> isViewAttached())
              .subscribe(this::handleRailEvent);
        addDisposable(disposable);
    }

    private void handleRailEvent(Object o) {
        if (o instanceof SkyRailEvents.OnActivityResultEvent) {
            SkyRailEvents.OnActivityResultEvent event = (SkyRailEvents.OnActivityResultEvent) o;
            getView().onActivityResult(event.getRequestCode(), event.getResultCode(), event.getIntent());
        }
    }

    public void deleteUserThumbnailFromRepository() {
        RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> RxUserThumbnail.getInstance().getStorageReference(user))
              .flatMap(storageReference -> RxUserThumbnail.getInstance().deleteThumbnail(storageReference))
              .compose(RxUtil.asyncObservableStream())
              .subscribe(___ -> getView().renderUserThumbnail(""), t -> getView().renderError(t));
    }

    public void uploadUserThumbnailToRepository(@NonNull Uri uri) {
        RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> RxUserThumbnail.getInstance().getStorageReference(user))
              .flatMap(storageReference -> RxUserThumbnail.getInstance().uploadThumbnail(storageReference, uri))
              .compose(RxUtil.asyncObservableStream())
              .subscribe(
                    taskSnapshot -> getView().renderUserThumbnail(taskSnapshot.getDownloadUrl().toString()),
                    throwable -> getView().renderError(throwable));
    }

    public void fetchUserThumbnailFromRepository() {
        RxFirebaseUser.getInstance().getCurrentUser()
              .flatMap(user -> RxUserThumbnail.getInstance().getStorageReference(user))
              .flatMap(storageReference -> RxUserThumbnail.getInstance().fetchThumbnail(storageReference))
              .compose(RxUtil.asyncObservableStream())
              .subscribe(url -> getView().renderUserThumbnail(url), this::handleErrorForFailedFetchingThumbnail);
    }

    private void handleErrorForFailedFetchingThumbnail(Throwable t) {
        if (t instanceof StorageException) {
            fetchFirebaseAccountThumbnail();
            return;
        }
        getView().renderError(t);
    }

    private void fetchFirebaseAccountThumbnail() {
        RxFirebaseUser.getInstance().getCurrentUser()
              .compose(RxUtil.asyncObservableStream())
              .subscribe(user -> getView().renderUserThumbnail(user.getPhotoUrl().toString()),
                    t -> getView().renderError(t));
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}