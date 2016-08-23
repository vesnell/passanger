package pl.alek.android.passanger.ui;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passanger.R;
import pl.alek.android.passanger.model.RailInfo;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationInfoAdapter extends RecyclerView.Adapter<StationInfoAdapter.ViewHolder> {

    private Context mContext;
    private List<RailInfo> mDataset = new ArrayList<RailInfo>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.llRailInfo)
        LinearLayout llRailInfo;
        @Bind(R.id.tvPlannedHour)
        TextView tvPlannedHour;
        @Bind(R.id.tvDelayedHour)
        TextView tvDelayedHour;
        @Bind(R.id.btnCauses)
        Button btnCauses;
        @Bind(R.id.tvPlatformTrack)
        TextView tvPlatformTrack;
        @Bind(R.id.tvCarrier)
        TextView tvCarrier;
        @Bind(R.id.tvTrainNo)
        TextView tvTrainNo;
        @Bind(R.id.tvRoute)
        TextView tvRoute;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public StationInfoAdapter(Context context, List<RailInfo> myDataset) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public StationInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station_info, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int height = (int) mContext.getResources().getDimension(R.dimen.list_station_info_item_height);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, height);
        holder.llRailInfo.setLayoutParams(params);

        RailInfo railInfo = mDataset.get(position);
        holder.tvPlannedHour.setText(railInfo.getPlannedHourLabel());
        String delayHourLabel = railInfo.getDelayedHourLabel();
        if (delayHourLabel != null) {
            holder.tvPlannedHour.setPaintFlags(holder.tvPlannedHour.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDelayedHour.setText(railInfo.getDelayedHourLabel());
            holder.tvDelayedHour.setVisibility(View.VISIBLE);
        } else {
            holder.tvPlannedHour.setPaintFlags(holder.tvPlannedHour.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            holder.tvDelayedHour.setVisibility(View.GONE);
        }
        List<List<String>> delayCauses = railInfo.PrzyczynyUtrudnienia;
        if (delayCauses.size() == 0) {
           holder.btnCauses.setVisibility(View.GONE);
        } else {
            holder.btnCauses.setVisibility(View.VISIBLE);
        }
        holder.tvPlatformTrack.setText(railInfo.getPlatformTrack());
        holder.tvCarrier.setText(railInfo.PrzewoznikSkrot);
        holder.tvTrainNo.setText(railInfo.NrPociagu);
        holder.tvRoute.setText(railInfo.getRoute());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
