package pl.alek.android.passenger.ui.details;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Track;
import pl.alek.android.passenger.model.Tracks;
import pl.alek.android.passenger.model.Train;
import pl.alek.android.passenger.online.PassengerReqVerToken;
import pl.alek.android.passenger.rest.manager.TracksManager;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 11.11.2016.
 */

public class TracksFragment extends Fragment implements PassengerReqVerToken.OnDownloadRequestTokenListener {

    protected static final String TAG = "TracksFragment";

    interface TracksCallback {
        void onCompletedTracks();
        void onErrorTracks(String msg);
    }

    @Bind(R.id.rvTracks)
    RecyclerView rvTracks;

    private TrackAdapter mAdapter;
    private PassengerReqVerToken passengerReqVerToken;
    private TracksCallback mCallback;
    private Subscription subscription;
    private Train train;
    private ArrayList<Track> trackList;

    public static TracksFragment createInstance(Train train) {
        TracksFragment fragment = new TracksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Train.TAG, train);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tracks, container, false);
        ButterKnife.bind(this, v);
        setRetainInstance(true);

        rvTracks.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvTracks.setLayoutManager(mLayoutManager);

        if (savedInstanceState == null) {
            passengerReqVerToken = PassengerReqVerToken.getInstance(this);
            train = (Train) getArguments().getSerializable(Train.TAG);
            passengerReqVerToken.setReqVerToken();
        } else {
            trackList = (ArrayList<Track>) savedInstanceState.getSerializable(Track.TRACK_LIST);
            setUI();
        }
        return v;
    }

    private void setUI() {
        if (trackList != null) {
            mAdapter = new TrackAdapter(getContext(), trackList);
            rvTracks.setAdapter(mAdapter);
        }
    }

    @Override
    public void onSuccess() {
        sendRequest();
    }

    @Override
    public void onError(String msg) {
        mCallback.onErrorTracks(msg);
        Log.e(TAG, msg);
    }

    private void sendRequest() {
        TracksManager tracksManager = new TracksManager();
        subscription = tracksManager.getTracks(train)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Tracks>() {
                    @Override
                    public void onCompleted() {
                        mCallback.onCompletedTracks();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onErrorTracks(e.getLocalizedMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(Tracks tracks) {
                        trackList = tracks.res;
                        setUI();
                    }
                });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (TracksCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
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
        outState.putSerializable(Track.TRACK_LIST, trackList);
    }
}
