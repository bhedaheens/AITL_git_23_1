package com.allintheloop.Adapter.Exhibitor;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_MyLeadData_Offline;
import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_MyLead_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nteam on 5/2/18.
 */

public class ExhibitorLead_MyLeadAdapterOffline extends RecyclerView.Adapter<ExhibitorLead_MyLeadAdapterOffline.ViewHolder> implements Filterable {


    ArrayList<ExhibitorLead_MyLeadData_Offline> exhibitorMyLeadList;
    ArrayList<ExhibitorLead_MyLeadData_Offline> tmp_exhibitorMyLeadList;
    Context context;
    SessionManager sessionManager;
    Exhibitor_MyLead_Fragment exhibitor_myLead_fragment;

    public ExhibitorLead_MyLeadAdapterOffline(ArrayList<ExhibitorLead_MyLeadData_Offline> exhibitorMyLeadList, Context context, Exhibitor_MyLead_Fragment exhibitor_myLead_fragment) {
        this.exhibitorMyLeadList = exhibitorMyLeadList;
        this.context = context;
        tmp_exhibitorMyLeadList = new ArrayList<>();
        tmp_exhibitorMyLeadList.addAll(exhibitorMyLeadList);
        sessionManager = new SessionManager(context);
        this.exhibitor_myLead_fragment = exhibitor_myLead_fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exhibitorlead_mylead, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExhibitorLead_MyLeadData_Offline myLeadData = (ExhibitorLead_MyLeadData_Offline) tmp_exhibitorMyLeadList.get(position);

        if (TextUtils.isEmpty(myLeadData.getFirstname()) && TextUtils.isEmpty(myLeadData.getLastname())) {
            holder.user_name.setText(myLeadData.getEmail());
        } else {
            holder.user_name.setText(myLeadData.getFirstname() + " " + myLeadData.getLastname());
        }
        if (myLeadData.getTitle() != null) {
            if (!(myLeadData.getTitle().equalsIgnoreCase(""))) {
                if (myLeadData.getCompanyName() != null) {
                    holder.user_desc.setVisibility(View.VISIBLE);
                    if (!(myLeadData.getCompanyName().equalsIgnoreCase(""))) {
                        holder.user_desc.setText(myLeadData.getTitle() + " at " + myLeadData.getCompanyName());
                    } else {
                        holder.user_desc.setText(myLeadData.getTitle());
                    }
                } else {
                    holder.user_desc.setVisibility(View.GONE);
                }
            } else {
                holder.user_desc.setVisibility(View.GONE);
            }
        } else {
            holder.user_desc.setVisibility(View.GONE);
        }

        holder.layout_relative.setOnClickListener(view -> {
            try {
                Log.e("MyLeadAdapterOffline", "data: " + myLeadData.toString());
                JSONObject jsonObject = new JSONObject(myLeadData.getData());
                exhibitor_myLead_fragment.openUserDailog(jsonObject, myLeadData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tmp_exhibitorMyLeadList.size();
    }

    @Override
    public Filter getFilter() {
        return new GtroupFilter(this, exhibitorMyLeadList);
    }


    private static class GtroupFilter extends Filter {
        private final ExhibitorLead_MyLeadAdapterOffline grouoQandAseesionListAdapter;
        ArrayList<ExhibitorLead_MyLeadData_Offline> exhibitorMyLeadList;
        ArrayList<ExhibitorLead_MyLeadData_Offline> tmp_exhibitorMyLeadList;

        public GtroupFilter(ExhibitorLead_MyLeadAdapterOffline grouoQandAseesionListAdapter, ArrayList<ExhibitorLead_MyLeadData_Offline> exhibitorMyLeadList) {
            this.grouoQandAseesionListAdapter = grouoQandAseesionListAdapter;
            this.exhibitorMyLeadList = exhibitorMyLeadList;
            tmp_exhibitorMyLeadList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final FilterResults results = new FilterResults();

            if (constraint.length() <= 0) {
                tmp_exhibitorMyLeadList.addAll(exhibitorMyLeadList);
            } else {
                final String filterString = constraint.toString().toLowerCase();

                for (ExhibitorLead_MyLeadData_Offline attenObj : exhibitorMyLeadList) {
                    String firstName = attenObj.getFirstname().toLowerCase();
                    String lastName = attenObj.getLastname().toLowerCase();
                    if (firstName.toLowerCase().contains(filterString.toLowerCase()) || lastName.toLowerCase().contains(filterString.toLowerCase())) {
                        tmp_exhibitorMyLeadList.add(attenObj);
                    }
                }
            }
            results.values = tmp_exhibitorMyLeadList;
            results.count = tmp_exhibitorMyLeadList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            grouoQandAseesionListAdapter.tmp_exhibitorMyLeadList.clear();
            grouoQandAseesionListAdapter.tmp_exhibitorMyLeadList.addAll((ArrayList<ExhibitorLead_MyLeadData_Offline>) results.values);
            grouoQandAseesionListAdapter.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name, user_desc;
        CardView app_back;
        RelativeLayout layout_relative;

        public ViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_desc = (TextView) itemView.findViewById(R.id.user_desc);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
            layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
        }
    }
}