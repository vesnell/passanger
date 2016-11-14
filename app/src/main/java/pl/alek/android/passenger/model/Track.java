package pl.alek.android.passenger.model;

import java.util.List;

/**
 * Created by Lenovo on 14.11.2016.
 */

public class Track {
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
}
