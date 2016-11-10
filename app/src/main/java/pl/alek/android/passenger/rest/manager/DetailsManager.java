package pl.alek.android.passenger.rest.manager;

import java.io.IOException;

import pl.alek.android.passenger.model.Details;
import pl.alek.android.passenger.model.TrainInfo;
import pl.alek.android.passenger.rest.RestAPI;
import pl.alek.android.passenger.rest.api.DetailsAPI;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Lenovo on 10.11.2016.
 */

public class DetailsManager {
    private static RestAPI api = new RestAPI(DetailsAPI.class);

    public Observable<Details> getDetails(final TrainInfo trainInfo) {
        return Observable.create(new Observable.OnSubscribe<Details>() {
            @Override
            public void call(Subscriber<? super Details> subscriber) {
                Call<Details> callResponse = api.getDetails(trainInfo);
                Response<Details> response = null;
                try {
                    response = callResponse.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null && response.isSuccessful()) {
                    Details details = response.body();
                    subscriber.onNext(details);
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
