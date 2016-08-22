package pl.alek.android.passanger.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.alek.android.passanger.R;
import pl.alek.android.passanger.model.Station;
import pl.alek.android.passanger.online.service.HttpCallback;
import pl.alek.android.passanger.online.service.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<ArrayList<Station>> {

    private static final String TAG = "MainActivity";
    private static final int START_SEARCH_SIZE_TEXT = 3;

    @Bind(R.id.etStationSearch)
    EditText etStationSearch;
    @Bind(R.id.btnStationSearch)
    Button btnStationSearch;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnStationSearch.setEnabled(false);
        etStationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    private void enableSubmitIfReady() {
        boolean isReady = etStationSearch.getText().toString().length() >= START_SEARCH_SIZE_TEXT;
        btnStationSearch.setEnabled(isReady);
    }

    @OnClick(R.id.btnStationSearch)
    public void submit() {
        String station = etStationSearch.getText().toString();
        sendRequest(station);
        setProgressBarVisible(true);
    }

    private void sendRequest(String stationName) {
        HttpCallback httpCallback = new HttpCallback(this);
        httpCallback.setStationName(stationName);
        ServiceGenerator.sendRequest(this, httpCallback);
    }

    @Override
    public void onResponse(Call<ArrayList<Station>> call, Response<ArrayList<Station>> response) {
        setProgressBarVisible(false);
        if (response.code() == 200) {
            ArrayList<Station> stations = response.body();
            if (stations.size() == 1) {
                Station station = stations.get(0);
                openStationInfoActivity(station);
            } else if (stations.size() > 0) {
                openStationsListActivity(stations);
            } else {
                Toast.makeText(this, R.string.station_not_found, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onFailure(Call<ArrayList<Station>> call, Throwable t) {
        setProgressBarVisible(false);
        Log.e(TAG, t.getMessage());
    }

    private void setProgressBarVisible(boolean isVisible) {
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void openStationInfoActivity(Station station) {
        Intent i = new Intent(this, StationInfoActivity.class);
        i.putExtra(Station.NAME, station);
        startActivity(i);
    }

    private void openStationsListActivity(ArrayList<Station> stations) {
        Intent intent = new Intent(this, StationsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Station.LIST, stations);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
