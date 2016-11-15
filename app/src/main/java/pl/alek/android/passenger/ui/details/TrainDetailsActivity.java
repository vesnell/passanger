package pl.alek.android.passenger.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Details;
import pl.alek.android.passenger.model.Track;
import pl.alek.android.passenger.model.Tracks;
import pl.alek.android.passenger.model.Train;
import pl.alek.android.passenger.model.TrainDetails;
import pl.alek.android.passenger.model.TrainInfo;
import pl.alek.android.passenger.online.PassengerReqVerToken;
import pl.alek.android.passenger.rest.manager.DetailsManager;
import pl.alek.android.passenger.rest.manager.TracksManager;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class TrainDetailsActivity extends AppCompatActivity implements PassengerReqVerToken.OnDownloadRequestTokenListener {

    private static final String TAG = "TrainDetailsActivity";

    @Bind(R.id.mainDetailsContainer)
    LinearLayout mainDetailsContainer;
    @Bind(R.id.trackContainer)
    LinearLayout trackContainer;
    @Bind(R.id.progressBarMainDetails)
    ProgressBar progressBarMainDetails;

    private Subscription subscription;

    private TrainInfo trainInfo;
    private PassengerReqVerToken passengerReqVerToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        trainInfo = (TrainInfo) bundle.getSerializable(TrainInfo.TAG);
        setTitle(trainInfo.getDetailsTitle());
        initUI();

        passengerReqVerToken = PassengerReqVerToken.getInstance(this);
        passengerReqVerToken.setReqVerToken();
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

    @Override
    public void onSuccess() {
        sendRequest();
    }

    @Override
    public void onError(String msg) {
        cleanUI(msg);
        Log.e(TAG, msg);
    }

    private void sendRequest() {
        final DetailsManager detailsManager = new DetailsManager();
        subscription = detailsManager.getDetails(trainInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Details>() {
                    @Override
                    public void onCompleted() {
                        setMainDetailsVisible(true);
                        getTracks(detailsManager.details.Dane.get(0).Pociagi.get(0));
                    }

                    @Override
                    public void onError(Throwable e) {
                        cleanUI(e.getLocalizedMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Details details) {
                        TrainDetails trainDetails = details.Dane.get(0);
                        inflateMainDetailsFragment(trainDetails);
                    }
                });
    }

    private void getTracks(Train train) {
        setTrackDetailsVisible(false);
        TracksManager tracksManager = new TracksManager();
        subscription = tracksManager.getTracks(train)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Tracks>() {
                    @Override
                    public void onCompleted() {
                        setTrackDetailsVisible(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        cleanUI(e.getLocalizedMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Tracks tracks) {
                        ArrayList<Track> trackList = tracks.res;
                        inflateTrackFragment(trackList);
                    }
                });
    }

    private void inflateMainDetailsFragment(TrainDetails trainDetails) {
        inflateFragment(mainDetailsContainer, MainDetailsFragment.createInstance(trainDetails));
    }

    private void inflateTrackFragment(ArrayList<Track> trackList) {
        inflateFragment(trackContainer, TracksFragment.createInstance(trackList));
    }

    private void inflateFragment(LinearLayout container, Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(container.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void cleanUI(String msg) {
        progressBarMainDetails.setVisibility(View.GONE);
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
