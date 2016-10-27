package pl.alek.android.passenger.database;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import io.realm.Realm;
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

    public void saveStation(Station station) {
        realm.beginTransaction();
        realm.copyToRealm(new StationUsed(station));
        realm.commitTransaction();
    }

}
