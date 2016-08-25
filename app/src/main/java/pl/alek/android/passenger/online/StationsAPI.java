package pl.alek.android.passenger.online;

import java.util.ArrayList;

import pl.alek.android.passenger.model.Station;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 17.08.2016.
 */
public interface StationsAPI {
    @Headers({"X-Requested-With:XMLHttpRequest",
            "User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/51.0.2704.79 Chrome/51.0.2704.79 Safari/537.36"})
    @GET("/{search}/{filter}")
    Call<ArrayList<Station>> loadStations(
            @Path("search") String search,
            @Path("filter") String filter,
            @Query(value=PassengerInterface.INPUT_TEXT, encoded=true) String stationName
    );
}
