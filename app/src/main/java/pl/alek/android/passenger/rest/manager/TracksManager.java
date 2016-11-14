package pl.alek.android.passenger.rest.manager;

import java.io.IOException;

import pl.alek.android.passenger.model.Tracks;
import pl.alek.android.passenger.model.Train;
import pl.alek.android.passenger.rest.RestAPI;
import pl.alek.android.passenger.rest.api.TrackAPI;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;

/**
 * Created by Lenovo on 25.10.2016.
 */

public class TracksManager {
    private static RestAPI api = new RestAPI(TrackAPI.class);

    public Observable<Tracks> getTracks(final Train train) {
        return Observable.create( new OnSubscribe<Tracks>() {

            @Override
            public void call(Subscriber<? super Tracks> subscriber) {
                Call<Tracks> callResponse = api.getTracks(train);
                Response<Tracks> response = null;
                try {
                    response = callResponse.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null && response.isSuccessful()) {
                    Tracks tracks = response.body();
                    subscriber.onNext(tracks);
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
