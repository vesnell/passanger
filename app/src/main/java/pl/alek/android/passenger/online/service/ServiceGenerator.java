package pl.alek.android.passenger.online.service;

import android.content.Context;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.alek.android.passenger.online.PassengerInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lenovo on 17.08.2016.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "https://portalpasazera.pl";
    private static final String TRACK_URL = API_BASE_URL + "/" + PassengerInterface.TRACK;

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    public static ClearableCookieJar cookie;
    public static OkHttpClient client;
    public static String reqVerToken;

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(serviceClass);
    }

    public static void sendRequest(Context ctx, Callback callback) {
        if (client == null) {
            setClient(ctx);
        }
        Request request = new Request.Builder()
                .url(TRACK_URL)
                .build();
        client.newCall(request).enqueue(callback);
    }

    private static OkHttpClient setClient(Context ctx) {
        instantCookie(ctx);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder
                .cookieJar(cookie)
                .addInterceptor(addLog());
        client = builder.build();
        return client;
    }

    private static ClearableCookieJar instantCookie(Context ctx) {
        cookie = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(ctx));
        return cookie;
    }

    private static HttpLoggingInterceptor addLog(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }
}
