package pl.alek.android.passanger.model;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

/**
 * Created by Lenovo on 17.08.2016.
 */
public class Station implements Serializable, Comparable<Station> {

    public static final String LIST = "stationsList";
    public static final String NAME = "station";
    public static final String TAG = "Station";

    public Integer ID;
    public String Nazwa;
    public String ObiektKod;
    public String ZarzadKod;
    public String ZakladKod;
    public Double SzerokoscGeograficzna;
    public Double DlugoscGeograficzna;
    public String KrajKod;

    @Override
    public int compareTo(Station station) {
        Collator collator = Collator.getInstance(new Locale("pl", "PL"));
        return collator.compare(this.Nazwa, station.Nazwa);
    }
}
