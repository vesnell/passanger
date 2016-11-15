package pl.alek.android.passenger.ui.details;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.model.Track;

/**
 * Created by Lenovo on 15.11.2016.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Track> tracks = new ArrayList<Track>();

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.llTrackItem)
        LinearLayout llTrackItem;
        @Bind(R.id.timeDeparture)
        TextView timeDeparture;
        @Bind(R.id.timeDepartureDelay)
        TextView timeDepartureDelay;
        @Bind(R.id.stationName)
        TextView stationName;
        @Bind(R.id.timeArrival)
        TextView timeArrival;
        @Bind(R.id.timeArrivalDelay)
        TextView timeArrivalDelay;
        @Bind(R.id.tvPlatformTrack)
        TextView tvPlatformTrack;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public TrackAdapter(Context context, ArrayList<Track> tracks) {
        mContext = context;
        this.tracks = tracks;
    }

    @Override
    public TrackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track, parent, false);
        TrackAdapter.ViewHolder vh = new TrackAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(TrackAdapter.ViewHolder holder, int position) {
        int height = (int) mContext.getResources().getDimension(R.dimen.list_station_info_item_height);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, height);
        holder.llTrackItem.setLayoutParams(params);

        Track track = tracks.get(position);
        holder.stationName.setText(track.StacjaNazwa);
        setStrikeTextView(holder.stationName, track.isStationCanceled());
        setLeftItems(holder.llTrackItem, track.isLeftStation());
        String arrivalTime = track.getArrivalTime();
        holder.timeArrival.setText(arrivalTime == null ? "" : arrivalTime);
        String departureTime = track.getDepartureTime();
        holder.timeDeparture.setText(departureTime == null ? "" : departureTime);
        setPlannedHour(holder.timeArrival, holder.timeArrivalDelay, track.getArrivalRealTime());
        setPlannedHour(holder.timeDeparture, holder.timeDepartureDelay, track.getDepartureRealTime());
        setDelayedColor(holder.timeArrivalDelay, track.getArrivalDelay());
        setDelayedColor(holder.timeDepartureDelay, track.getDepartureDelay());
        setPlatformTrack(holder.tvPlatformTrack, track.getPlatformTrack());
    }

    private void setPlatformTrack(TextView tv, String platformTrack) {
        if (platformTrack != null) {
            tv.setVisibility(View.VISIBLE);
            tv.setText(platformTrack);
        } else {
            tv.setVisibility(View.GONE);
        }
    }

    private void setLeftItems(LinearLayout ll, boolean isLeftStation) {
        if (isLeftStation) {
            ll.setBackgroundResource(R.drawable.item_info_bg_left);
        } else {
            ll.setBackgroundResource(R.drawable.item_info_bg_not_left);
        }
    }

    private void setStrikeTextView(TextView tv, boolean isStrike) {
        if (isStrike) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
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

    @Override
    public int getItemCount() {
        return tracks.size();
    }
}
