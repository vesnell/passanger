package pl.alek.android.passenger.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import pl.alek.android.passenger.R;

/**
 * Created by Lenovo on 25.08.2016.
 */
public class AboutAppFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about_app, container, false);
        ButterKnife.bind(this, v);

        return v;
    }
}
