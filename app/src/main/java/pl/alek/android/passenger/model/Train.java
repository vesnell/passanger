package pl.alek.android.passenger.model;

import java.util.List;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class Train {
    public String KategoriaHandlowaNazwa;
    public String PrzewoznikNazwa;
    public String KategoriaSzybkosciNazwa;
    public String KategorieHandloweNaTrasieOpis;
    public String UtrudnieniaOpis;
    public Integer RelacjaPoczatkowaID;
    public Integer RelacjaPoczatkowaNumerNaTrasie;
    public Integer RelacjaKoncowaID;
    public Integer RelacjaKoncowaNumerNaTrasie;
    public String UtrudnieniaKomunikatyUporzadkowane;
    public String NumerKupBilet;
    public Integer RozkladID;
    public Integer GrupaSKRJID;
    public Integer ZamowienieSKRJID;
    public Boolean Zamknieciowa;
    public String RelacjaPoczatkowa;
    public String RelacjaKoncowa;
    public String Numer;
    public String Nazwa;
    public String PrzewznikSkrot;
    public String KategoriaHandlowaSymbol;
    public String KategoriaSzybkosciKod;
    public List<String> KategorieHandloweNaTrasie;
    public String Kursowanie;
    public Integer StacjaPoczatkowaID;
    public Integer StacjaPoczatkowaNumerNaTrasie;
    public String StacjaPoczatkowaNazwa;
    public String Odjazd;
    public String OdjazdStr;
    public Integer StacjaKoncowaID;
    public Integer StacjaKoncowaNumerNaTrasie;
    public String StacjaKoncowaNazwa;
    public String Przyjazd;
    public String PrzyjazdStr;
    public String DataKursowania;
    public String DataKursowaniaStr;
    public String PeronPoczatkowy;
    public String TorPoczatkowy;
    public String PeronKoncowy;
    public String TorKoncowy;
    public Double KmKumulowany;
    public List<Object> Utrudnienia;
    public Object Uslugi;
    public Object RodzajPowiazaniaKod;
    public Integer CzasOczekiwania;
    public Object NrWagonow;
    public Object NrWagonowBezposrednich;
}
