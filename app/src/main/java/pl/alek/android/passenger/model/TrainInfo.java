package pl.alek.android.passenger.model;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.util.ModelUtils;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class TrainInfo implements Serializable {

    public static final String TAG = "TrainInfo";

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

    public boolean isLeftStation() {
        String timeToCompare = null;
        if ((!isTrainCanceled()) && (!isTrainCanceledPartly()) && Opoznienie > 0) {
            timeToCompare = Godzina;
        } else {
             timeToCompare = GodzinaPlanowa;
        }
        long tToCompare = ModelUtils.getTimestamp(timeToCompare);

        return ModelUtils.getCurrZoneTimeInMillis() > tToCompare;
    }

    public String getDelayedHourLabel(Context context) {
        if (Opoznienie > 0) {
            if (isTrainCanceled()) {
                return context.getResources().getString(R.string.canceled);
            } else if (isTrainCanceledPartly()) {
                return context.getResources().getString(R.string.partly_canceled);
            } else {
                return ModelUtils.getHourInString(Godzina) + " (+" + Opoznienie + ")";
            }
        } else {
            return null;
        }
    }

    public String getPlannedHourLabel() {
        return ModelUtils.getHourInString(GodzinaPlanowa);
    }

    public String getPlatformTrack() {
        if (StacjaPoczatkowaTor != null) {
            return StacjaPoczatkowaPeron + " / " + StacjaPoczatkowaTor;
        } else {
            return StacjaPoczatkowaPeron;
        }
    }

    public String getEndStation() {
        return ModelUtils.ARROW_UNICODE + " " + RelacjaKoncowaNazwa;
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
