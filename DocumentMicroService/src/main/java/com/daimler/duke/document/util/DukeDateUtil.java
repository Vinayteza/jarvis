package com.daimler.duke.document.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.daimler.duke.document.exception.BaseException;

public final class DukeDateUtil {
    private DukeDateUtil() {
    }

    public static final String DDMMYYYYHHMMSS_DOTDELIM = "dd.MM.yyyy.HH:mm:ss";

    /**
     * @param date.
     * @param dateformat
     * @return
     */
    public static String toDateString(final Date date,
                                      final String dateformat) {
        String dateString = "";
        if (null != date && null != dateformat) {
            final DateFormat dateFormat = new SimpleDateFormat(dateformat);
            dateString = dateFormat.format(date);
        }
        return dateString;
    }

    /**
     * Converts the string to date with Specified format in the dateformat
     * parameter.
     *
     * @param dateString String <code>String dateString</code>
     * @param dataFormat Dateformat <code>String dataFormat</code>
     * @return today Date <code>Date</code>
     * @throws BaseException In case date conversion fails.
     */
    public static Date toDate(final String dateString, final String dataFormat)
            throws Exception {
        final DateFormat dateFormat = new SimpleDateFormat(dataFormat);
        Date today = null;
        today = dateFormat.parse(dateString);
        return today;
    }

}
