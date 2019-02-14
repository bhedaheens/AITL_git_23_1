package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.Bean.GamificationPointListing;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 19/6/17.
 */

public class GamificationPointListAdapter extends RecyclerView.Adapter<GamificationPointListAdapter.ViewHolder> {
    ArrayList<GamificationPointListing> pointListingArrayList;
    Context context;

    public GamificationPointListAdapter(ArrayList<GamificationPointListing> pointListingArrayList, Context context) {
        this.pointListingArrayList = pointListingArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gamification_pointlisting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GamificationPointListing pointListing = pointListingArrayList.get(position);
        holder.txt_point.setText(pointListing.getPoint());
        holder.txt_name.setText(pointListing.getRank());
    }

    @Override
    public int getItemCount() {
        return pointListingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_point, txt_name;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_point = (TextView) itemView.findViewById(R.id.txt_point);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
        }
    }
}
