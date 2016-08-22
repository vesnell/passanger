package pl.alek.android.passanger.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import pl.alek.android.passanger.R;
import pl.alek.android.passanger.model.ServerInfoResponse;
import pl.alek.android.passanger.model.Station;
import pl.alek.android.passanger.online.service.ServiceGenerator;
import pl.alek.android.passanger.online.service.StationInfoAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationInfoActivity extends AppCompatActivity implements Callback<ServerInfoResponse> {

    private static final String TAG = "StationInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);

        Station station = (Station) getIntent().getSerializableExtra(Station.NAME);
        Toast.makeText(this, station.Nazwa, Toast.LENGTH_LONG).show();

        sendRequest(station);
    }

    private void sendRequest(Station station) {
        StationInfoAPI stationInfoAPI = ServiceGenerator.createService(StationInfoAPI.class);
        Map<String, Object> params = getParams(station.ID);
        Call<ServerInfoResponse> call = stationInfoAPI.loadData(params);
        call.enqueue(this);
    }

    private Map<String, Object> getParams(Integer stationID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("stacjaID", stationID);
        params.put("odjazdy", true);
        params.put("dostepneKH", "IC,IC;R,PR;EIC,IC;TLK,IC;EIP,IC;Os,KS;Os,KM;IR,PR;");
        params.put("__RequestVerificationToken", ServiceGenerator.reqVerToken);
        return params;
    }

    @Override
    public void onResponse(Call<ServerInfoResponse> call, Response<ServerInfoResponse> response) {
        if (response.body() != null) {
            Log.d(TAG, response.body().toString());
        } else {
            Log.e(TAG, response.message());
        }
    }

    @Override
    public void onFailure(Call<ServerInfoResponse> call, Throwable t) {
        Log.e(TAG, t.getMessage());
    }
}
