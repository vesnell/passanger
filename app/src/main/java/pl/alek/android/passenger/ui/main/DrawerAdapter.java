package pl.alek.android.passenger.ui.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.alek.android.passenger.R;

/**
 * Created by Lenovo on 25.08.2016.
 */
public class DrawerAdapter extends ArrayAdapter<String> {

        private final Context context;
        private String[] data;

        public DrawerAdapter(Context context, String[] data) {
            super(context, R.layout.drawer_list_item, data);
            this.context = context;
            this.data = data;
        }

        static class ViewHolder {
            @Bind(R.id.tvDrawerItem)
            TextView tvDrawerItem;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.drawer_list_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String item = data[position];
        if (item != null) {
            viewHolder.tvDrawerItem.setText(data[position]);
            viewHolder.tvDrawerItem.setTag(position);
        }
        return convertView;
    }

}
