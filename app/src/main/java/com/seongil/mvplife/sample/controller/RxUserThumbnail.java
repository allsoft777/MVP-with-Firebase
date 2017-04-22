package com.seongil.mvplife.sample.controller;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seongil.mvplife.sample.common.file.FileManager;

import io.reactivex.Observable;

/**
 * @author seong-il, kim
 * @since 17. 4. 22
 */
public class RxUserThumbnail {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String STORAGE_ROOT_URL = "gs://mvp-with-firebase.appspot.com/";
    private static final String REPO_THUMBNAIL_PATH = "user_profile_thumbnail/basic_size";

    // ========================================================================
    // fields
    // ========================================================================
    private static RxUserThumbnail sInstance;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized RxUserThumbnail getInstance() {
        if (sInstance == null) {
            sInstance = new RxUserThumbnail();
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
    public Observable<UploadTask.TaskSnapshot> uploadThumbnail(@NonNull StorageReference storageReference,
          @NonNull Uri uri) {
        return Observable.create(e -> {
            UploadTask uploadTask = storageReference.putFile(uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                if (e.isDisposed()) {
                    return;
                }
                e.onNext(taskSnapshot);
            });
            uploadTask.addOnFailureListener(t -> {
                if (e.isDisposed()) {
                    return;
                }
                e.onError(t);
            });
        });
    }

    public Observable<Boolean> deleteThumbnail(@NonNull StorageReference storageReference) {
        return Observable.create(e -> {
            storageReference.delete();
            if (e.isDisposed()) {
                return;
            }
            e.onNext(true);
        });
    }

    public Observable<StorageReference> getStorageReference(@NonNull FirebaseUser firebaseUser) {
        return Observable.create(e -> {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference profileRef = storage.getReferenceFromUrl(STORAGE_ROOT_URL);
            StorageReference basicSizeRef = profileRef.child(REPO_THUMBNAIL_PATH);
            final String fileName = firebaseUser.getUid() + "." + FileManager.THUMBNAIL_EXT;
            StorageReference storageReference = basicSizeRef.child(fileName);
            if (e.isDisposed()) {
                return;
            }
            e.onNext(storageReference);
        });
    }

    public Observable<String> fetchThumbnail(@NonNull StorageReference storageReference) {
        return Observable.create(e ->
              storageReference.getDownloadUrl().addOnSuccessListener(taskSnapshot -> {
                  if (e.isDisposed()) {
                      return;
                  }
                  e.onNext(taskSnapshot.toString());
              }).addOnFailureListener(t -> {
                  if (e.isDisposed()) {
                      return;
                  }
                  e.onError(t);
              })
        );
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
