package com.seongil.mvplife.sample.common.ui.imagecropper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

import com.seongil.mvplife.sample.R;
import com.seongil.mvplife.sample.application.MainApplication;
import com.seongil.mvplife.sample.common.extrakey.ExtraKeys;
import com.seongil.mvplife.sample.common.file.FileManager;
import com.seongil.mvplife.sample.common.reporter.CrashReporter;
import com.seongil.mvplife.sample.common.utils.ToastUtil;
import com.seongil.mvplife.sample.ui.base.BaseFragment;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author seong-il, kim
 * @since 17. 4. 16
 */
public class ImageCropperFragment extends BaseFragment<ImageCropperView, ImageCropperPresenter>
      implements ImageCropperView, CropImageView.OnSetImageUriCompleteListener,
      CropImageView.OnCropImageCompleteListener {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private CropImageView mCropImageView;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized ImageCropperFragment newInstance() {
        return new ImageCropperFragment();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @NonNull
    @Override
    public ImageCropperPresenter createPresenter() {
        return new ImageCropperPresenter();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_image_cropper;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCropImageView = (CropImageView) view.findViewById(R.id.crop_image_view);
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);
        mCropImageView.setAspectRatio(1, 1);
        mCropImageView.setFixedAspectRatio(true);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Uri uri = intent.getParcelableExtra(ExtraKeys.KEY_CROP_IMAGE_URI);
            mCropImageView.setImageUriAsync(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnCropImageCompleteListener(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_action_crop) {
            mCropImageView.getCroppedImageAsync();
            return true;
        } else if (item.getItemId() == R.id.main_action_rotate) {
            mCropImageView.rotateImage(90);
            return true;
        } else if (item.getItemId() == R.id.main_action_flip_horizontally) {
            mCropImageView.flipImageHorizontally();
            return true;
        } else if (item.getItemId() == R.id.main_action_flip_vertically) {
            mCropImageView.flipImageVertically();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error != null) {
            CrashReporter.getInstance().report(error);
            ToastUtil.showToast("Image load failed: " + error.getMessage());
        }
    }

    // ========================================================================
    // methods
    // ========================================================================
    private void handleCropResult(@NonNull CropImageView.CropResult result) {
        if (result.getError() == null) {
            Intent resultIntent = new Intent();
            if (result.getUri() != null) {
                resultIntent.putExtra(ExtraKeys.KEY_CROP_IMAGE_URI, result.getUri());
            } else {
                FileManager fm = new FileManager();
                Uri bitmapUri =
                      fm.createTempFileWithContentUriType(MainApplication.getAppContext(), FileManager.THUMBNAIL_EXT);
                saveAvatarImgToFileSystemStorage(bitmapUri, result.getBitmap());
                resultIntent.putExtra(ExtraKeys.KEY_CROP_IMAGE_URI, bitmapUri);
            }
            getActivity().setResult(Activity.RESULT_OK, resultIntent);
            getActivity().finish();
        } else {
            ToastUtil.showToast("Image crop failed: " + result.getError().getMessage());
            CrashReporter.getInstance().report(result.getError());
        }
    }

    private void saveAvatarImgToFileSystemStorage(Uri uri, @NonNull Bitmap bitmap) {
        FileManager fm = new FileManager();
        final File tempFile = new File(uri.getPath());
        final File file = new File(fm.getDirectoryFile(MainApplication.getAppContext()), tempFile.getName());
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
