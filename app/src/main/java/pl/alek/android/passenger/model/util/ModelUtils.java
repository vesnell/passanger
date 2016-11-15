package pl.alek.android.passenger.model.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Lenovo on 15.11.2016.
 */

public class ModelUtils {

    private static final String DATE_FORMAT = "HH:mm";
    private static final String TIMEZONE_UTC = "UTC";
    public static final String ARROW_UNICODE = "\u21B3";

    public static long getTimestamp(String portalDate) {
        String time = portalDate.split("\\(|\\)")[1];
        return Long.parseLong(time);
    }

    public static String getHourInString(String godzina) {
        long timestamp = ModelUtils.getTimestamp(godzina);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        String t = dateFormat.format(timestamp);
        return t;
    }

    public static long getCurrZoneTimeInMillis() {
        Calendar rightNow = Calendar.getInstance();
        long offset = rightNow.get(Calendar.ZONE_OFFSET) +
                rightNow.get(Calendar.DST_OFFSET);
        return rightNow.getTimeInMillis() + offset;
    }
}
