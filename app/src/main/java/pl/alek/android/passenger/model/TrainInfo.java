package pl.alek.android.passenger.model;

import android.content.Context;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import pl.alek.android.passenger.R;
import pl.alek.android.passenger.ui.util.AndroidUtils;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class TrainInfo implements Serializable {

    public static final String TAG = "TrainInfo";

    private static final String DATE_FORMAT = "HH:mm";
    private static final String TIMEZONE_UTC = "UTC";
    private static final String ARROW_UNICODE = "\u21B3";

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

    private String getHourInString(String godzina) {
        long timestamp = getTimestamp(godzina);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE_UTC));
        String t = dateFormat.format(timestamp);
        return t;
    }

    private long getTimestamp(String portalDate) {
        String time = portalDate.split("\\(|\\)")[1];
        return Long.parseLong(time);
    }

    public boolean isLeftStation() {
        String timeToCompare = null;
        if ((!isTrainCanceled()) && (!isTrainCanceledPartly()) && Opoznienie > 0) {
            timeToCompare = Godzina;
        } else {
             timeToCompare = GodzinaPlanowa;
        }
        long tToCompare = getTimestamp(timeToCompare);

        return AndroidUtils.getCurrZoneTimeInMillis() > tToCompare;
    }

    public String getDelayedHourLabel(Context context) {
        if (Opoznienie > 0) {
            if (isTrainCanceled()) {
                return context.getResources().getString(R.string.canceled);
            } else if (isTrainCanceledPartly()) {
                return context.getResources().getString(R.string.partly_canceled);
            } else {
                return getHourInString(Godzina) + " (+" + Opoznienie + ")";
            }
        } else {
            return null;
        }
    }

    public String getPlannedHourLabel() {
        return getHourInString(GodzinaPlanowa);
    }

    public String getPlatformTrack() {
        if (StacjaPoczatkowaTor != null) {
            return StacjaPoczatkowaPeron + " / " + StacjaPoczatkowaTor;
        } else {
            return StacjaPoczatkowaPeron;
        }
    }

    public String getEndStation() {
        return ARROW_UNICODE + " " + RelacjaKoncowaNazwa;
    }

    public String getCarrier() {
        return PrzewoznikSkrot + ":" + NrPociagu;
    }

    public boolean isTrainCanceled() {
        return Opoznienie == Integer.MAX_VALUE;
    }

    public boolean isTrainCanceledPartly() {
        return Opoznienie == Integer.MAX_VALUE - 1;
    }

    public String getDetailsTitle() {
        return StacjaPoczatkowaNazwa + " - " + StacjaKoncowaNazwa;
    }
}
