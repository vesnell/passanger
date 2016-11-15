package pl.alek.android.passenger.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Train;
import pl.alek.android.passenger.model.TrainDetails;

/**
 * Created by Lenovo on 11.11.2016.
 */

public class MainDetailsFragment extends Fragment {

    @Bind(R.id.tvStartStation)
    TextView tvStartStation;
    @Bind(R.id.tvEndStation)
    TextView tvEndStation;
    @Bind(R.id.tvPlatformTrack)
    TextView tvPlatformTrack;
    @Bind(R.id.tvTrainNo)
    TextView tvTrainNo;

    public static MainDetailsFragment createInstance(TrainDetails trainDetails) {
        MainDetailsFragment fragment = new MainDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TrainDetails.TAG, trainDetails);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main_details, container, false);
        ButterKnife.bind(this, v);
        setRetainInstance(true);

        TrainDetails trainDetails = (TrainDetails) getArguments().getSerializable(TrainDetails.TAG);
        if (trainDetails != null) {
            Train train = trainDetails.Pociagi.get(0);
            tvStartStation.setText(train.RelacjaPoczatkowa);
            tvEndStation.setText(train.getEndStation());
            tvPlatformTrack.setText(train.getStartPlatformTrack());
            tvTrainNo.setText(train.getCarrier());
        }

        return v;
    }
}
