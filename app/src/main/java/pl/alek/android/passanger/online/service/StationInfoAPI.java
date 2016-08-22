package pl.alek.android.passanger.online.service;

import java.util.Map;

import pl.alek.android.passanger.model.ServerInfoResponse;
import pl.alek.android.passanger.online.PassengerInterface;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Lenovo on 22.08.2016.
 */
public interface StationInfoAPI {
    @Headers({"X-Requested-With:XMLHttpRequest",
            "Accept:application/json, text/javascript, */*; q=0.01",
            "User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/51.0.2704.79 Chrome/51.0.2704.79 Safari/537.36"})
    @POST("/" + PassengerInterface.TRACK + "/" + PassengerInterface.DOWNLOAD)
    @FormUrlEncoded
    Call<ServerInfoResponse> loadData(
            @FieldMap Map<String, Object> params);
}
