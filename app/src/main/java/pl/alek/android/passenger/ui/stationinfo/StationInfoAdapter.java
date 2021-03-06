package pl.alek.android.passenger.ui.stationinfo;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.GeneralStationInfo;
import pl.alek.android.passenger.model.TrainInfo;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationInfoAdapter extends RecyclerView.Adapter<StationInfoAdapter.ViewHolder> {

    private Context mContext;
    private List<TrainInfo> mDataset = new ArrayList<TrainInfo>();
    private List<String> mDelayCauses = new ArrayList<String>();
    private GeneralStationInfo generalStationInfo;
    OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public StationInfoAdapter(Context context, GeneralStationInfo generalStationInfo) {
        mContext = context;
        init(generalStationInfo);
    }

    private void init(GeneralStationInfo generalStationInfo) {
        this.generalStationInfo = generalStationInfo;
        mDataset = generalStationInfo.Rozklad;
        mDelayCauses = generalStationInfo.Utrudnienia;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
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

        TrainInfo trainInfo = mDataset.get(position);
        setLeftItems(holder.llRailInfo, trainInfo.isLeftStation());
        holder.tvPlannedHour.setText(trainInfo.getPlannedHourLabel());
        String delayHourLabel = trainInfo.getDelayedHourLabel(mContext);
        setPlannedHour(holder.tvPlannedHour, holder.tvDelayedHour, delayHourLabel);
        setVisiblePlannedHour(holder.tvPlannedHour, trainInfo);
        setDelayedColor(holder.tvDelayedHour, trainInfo.Opoznienie);
        final List<List<String>> delayCauses = trainInfo.PrzyczynyUtrudnienia;
        setGoneBtn(holder.btnCauses, delayCauses.size() == 0);
        holder.tvPlatformTrack.setText(trainInfo.getPlatformTrack());
        holder.tvTrainNo.setText(trainInfo.getCarrier());
        holder.tvStartStation.setText(trainInfo.RelacjaPoczatkowaNazwa);
        holder.tvEndStation.setText(trainInfo.getEndStation());

        holder.btnCauses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlertDialogCauses(delayCauses);
            }
        });
    }

    private void setLeftItems(LinearLayout ll, boolean isLeftStation) {
        if (isLeftStation) {
            ll.setBackgroundResource(R.drawable.item_info_bg_left);
        } else {
            ll.setBackgroundResource(R.drawable.item_info_bg_not_left);
        }
    }

    private void setAlertDialogCauses(List<List<String>> delayCauses) {
        String causes = getMsg(delayCauses);
        new AlertDialog.Builder(mContext)
            .setTitle(R.string.alert_title)
            .setMessage(causes)
            .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    private String getMsg(List<List<String>> delayCauses) {
        String causes = "";
        List<String> infoCauses = new ArrayList<String>();
        int j = 0;
        for (int i = 0; i < delayCauses.size(); i++) {
            int nrOfCause = Integer.parseInt(delayCauses.get(i).get(1));
            String cause = mDelayCauses.get(nrOfCause);
            if (!infoCauses.contains(cause)) {
                causes += (i + 1 - j) + ". " + cause + "\n";
                if (i < delayCauses.size() - 1) {
                    causes += "\n";
                }
                infoCauses.add(cause);
            } else {
                j++;
            }
        }
        String lastChars = causes.substring(causes.length() - 2);
        if (lastChars.equals("\n\n")) {
            causes = causes.substring(0, causes.length() - 1);
        }
        return causes;
    }

    private void setVisiblePlannedHour(TextView tvPlannedHour, TrainInfo trainInfo) {
        if (trainInfo.isTrainCanceled() || trainInfo.isTrainCanceledPartly()) {
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
        if (mDataset != null) {
            return mDataset.size();
        }
        return 0;
    }

    public GeneralStationInfo getItems() {
        return generalStationInfo;
    }

    public TrainInfo getTrainInfoItem(int position) {
        return mDataset.get(position);
    }

    public void updateData(GeneralStationInfo generalStationInfo) {
        init(generalStationInfo);
        notifyDataSetChanged();
    }
}
