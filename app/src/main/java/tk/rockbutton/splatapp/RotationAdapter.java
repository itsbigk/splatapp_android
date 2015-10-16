package tk.rockbutton.splatapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RotationAdapter extends RecyclerView.Adapter<RotationAdapter.ViewHolder> {
    private MapRotation.ScheduleItem[] dataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View cardView;

        public ViewHolder(View v) {
            super(v);
            cardView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RotationAdapter(MapRotation.ScheduleItem[] _dataset) {
        dataset = _dataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RotationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        //TextView v=null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rotation_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        //TODO...
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView timetext = (TextView) holder.cardView.findViewById(R.id.rotcard_time);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getDefault());
        String starttime = dateFormatter.format(new Date(dataset[position].startTime));
        String endtime = dateFormatter.format(new Date(dataset[position].endTime));

        timetext.setText(starttime + " - " + endtime);

        //Regular
        TextView map1name_regular = (TextView) holder.cardView.findViewById(R.id.rotcard_map1_mapName_regular);
        ImageView map1img_regular = (ImageView) holder.cardView.findViewById(R.id.rotcard_map1_img_regular);
        TextView map2name_regular = (TextView) holder.cardView.findViewById(R.id.rotcard_map2_mapName_regular);
        ImageView map2img_regular = (ImageView) holder.cardView.findViewById(R.id.rotcard_map2_img_regular);

        map1name_regular.setText(dataset[position].regular[0].nameEn);
        map1img_regular.setImageResource(getMapImage(dataset[position].regular[0].nameEn));
        map2name_regular.setText(dataset[position].regular[1].nameEn);
        map2img_regular.setImageResource(getMapImage(dataset[position].regular[1].nameEn));

        //Ranked
        TextView rankedrules = (TextView) holder.cardView.findViewById(R.id.rotcard_header_rules);
        TextView map1name_ranked = (TextView) holder.cardView.findViewById(R.id.rotcard_map1_mapName_ranked);
        ImageView map1img_ranked = (ImageView) holder.cardView.findViewById(R.id.rotcard_map1_img_ranked);
        TextView map2name_ranked = (TextView) holder.cardView.findViewById(R.id.rotcard_map2_mapName_ranked);
        ImageView map2img_ranked = (ImageView) holder.cardView.findViewById(R.id.rotcard_map2_img_ranked);

        rankedrules.setText(dataset[position].rankedRulesEn);
        map1name_ranked.setText(dataset[position].ranked[0].nameEn);
        map1img_ranked.setImageResource(getMapImage(dataset[position].ranked[0].nameEn));
        map2name_ranked.setText(dataset[position].ranked[1].nameEn);
        map2img_ranked.setImageResource(getMapImage(dataset[position].ranked[1].nameEn));
    }

    public static int getMapImage(String mapName) {
        switch (mapName) {
            case "Walleye Warehouse":
                return R.drawable.stage1;
            case "Flounder Heights":
                return R.drawable.stage2;
            case "Saltspray Rig":
                return R.drawable.stage3;
            case "Kelp Dome":
                return R.drawable.stage4;
            case "Arowana Mall":
                return R.drawable.stage5;
            case "Hammerhead Bridge":
                return R.drawable.stage6;
            case "Urchin Underpass":
                return R.drawable.stage7;
            case "Bluefin Depot":
                return R.drawable.stage8;
            case "Camp Triggerfish":
                return R.drawable.stage9;
            case "Blackbelly Skatepark":
                return R.drawable.stage10;
            case "Moray Towers":
                return R.drawable.stage11;
            case "Port Mackerel":
                return R.drawable.stage12;
        }

        return R.drawable.stage1;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (dataset == null) return 0;
        return dataset.length;
    }

    public void updateDataset(MapRotation.ScheduleItem[] _dataset) {
        dataset = _dataset;

        MainActivity.context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }
}