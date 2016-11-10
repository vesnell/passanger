package pl.alek.android.passenger.online;

/**
 * Created by Lenovo on 21.08.2016.
 */
public interface PassengerInterface {
    String TRACK = "Trasa";
    String CONN_SEARCH = "WyszukiwaniePolaczen";
    String FILTER = "StacjeFiltrRead";
    String INPUT_TEXT = "wprowadzonyTekst";
    String DOWNLOAD = "Pobierz";
    String DOWNLOAD_TRAIN_DETAILS = "PobierzSzczegolyPociagu";
    String REQ_VER_TOK = "__RequestVerificationToken";

    String STATION_ID = "stacjaID";
    String DEPARTURES = "odjazdy";
    String AVAILABLE_KH = "dostepneKH";
    String AVAILABLE_KH_VALUE = "IC,IC;R,PR;EIC,IC;TLK,IC;EIP,IC;Os,KS;Os,KM;IR,PR;";

    String HEADER_REQUESTED_XML = "X-Requested-With:XMLHttpRequest";
    String HEADER_USER_AGENT_MOZILLA = "User-Agent:Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/51.0.2704.79 Chrome/51.0.2704.79 Safari/537.36";
    String HEADER_ACCEPT_JSON = "Accept:application/json, text/javascript, */*; q=0.01";

    String SCHEDULE_ID = "RozkladID";
    String SKRJID_ORDER = "ZamowienieSKRJID";
    String PLANNED_DATE = "DataPlanowa";
    String START_STATION_ID = "StacjaPoczatkowaID";
    String END_STATION_ID = "StacjaKoncowaID";
}
