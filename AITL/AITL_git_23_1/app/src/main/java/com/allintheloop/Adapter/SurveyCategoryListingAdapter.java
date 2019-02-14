package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.SurveyCategoryListing;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 10/6/17.
 */

public class SurveyCategoryListingAdapter extends RecyclerView.Adapter<SurveyCategoryListingAdapter.ViewHolder> {
    ArrayList<SurveyCategoryListing> categoryListings;
    Context context;

    public SurveyCategoryListingAdapter(ArrayList<SurveyCategoryListing> categoryListings, Context context) {
        this.categoryListings = categoryListings;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_twitterhashtag_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tag_name.setText(categoryListings.get(position).getSurvey_name());

    }

    @Override
    public int getItemCount() {
        return categoryListings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag_name;
        ImageView right_arrow;

        public ViewHolder(View itemView) {
            super(itemView);
            tag_name = (TextView) itemView.findViewById(R.id.tag_name);
            right_arrow = (ImageView) itemView.findViewById(R.id.right_arrow);
        }
    }
}
