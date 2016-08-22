package pl.alek.android.passanger.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import pl.alek.android.passanger.R;
import pl.alek.android.passanger.model.Station;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_info);

        Station station = (Station) getIntent().getSerializableExtra(Station.NAME);
        Toast.makeText(this, station.Nazwa, Toast.LENGTH_LONG).show();
    }
}
