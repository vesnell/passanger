package pl.alek.android.passenger.rest.api;

import pl.alek.android.passenger.model.Tracks;
import pl.alek.android.passenger.online.PassengerInterface;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 14.11.2016.
 */

public interface TrackAPI {
    @Headers({PassengerInterface.HEADER_REQUESTED_XML,
            PassengerInterface.HEADER_ACCEPT_JSON,
            PassengerInterface.HEADER_USER_AGENT_MOZILLA})
    @GET("/" + PassengerInterface.TRACK + "/" + PassengerInterface.DOWNLOAD_TRACK + "?" + PassengerInterface.AVAILABLE_U + "=")
    Call<Tracks> loadData(
            @Query(value = PassengerInterface.CRITERIA, encoded = true) String criteria);
}
