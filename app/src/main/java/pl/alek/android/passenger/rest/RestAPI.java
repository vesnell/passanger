package pl.alek.android.passenger.rest;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.alek.android.passenger.App;
import pl.alek.android.passenger.model.Details;
import pl.alek.android.passenger.model.GeneralStationInfo;
import pl.alek.android.passenger.model.Station;
import pl.alek.android.passenger.model.TrainInfo;
import pl.alek.android.passenger.rest.api.DetailsAPI;
import pl.alek.android.passenger.rest.api.StationInfoAPI;
import pl.alek.android.passenger.rest.api.StationsAPI;
import pl.alek.android.passenger.online.PassengerReqVerToken;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lenovo on 17.08.2016.
 */
public class RestAPI {

    public static final String API_BASE_URL = "https://portalpasazera.pl";
    private static final long TIMEOUT_SEC = 10;

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private static ClearableCookieJar cookie = instantCookie();
    public static OkHttpClient client = setClient();

    private Object api;

    public RestAPI(Class<?> apiClass) {
        api = createService(apiClass);
    }

    private <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }

    private static OkHttpClient setClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder
                .connectTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SEC, TimeUnit.SECONDS)
                .cookieJar(cookie)
                .addInterceptor(addLog());
        return builder.build();
    }

    private static ClearableCookieJar instantCookie() {
        return new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(App.getAppContext()));
    }

    private static HttpLoggingInterceptor addLog(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    public Call<ArrayList<Station>> getStations(String stationName) {
        return ((StationsAPI) api).loadStations(stationName);
    }

    public Call<GeneralStationInfo> getStationInfo(Integer stationID) {
        Map<String, Object> params = PassengerReqVerToken.getStationInfoParams(stationID);
        return ((StationInfoAPI) api).loadData(params);
    }

    public Call<Details> getDetails(TrainInfo trainInfo) {
        Map<String, Object> params = PassengerReqVerToken.getTrainDeatilsInfoParams(trainInfo);
        return ((DetailsAPI) api).loadData(params);
    }
}
