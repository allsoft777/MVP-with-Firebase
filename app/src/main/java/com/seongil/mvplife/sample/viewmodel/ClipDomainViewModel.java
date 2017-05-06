package com.seongil.mvplife.sample.viewmodel;

import com.seongil.mvplife.sample.domain.ClipDomain;
import com.seongil.recyclerviewlife.model.common.RecyclerViewItem;

/**
 * @author seong-il, kim
 * @since 17. 4. 26
 */
public class ClipDomainViewModel implements RecyclerViewItem {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================
    private ClipDomain domain;

    // ========================================================================
    // constructors
    // ========================================================================
    public ClipDomainViewModel(ClipDomain domain) {
        this.domain = domain;
    }

    // ========================================================================
    // getter & setter
    // ========================================================================

    public ClipDomain getDomain() {
        return domain;
    }

    public void setDomain(ClipDomain domain) {
        this.domain = domain;
    }

    // ========================================================================
    // methods for/from superclass/interfaces
    // ========================================================================

    // ========================================================================
    // methods
    // ========================================================================
    public boolean isInvalidDomain() {
        return ClipDomain.EMPTY_KEY.equals(domain.getKey());
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}