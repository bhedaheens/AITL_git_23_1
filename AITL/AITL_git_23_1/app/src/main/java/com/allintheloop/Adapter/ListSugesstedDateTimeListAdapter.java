package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.Bean.RequestMeeting.ViewRequestMettigDateTime;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 13/1/17.
 */

public class ListSugesstedDateTimeListAdapter extends RecyclerView.Adapter<ListSugesstedDateTimeListAdapter.ViewHolder> {
    ArrayList<ViewRequestMettigDateTime> dateTimeArrayList;
    Context context;

    public ListSugesstedDateTimeListAdapter(ArrayList<ViewRequestMettigDateTime> dateTimeArrayList, Context context) {
        this.dateTimeArrayList = dateTimeArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewrequesttimelist_adapter, parent, false);
        return new ListSugesstedDateTimeListAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViewRequestMettigDateTime obj = dateTimeArrayList.get(position);
        holder.txt_heading.setText(obj.getHeading());
        holder.txt_dateTime.setText(obj.getTime());


    }

    @Override
    public int getItemCount() {
        return dateTimeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_heading, txt_dateTime;
        CardView app_back;

        public ViewHolder(View itemView) {
            super(itemView);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
            txt_heading = (TextView) itemView.findViewById(R.id.txt_heading);
            txt_dateTime = (TextView) itemView.findViewById(R.id.txt_dateTime);

        }
    }
}
