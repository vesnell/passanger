package pl.alek.android.passenger.ui.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Track;

/**
 * Created by Lenovo on 11.11.2016.
 */

public class TracksFragment extends Fragment {

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

        ArrayList<Track> tracks = (ArrayList<Track>) getArguments().getSerializable(Track.TRACK_LIST);
        Toast.makeText(getContext(), tracks.get(0).StacjaNazwa, Toast.LENGTH_LONG).show();
        return v;
    }
}
