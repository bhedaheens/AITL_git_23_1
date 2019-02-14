package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.R;
import com.allintheloop.Util.BeaconFoundDailog;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 18/4/17.
 */

public class Adapter_BeaconFoundDailog extends RecyclerView.Adapter<Adapter_BeaconFoundDailog.ViewHolder> {
    ArrayList<BeaconFoundDailog> beaconFoundDailogArrayList;
    Context context;


    public Adapter_BeaconFoundDailog(ArrayList<BeaconFoundDailog> beaconFoundDailogArrayList, Context context) {
        this.beaconFoundDailogArrayList = beaconFoundDailogArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_beaconfound_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BeaconFoundDailog beaconFoundDailogObj = beaconFoundDailogArrayList.get(position);

        if (beaconFoundDailogObj.getTag().equalsIgnoreCase("iBeacon")) {
            holder.txt_uuid.setText(beaconFoundDailogObj.getUuid());
            holder.txt_major.setText("Maj : " + beaconFoundDailogObj.getMajor());
            holder.txt_minor.setText("Min : " + beaconFoundDailogObj.getMinor());
        } else if (beaconFoundDailogObj.getTag().equalsIgnoreCase("eddyStone")) {
            holder.txt_uuid.setVisibility(View.GONE);
            holder.txt_minor.setText(beaconFoundDailogObj.getEdystoneInstance().toUpperCase());
            holder.txt_major.setText(beaconFoundDailogObj.getEdystoneNameSpace());
        }
    }

    @Override
    public int getItemCount() {
        return beaconFoundDailogArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_uuid, txt_major, txt_minor;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_uuid = (TextView) itemView.findViewById(R.id.txt_uuid);
            txt_major = (TextView) itemView.findViewById(R.id.txt_major);
            txt_minor = (TextView) itemView.findViewById(R.id.txt_minor);

        }
    }
}
