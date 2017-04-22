package com.seongil.mvplife.sample.common.firebase.exception;

import com.google.android.gms.common.ConnectionResult;

/**
 * @author seong-il, kim
 * @since 17. 4. 10
 */
public class GoogleApiClientConnectionFailThrowable extends Throwable {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private ConnectionResult mConnectionResult;

    // ========================================================================
    // constructors
    // ========================================================================
    public GoogleApiClientConnectionFailThrowable(ConnectionResult result) {
        mConnectionResult = result;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================
    public ConnectionResult getConnectionResult() {
        return mConnectionResult;
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
