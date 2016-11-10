package pl.alek.android.passenger.ui.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Details;
import pl.alek.android.passenger.model.TrainDetails;
import pl.alek.android.passenger.model.TrainInfo;
import pl.alek.android.passenger.online.PassengerReqVerToken;
import pl.alek.android.passenger.rest.manager.DetailsManager;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class TrainDetailsActivity extends AppCompatActivity implements PassengerReqVerToken.OnDownloadRequestTokenListener {

    private static final String TAG = "TrainDetailsActivity";

    @Bind(R.id.llMainDetails)
    LinearLayout llMainDetails;
    @Bind(R.id.tvTimeTravel)
    TextView tvTimeTravel;
    @Bind(R.id.tvStartStation)
    TextView tvStartStation;
    @Bind(R.id.tvEndStation)
    TextView tvEndStation;
    @Bind(R.id.tvPlatformTrack)
    TextView tvPlatformTrack;
    @Bind(R.id.tvTrainNo)
    TextView tvTrainNo;
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
        setMainDetailsVisible(false);
    }

    private void setMainDetailsVisible(boolean b) {
        if (b) {
            llMainDetails.setVisibility(View.VISIBLE);
            progressBarMainDetails.setVisibility(View.GONE);
        } else {
            llMainDetails.setVisibility(View.GONE);
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
        DetailsManager detailsManager = new DetailsManager();
        subscription = detailsManager.getDetails(trainInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Details>() {
                    @Override
                    public void onCompleted() {
                        setMainDetailsVisible(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        cleanUI(e.getLocalizedMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Details details) {
                        Toast.makeText(TrainDetailsActivity.this, Integer.toString(details.Dane.get(0).CzasPodrozy.Minutes), Toast.LENGTH_LONG).show();
                    }
                });
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
