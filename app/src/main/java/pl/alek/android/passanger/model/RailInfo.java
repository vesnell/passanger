package pl.alek.android.passanger.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class RailInfo {

    private static final String DATE_FORMAT = "HH:mm";
    private static final String TIMEZONE = "UTC";

    public Integer RozkladID;
    public String ZamowienieSKRJID;
    public String DataPlanowa;
    public Integer StacjaPoczatkowaID;
    public Integer StacjaKoncowaID;
    public boolean PosiadaWykonanie;
    public String KategoriaHandlowaSymbol;
    public String PrzewoznikSkrot;
    public String NrPociagu;
    public Integer RelacjaPoczatkowaID;
    public String RelacjaPoczatkowaNazwa;
    public Integer RelacjaKoncowaID;
    public String RelacjaKoncowaNazwa;
    public Integer Opoznienie;
    public String GodzinaPlanowa;
    public String Godzina;
    public List<List<String>> PrzyczynyUtrudnienia;
    public String NazwaPociagu;
    public String KategoriaSzybkosciNazwa;
    public String KategoriaHandlowaNazwa;
    public String PrzewoznikaNazwa;
    public String StacjaPoczatkowaNazwa;
    public String StacjaPoczatkowaPeron;
    public String StacjaPoczatkowaTor;
    public String StacjaKoncowaNazwa;
    public String StacjaKoncowaPeron;
    public String StacjaKoncowaTor;
    public String StacjePosrednie;
    public boolean KomunikacjaZastepcza;

    private String getHour(String godzina) {
        String time = godzina.split("\\(|\\)")[1];
        long timeInMillis = Long.parseLong(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
        Timestamp timestamp = new Timestamp(timeInMillis);
        String t = dateFormat.format(timestamp);
        return t;
    }

    public String getDelayedHourLabel() {
        if (Opoznienie >= 0) {
            return getHour(Godzina) + " (+" + Opoznienie + ")";
        } else {
            return getHour(Godzina) + " (" + Opoznienie + ")";
        }
    }

    public String getPlannedHourLabel() {
        return getHour(GodzinaPlanowa);
    }

    public String getPlatformTrack() {
        return StacjaPoczatkowaPeron + StacjaPoczatkowaTor;
    }

    public String getRoute() {
        return RelacjaPoczatkowaNazwa + " - " + RelacjaKoncowaNazwa;
    }
}
