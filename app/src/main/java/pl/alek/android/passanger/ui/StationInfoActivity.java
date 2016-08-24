package pl.alek.android.passanger.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import pl.alek.android.passanger.ui.util.AndroidUtils;
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
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.tvNoResult)
    TextView tvNoResult;

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

        final Station station = (Station) getIntent().getSerializableExtra(Station.NAME);
        setTitle(station.Nazwa);

        swipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        sendRequest(station);
                    }
                });

        sendRequest(station);
    }

    private void sendRequest(Station station) {
        if (AndroidUtils.isNetworkAvailable(this)) {
            StationInfoAPI stationInfoAPI = ServiceGenerator.createService(StationInfoAPI.class);
            Map<String, Object> params = HttpUtils.getStationInfoParams(station.ID);
            Call<ServerInfoResponse> call = stationInfoAPI.loadData(params);
            call.enqueue(this);
        } else {
            showAlertDialogNoInternetConn();
        }
    }

    private void showAlertDialogNoInternetConn() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            setEmptyInfo();
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title_search)
                .setMessage(R.string.alert_msg_no_internet)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onResponse(Call<ServerInfoResponse> call, Response<ServerInfoResponse> response) {
        setProgressBarVisible(false);
        if (response.body() != null) {
            ServerInfoResponse serverInfoResponse = response.body();
            List<RailInfo> scheduleList = serverInfoResponse.Rozklad;
            if (scheduleList.size() > 0) {
                mAdapter = new StationInfoAdapter(this, scheduleList, serverInfoResponse.Utrudnienia);
                rvStationInfoList.setAdapter(mAdapter);
            } else {
                setEmptyInfo();
            }
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
        tvNoResult.setVisibility(View.GONE);
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
            rvStationInfoList.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            rvStationInfoList.setVisibility(View.VISIBLE);
            swipeContainer.setRefreshing(false);
        }
    }

    private void setEmptyInfo() {
        progressBar.setVisibility(View.GONE);
        swipeContainer.setRefreshing(false);
        tvNoResult.setVisibility(View.VISIBLE);
    }
}
