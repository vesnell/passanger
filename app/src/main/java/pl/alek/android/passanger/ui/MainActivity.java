package pl.alek.android.passanger.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

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

public class MainActivity extends AppCompatActivity implements Callback<List<Station>> {

    private static final String TAG = "MainActivity";
    private static final int START_SEARCH_SIZE_TEXT = 3;

    @Bind(R.id.etStationSearch)
    EditText etStationSearch;
    @Bind(R.id.btnStationSearch)
    Button btnStationSearch;

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
            if (stations.size() == 1) {
                Station station = stations.get(0);
                //open StationInfoActivity
            } else if (stations.size() > 0) {
                //open ListStationsActivity
            } else {
                Toast.makeText(this, R.string.station_not_found, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onFailure(Call<List<Station>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
    }
}
