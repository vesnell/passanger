package pl.alek.android.passanger.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passanger.R;
import pl.alek.android.passanger.model.RailInfo;
import pl.alek.android.passanger.model.ServerInfoResponse;
import pl.alek.android.passanger.model.Station;
import pl.alek.android.passanger.online.service.ServiceGenerator;
import pl.alek.android.passanger.online.service.StationInfoAPI;
import pl.alek.android.passanger.online.utils.HttpUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationInfoActivity extends AppCompatActivity implements Callback<ServerInfoResponse> {

    private static final String TAG = "StationInfoActivity";

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.rvStationInfoList)
    RecyclerView rvStationInfoList;

    private StationInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        ButterKnife.bind(this);

        setProgressBarVisible(true);

        rvStationInfoList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvStationInfoList.setLayoutManager(mLayoutManager);

        Station station = (Station) getIntent().getSerializableExtra(Station.NAME);
        setTitle(station.Nazwa);

        sendRequest(station);
    }

    private void sendRequest(Station station) {
        StationInfoAPI stationInfoAPI = ServiceGenerator.createService(StationInfoAPI.class);
        Map<String, Object> params = HttpUtils.getStationInfoParams(station.ID);
        Call<ServerInfoResponse> call = stationInfoAPI.loadData(params);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ServerInfoResponse> call, Response<ServerInfoResponse> response) {
        setProgressBarVisible(false);
        if (response.body() != null) {
            ServerInfoResponse serverInfoResponse = response.body();
            List<RailInfo> scheduleList = serverInfoResponse.Rozklad;
            mAdapter = new StationInfoAdapter(this, scheduleList, serverInfoResponse.Utrudnienia);
            rvStationInfoList.setAdapter(mAdapter);
        } else {
            Log.e(TAG, response.message());
        }
    }

    @Override
    public void onFailure(Call<ServerInfoResponse> call, Throwable t) {
        setProgressBarVisible(false);
        Log.e(TAG, t.getMessage());
    }

    private void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
            rvStationInfoList.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            rvStationInfoList.setVisibility(View.VISIBLE);
        }
    }
}
