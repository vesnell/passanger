package pl.alek.android.passenger.online;

import android.app.Activity;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pl.alek.android.passenger.model.TrainInfo;
import pl.alek.android.passenger.rest.RestAPI;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class PassengerReqVerToken {

    private static final String TAG = "PassengerReqVerToken";

    private static final String TRACK_URL = RestAPI.API_BASE_URL + "/" + PassengerInterface.TRACK;

    public static String reqVerToken;

    private static PassengerReqVerToken instance;

    private OnDownloadRequestTokenListener listener;

    public static PassengerReqVerToken getInstance(OnDownloadRequestTokenListener listener) {
        if (instance == null) {
            return new PassengerReqVerToken(listener);
        }
        return instance;
    }

    private PassengerReqVerToken(OnDownloadRequestTokenListener listener) {
        this.listener = listener;
    }

    public void setReqVerToken(final Activity activity) {
        Request request = new Request.Builder()
                .url(TRACK_URL)
                .build();
        RestAPI.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                onErrorMsg(activity, e.getMessage(), e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                onOpenResponse(activity, response);
            }
        });
    }

    private void onOpenResponse(Activity activity, final Response response) {
        try {
            ResponseBody responseBody = response.body();
            String bodyHtml = responseBody.string();
            responseBody.close();
            parseBody(activity, bodyHtml);
        } catch (IOException e) {
            onErrorMsg(activity, e.getMessage(), e.getLocalizedMessage());
        }
    }

    private void parseBody(Activity activity, String bodyHtml) throws IOException {
        Document doc = Jsoup.parse(bodyHtml);
        if (doc != null) {
            Element input = doc.select("input[name=" + PassengerInterface.REQ_VER_TOK + "]").first();
            if (input != null) {
                reqVerToken = input.attr("value");
                onSuccessMsg(activity);
            } else {
                onErrorMsg(activity, "Empty body", "Could not get html body of Portal Pasażera");
            }
        } else {
            onErrorMsg(activity, "Empty body", "Could not get html body of Portal Pasażera");
        }
    }

    private void onSuccessMsg(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onSuccess();
            }
        });
    }

    private void onErrorMsg(Activity activity, String msg, final String locMsg) {
        Log.e(TAG, msg);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listener.onError(locMsg);
            }
        });
    }

    public static Map<String, Object> getStationInfoParams(Integer stationID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PassengerInterface.STATION_ID, stationID);
        params.put(PassengerInterface.DEPARTURES, true);
        params.put(PassengerInterface.AVAILABLE_KH, PassengerInterface.AVAILABLE_KH_VALUE);
        params.put(PassengerInterface.REQ_VER_TOK, reqVerToken);
        return params;
    }

    public static Map<String, Object> getTrainDeatilsInfoParams(TrainInfo trainInfo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PassengerInterface.SCHEDULE_ID, trainInfo.RozkladID);
        params.put(PassengerInterface.SKRJID_ORDER, trainInfo.ZamowienieSKRJID);
        params.put(PassengerInterface.PLANNED_DATE, trainInfo.DataPlanowa);
        params.put(PassengerInterface.START_STATION_ID, trainInfo.StacjaPoczatkowaID);
        params.put(PassengerInterface.END_STATION_ID, trainInfo.StacjaKoncowaID);
        params.put(PassengerInterface.DEPARTURES, true);
        params.put(PassengerInterface.AVAILABLE_KH, PassengerInterface.AVAILABLE_KH_VALUE);
        params.put(PassengerInterface.REQ_VER_TOK, reqVerToken);
        return params;
    }

    public interface OnDownloadRequestTokenListener {
        void onSuccess();
        void onError(String msg);
    }
}
