package pl.alek.android.passenger.rest.manager;

import java.io.IOException;
import java.util.ArrayList;

import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.rest.RestAPI;
import pl.alek.android.passenger.rest.api.StationsAPI;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

/**
 * Created by Lenovo on 25.10.2016.
 */

public class StationsManager {
    private static RestAPI api = new RestAPI(StationsAPI.class);

    public Observable<ArrayList<Station>> getStations(final String stationName) {
        return Observable.create( new OnSubscribe<ArrayList<Station>>() {

            @Override
            public void call(Subscriber<? super ArrayList<Station>> subscriber) {
                Call<ArrayList<Station>> callResponse = api.getStations(stationName);
                Response<ArrayList<Station>> response = null;
                try {
                    response = callResponse.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null && response.isSuccessful()) {
                    ArrayList<Station> stations = response.body();
                    subscriber.onNext(stations);
                    subscriber.onCompleted();
                } else {
                    if (response != null) {
                        subscriber.onError(new Throwable(response.message()));
                    } else {
                        subscriber.onError(new Throwable("response is null"));
                    }
                }
            }
        });
    }
}
