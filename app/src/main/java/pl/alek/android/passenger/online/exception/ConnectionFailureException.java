package pl.alek.android.passenger.online.exception;

/**
 * Created by Lenovo on 26.08.2016.
 */
public class ConnectionFailureException extends Exception {
    public ConnectionFailureException(Throwable t) {
        super(t);
    }

    public ConnectionFailureException(String msg) {
        super(msg);
    }
}
