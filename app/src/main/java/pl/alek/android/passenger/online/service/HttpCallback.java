package pl.alek.android.passenger.online.service;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.online.PassengerInterface;
import pl.alek.android.passenger.online.StationsAPI;
import pl.alek.android.passenger.online.exception.ConnectionFailureException;
import pl.alek.android.passenger.online.utils.HttpUtils;

/**
 * Created by Lenovo on 21.08.2016.
 */
public class HttpCallback implements Callback {

    private static final String TAG = "HttpCallback";

    private retrofit2.Callback<ArrayList<Station>> callback;
    private String stationName = "";
    private String search;
    private String filter;

    public HttpCallback(retrofit2.Callback<ArrayList<Station>> callback) {
        this.callback = callback;
    }

    public void setStationName(String stationName) {
        this.search = PassengerInterface.CONN_SEARCH;
        this.filter = PassengerInterface.FILTER;
        this.stationName = stationName;
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
        ServiceGenerator.reqVerToken = HttpUtils.getReqVerToken(response);
        StationsAPI stations = ServiceGenerator.createService(StationsAPI.class);
        retrofit2.Call<ArrayList<Station>> call = stations.loadStations(search, filter, stationName);
        call.enqueue(callback);
    }
}
