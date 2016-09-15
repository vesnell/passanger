package pl.alek.android.passenger;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lenovo on 15.09.2016.
 */
public class App extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }
}
