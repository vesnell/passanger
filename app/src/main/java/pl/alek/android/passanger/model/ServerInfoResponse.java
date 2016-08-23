package pl.alek.android.passanger.model;

import java.util.List;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class ServerInfoResponse {
    public String CzasStatystyk;
    public String CzasImportu;
    public String CzasObliczen;
    public Integer MilisekundyNastepnyImport;
    public Integer GranicaCzasowaPrzed;
    public Integer GranicaCzasowaPo;
    public Integer ProcPunktualnoscUruchomien;
    public Integer ProcPunktualnoscNaTrasie;
    public Integer ProcPunktualnoscZakonczen;
    public Integer ProcUruchomien;
    public Integer LiczbaUruchomionych;
    public Integer LiczbaNaTrasie;
    public Integer LiczbaZakonczonych;
    public Integer LiczbaDoUruchomienia;
    public Integer LiczbaOdwolanych;
    public String NajblizszyWRozkladzie;
    public List<String> Utrudnienia;
    public boolean Odjazdy;
    public List<RailInfo> Rozklad;
    public List<RailInfo> Opoznione;
}
