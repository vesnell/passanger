package pl.alek.android.passenger.database;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import pl.alek.android.passenger.database.model.StationUsed;
import pl.alek.android.passenger.model.Station;

/**
 * Created by Lenovo on 27.10.2016.
 */

public class RealmController {
    private static RealmController instance;
    private Realm realm;

    public RealmController(Application application) {
        realm = Realm.getInstance(application);
    }

    public static RealmController with(Fragment fragment) {
        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {
        return instance;
    }

    public Realm getRealm() {
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public void refresh() {
        realm.refresh();
    }

    public ArrayList<StationUsed> getStationsUsed() {
        return new ArrayList<>(realm.where(StationUsed.class).findAll());
    }

    public void saveStation(final Station station) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (!isExists(station.ID)) {
                    int order = getStationsUsed().size();
                    StationUsed stationUsed = new StationUsed(station, order);
                    realm.copyToRealm(stationUsed);
                }
            }
        });
    }

    public void removeStation(final StationUsed stationUsed) {
        final int order = stationUsed.getOrder();
        realm.beginTransaction();
        RealmResults<StationUsed> rows = realm.where(StationUsed.class).equalTo("id", stationUsed.getId()).findAll();
        rows.clear();
        realm.commitTransaction();
        updateStationOrders(order);
    }

    private void updateStationOrders(final int order) {
        for (int i = order + 1; i <= getStationsUsed().size(); i++) {
            updateStationOrder(i, i - 1);
        }
    }

    private void updateStationOrder(int fromOrder, int toOrder) {
        realm.beginTransaction();
        StationUsed stationUsed = getStationUsedByOrder(fromOrder);
        stationUsed.setOrder(toOrder);
        realm.copyToRealmOrUpdate(stationUsed);
        realm.commitTransaction();
    }

    private StationUsed getStationUsedByOrder(int order) {
        return realm.where(StationUsed.class).equalTo("order", order).findFirst();
    }

    public void swapStations(int order1, int order2) {
        realm.beginTransaction();
        StationUsed stationUsed1 = getStationUsedByOrder(order1);
        StationUsed stationUsed2 = getStationUsedByOrder(order2);
        stationUsed1.setOrder(order2);
        stationUsed2.setOrder(order1);
        realm.copyToRealmOrUpdate(stationUsed1);
        realm.copyToRealmOrUpdate(stationUsed2);
        realm.commitTransaction();
    }

    private boolean isExists(int id){
        RealmQuery<StationUsed> query = realm.where(StationUsed.class).equalTo("id", id);
        return query.count() != 0;
    }

}
