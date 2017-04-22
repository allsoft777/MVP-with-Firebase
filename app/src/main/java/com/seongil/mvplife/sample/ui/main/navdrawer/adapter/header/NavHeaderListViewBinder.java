package com.seongil.mvplife.sample.ui.main.navdrawer.adapter.header;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.model.SingleTextAndDrawerViewData;
import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;
import com.seongil.recyclerviewlife.single.viewbinder.AbstractViewBinder;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class NavHeaderListViewBinder extends AbstractViewBinder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================
    public NavHeaderListViewBinder(int viewType, @NonNull LayoutInflater inflater, @Nullable
          RecyclerViewItemClickListener itemClickListener) {
        super(viewType, inflater, itemClickListener);
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public boolean isForViewType(@NonNull RecyclerViewItem item) {
        return item instanceof SingleTextAndDrawerViewData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SingleTextAndDrawerViewHolder(
              mLayoutInflater.inflate(R.layout.list_item_navigation_header, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItem item, @NonNull RecyclerView.ViewHolder holder) {
        SingleTextAndDrawerViewData data = (SingleTextAndDrawerViewData) item;
        SingleTextAndDrawerViewHolder viewHolder = (SingleTextAndDrawerViewHolder) holder;
        viewHolder.text.setText(data.getText());
        Glide
              .with(viewHolder.itemView.getContext())
              .fromResource()
              .load(data.getDrawerResId())
              .into(viewHolder.icon);

        viewHolder.itemView.setOnClickListener(v -> {
            if (mItemViewClickListener != null) {
                mItemViewClickListener.onClickedRecyclerViewItem(
                      holder, item, viewHolder.getLayoutPosition());
            }
        });
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public static class SingleTextAndDrawerViewHolder extends RecyclerView.ViewHolder {

        public final ImageView icon;
        public final TextView text;

        public SingleTextAndDrawerViewHolder(View view) {
            super(view);

            text = (TextView) view.findViewById(R.id.message);
            icon = (ImageView) view.findViewById(R.id.icon);
        }
    }
}
