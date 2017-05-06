package com.seongil.mvplife.sample.domain;

/**
 * @author seong-il, kim
 * @since 17. 4. 30
 */
public class ClipDomain {

    // ========================================================================
    // constants
    // ========================================================================
    public static final String EMPTY_KEY = "";

    // ========================================================================
    // fields
    // ========================================================================
    private long createdAt;
    private String textData;
    private String source;
    private boolean favouritesItem;
    private String key;

    // ========================================================================
    // constructors
    // ========================================================================
    public ClipDomain() {
        key = EMPTY_KEY;
        createdAt = 0L;
        textData = "";
        source = "";
        favouritesItem = false;
    }

    public ClipDomain(ClipDomain target) {
        key = target.key;
        createdAt = target.createdAt;
        textData = target.textData;
        source = target.source;
        favouritesItem = target.favouritesItem;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================
    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getTextData() {
        return textData;
    }

    public void setTextData(String textData) {
        this.textData = textData;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavouritesItem() {
        return favouritesItem;
    }

    public void setFavouritesItem(boolean favouritesItem) {
        this.favouritesItem = favouritesItem;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}