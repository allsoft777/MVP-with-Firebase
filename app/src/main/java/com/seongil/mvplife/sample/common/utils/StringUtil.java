package com.seongil.mvplife.sample.common.utils;

import com.seongil.mvplife.sample.common.firebase.reporter.CrashReporter;

/**
 * @author seong-il, kim
 * @since 17. 4. 30
 */
public class StringUtil {

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
    public static String subString(String str, int length) {
        return parseStringByBytes(str, length, "UTF-8")[0];
    }

    public static String[] parseStringByBytes(String raw, int len, String encoding) {
        if (raw == null) {
            return null;
        }

        String[] ary = null;
        try {
            byte[] rawBytes = raw.getBytes(encoding);
            int rawLength = rawBytes.length;

            int index = 0;
            int minusByteNum;
            int offset;
            int hangulByteNum = encoding.equals("UTF-8") ? 3 : 2;

            if (rawLength > len) {
                int aryLength = (rawLength / len) + (rawLength % len != 0 ? 1 : 0);
                ary = new String[aryLength];
                for (int i = 0; i < aryLength; i++) {
                    minusByteNum = 0;
                    offset = len;
                    if (index + offset > rawBytes.length) {
                        offset = rawBytes.length - index;
                    }
                    for (int j = 0; j < offset; j++) {
                        if (((int) rawBytes[index + j] & 0x80) != 0) {
                            minusByteNum++;
                        }
                    }
                    if (minusByteNum % hangulByteNum != 0) {
                        offset -= minusByteNum % hangulByteNum;
                    }
                    ary[i] = new String(rawBytes, index, offset, encoding);
                    index += offset;
                }
            } else {
                ary = new String[] { raw };
            }
        } catch (Exception e) {
            CrashReporter.getInstance().report(new Throwable("Failed to parse string with the given length"));
        }
        return ary;
    }

    // ========================================================================
    // inner and anonymous classes
    // ========================================================================
}
