package pl.alek.android.passenger.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Train;
import pl.alek.android.passenger.model.TrainInfo;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class TrainDetailsActivity extends AppCompatActivity implements MainDetailsFragment.DetailsCallback,
        TracksFragment.TracksCallback {

    private static final String TAG = "TrainDetailsActivity";

    @Bind(R.id.mainDetailsContainer)
    LinearLayout mainDetailsContainer;
    @Bind(R.id.trackContainer)
    LinearLayout trackContainer;
    @Bind(R.id.progressBarMainDetails)
    ProgressBar progressBarMainDetails;

    private FragmentManager fm;
    private MainDetailsFragment mainDetailsFragment;
    private TracksFragment tracksFragment;
    private TrainInfo trainInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        fm = getSupportFragmentManager();
        mainDetailsFragment = (MainDetailsFragment) fm.findFragmentByTag(MainDetailsFragment.TAG);
        tracksFragment = (TracksFragment) fm.findFragmentByTag(TracksFragment.TAG);

        Bundle bundle = getIntent().getExtras();
        trainInfo = (TrainInfo) bundle.getSerializable(TrainInfo.TAG);
        setTitle(trainInfo.getDetailsTitle());

        if (savedInstanceState == null) {
            init();
        }
    }

    private void init() {
        initUI();
        setMainDetailsFragment();
    }

    private void setMainDetailsFragment() {
        if (mainDetailsFragment == null) {
            mainDetailsFragment = MainDetailsFragment.createInstance(trainInfo);
            inflateFragment(mainDetailsContainer, mainDetailsFragment, MainDetailsFragment.TAG);
        }
    }

    private void setTracksFragment(Train train) {
        if (tracksFragment == null) {
            tracksFragment = TracksFragment.createInstance(train);
            inflateFragment(trackContainer, tracksFragment, TracksFragment.TAG);
        }
    }

    private void initUI() {
        trackContainer.setVisibility(View.GONE);
        setMainDetailsVisible(false);
        setTrackDetailsVisible(false);
    }

    private void setMainDetailsVisible(boolean b) {
        setContainerVisible(mainDetailsContainer, b);
    }

    private void setTrackDetailsVisible(boolean b) {
        setContainerVisible(trackContainer, b);
    }

    private void setContainerVisible(LinearLayout container, boolean b) {
        if (b) {
            container.setVisibility(View.VISIBLE);
            progressBarMainDetails.setVisibility(View.GONE);
        } else {
            container.setVisibility(View.GONE);
            progressBarMainDetails.setVisibility(View.VISIBLE);
        }
    }

    private void inflateFragment(LinearLayout container, Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(container.getId(), fragment, tag);
        fragmentTransaction.commit();
    }

    private void cleanUI(String msg) {
        progressBarMainDetails.setVisibility(View.GONE);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCompletedDetails(Train train) {
        setMainDetailsVisible(true);
        setTrackDetailsVisible(false);
        setTracksFragment(train);
    }

    @Override
    public void onErrorDetails(String msg) {
        cleanUI(msg);
    }

    @Override
    public void onCompletedTracks() {
        setTrackDetailsVisible(true);
    }

    @Override
    public void onErrorTracks(String msg) {
        cleanUI(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuRefresh:
                refresh();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refresh() {
        mainDetailsFragment = null;
        tracksFragment = null;
        init();
    }
}
