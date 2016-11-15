package pl.alek.android.passenger.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Track;

/**
 * Created by Lenovo on 11.11.2016.
 */

public class TracksFragment extends Fragment {

    @Bind(R.id.rvTracks)
    RecyclerView rvTracks;

    private TrackAdapter mAdapter;

    public static TracksFragment createInstance(ArrayList<Track> tracks) {
        TracksFragment fragment = new TracksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Track.TRACK_LIST, tracks);
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

        ArrayList<Track> tracks = (ArrayList<Track>) getArguments().getSerializable(Track.TRACK_LIST);

        if (tracks != null) {
            mAdapter = new TrackAdapter(getContext(), tracks);
            rvTracks.setAdapter(mAdapter);
        }

        return v;
    }
}
