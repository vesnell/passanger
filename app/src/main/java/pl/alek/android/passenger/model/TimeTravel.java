package pl.alek.android.passenger.model;

import java.io.Serializable;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class TimeTravel implements Serializable {

    private static final int MIUTES_IN_HOUR = 60;
    private static final int HOURS_IN_DAY = 24;

    public Long Ticks;
    public Integer Days;
    public Integer Hours;
    public Integer Milliseconds;
    public Integer Minutes;
    public Integer Seconds;
    public Double TotalDays;
    public Double TotalHours;
    public Integer TotalMilliseconds;
    public Double TotalMinutes;
    public Double TotalSeconds;

    public String getTimeTravel() {
        String time = "";
        if (Days > 0) {
            time += Integer.toString(Days) + "d ";
        }
        if (Hours > 0 || Days > 0) {
            time += Integer.toString(Hours) + "h ";
        }
        return time += Integer.toString(Minutes) + "min";
    }

    public Integer getTimeTravelInMinutes() {
        Integer time = 0;
        if (Days > 0) {
            time += Days * HOURS_IN_DAY * MIUTES_IN_HOUR;
        }
        if (Hours > 0) {
            time += Hours * MIUTES_IN_HOUR;
        }
        return time += Minutes;
    }
}
