package pl.alek.android.passenger.online.service.api;

import java.util.ArrayList;

import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.online.PassengerInterface;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 17.08.2016.
 */
public interface StationsAPI {
    @Headers({PassengerInterface.HEADER_REQUESTED_XML,
            PassengerInterface.HEADER_USER_AGENT_MOZILLA})
    @GET("/" + PassengerInterface.CONN_SEARCH + "/" + PassengerInterface.FILTER)
    Call<ArrayList<Station>> loadStations(
            @Query(value= PassengerInterface.INPUT_TEXT, encoded=true) String stationName
    );
}
