package com.seongil.mvplife.sample.ui.detailview.skyrail;

import com.seongil.mvplife.sample.common.rxskyrail.RxSkyRail;

/**
 * @author seong-il, kim
 * @since 17. 5. 2
 */
public class DetailViewSkyRail {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private RxSkyRail mRxSkyRail;
    private static DetailViewSkyRail sInstance;

    // ========================================================================
    // constructors
    // ========================================================================
    public static synchronized DetailViewSkyRail getInstance() {
        if (sInstance == null) {
            sInstance = new DetailViewSkyRail();
        }
        return sInstance;
    }

    public DetailViewSkyRail() {
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
