package pl.alek.android.passenger.model;

import com.google.gson.Gson;

/**
 * Created by Lenovo on 14.11.2016.
 */

public class TrainForParams {

    public Integer RozkladID;
    public Integer GrupaSKRJID;
    public Integer ZamowienieSKRJID;
    public String DataKursowania;
    public Integer StacjaPoczatkowaID;
    public Integer StacjaKoncowaID;
    public Integer StacjaPoczatkowaNumerNaTrasie;
    public Integer StacjaKoncowaNumerNaTrasie;

    public TrainForParams(Train train) {
        this.RozkladID = train.RozkladID;
        this.GrupaSKRJID = train.GrupaSKRJID;
        this.ZamowienieSKRJID = train.ZamowienieSKRJID;
        this.DataKursowania = train.DataKursowaniaStr;
        this.StacjaPoczatkowaID = train.StacjaPoczatkowaID;
        this.StacjaKoncowaID = train.StacjaKoncowaID;
        this.StacjaPoczatkowaNumerNaTrasie = train.StacjaPoczatkowaNumerNaTrasie;
        this.StacjaKoncowaNumerNaTrasie = train.StacjaKoncowaNumerNaTrasie;
    }

    public String getQuery() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
