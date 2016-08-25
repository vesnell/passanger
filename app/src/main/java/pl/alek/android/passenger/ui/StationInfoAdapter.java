package pl.alek.android.passenger.ui;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.RailInfo;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationInfoAdapter extends RecyclerView.Adapter<StationInfoAdapter.ViewHolder> {

    private Context mContext;
    private List<RailInfo> mDataset = new ArrayList<RailInfo>();
    private List<String> mDelayCauses = new ArrayList<String>();

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
        @Bind(R.id.tvTrainNo)
        TextView tvTrainNo;
        @Bind(R.id.tvStartStation)
        TextView tvStartStation;
        @Bind(R.id.tvEndStation)
        TextView tvEndStation;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public StationInfoAdapter(Context context, List<RailInfo> myDataset, List<String> delayCauses) {
        mDataset = myDataset;
        mContext = context;
        mDelayCauses = delayCauses;
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
        String delayHourLabel = railInfo.getDelayedHourLabel(mContext);
        setPlannedHour(holder.tvPlannedHour, holder.tvDelayedHour, delayHourLabel);
        setVisiblePlannedHour(holder.tvPlannedHour, railInfo);
        setDelayedColor(holder.tvDelayedHour, railInfo.Opoznienie);
        final List<List<String>> delayCauses = railInfo.PrzyczynyUtrudnienia;
        setGoneBtn(holder.btnCauses, delayCauses.size() == 0);
        holder.tvPlatformTrack.setText(railInfo.getPlatformTrack());
        holder.tvTrainNo.setText(railInfo.getCarrier());
        holder.tvStartStation.setText(railInfo.RelacjaPoczatkowaNazwa);
        holder.tvEndStation.setText(railInfo.getEndStation());

        holder.btnCauses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dCause = "";
                for (List<String> cause : delayCauses) {
                    int nrOfCause = Integer.parseInt(cause.get(1));
                    dCause += mDelayCauses.get(nrOfCause) + "\n";
                }
                Toast.makeText(mContext, dCause, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setVisiblePlannedHour(TextView tvPlannedHour, RailInfo railInfo) {
        if (railInfo.isTrainCanceled() || railInfo.isTrainCanceledPartly()) {
            tvPlannedHour.setVisibility(View.GONE);
        } else {
            tvPlannedHour.setVisibility(View.VISIBLE);
        }
    }

    private void setPlannedHour(TextView tvPlannedHour, TextView tvDelayedHour, String delayHourLabel) {
        if (delayHourLabel != null) {
            tvPlannedHour.setPaintFlags(tvPlannedHour.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPlannedHour.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            tvDelayedHour.setText(delayHourLabel);
            tvDelayedHour.setVisibility(View.VISIBLE);
        } else {
            tvPlannedHour.setPaintFlags(tvPlannedHour.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            tvPlannedHour.setTextColor(ContextCompat.getColor(mContext, R.color.darkBlue));
            tvDelayedHour.setVisibility(View.GONE);
        }
    }

    private void setDelayedColor(TextView tvDelayed, int delay) {
        if (delay < 5) {
            tvDelayed.setTextColor(ContextCompat.getColor(mContext, R.color.darkBlue));
        } else if (delay >= 5 && delay < 15) {
            tvDelayed.setTextColor(ContextCompat.getColor(mContext, R.color.orange));
        } else {
            tvDelayed.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }
    }

    private void setGoneBtn(Button btn, boolean isGone) {
        if (isGone) {
            btn.setVisibility(View.GONE);
        } else {
            btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}