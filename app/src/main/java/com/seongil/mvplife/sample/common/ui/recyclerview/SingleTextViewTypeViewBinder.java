package com.seongil.mvplife.sample.common.ui.recyclerview;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.model.SingleTextViewData;
import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;
import com.seongil.recyclerviewlife.single.viewbinder.AbstractViewBinder;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public class SingleTextViewTypeViewBinder extends AbstractViewBinder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================
    public SingleTextViewTypeViewBinder(
          int viewType,
          @NonNull LayoutInflater inflater,
          @Nullable RecyclerViewItemClickListener itemClickListener) {
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
        return item instanceof SingleTextViewData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new SingleTextViewHolder(
              mLayoutInflater.inflate(R.layout.list_item_common_singletext_center, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItem item, @NonNull RecyclerView.ViewHolder holder) {
        final SingleTextViewData data = (SingleTextViewData) item;
        final SingleTextViewHolder viewHolder = (SingleTextViewHolder) holder;
        viewHolder.text1.setText(data.getText());

        if (mItemViewClickListener != null) {
            viewHolder.itemView.setOnClickListener(
                  v -> mItemViewClickListener.onClickedRecyclerViewItem(holder, data,
                        holder.getLayoutPosition()));
        }
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public static class SingleTextViewHolder extends RecyclerView.ViewHolder {

        public final TextView text1;

        public SingleTextViewHolder(View view) {
            super(view);

            text1 = (TextView) view.findViewById(R.id.message);
        }
    }
}
