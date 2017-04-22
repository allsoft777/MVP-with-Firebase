package com.seongil.mvplife.sample.common.model;

import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * @author seong-il, kim
 * @since 17. 4. 11
 */
public class SkyRailEvents {

    // ========================================================================
    // constants
    // ========================================================================

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

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
    public static class UpdateProfileThumbnailEvent {

        private String url;

        public UpdateProfileThumbnailEvent(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class OnActivityResultEvent {

        private int requestCode;
        private int resultCode;
        private Intent intent;

        public OnActivityResultEvent(int requestCode, int resultCode, Intent intent) {
            this.requestCode = requestCode;
            this.resultCode = resultCode;
            this.intent = intent;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public int getResultCode() {
            return resultCode;
        }

        public Intent getIntent() {
            return intent;
        }
    }

    public static class RuntimePermissionResultEvent {

        private int requestCode;

        @NonNull
        private String[] permissions;

        @NonNull
        private int[] grantResults;

        public RuntimePermissionResultEvent(int requestCode, @NonNull String[] permissions,
              @NonNull int[] grantResults) {
            this.requestCode = requestCode;
            this.permissions = permissions;
            this.grantResults = grantResults;
        }

        public int getRequestCode() {
            return requestCode;
        }

        @NonNull
        public String[] getPermissions() {
            return permissions;
        }

        @NonNull
        public int[] getGrantResults() {
            return grantResults;
        }
    }
}
