package pl.alek.android.passanger.online.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Lenovo on 23.08.2016.
 */
public class HttpUtils {
    public static String getReqVerToken(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        String bodyHtml = responseBody.string();
        responseBody.close();
        Document doc = Jsoup.parse(bodyHtml);
        if (doc != null) {
            Element input = doc.select("input[name=__RequestVerificationToken]").first();
            return input.attr("value");
        }
        return null;
    }
}
