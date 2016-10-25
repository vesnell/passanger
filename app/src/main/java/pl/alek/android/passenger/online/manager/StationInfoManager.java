package pl.alek.android.passenger.online.manager;

import java.io.IOException;

import pl.alek.android.passenger.model.GeneralStationInfo;
import pl.alek.android.passenger.online.service.RestAPI;
import pl.alek.android.passenger.online.service.api.StationInfoAPI;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by Lenovo on 26.10.2016.
 */

public class StationInfoManager {
    private static RestAPI api = new RestAPI(StationInfoAPI.class);

    public Observable<GeneralStationInfo> getStationInfo(final Integer stationID) {
        return Observable.create( new Observable.OnSubscribe<GeneralStationInfo>() {
            @Override
            public void call(Subscriber<? super GeneralStationInfo> subscriber) {
                Call<GeneralStationInfo> callResponse = api.getStationInfo(stationID);
                Response<GeneralStationInfo> response = null;
                try {
                    response = callResponse.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (response != null && response.isSuccessful()) {
                    GeneralStationInfo generalStationInfo = response.body();
                    subscriber.onNext(generalStationInfo);
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
