package pl.alek.android.passenger.database.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import pl.alek.android.passenger.model.Station;

/**
 * Created by Lenovo on 27.10.2016.
 */

public class StationUsed extends RealmObject {

    private int order;
    @PrimaryKey
    private Integer id;
    private String name;
    private String ObiektKod;
    private String ZarzadKod;
    private String ZakladKod;
    private Double SzerokoscGeograficzna;
    private Double DlugoscGeograficzna;
    private String KrajKod;

    public StationUsed() {}

    public StationUsed(Station station, int order) {
        this.id = station.ID;
        this.name = station.Nazwa;
        this.ObiektKod = station.ObiektKod;
        this.ZarzadKod = station.ZarzadKod;
        this.ZakladKod = station.ZakladKod;
        this.SzerokoscGeograficzna = station.SzerokoscGeograficzna;
        this.DlugoscGeograficzna = station.DlugoscGeograficzna;
        this.KrajKod = station.KrajKod;
        this.order = order;
    }

    public static Station parseToStation(StationUsed stationUsed) {
        Station station = new Station();
        station.ID = stationUsed.getId();
        station.Nazwa = stationUsed.getName();
        station.ObiektKod = stationUsed.getObiektKod();
        station.ZarzadKod = stationUsed.getZarzadKod();
        station.ZakladKod = stationUsed.getZakladKod();
        station.SzerokoscGeograficzna = stationUsed.getSzerokoscGeograficzna();
        station.DlugoscGeograficzna = stationUsed.getDlugoscGeograficzna();
        station.KrajKod = stationUsed.getKrajKod();
        return station;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObiektKod() {
        return ObiektKod;
    }

    public void setObiektKod(String obiektKod) {
        ObiektKod = obiektKod;
    }

    public String getZarzadKod() {
        return ZarzadKod;
    }

    public void setZarzadKod(String zarzadKod) {
        ZarzadKod = zarzadKod;
    }

    public String getZakladKod() {
        return ZakladKod;
    }

    public void setZakladKod(String zakladKod) {
        ZakladKod = zakladKod;
    }

    public Double getSzerokoscGeograficzna() {
        return SzerokoscGeograficzna;
    }

    public void setSzerokoscGeograficzna(Double szerokoscGeograficzna) {
        SzerokoscGeograficzna = szerokoscGeograficzna;
    }

    public Double getDlugoscGeograficzna() {
        return DlugoscGeograficzna;
    }

    public void setDlugoscGeograficzna(Double dlugoscGeograficzna) {
        DlugoscGeograficzna = dlugoscGeograficzna;
    }

    public String getKrajKod() {
        return KrajKod;
    }

    public void setKrajKod(String krajKod) {
        KrajKod = krajKod;
    }
}
