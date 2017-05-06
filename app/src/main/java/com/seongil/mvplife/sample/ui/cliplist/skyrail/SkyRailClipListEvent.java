package com.seongil.mvplife.sample.ui.cliplist.skyrail;

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
}
