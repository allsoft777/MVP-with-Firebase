package com.seongil.mvplife.sample.service;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.seongil.mvplife.sample.common.utils.LogUtil;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.repository.clip.RxFirebaseClipItem;
import com.seongil.mvplife.sample.repository.common.RepoTableContracts;
import com.seongil.mvplife.sample.repository.detailpost.DetailTableRef;
import com.seongil.mvplife.sample.repository.summarypost.SummaryTableRef;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRail;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRailEvents;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author seong-il, kim
 * @since 17. 5. 2
 */
public class ClipboardService extends Service {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "ClipboardService";

    // ========================================================================
    // fields
    // ========================================================================
    private ClipboardManager mClipboardMgr;
    private static boolean sHasClipChangedListener = false;

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public void onCreate() {
        super.onCreate();
        mClipboardMgr = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        registerListener();
    }

    @Override
    public void onDestroy() {
        unregisterListener();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void registerListener() {
        if (sHasClipChangedListener) {
            return;
        }
        mClipboardMgr.addPrimaryClipChangedListener(this::checkClipboardItem);
        sHasClipChangedListener = true;
        LogUtil.d(TAG, "The service observing the clipboard is been registered.");
    }

    private void unregisterListener() {
        if (sHasClipChangedListener) {
            mClipboardMgr.removePrimaryClipChangedListener(this::checkClipboardItem);
            sHasClipChangedListener = false;
            LogUtil.d(TAG, "The service observing the clipboard is been unregistered.");
        }
    }

    private void checkClipboardItem() {
        Observable
              .fromCallable(() -> mClipboardMgr.getPrimaryClip())
              .filter(data -> data.getItemCount() > 0)
              .map(data -> data.getItemAt(0).getText())
              .filter(str -> !TextUtils.isEmpty(str.toString()) && !str.toString().trim().isEmpty())
              .subscribeOn(Schedulers.io())
              .observeOn(Schedulers.io())
              .subscribe(this::insertDataToRepository);
    }

    private void insertDataToRepository(@NonNull CharSequence data) {
        ClipDomain domain = new ClipDomain();
        domain.setTextData(data.toString());
        domain.setCreatedAt(System.currentTimeMillis());
        domain.setSource(RepoTableContracts.DATA_SOURCE);

        insertNewClipItemToRepository(domain);
    }

    public void insertNewClipItemToRepository(@NonNull ClipDomain domain) {
        RxFirebaseClipItem.getInstance().genNewKey()
              .flatMap(newItemKey -> SummaryTableRef.getInstance().insertNewItemToRepository(newItemKey, domain))
              .flatMap(newItemKey -> DetailTableRef.getInstance().insertNewItemToRepository(newItemKey, domain))
              .compose(RxTransformer.asyncSingleStream())
              .subscribe(newItemKey -> {
                  domain.setKey(newItemKey);
                  ClipListViewSkyRail.getInstance()
                        .getSkyRail().send(new ClipListViewSkyRailEvents.InsertedNewItem(domain));
              });
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
