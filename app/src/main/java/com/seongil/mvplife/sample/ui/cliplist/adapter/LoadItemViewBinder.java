/*
 * Copyright (C) 2017 Seongil Kim <kims172@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.seongil.mvplife.sample.ui.cliplist.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.ui.cliplist.adapter.footer.ClipListFooterItem;
import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;
import com.seongil.recyclerviewlife.model.common.ViewStatus;
import com.seongil.recyclerviewlife.single.viewbinder.AbstractFooterViewBinder;

/**
 * @author seong-il, kim
 * @since 17. 4. 8
 */
public class LoadItemViewBinder extends AbstractFooterViewBinder {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================
    public LoadItemViewBinder(LayoutInflater inflater) {
        super(inflater, null);
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    public boolean isForViewType(@NonNull RecyclerViewItem item) {
        return item instanceof ClipListFooterItem;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new FooterViewHolder(mLayoutInflater.inflate(R.layout.list_item_footer_load_more, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItem item, @NonNull RecyclerView.ViewHolder holder) {
        final ClipListFooterItem data = (ClipListFooterItem) item;
        final FooterViewHolder viewHolder = (FooterViewHolder) holder;
        final Resources res = holder.itemView.getResources();
        final ViewStatus code = data.getStatusCode();
        if (code == ViewStatus.VISIBLE_LOADING_VIEW) {
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setVisibility(View.VISIBLE);
            viewHolder.message.setText(res.getString(R.string.msg_loading));
            viewHolder.itemView.setOnClickListener(null);
        } else if (code == ViewStatus.VISIBLE_LABEL_VIEW) {
            viewHolder.itemView.setVisibility(View.VISIBLE);
            viewHolder.progressBar.setVisibility(View.GONE);
            viewHolder.message.setText(res.getString(R.string.msg_no_more_item));
        } else {
            throw new AssertionError("You are not allowed the other state.");
        }
    }

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public final View container;
        public final ProgressBar progressBar;
        public final TextView message;

        public FooterViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.container);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            message = (TextView) view.findViewById(R.id.message);
        }
    }
}
