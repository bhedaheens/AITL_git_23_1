package com.allintheloop.Adapter.Agenda;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.Bean.AgendaData.AgendaBookStatus;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * Created by nteam on 17/8/16.
 */
public class AgendaBookStatusAdapter extends RecyclerView.Adapter<AgendaBookStatusAdapter.ViewHolder> {
    ArrayList<AgendaBookStatus> bookStatusArrayList;
    Context context;

    public AgendaBookStatusAdapter(ArrayList<AgendaBookStatus> bookStatusArrayList, Context context) {
        this.bookStatusArrayList = bookStatusArrayList;
        this.context = context;
    }

    @Override
    public AgendaBookStatusAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_agendabookstatus_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgendaBookStatusAdapter.ViewHolder holder, int position) {
        AgendaBookStatus agendabookStatus = bookStatusArrayList.get(position);


        if (!(agendabookStatus.getHeading().equalsIgnoreCase(""))) {
            holder.txt_name.setText(agendabookStatus.getHeading().replaceAll("", ""));
        }

        holder.txt_time.setText(agendabookStatus.getStart_time());
        holder.txt_placesleft.setText(agendabookStatus.getResion());
        if (agendabookStatus.getBook().equalsIgnoreCase("1")) {
            holder.txt_status.setText(context.getResources().getString(R.string.txtYes));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.white));
            holder.txt_status.setBackgroundColor(context.getResources().getColor(R.color.green));
        } else {
            holder.txt_status.setText(context.getResources().getString(R.string.txtNo));
            holder.txt_status.setTextColor(context.getResources().getColor(R.color.white));
            holder.txt_status.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        if (position % 2 == 0) {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.card_back));
        } else {
            holder.card_order.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return bookStatusArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_time, txt_status, txt_placesleft;
        CardView card_order;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_status = (TextView) itemView.findViewById(R.id.txt_status);
            txt_placesleft = (TextView) itemView.findViewById(R.id.txt_placesleft);
            card_order = (CardView) itemView.findViewById(R.id.card_order);
        }
    }
}
