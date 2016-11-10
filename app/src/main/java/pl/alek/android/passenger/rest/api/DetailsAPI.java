package pl.alek.android.passenger.rest.api;

import java.util.Map;

import pl.alek.android.passenger.model.Details;
import pl.alek.android.passenger.online.PassengerInterface;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Lenovo on 10.11.2016.
 */

public interface DetailsAPI {
    @Headers({PassengerInterface.HEADER_REQUESTED_XML,
            PassengerInterface.HEADER_ACCEPT_JSON,
            PassengerInterface.HEADER_USER_AGENT_MOZILLA})
    @POST("/" + PassengerInterface.TRACK + "/" + PassengerInterface.DOWNLOAD_TRAIN_DETAILS)
    @FormUrlEncoded
    Call<Details> loadData(@FieldMap Map<String, Object> params);
}
