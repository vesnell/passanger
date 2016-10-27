package pl.alek.android.passenger.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;
import pl.alek.android.passenger.database.model.StationUsed;
import pl.alek.android.passenger.model.Station;

/**
 * Created by Lenovo on 22.08.2016.
 */
public class StationsListAdapter extends RecyclerView.Adapter<StationsListAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<?> mDataset = new ArrayList<>();
    OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.stationName)
        TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public StationsListAdapter(Context context, ArrayList<?> myDataset) {
        mDataset = myDataset;
        mContext = context;
    }

    @Override
    public StationsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_station, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Object o = mDataset.get(position);
        holder.mTextView.setText(getName(o));

        int height = (int) mContext.getResources().getDimension(R.dimen.list_station_item_height);
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, height);
        holder.mTextView.setLayoutParams(params);
    }

    private String getName(Object o) {
        String name = "";
        if (o instanceof Station) {
            name = ((Station) o).Nazwa;
        } else if (o instanceof StationUsed) {
            name = ((StationUsed) o).getName();
        }
        return name;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public ArrayList<?> getStationsList() {
        return mDataset;
    }
}
