package com.clowdtech.data;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateTimeHelper {

    static DateTimeFormatter friendlyDisplayFormatDateOnly = DateTimeFormat.forPattern("d MMMM YYYY");
    static DateTimeFormatter friendlyDisplayFormatTimeOnly = DateTimeFormat.forPattern("HH:mm:ss a");
    static DateTimeFormatter friendlyDisplayFormatTimeAndDate = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss a");

    public static String getFriendlyDateString(DateTime timestamp) {
        return friendlyDisplayFormatDateOnly.print(timestamp);
    }

    public static String getFriendlyDateString(long timestamp) {
        return friendlyDisplayFormatDateOnly.print(timestamp);
    }

    public static String getFriendlyTimeString(DateTime timestamp) {
        return friendlyDisplayFormatTimeOnly.print(timestamp);
    }

    public static String getFriendlyDateAndTimeString(DateTime timestamp) {
        return friendlyDisplayFormatTimeAndDate.print(timestamp);
    }
}
