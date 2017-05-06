package com.seongil.mvplife.sample.ui.cliplist.skyrail;

import com.seongil.mvplife.sample.common.rxskyrail.RxSkyRail;

/**
 * @author seong-il, kim
 * @since 17. 5. 2
 */
public class ClipListViewSkyRail {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private RxSkyRail mRxSkyRail;
    private static ClipListViewSkyRail sInstance;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized ClipListViewSkyRail getInstance() {
        if (sInstance == null) {
            sInstance = new ClipListViewSkyRail();
        }
        return sInstance;
    }

    public ClipListViewSkyRail() {
        mRxSkyRail = new RxSkyRail();
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    public RxSkyRail getSkyRail() {
        return mRxSkyRail;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
