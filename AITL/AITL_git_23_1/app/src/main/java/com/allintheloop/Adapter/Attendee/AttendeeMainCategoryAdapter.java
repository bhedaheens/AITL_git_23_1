package com.allintheloop.Adapter.Attendee;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.AttendeeCategoryList;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AttendeeMainCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    List<AttendeeCategoryList.AttendeeCategory> attandeeGroupList;
    List<AttendeeCategoryList.AttendeeCategory> attandeeGroupListFiltered;
    Context context;
    SessionManager sessionManager;


    public AttendeeMainCategoryAdapter(List<AttendeeCategoryList.AttendeeCategory> attandeeGroupList, Context context) {
        this.context = context;
        this.attandeeGroupList = attandeeGroupList;
        this.attandeeGroupListFiltered = new ArrayList<>();
        this.attandeeGroupListFiltered.addAll(attandeeGroupList);
        sessionManager = new SessionManager(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attandeegroup, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        AttendeeCategoryList.AttendeeCategory attendeeCategory = attandeeGroupListFiltered.get(position);

        viewHolder.groupName.setText(attendeeCategory.getCategory());


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.rative_main.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
        }

        if (position % 2 == 0) {
            viewHolder.app_back.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            viewHolder.app_back.setCardBackgroundColor(context.getResources().getColor(R.color.ViewColor));
        }

        viewHolder.rative_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sessionManager.setAttendeeMainCategoryData(attendeeCategory.getId());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragment;
                        ((MainActivity) context).loadFragment();
                    }
                }, 300);
            }
        });

    }

    @Override
    public int getItemCount() {
        return attandeeGroupListFiltered.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    attandeeGroupListFiltered = attandeeGroupList;
                } else {
                    List<AttendeeCategoryList.AttendeeCategory> filteredList = new ArrayList<>();
                    for (AttendeeCategoryList.AttendeeCategory row : attandeeGroupList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCategory().toLowerCase().toString().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    attandeeGroupListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = attandeeGroupListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                attandeeGroupListFiltered = (ArrayList<AttendeeCategoryList.AttendeeCategory>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        RelativeLayout rative_main;
        CardView app_back;

        public ViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.group_name);
            rative_main = (RelativeLayout) itemView.findViewById(R.id.rative_main);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
        }
    }

}
