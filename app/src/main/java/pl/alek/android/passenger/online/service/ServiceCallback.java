package pl.alek.android.passenger.online.service;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.online.service.api.StationsAPI;
import pl.alek.android.passenger.online.exception.ConnectionFailureException;
import pl.alek.android.passenger.online.utils.PassengerReqVerToken;
import pl.alek.android.passenger.ui.StationInfoActivity;

/**
 * Created by Lenovo on 21.08.2016.
 */
public class ServiceCallback implements Callback {

    private static final String TAG = "ServiceCallback";

    private retrofit2.Callback<ArrayList<Station>> callback;
    private StationInfoActivity stationInfoActivity;
    private String stationName;

    public ServiceCallback(retrofit2.Callback<ArrayList<Station>> callback, String stationName) {
        this.callback = callback;
        this.stationName = stationName;
    }

    public ServiceCallback(StationInfoActivity stationInfoActivity) {
        this.stationInfoActivity = stationInfoActivity;
    }

    @Override
    public void onFailure(Call call, IOException err) {
        try {
            throw new ConnectionFailureException(err.getMessage());
        } catch (ConnectionFailureException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onResponse(Call c, Response response) throws IOException {
        PassengerReqVerToken.setReqVerToken(response);
        if (!isRefreshRequest()) {
            StationsAPI stations = ServiceGenerator.createService(StationsAPI.class);
            retrofit2.Call<ArrayList<Station>> call = stations.loadStations(stationName);
            call.enqueue(callback);
        } else {
            this.stationInfoActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    stationInfoActivity.refresh();
                }
            });
        }
    }

    private boolean isRefreshRequest() {
        return stationInfoActivity != null;
    }
}