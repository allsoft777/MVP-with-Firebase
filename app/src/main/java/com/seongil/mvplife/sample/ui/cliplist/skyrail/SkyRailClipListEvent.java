package com.seongil.mvplife.sample.ui.cliplist.skyrail;

import android.support.annotation.NonNull;

import com.seongil.mvplife.sample.domain.ClipDomain;

/**
 * @author seong-il, kim
 * @since 2017. 4. 30.
 */
public class SkyRailClipListEvent {

    public static class FavoriteItemEvent {

        private String key;
        private boolean favourites;

        public FavoriteItemEvent(String key, boolean favourites) {
            this.key = key;
            this.favourites = favourites;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean isFavourites() {
            return favourites;
        }

        public void setFavourites(boolean favourites) {
            this.favourites = favourites;
        }
    }

    public static class ClickItemEvent {

        private String key;

        public ClickItemEvent(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class LongClickItemEvent {

        private String key;

        public LongClickItemEvent(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class CopyItemToClipboard {

        private String key;

        public CopyItemToClipboard(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }

    public static class InsertedNewItem {

        private ClipDomain domain;

        public InsertedNewItem(ClipDomain domain) {
            this.domain = domain;
        }

        public ClipDomain getDomain() {
            return domain;
        }

        public void setDomain(ClipDomain domain) {
            this.domain = domain;
        }
    }

    public static class DeletedItem {

        private String itemKey;

        public DeletedItem(String itemKey) {
            this.itemKey = itemKey;
        }

        public String getItemKey() {
            return itemKey;
        }

        public void setItemKey(String itemKey) {
            this.itemKey = itemKey;
        }
    }

    public static class UpdatedItem {

        private ClipDomain domain;

        public UpdatedItem(ClipDomain domain) {
            this.domain = domain;
        }

        public ClipDomain getDomain() {
            return domain;
        }

        public void setDomain(ClipDomain domain) {
            this.domain = domain;
        }
    }

    public static class UpdateFavouritesState {

        private String itemKey;
        private boolean isFavouritesItem;

        public UpdateFavouritesState(@NonNull String itemKey, boolean isFavouritesItem) {
            this.itemKey = itemKey;
            this.isFavouritesItem = isFavouritesItem;
        }

        public String getItemKey() {
            return itemKey;
        }

        public void setItemKey(String itemKey) {
            this.itemKey = itemKey;
        }

        public boolean isFavouritesItem() {
            return isFavouritesItem;
        }

        public void setFavouritesItem(boolean favouritesItem) {
            isFavouritesItem = favouritesItem;
        }
    }
}
