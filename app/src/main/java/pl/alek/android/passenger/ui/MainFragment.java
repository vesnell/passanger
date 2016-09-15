package pl.alek.android.passenger.ui;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.online.exception.ConnectionFailureException;
import pl.alek.android.passenger.online.service.HttpCallback;
import pl.alek.android.passenger.online.service.ServiceGenerator;
import pl.alek.android.passenger.ui.util.AndroidUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Lenovo on 25.08.2016.
 */
public class MainFragment extends Fragment implements Callback<ArrayList<Station>> {

    private static final String TAG = "MainFragment";
    private static final int START_SEARCH_SIZE_TEXT = 3;
    private static final String IS_WAITING_FOR_RESP_KEY = "isWaitingForResp";

    @Bind(R.id.etStationSearch)
    EditText etStationSearch;
    @Bind(R.id.btnStationSearch)
    Button btnStationSearch;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private boolean isBtnEnabled = false;
    private boolean isWaitingForResponse = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        setRetainInstance(true);

        setBtnBgColor(false);
        etStationSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                enableSubmitIfReady(editable.length());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        //if on keyboard press ok
        etStationSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN) {
                    return true;
                }
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:
                        submit();
                        break;
                    case KeyEvent.KEYCODE_BACK:
                        getActivity().onBackPressed();
                        break;
                }
                return true;
            }
        });

        if (savedInstanceState != null) {
            isWaitingForResponse = savedInstanceState.getBoolean(IS_WAITING_FOR_RESP_KEY);
            setProgressBarVisible(isWaitingForResponse);
            enableSubmitIfReady(etStationSearch.getText().length());
        }

        return v;
    }

    private void enableSubmitIfReady(int editableLength) {
        boolean isReady = editableLength >= START_SEARCH_SIZE_TEXT;
        setBtnEnabled(isReady);
    }

    @OnClick(R.id.btnStationSearch)
    public void submit() {
        if (isBtnEnabled) {
            if (AndroidUtils.isNetworkAvailable(getContext())) {
                AndroidUtils.hideKeyboard(getActivity());
                setProgressBarVisible(true);
                String stationName = etStationSearch.getText().toString();
                sendRequest(stationName);
            } else {
                showAlertDialog(R.string.alert_msg_no_internet);
            }
        } else {
            showAlertDialog(R.string.alert_msg_search);
        }
    }

    private void showAlertDialog(int msg) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.alert_title)
                .setMessage(msg)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void sendRequest(String stationName) {
        HttpCallback httpCallback = new HttpCallback(this);
        httpCallback.setStationName(stationName);
        ServiceGenerator.sendRequest(httpCallback);
        isWaitingForResponse = true;
    }

    @Override
    public void onResponse(Call<ArrayList<Station>> call, Response<ArrayList<Station>> response) {
        isWaitingForResponse = false;
        setProgressBarVisible(false);
        setBtnEnabled(true);
        if (response.code() == 200) {
            ArrayList<Station> stations = response.body();
            if (stations.size() == 1) {
                Station station = stations.get(0);
                openStationInfoActivity(station);
            } else if (stations.size() > 0) {
                Collections.sort(stations);
                openStationsListActivity(stations);
            } else {
                Toast.makeText(getContext(), R.string.station_not_found, Toast.LENGTH_LONG).show();
            }
        } else {
            showAlertDialog(R.string.alert_msg_500);
        }
    }

    @Override
    public void onFailure(Call<ArrayList<Station>> call, Throwable t) {
        isWaitingForResponse = false;
        setProgressBarVisible(false);
        try {
            throw new ConnectionFailureException(t);
        } catch (ConnectionFailureException e) {
            Log.e(TAG, t.getMessage());
        }
    }

    private void setProgressBarVisible(boolean isVisible) {
        btnStationSearch.setBackgroundResource(R.drawable.btn_flat_selector);
        btnStationSearch.setEnabled(!isVisible);
        if (isVisible) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void openStationInfoActivity(Station station) {
        Intent i = new Intent(getContext(), StationInfoActivity.class);
        i.putExtra(Station.NAME, station);
        startActivity(i);
    }

    private void openStationsListActivity(ArrayList<Station> stations) {
        Intent intent = new Intent(getContext(), StationsListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Station.LIST, stations);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setBtnEnabled(boolean isEnabled) {
        if (isEnabled != isBtnEnabled) {
            isBtnEnabled = isEnabled;
            setBtnBgColor(isEnabled);
        }
    }

    private void setBtnBgColor(boolean isEnabled) {
        int red = ContextCompat.getColor(getContext(), R.color.red);
        int darkBlue = ContextCompat.getColor(getContext(), R.color.darkBlue);
        if (isEnabled) {
            setBgColor(red, darkBlue, btnStationSearch);
        } else {
            setBgColor(darkBlue, red, btnStationSearch);
        }
    }

    private void setBgColor(int colorFrom, int colorTo, final View view) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_WAITING_FOR_RESP_KEY, isWaitingForResponse);
        super.onSaveInstanceState(outState);
    }
}
