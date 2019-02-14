package com.allintheloop.Adapter.Agenda;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.AgendaData.AgendaCategory;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by nteam on 30/11/17.
 */

public class AgendaCatListAdapter extends RecyclerView.Adapter<AgendaCatListAdapter.ViewHolder> implements Filterable {
    ArrayList<AgendaCategory> groupModuleDataArrayList;
    ArrayList<AgendaCategory> tmp_groupModuleDataArrayList;
    Context context;
    SessionManager sessionManager;

    public AgendaCatListAdapter(ArrayList<AgendaCategory> groupModuleDataArrayList, Context context) {
        this.groupModuleDataArrayList = groupModuleDataArrayList;
        this.context = context;
        tmp_groupModuleDataArrayList = new ArrayList<>();
        tmp_groupModuleDataArrayList.addAll(groupModuleDataArrayList);
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_group_list_fragment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        final AgendaCategory moduleDataObj = tmp_groupModuleDataArrayList.get(i);
        holder.group_name.setText(moduleDataObj.getCategoryName());
        Glide.with(context).load(moduleDataObj.getCategoryImage()).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                holder.img_group.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.img_group.setVisibility(View.VISIBLE);
                return false;
            }
        }).into(holder.img_group);


        holder.app_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.setAgendaCategoryId(moduleDataObj.getId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                ((MainActivity) context).loadFragment();
            }
        });

    }

    @Override
    public int getItemCount() {
        return tmp_groupModuleDataArrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new AgendaCatListAdapter.GtroupFilter(this, groupModuleDataArrayList);
    }

    private static class GtroupFilter extends Filter {
        private final AgendaCatListAdapter groupMapListAdapter;
        private final ArrayList<AgendaCategory> groupModuleDataArrayList;
        private final ArrayList<AgendaCategory> tmpgroupModuleDataArrayList;

        public GtroupFilter(AgendaCatListAdapter groupMapListAdapter, ArrayList<AgendaCategory> groupModuleDataArrayList) {
            this.groupMapListAdapter = groupMapListAdapter;
            this.groupModuleDataArrayList = groupModuleDataArrayList;
            tmpgroupModuleDataArrayList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            final FilterResults results = new FilterResults();

            if (constraint.length() <= 0) {
                tmpgroupModuleDataArrayList.addAll(groupModuleDataArrayList);
            } else {
                final String filterString = constraint.toString().toLowerCase();

                for (AgendaCategory attenObj : groupModuleDataArrayList) {
                    String title = attenObj.getCategoryName().toLowerCase();
                    if (title.contains(filterString)) {
                        tmpgroupModuleDataArrayList.add(attenObj);
                    }
                }

            }
            results.values = tmpgroupModuleDataArrayList;
            results.count = tmpgroupModuleDataArrayList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            groupMapListAdapter.tmp_groupModuleDataArrayList.clear();
            groupMapListAdapter.tmp_groupModuleDataArrayList.addAll((ArrayList<AgendaCategory>) results.values);
            groupMapListAdapter.notifyDataSetChanged();
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_group;
        TextView group_name;
        CardView app_back;

        public ViewHolder(View itemView) {
            super(itemView);
            img_group = (ImageView) itemView.findViewById(R.id.img_group);
            group_name = (TextView) itemView.findViewById(R.id.group_name);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
        }
    }
}
