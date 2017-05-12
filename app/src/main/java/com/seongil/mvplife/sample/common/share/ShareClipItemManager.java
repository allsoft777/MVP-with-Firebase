package com.seongil.mvplife.sample.common.share;

import android.support.annotation.NonNull;

import com.seongil.mvplife.sample.common.datetime.RxFormat;
import com.seongil.mvplife.sample.domain.ClipDomain;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * @author seong-il, kim
 * @since 17. 5. 12
 */
public class ShareClipItemManager {

    // ========================================================================
    // constants
    // ========================================================================
    private static final SimpleDateFormat DATE_FORMAT =
          new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.getDefault());

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
    public String buildShareText(@NonNull List<ClipDomain> domainList) {
        StringBuilder sb = new StringBuilder(1024);
        final int size = domainList.size();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sb.append("\n\n");
            }
            sb.append(RxFormat.buildDateTimeString(DATE_FORMAT, domainList.get(i).getCreatedAt())).append("\n");
            sb.append(domainList.get(i).getTextData());
        }
        return sb.toString();
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
