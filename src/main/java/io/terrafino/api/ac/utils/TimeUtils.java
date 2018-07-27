package io.terrafino.api.ac.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class TimeUtils {

    public static final DateTimeZone LOCAL= DateTimeZone.forID("Europe/London");

    public static long getTimestamp(int date, int time) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd HHmmss").withZone(LOCAL);
        return formatter.parseDateTime(String.format("%08d %06d", date, time)).getMillis();
    }

    public static int getDate(long timeStamp) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd").withZone(LOCAL);
        return Integer.parseInt(formatter.print(timeStamp));
    }
    public static int getTime(long timeStamp) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("HHmmss").withZone(LOCAL);
        return Integer.parseInt(formatter.print(timeStamp));
    }

}
