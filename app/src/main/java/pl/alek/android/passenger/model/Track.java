package pl.alek.android.passenger.model;

import java.io.Serializable;
import java.util.List;

import pl.alek.android.passenger.model.util.ModelUtils;

/**
 * Created by Lenovo on 14.11.2016.
 */

public class Track implements Serializable {

    public static final String TAG = "Track";
    public static final String TRACK_LIST = "trackList";

    public String RozkladID;
    public Integer GrupaSKRJID;
    public Integer ZamowienieSKRJID;
    public Integer StacjaID;
    public Integer StacjaNumerNaTrasie;
    public Integer StacjaNumerNaTrasieStary;
    public String StacjaNazwa;
    public String Przyjazd;
    public String PrzyjazdRzecz;
    public TimeTravel PrzyjazdOpoznienie;
    public String Odjazd;
    public String OdjazdRzecz;
    public TimeTravel OdjazdOpoznienie;
    public String Peron;
    public String Tor;
    public String NumerWjazdowy;
    public String NumerWyjazdowy;
    public Boolean Utrudnienia;
    public Boolean Prognoza;
    public Boolean KomunikacjaZastepcza;
    public Boolean PokazujNaPlakacie;
    public Boolean Wykonana;
    public Boolean Odwolana;
    public Integer KolorOpoznieniePrzyjazd;
    public Integer KolorOpoznienieOdjazd;
    public Object Uslugi;
    public Double KmKumulowany;
    public Double KmKumulowanyStary;
    public Object NrPociagu;
    public Object UtrudnieniaNaStacji;
    public Boolean PoprzedniBezPodroznych;
    public Boolean NastepnyBezPodroznych;
    public String PrzyjazdStr;
    public String PrzyjazdRzeczStr;
    public String OdjazdStr;
    public String OdjazdRzeczStr;
    public List<Icon> ico;

    public boolean isLeftStation() {
        return Wykonana;
    }

    private boolean isDepartureDelay() {
        return OdjazdOpoznienie != null;
    }

    private boolean isArrivalDelay() {
        return PrzyjazdOpoznienie != null;
    }

    public boolean isStationCanceled() {
        return Odwolana;
    }

    public Integer getDepartureDelay() {
        if (isDepartureDelay()) {
            return OdjazdOpoznienie.Minutes;
        }
        return 0;
    }

    public Integer getArrivalDelay() {
        if (isArrivalDelay()) {
            return PrzyjazdOpoznienie.Minutes;
        }
        return 0;
    }

    public String getDepartureTime() {
        if (Odjazd != null) {
            return ModelUtils.getHourInString(Odjazd);
        }
        return null;
    }

    public String getArrivalTime() {
        if (Przyjazd != null) {
            return ModelUtils.getHourInString(Przyjazd);
        }
        return null;
    }

    public String getDepartureRealTime() {
        if (isDepartureDelay() && getDepartureDelay() != 0) {
            return ModelUtils.getHourInString(OdjazdRzecz) + " (+" + Integer.toString(getDepartureDelay()) + ")";
        }
        return null;
    }

    public String getArrivalRealTime() {
        if (isArrivalDelay() && getArrivalDelay() != 0) {
            return ModelUtils.getHourInString(PrzyjazdRzecz) + " (+" + Integer.toString(getArrivalDelay()) + ")";
        }
        return null;
    }

    public String getPlatformTrack() {
        if (Tor != null) {
            return Peron + " / " + Tor;
        } else {
            return Peron;
        }
    }
}
