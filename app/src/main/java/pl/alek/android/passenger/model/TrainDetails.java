package pl.alek.android.passenger.model;

import java.util.List;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class TrainDetails {
    public Boolean PolaczeniePierwotne;
    public Integer Identyfikator;
    public Integer RozkladID;
    public Integer StacjaPoczatkowaID;
    public String StacjaPoczatkowaNazwa;
    public Integer StacjaKoncowaID;
    public String StacjaKoncowaNazwa;
    public String Odjazd;
    public String OdjazdStr;
    public Integer OdjazdOpoznienie;
    public Boolean OdjazdOpoznienieWykonanie;
    public String Przyjazd;
    public String PrzyjazdStr;
    public Integer PrzyjazdOpoznienie;
    public Boolean PrzyjazdOpoznienieWykonanie;
    public TimeTravel CzasPodrozy;
    public Integer LiczbaPrzesiadek;
    public Integer Ostrzezenia;
    public List<Train> Pociagi;
    public Object WagonyBezposrednieLista;
    public Object KmKumulowany;
    public Integer LiczbaPrzesiadekMinimalna;
    public Object WagonyBezposrednie;
    public Boolean GlobalnyKupBiletItTrans;
    public Boolean GlobalnyKupBiletIC;
}
