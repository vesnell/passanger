package pl.alek.android.passenger.database.util;

import java.util.Comparator;

import pl.alek.android.passenger.database.model.StationUsed;

/**
 * Created by Lenovo on 27.10.2016.
 */

public class StationUsedComparator implements Comparator<StationUsed> {

    @Override
    public int compare(StationUsed st1, StationUsed st2) {
        return st1.getOrder() - st2.getOrder();
    }
}
