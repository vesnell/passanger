package pl.alek.android.passenger.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Details;
import pl.alek.android.passenger.model.Train;
import pl.alek.android.passenger.model.TrainDetails;
import pl.alek.android.passenger.model.TrainInfo;
import pl.alek.android.passenger.online.PassengerReqVerToken;
import pl.alek.android.passenger.rest.manager.DetailsManager;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 11.11.2016.
 */

public class MainDetailsFragment extends Fragment implements PassengerReqVerToken.OnDownloadRequestTokenListener {

    protected static final String TAG = "MainDetailsFragment";

    interface DetailsCallback {
        void onCompletedDetails(Train train);
        void onErrorDetails(String msg);
    }

    @Bind(R.id.tvStartStation)
    TextView tvStartStation;
    @Bind(R.id.tvEndStation)
    TextView tvEndStation;
    @Bind(R.id.tvPlatformTrack)
    TextView tvPlatformTrack;
    @Bind(R.id.tvTrainNo)
    TextView tvTrainNo;

    private TrainInfo trainInfo;
    private PassengerReqVerToken passengerReqVerToken;
    private DetailsCallback mCallback;
    private Subscription subscription;
    private TrainDetails trainDetails;

    public static MainDetailsFragment createInstance(TrainInfo trainInfo) {
        MainDetailsFragment fragment = new MainDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TrainInfo.TAG, trainInfo);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_details, container, false);
        ButterKnife.bind(this, v);
        setRetainInstance(true);

        if (savedInstanceState == null) {
            passengerReqVerToken = PassengerReqVerToken.getInstance(this);
            trainInfo = (TrainInfo) getArguments().getSerializable(TrainInfo.TAG);
            passengerReqVerToken.setReqVerToken(getActivity());
        } else {
            trainDetails = (TrainDetails) savedInstanceState.getSerializable(TrainDetails.TAG);
            setUI();
        }
        return v;
    }

    private void setUI() {
        if (trainDetails != null) {
            Train train = trainDetails.Pociagi.get(0);
            tvStartStation.setText(train.RelacjaPoczatkowa);
            tvEndStation.setText(train.getEndStation());
            tvPlatformTrack.setText(train.getStartPlatformTrack());
            tvTrainNo.setText(train.getCarrier());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (DetailsCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onSuccess() {
        sendRequest();
    }

    @Override
    public void onError(String msg) {
        mCallback.onErrorDetails(msg);
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
                        Train train = detailsManager.details.Dane.get(0).Pociagi.get(0);
                        mCallback.onCompletedDetails(train);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onErrorDetails(e.getLocalizedMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Details details) {
                        trainDetails = details.Dane.get(0);
                        setUI();
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(TrainDetails.TAG, trainDetails);
    }
}
