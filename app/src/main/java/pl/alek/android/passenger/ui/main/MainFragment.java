package pl.alek.android.passenger.ui.main;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import pl.alek.android.passenger.database.RealmController;
import pl.alek.android.passenger.database.model.StationUsed;
import pl.alek.android.passenger.database.util.StationUsedComparator;
import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.rest.manager.StationsManager;
import pl.alek.android.passenger.online.PassengerReqVerToken;
import pl.alek.android.passenger.ui.util.PassengerViewInterface;
import pl.alek.android.passenger.ui.stationinfo.StationInfoActivity;
import pl.alek.android.passenger.ui.stationslist.StationsListActivity;
import pl.alek.android.passenger.ui.util.StationsListAdapter;
import pl.alek.android.passenger.ui.util.AndroidUtils;

import pl.alek.android.passenger.ui.util.SimpleItemTouchHelperCallback;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Lenovo on 25.08.2016.
 */
public class MainFragment extends Fragment implements PassengerViewInterface {

    private static final String TAG = "MainFragment";
    private static final int START_SEARCH_SIZE_TEXT = 3;
    private static final String IS_WAITING_FOR_RESP_KEY = "isWaitingForResp";

    @Bind(R.id.etStationSearch)
    EditText etStationSearch;
    @Bind(R.id.btnStationSearch)
    Button btnStationSearch;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.rvStationUsedList)
    RecyclerView rvStationUsedList;

    private Subscription subscription;

    private boolean isBtnEnabled = false;
    private boolean isWaitingForResponse = false;


    private StationsListAdapter mAdapter;
    private ArrayList<StationUsed> stations = new ArrayList<StationUsed>();

    private class MyTextWatcher implements TextWatcher {
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
    }

    private void enableSubmitIfReady(int editableLength) {
        boolean isReady = editableLength >= START_SEARCH_SIZE_TEXT;
        setBtnEnabled(isReady);
    }

    //if on keyboard press ok
    private class PressOKOnKeyListener implements View.OnKeyListener {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        setRetainInstance(true);

        setBtnBgColor(false);
        etStationSearch.addTextChangedListener(new MyTextWatcher());
        etStationSearch.setOnKeyListener(new PressOKOnKeyListener());

        setRvStationUsedList();

        if (savedInstanceState != null) {
            isWaitingForResponse = savedInstanceState.getBoolean(IS_WAITING_FOR_RESP_KEY);
            setProgressBarVisible(isWaitingForResponse);
            if (!isWaitingForResponse) {
                enableSubmitIfReady(etStationSearch.getText().length());
            } else {
                trySetReqVerToken();
            }
        }

        return v;
    }

    private void setRvStationUsedList() {
        rvStationUsedList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvStationUsedList.setLayoutManager(mLayoutManager);
        mAdapter = new StationsListAdapter(getContext(), stations);
        rvStationUsedList.setAdapter(mAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rvStationUsedList);
        mAdapter.setOnItemClickListener(new StationsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                StationUsed station = (StationUsed) mAdapter.getStationsList().get(position);
                openStationInfoActivity(StationUsed.parseToStation(station));
            }
        });
        mAdapter.setOnLastItemRemovedListener(new StationsListAdapter.OnLastItemRemovedListener() {
            @Override
            public void onLastItemRemoved() {
                rvStationUsedList.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateRvStationUsedList();
    }

    private void updateRvStationUsedList() {
        ArrayList<StationUsed> stations = RealmController.getInstance().getStationsUsed();
        Collections.sort(stations, new StationUsedComparator());
        mAdapter.updateItems(stations);
        setRvStationUsedListVisibility(stations);
    }

    private void setRvStationUsedListVisibility(ArrayList<StationUsed> stations) {
        if (stations.size() > 0) {
            rvStationUsedList.setVisibility(View.VISIBLE);
        } else {
            rvStationUsedList.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btnStationSearch)
    public void submit() {
        if (isBtnEnabled) {
            if (AndroidUtils.isNetworkAvailable(getContext())) {
                prepareSubmitBtnAction();
                trySetReqVerToken();
            } else {
                showAlertDialog(R.string.alert_msg_no_internet);
            }
        } else {
            showAlertDialog(R.string.alert_msg_search);
        }
    }

    @Override
    public void trySetReqVerToken() {
        isWaitingForResponse = true;
        new PassengerReqVerToken(getActivity(), new PassengerReqVerToken.OnDownloadRequestTokenListener() {
            @Override
            public void onSuccess() {
                String stationName = etStationSearch.getText().toString();
                sendRequest(stationName);
            }

            @Override
            public void onError(String msg) {
                cleanUI(msg);
                Log.e(TAG, msg);
            }
        }).setReqVerToken();
    }

    private void prepareSubmitBtnAction() {
        AndroidUtils.hideKeyboard(getActivity());
        setProgressBarVisible(true);
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
        StationsManager stationsManager = new StationsManager();
        subscription = stationsManager.getStations(stationName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Station>>() {
                    @Override
                    public void onCompleted() {
                        isWaitingForResponse = false;
                        setProgressBarVisible(false);
                        setBtnEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        cleanUI(e.getLocalizedMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Station> stations) {
                        if (stations.size() == 1) {
                            Station station = stations.get(0);
                            RealmController.getInstance().saveStation(station);
                            openStationInfoActivity(station);
                        } else if (stations.size() > 1) {
                            Collections.sort(stations);
                            openStationsListActivity(stations);
                        } else {
                            Toast.makeText(getContext(), R.string.station_not_found, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void cleanUI(String msg) {
        isWaitingForResponse = false;
        setProgressBarVisible(false);
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
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
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_WAITING_FOR_RESP_KEY, isWaitingForResponse);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
