package pl.alek.android.passenger.model;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class TimeTravel {
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
}
