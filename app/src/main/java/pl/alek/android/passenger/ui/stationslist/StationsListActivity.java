package pl.alek.android.passenger.ui.stationslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.database.RealmController;
import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.ui.stationinfo.StationInfoActivity;
import pl.alek.android.passenger.ui.util.StationsListAdapter;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationsListActivity extends AppCompatActivity {

    @Bind(R.id.rvStationsList)
    RecyclerView rvStationsList;

    private StationsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_list);
        ButterKnife.bind(this);

        setTitle(R.string.stations_list);

        Bundle bundle = getIntent().getExtras();
        ArrayList<Station> stations = (ArrayList<Station>) bundle.getSerializable(Station.LIST);

        rvStationsList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvStationsList.setLayoutManager(mLayoutManager);

        mAdapter = new StationsListAdapter(this, stations);
        rvStationsList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new StationsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Station station = (Station) mAdapter.getStationsList().get(position);
                RealmController.getInstance().saveStation(station);
                openStationInfoActivity(station);
            }
        });
    }

    private void openStationInfoActivity(Station station) {
        Intent i = new Intent(StationsListActivity.this, StationInfoActivity.class);
        i.putExtra(Station.NAME, station);
        startActivity(i);
    }
}
