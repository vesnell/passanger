package pl.alek.android.passanger.model;

import java.util.List;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class RailInfo {
    public Integer RozkladID;
    public String ZamowienieSKRJID;
    public String DataPlanowa;
    public Integer StacjaPoczatkowaID;
    public Integer StacjaKoncowaID;
    public boolean PosiadaWykonanie;
    public String KategoriaHandlowaSymbol;
    public String PrzwoznikSkrot;
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
}
