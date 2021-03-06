package com.seongil.mvplife.sample.common.datetime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.Observable;

/**
 * @author seong-il, kim
 * @since 17. 4. 30
 */
public class RxFormat {

    // ========================================================================
    // constants
    // ========================================================================

    // ========================================================================
    // fields
    // ========================================================================

    // ========================================================================
    // constructors
    // ========================================================================
    private RxFormat() {
        throw new AssertionError("Could not create instance.");
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
    public static Observable<String> convertDateTimeString(SimpleDateFormat sdf, long millis) {
        return Observable.create(e -> {
            String result = buildDateTimeString(sdf, millis);
            if (e.isDisposed()) {
                return;
            }
            e.onNext(result);
        });
    }

    public static Observable<String> decimalFormat(String format, long number1) {
        return Observable.fromCallable(() -> new DecimalFormat(format).format(number1));
    }

    public static String buildDateTimeString(SimpleDateFormat sdf, long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        sdf.setCalendar(calendar);
        return sdf.format(calendar.getTime());
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
