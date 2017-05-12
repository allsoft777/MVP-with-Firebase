package com.seongil.mvplife.sample.ui.cliplist.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.datetime.RxFormat;
import com.seongil.mvplife.sample.common.utils.RxTransformer;
import com.seongil.mvplife.sample.common.utils.StringUtil;
import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRail;
import com.seongil.mvplife.sample.ui.cliplist.skyrail.ClipListViewSkyRailEvents;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;
import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;
import com.seongil.recyclerviewlife.single.viewbinder.AbstractViewBinder;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipItemBasicViewBinder extends AbstractViewBinder {

    // ========================================================================
    // constants
    // ========================================================================
    private static SimpleDateFormat dateTimeFormat =
          new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault());

    // ========================================================================
    // fields
    // ========================================================================
    private ClipListAdapterListener mAdapterListener;

    // ========================================================================
    // constructors
    // ========================================================================
    public ClipItemBasicViewBinder(final int viewType, @NonNull LayoutInflater inflater, @NonNull ClipListAdapterListener adapterListener) {
        super(viewType, inflater);
        mAdapterListener = adapterListener;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public boolean isForViewType(@NonNull RecyclerViewItem item) {
        return item instanceof ClipDomainViewModel;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ClipItemBasicTypeViewHolder(
              mLayoutInflater.inflate(R.layout.list_item_clip_basic_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItem item, @NonNull RecyclerView.ViewHolder holder) {
        ClipItemBasicTypeViewHolder viewHolder = (ClipItemBasicTypeViewHolder) holder;
        ClipDomainViewModel domain = (ClipDomainViewModel) item;
        ClipDomain data = domain.getDomain();

        RxFormat.convertDateTimeString(dateTimeFormat, data.getCreatedAt())
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(src -> {
                  final String[] dateTime = src.split(",");
                  viewHolder.date.setText(dateTime[0]);
                  viewHolder.time.setText(dateTime[1]);
              });

        viewHolder.selectionBox.setVisibility(mAdapterListener.isSelectionMode() ? View.VISIBLE : View.GONE);
        viewHolder.favouritesItem.setVisibility(mAdapterListener.isSelectionMode() ? View.GONE : View.VISIBLE);
        viewHolder.selectionBox.setChecked(domain.isSelected());

        Observable.fromCallable(() -> normalizeText(data.getTextData()))
              .compose(RxTransformer.asyncObservableStream())
              .subscribe(viewHolder.text::setText);

        RxView.clicks(viewHolder.itemView)
              .compose(this::applyThrottleForButton)
              .subscribe(v -> ClipListViewSkyRail.getInstance().getSkyRail()
                    .send(new ClipListViewSkyRailEvents.ClickItemEvent(data.getKey())));

        RxView.longClicks(viewHolder.itemView)
              .compose(this::applyThrottleForButton)
              .subscribe(v -> ClipListViewSkyRail.getInstance().getSkyRail()
                    .send(new ClipListViewSkyRailEvents.LongClickItemEvent(data.getKey())));

        RxView.clicks(viewHolder.copy)
              .compose(this::applyThrottleForButton)
              .subscribe(v -> ClipListViewSkyRail.getInstance().getSkyRail().send(
                    new ClipListViewSkyRailEvents.CopyItemToClipboard(data.getKey())));

        RxView.clicks(viewHolder.favouritesItem)
              .compose(this::applyThrottleForButton)
              .subscribe(v -> ClipListViewSkyRail.getInstance().getSkyRail()
                    .send(new ClipListViewSkyRailEvents.FavoriteItemEvent(data.getKey(), !data.isFavouritesItem())));

        viewHolder.favouritesItem.setBackgroundResource(
              data.isFavouritesItem() ? R.drawable.selector_ic_favorite_true : R.drawable.selector_ic_favorite_false);
    }

    // ========================================================================
    // methods
    // ========================================================================
    private Observable<Object> applyThrottleForButton(Observable<Object> obj) {
        return obj.throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread());
    }

    private String normalizeText(String textData) throws Exception {
        if (TextUtils.isEmpty(textData)) {
            return "null";
        }

        String removeLineFeed = textData.replace("\n", " ").replace("\r", " ");
        return StringUtil.subString(removeLineFeed, 0, 60);
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    private static class ClipItemBasicTypeViewHolder extends RecyclerView.ViewHolder {

        private final CheckBox selectionBox;
        private final ImageView favouritesItem;
        private final TextView date;
        private final TextView time;
        private final TextView text;
        private final ImageView copy;

        private ClipItemBasicTypeViewHolder(View view) {
            super(view);

            selectionBox = (CheckBox) view.findViewById(R.id.selection_box);
            favouritesItem = (ImageView) view.findViewById(R.id.btn_favourites);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
            text = (TextView) view.findViewById(R.id.text);
            copy = (ImageView) view.findViewById(R.id.copy_item);
        }
    }
}