package com.seongil.mvplife.sample.ui.main.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding2.view.RxView;
import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.extrakey.ExtraKeys;
import com.seongil.mvplife.sample.common.model.SkyRailEvents;
import com.seongil.mvplife.sample.common.ui.dialog.picturechooser.PictureChooserDialog;
import com.seongil.mvplife.sample.common.ui.imagecropper.ImageCropperActivity;
import com.seongil.mvplife.sample.common.utils.ToastUtil;
import com.seongil.mvplife.sample.ui.base.BaseFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author seong-il, kim
 * @since 17. 3. 31
 */
public class UserProfileFragment
      extends BaseFragment<UserProfileView, UserProfilePresenter>
      implements UserProfileView, PictureChooserDialog.PictureChooserDialogListener {

    // ========================================================================
    // constants
    // ========================================================================
    private static final int REQ_CODE_CROP_IMAGE = 1;

    // ========================================================================
    // fields
    // ========================================================================
    private ProgressBar mProgressBar;
    private ImageView mProfileThumbnail;
    private TextView mName;
    private TextView mEmailAddress;
    private Uri mCropImageUri;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================
    @NonNull
    @Override
    public UserProfilePresenter createPresenter() {
        return new UserProfilePresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_user_profile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().registerSkyRail();

        mProfileThumbnail = (ImageView) view.findViewById(R.id.user_profile_thumbnail);
        mName = (TextView) view.findViewById(R.id.name);
        mEmailAddress = (TextView) view.findViewById(R.id.email_address);
        mProgressBar = (ProgressBar) view.findViewById(R.id.loading_bar);

        setThumbnailMenuListener();
        getPresenter().fetchUserThumbnailFromRepository();
    }

    @Override
    public void onRequestPermissionsResult(
          int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            handlePickImagePermissionRequestCode(grantResults);
        }
    }

    @Override
    public void onPause() {
        dismissPictureChooserDialog();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
            handleReqCodeForPickedImage(data);
        } else if (requestCode == REQ_CODE_CROP_IMAGE) {
            handleReqCodeForCroppedImage(data);
        }
    }

    @Override
    public void onClearImage() {
        getPresenter().deleteUserThumbnailFromRepository();
    }

    @Override
    public void onPickImageFromChooserPopup() {
        if (CropImage.isExplicitCameraPermissionRequired(getActivity())) {
            requestPermissions(new String[] { Manifest.permission.CAMERA },
                  CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
        } else {
            CropImage.startPickImageActivity(getActivity());
        }
    }

    @Override
    public void renderUserThumbnail(String url) {
        mProgressBar.setVisibility(View.GONE);
        setThumbnailMenuListener();

        Glide
              .with(MainApplication.getAppContext())
              .load(url)
              .fitCenter()
              .bitmapTransform(new CropCircleTransformation(MainApplication.getAppContext()))
              .placeholder(R.drawable.user_profile_placeholder)
              .into(mProfileThumbnail);

        MainApplication.getSkyRail().send(new SkyRailEvents.UpdateProfileThumbnailEvent(url));
    }

    @Override
    public void renderError(Throwable t) {
        renderToastMsg(t.getMessage());
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void setThumbnailMenuListener() {
        RxView
              .clicks(mProfileThumbnail)
              .throttleFirst(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
              .subscribe(o -> showPicturePickerMenuDialog());
    }

    private void clearThumbnailMenuListener() {
        mProfileThumbnail.setOnClickListener(null);
    }

    private void handlePickImagePermissionRequestCode(@NonNull int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            CropImage.activity(mCropImageUri).start(getActivity());
        } else {
            ToastUtil.showToast(getString(R.string.permission_is_not_granted));
        }
    }

    private void handleReqCodeForPickedImage(Intent data) {
        final Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);
        if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
            mCropImageUri = imageUri;
            requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                  CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            Intent intent = new Intent(getActivity(), ImageCropperActivity.class);
            intent.putExtra(ExtraKeys.KEY_CROP_IMAGE_URI, imageUri);
            startActivityForResult(intent, REQ_CODE_CROP_IMAGE);
        }
    }

    private void handleReqCodeForCroppedImage(Intent data) {
        clearThumbnailMenuListener();
        mProgressBar.setVisibility(View.VISIBLE);
        Uri uri = data.getParcelableExtra(ExtraKeys.KEY_CROP_IMAGE_URI);
        getPresenter().uploadUserThumbnailToRepository(uri);
    }

    private void showPicturePickerMenuDialog() {
        PictureChooserDialog dlg = PictureChooserDialog.newInstance();
        dlg.setTargetFragment(this, 1);
        dlg.setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_Window_NoMinWidth);
        dlg.show(getFragmentManager(), "fragment_dialog");
    }

    private void dismissPictureChooserDialog() {
        Fragment prev = getFragmentManager().findFragmentByTag("fragment_dialog");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismissAllowingStateLoss();
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}