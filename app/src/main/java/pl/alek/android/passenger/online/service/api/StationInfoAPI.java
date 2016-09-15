package pl.alek.android.passenger.online.service.api;

import java.util.Map;

import pl.alek.android.passenger.model.ServerInfoResponse;
import pl.alek.android.passenger.online.PassengerInterface;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Lenovo on 22.08.2016.
 */
public interface StationInfoAPI {
    @Headers({PassengerInterface.HEADER_REQUESTED_XML,
            PassengerInterface.HEADER_ACCEPT_JSON,
            PassengerInterface.HEADER_USER_AGENT_MOZILLA})
    @POST("/" + PassengerInterface.TRACK + "/" + PassengerInterface.DOWNLOAD)
    @FormUrlEncoded
    Call<ServerInfoResponse> loadData(
            @FieldMap Map<String, Object> params);
}
