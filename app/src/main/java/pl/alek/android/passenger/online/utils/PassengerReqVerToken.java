package pl.alek.android.passenger.online.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import okhttp3.ResponseBody;
import pl.alek.android.passenger.online.PassengerInterface;
import pl.alek.android.passenger.online.service.ServiceGenerator;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class PassengerReqVerToken {

    public static String reqVerToken;

    public static void setReqVerToken(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        String bodyHtml = responseBody.string();
        responseBody.close();
        Document doc = Jsoup.parse(bodyHtml);
        if (doc != null) {
            Element input = doc.select("input[name=" + PassengerInterface.REQ_VER_TOK + "]").first();
            reqVerToken = input.attr("value");
        }
    }

    public static Map<String, Object> getStationInfoParams(Integer stationID) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(PassengerInterface.STATION_ID, stationID);
        params.put(PassengerInterface.DEPARTURES, true);
        params.put(PassengerInterface.AVAILABLE_KH, PassengerInterface.AVAILABLE_KH_VALUE);
        params.put(PassengerInterface.REQ_VER_TOK, reqVerToken);
        return params;
    }
}
