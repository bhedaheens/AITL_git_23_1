package com.allintheloop.Adapter.Exhibitor;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_MyLeadData;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * Created by nteam on 8/9/17.
 */

public class ExhibitorLead_MyLeadAdapter extends RecyclerView.Adapter<ExhibitorLead_MyLeadAdapter.ViewHolder> {


    ArrayList<Object> exhibitorMyLeadList;
    Context context;
    SessionManager sessionManager;

    public ExhibitorLead_MyLeadAdapter(ArrayList<Object> exhibitorMyLeadList, Context context) {
        this.exhibitorMyLeadList = exhibitorMyLeadList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exhibitorlead_mylead, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExhibitorLead_MyLeadData myLeadData = (ExhibitorLead_MyLeadData) exhibitorMyLeadList.get(position);

        holder.user_name.setText(myLeadData.getFirstname() + " " + myLeadData.getLastname());

        if (!(myLeadData.getTitle().equalsIgnoreCase(""))) {
            holder.user_desc.setVisibility(View.VISIBLE);
            if (!(myLeadData.getCompanyName().equalsIgnoreCase(""))) {
                holder.user_desc.setText(myLeadData.getTitle() + " at " + myLeadData.getCompanyName());
            } else {
                holder.user_desc.setText(myLeadData.getTitle());
            }
        } else {
            holder.user_desc.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return exhibitorMyLeadList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, user_desc;
        CardView app_back;

        public ViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_desc = (TextView) itemView.findViewById(R.id.user_desc);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
        }
    }


    public void updateList(ArrayList<Object> activityInternalFeeds) {
        this.exhibitorMyLeadList = activityInternalFeeds;
        notifyItemChanged(0);
    }
}
