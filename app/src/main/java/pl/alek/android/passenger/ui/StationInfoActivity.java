package pl.alek.android.passenger.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.TrainInfo;
import pl.alek.android.passenger.model.GeneralStationInfo;
import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.online.manager.StationInfoManager;
import pl.alek.android.passenger.online.utils.PassengerReqVerToken;
import pl.alek.android.passenger.ui.util.AndroidUtils;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationInfoActivity extends AppCompatActivity {

    private static final String TAG = "StationInfoActivity";
    private static final int MAX_SEND_REFRESH_REG_TOKEN_REQUESTS = 3;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.rvStationInfoList)
    RecyclerView rvStationInfoList;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;
    @Bind(R.id.llNoResult)
    LinearLayout llNoResult;

    private Station station;
    private StationInfoAdapter mAdapter;
    private int requestsIterator = 0;

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);
        ButterKnife.bind(this);

        rvStationInfoList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvStationInfoList.setLayoutManager(mLayoutManager);

        station = (Station) getIntent().getSerializableExtra(Station.NAME);
        setTitle(station.Nazwa);

        swipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        prepareStationInfoView(station, true);
                    }
                });

        prepareStationInfoView(station, false);
    }

    @OnClick(R.id.btnRefresh)
    public void refresh() {
        prepareStationInfoView(station, false);
    }

    private void prepareStationInfoView(Station station, boolean isRefreshBySwipe) {
        if (!isRefreshBySwipe) {
            setProgressBarVisible(true);
        }
        if (AndroidUtils.isNetworkAvailable(this)) {
            sendRequest(station);
        } else {
            showAlertDialogNoInternetConn();
        }
    }

    private void sendRequest(Station station) {
        StationInfoManager stationInfoManager = new StationInfoManager();
        subscription = stationInfoManager.getStationInfo(station.ID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GeneralStationInfo>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                        cleanUI(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(GeneralStationInfo generalStationInfo) {
                        setProgressBarVisible(false);
                        List<TrainInfo> scheduleList = generalStationInfo.Rozklad;
                        if (scheduleList.size() > 0) {
                            mAdapter = new StationInfoAdapter(getApplicationContext(), scheduleList, generalStationInfo.Utrudnienia);
                            rvStationInfoList.setAdapter(mAdapter);
                        } else {
                            setEmptyInfo();
                        }
                    }
                });
    }

    private void showAlertDialogNoInternetConn() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            setEmptyInfo();
        }
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }
        showAlertDialog(R.string.alert_msg_no_internet);
    }

    private void refreshRequestParams() {
        if (requestsIterator < MAX_SEND_REFRESH_REG_TOKEN_REQUESTS) {
            requestsIterator++;
            trySetReqVerToken();
        } else {
            setEmptyInfo();
            showAlertDialog(R.string.alert_msg_500);
        }
    }

    private void showAlertDialog(int msgId) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_title)
                .setMessage(msgId)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void trySetReqVerToken() {
        new PassengerReqVerToken(this, new PassengerReqVerToken.OnDownloadRequestTokenListener() {
            @Override
            public void onSuccess() {
                refresh();
            }

            @Override
            public void onError(String msg) {
                Toast.makeText(StationInfoActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }).setReqVerToken();
    }

    private void cleanUI(String msg) {
        setProgressBarVisible(false);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setProgressBarVisible(boolean isVisible) {
        llNoResult.setVisibility(View.GONE);
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
        llNoResult.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
