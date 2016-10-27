package pl.alek.android.passenger.ui.util;

/**
 * Created by Lenovo on 27.10.2016.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
