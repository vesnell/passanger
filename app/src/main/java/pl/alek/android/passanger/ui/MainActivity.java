package pl.alek.android.passanger.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import pl.alek.android.passanger.R;
import pl.alek.android.passanger.model.Station;
import pl.alek.android.passanger.online.service.HttpCallback;
import pl.alek.android.passanger.online.service.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<List<Station>> {

    private static final String TAG = "MainActivity";
    private static final String STATION = "Brwinów";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendRequest(STATION);
    }

    private void sendRequest(String stationName) {
        HttpCallback httpCallback = new HttpCallback(this);
        httpCallback.setStationName(stationName);
        ServiceGenerator.sendRequest(this, httpCallback);
    }

    @Override
    public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
        if (response.code() == 200) {
            List<Station> stations = response.body();
            if (stations.size() > 0) {
                Station station = stations.get(0);
                Log.d(TAG, station.ID + "; " + station.Nazwa);
            }
        }
    }

    @Override
    public void onFailure(Call<List<Station>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
    }
}
