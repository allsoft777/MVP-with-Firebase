package com.seongil.mvplife.sample.ui.cliplist.adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.seongil.mvplife.sample.ui.cliplist.adapter.footer.ClipListFooterItem;
import com.seongil.mvplife.sample.viewmodel.ClipDomainViewModel;
import com.seongil.recyclerviewlife.model.RecyclerViewFooterItem;
import com.seongil.recyclerviewlife.model.common.ViewStatus;
import com.seongil.recyclerviewlife.single.RecyclerListViewAdapter;
import com.seongil.recyclerviewlife.single.viewbinder.AbstractFooterViewBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipListAdapter extends RecyclerListViewAdapter<ClipDomainViewModel> implements ClipListAdapterListener {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int VIEW_TYPE_DEFAULT = 1;

    // ========================================================================
    // fields
    // ========================================================================
    private boolean mIsSelectionMode;

    // ========================================================================
    // constructors
    // ========================================================================
    public ClipListAdapter(@NonNull LayoutInflater layoutInflater) {
        super(layoutInflater);
        addViewBinder(new ClipItemBasicViewBinder(VIEW_TYPE_DEFAULT, layoutInflater, this));
    }

    // ========================================================================
    // getter & setter
    // ========================================================================
    public void setSelectionMode(boolean selectionMode) {
        mIsSelectionMode = selectionMode;
    }

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Override
    protected AbstractFooterViewBinder getNewInstanceOfFooterViewBinder() {
        return new LoadItemViewBinder(mLayoutInflater);
    }

    @Override
    protected RecyclerViewFooterItem getNewInstanceOfFooterItem() {
        return new ClipListFooterItem(ViewStatus.VISIBLE_LOADING_VIEW);
    }

    @Override
    public boolean isSelectionMode() {
        return mIsSelectionMode;
    }

    // ========================================================================
    // methods
    // ========================================================================
    public int getSelectedItemCount() {
        int cnt = 0;
        for (ClipDomainViewModel model : getDataSet(false)) {
            if (model.isSelected()) {
                cnt++;
            }
        }
        return cnt;
    }

    public void clearSelectedItems() {
        for (ClipDomainViewModel model : getDataSet(false)) {
            model.setSelected(false);
        }
    }

    @NonNull
    public List<String> retrieveSelectedItemKeys() {
        List<String> result = new ArrayList<>();
        for (ClipDomainViewModel model : getDataSet(false)) {
            if (model.isSelected()) {
                result.add(model.getDomain().getKey());
            }
        }
        return result;
    }

    public void removeItem(@NonNull String itemKey) {
        for (ClipDomainViewModel model : getDataSet(false)) {
            if (model.getDomain().getKey().equals(itemKey)) {
                getDataSet().remove(model);
                return;
            }
        }
    }

    @NonNull
    public List<ClipDomainViewModel> retrieveSelectedDomainItems() {
        List<ClipDomainViewModel> result = new ArrayList<>();
        for (ClipDomainViewModel viewModel : getDataSet(false)) {
            if (viewModel.isSelected()) {
                result.add(viewModel);
            }
        }
        return result;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}