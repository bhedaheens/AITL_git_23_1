package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.Bean.TwitterHashTagList;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 15/5/17.
 */

public class TwitterHashTagListAdapter extends RecyclerView.Adapter<TwitterHashTagListAdapter.ViewHolder> {
    ArrayList<TwitterHashTagList> hashTagListArrayList;
    Context context;

    public TwitterHashTagListAdapter(Context context, ArrayList<TwitterHashTagList> hashTagListArrayList) {
        this.context = context;
        this.hashTagListArrayList = hashTagListArrayList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_twitterhashtag_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tag_name.setText(hashTagListArrayList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return hashTagListArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tag_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tag_name = (TextView) itemView.findViewById(R.id.tag_name);
        }
    }
}
