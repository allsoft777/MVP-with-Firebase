package com.seongil.mvplife.sample.common.ui.dialog.picturechooser;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.common.model.SingleTextViewData;
import com.seongil.mvplife.sample.common.reporter.CrashReporter;
import com.seongil.mvplife.sample.common.ui.dialog.picturechooser.adapter.PictureChooserMenuAdapter;
import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public class PictureChooserDialog extends DialogFragment {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private List<SingleTextViewData> mMenuList;
    private PictureChooserDialogListener mCallerCallback;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized PictureChooserDialog newInstance() {
        return new PictureChooserDialog();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
          @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_picture_chooser, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallerCallback = (PictureChooserDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            CrashReporter.getInstance().report(e);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupMenu();
        renderMenuListView(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        if (window != null) {
            ViewGroup.LayoutParams params = window.getAttributes();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes((android.view.WindowManager.LayoutParams) params);
        }
        super.onResume();
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void setupMenu() {
        mMenuList = new ArrayList<>();
        mMenuList.add(new SingleTextViewData(getString(R.string.picture_chooser_menu_default)));
        mMenuList.add(new SingleTextViewData(getString(R.string.picture_chooser_menu_gallery_camera)));
    }

    @SuppressWarnings("unchecked")
    private void renderMenuListView(View view) {
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.recycler_view);
        PictureChooserMenuAdapter adapter =
              new PictureChooserMenuAdapter(getActivity().getLayoutInflater(), this::handleClickItem);
        adapter.addLastCollection(mMenuList);
        final LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(llm);
        listView.setAdapter(adapter);
        listView.setHasFixedSize(true);
        listView.setFocusable(false);
    }

    private void handleClickItem(
          @NonNull RecyclerView.ViewHolder vh,
          @NonNull RecyclerViewItem info, final int position) {

        switch (position) {
            case 0:
                mCallerCallback.onClearImage();
                break;
            case 1:
                mCallerCallback.onPickImageFromChooserPopup();
                break;
            default:
                throw new IllegalArgumentException("Invalid position");
        }
        dismiss();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public interface PictureChooserDialogListener {

        void onClearImage();

        void onPickImageFromChooserPopup();
    }
}
