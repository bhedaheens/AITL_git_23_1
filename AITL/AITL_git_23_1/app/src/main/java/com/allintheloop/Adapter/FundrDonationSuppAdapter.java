package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Fundraising.FundraisongdonationSupporter;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * Created by nteam on 15/9/16.
 */
public class FundrDonationSuppAdapter extends RecyclerView.Adapter<FundrDonationSuppAdapter.ViewHolder> {

    ArrayList<FundraisongdonationSupporter> supporterArrayList;
    Context context;
    String currency_status;
    SessionManager sessionManager;

    public FundrDonationSuppAdapter(ArrayList<FundraisongdonationSupporter> supporterArrayList, Context context, String currency_status) {
        this.supporterArrayList = supporterArrayList;
        this.context = context;
        this.currency_status = currency_status;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_fund_supporter, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        FundraisongdonationSupporter donationObj = supporterArrayList.get(position);
        if (!(donationObj.getAmount().equalsIgnoreCase(""))) {
            if (currency_status.equalsIgnoreCase("euro")) {
                holder.txt_price.setText(context.getResources().getString(R.string.euro) + donationObj.getAmount());
            } else if (currency_status.equalsIgnoreCase("gbp")) {
                holder.txt_price.setText(context.getResources().getString(R.string.pound_sign) + donationObj.getAmount());
            } else if (currency_status.equalsIgnoreCase("usd") || currency_status.equalsIgnoreCase("aud")) {
                holder.txt_price.setText(context.getResources().getString(R.string.dollor) + donationObj.getAmount());
            }
        }

        holder.txt_time.setText(donationObj.getTime());
        holder.txt_name.setText(donationObj.getName());
        holder.linear_side.setBackgroundColor(Color.parseColor(sessionManager.getFunThemeColor()));
    }

    @Override
    public int getItemCount() {
        return supporterArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_price, txt_time, txt_name;
        LinearLayout linear_side;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_price = (TextView) itemView.findViewById(R.id.txt_price);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            linear_side = (LinearLayout) itemView.findViewById(R.id.linear_side);
        }
    }
}
