package com.seongil.mvplife.sample.common.file;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.seongil.mvplife.sample.BuildConfig;
import com.seongil.mvplife.sample.application.MainApplication;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author seong-il, kim
 * @since 17. 4. 16
 */
public class FileManager {

    // ========================================================================
    // constants
    // ========================================================================
    private static final String TAG = "FileManager";
    private static final String FILES_PATH_ALIAS = "my_thumbnails";
    private static final String TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss";
    private static final String AVATAR_FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";
    public static final String THUMBNAIL_EXT = "jpeg";

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    @NonNull
    public File getDirectoryFile(@NonNull Context context) {
        return createOrGetDirectoryFromFileSystem(context, FILES_PATH_ALIAS);
    }

    public File createOrGetDirectoryFromFileSystem(@NonNull Context context, @NonNull String dirName) {
        final File dir = new File(context.getFilesDir(), dirName);
        if (dir.exists()) {
            return dir;
        }

        final boolean result = dir.mkdirs();
        if (!result) {
            Log.d(TAG, "Failed to create a directory on the filesystem.");
        }
        return dir;
    }

    public Uri createTempFileWithContentUriType(Context context, String fileExt) {
        final File directory = getDirectoryFile(MainApplication.getAppContext());
        final String timeStamp = new SimpleDateFormat(TIMESTAMP_FORMAT, Locale.KOREA).format(new Date());
        try {
            File tempFile = File.createTempFile(timeStamp, "." + fileExt, directory);
            return FileProvider.getUriForFile(context, AVATAR_FILE_PROVIDER_AUTHORITY, tempFile);
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
