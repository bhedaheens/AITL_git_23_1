package com.allintheloop.Adapter.Exhibitor;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 29/6/17.
 */

public class ExhibitorCategoryListingAdapter extends RecyclerView.Adapter<ExhibitorCategoryListingAdapter.ViewHolder> {
    ArrayList<ExhibitorCategoryListing> categoryListings;
    Context context;
    SessionManager sessionManager;

    public ExhibitorCategoryListingAdapter(ArrayList<ExhibitorCategoryListing> categoryListings, Context context) {
        this.categoryListings = categoryListings;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exhibitorlist_category_listing, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ExhibitorCategoryListing gulfoodSectorObj = categoryListings.get(position);
        Glide.with(context).load(gulfoodSectorObj.getImg()).into(holder.img_sector);
        holder.txt_sectorName.setText(gulfoodSectorObj.getSector());


        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            holder.view_line.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
        } else {
            holder.view_line.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
        }

        if (gulfoodSectorObj.isSelected()) {
            holder.view_line.setVisibility(View.VISIBLE);
        } else {
            holder.view_line.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return categoryListings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_sector;
        TextView txt_sectorName;
        View view_line;
        LinearLayout linear_main;

        public ViewHolder(View itemView) {
            super(itemView);
            img_sector = (ImageView) itemView.findViewById(R.id.img_sector);
            txt_sectorName = itemView.findViewById(R.id.txt_sectorName);
            view_line = (View) itemView.findViewById(R.id.view_line);
            linear_main = (LinearLayout) itemView.findViewById(R.id.linear_main);

        }
    }
}