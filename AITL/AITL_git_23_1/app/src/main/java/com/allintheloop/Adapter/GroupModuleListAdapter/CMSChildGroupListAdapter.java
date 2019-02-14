package com.allintheloop.Adapter.GroupModuleListAdapter;

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

import com.allintheloop.Bean.CmsGroupData.CmsChildGroupData;
import com.allintheloop.Bean.CmsGroupData.CmsSuperGroupData;
import com.allintheloop.Bean.GroupingData.GroupModuleData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by nteam on 11/12/17.
 */

public class CMSChildGroupListAdapter extends RecyclerView.Adapter<CMSChildGroupListAdapter.ViewHolder> implements Filterable {

    ArrayList<GroupModuleData> groupModuleDataArrayList;
    ArrayList<GroupModuleData> tmp_groupModuleDataArrayList;
    Context context;
    SessionManager sessionManager;

    public CMSChildGroupListAdapter(ArrayList<GroupModuleData> groupModuleDataArrayList, Context context) {
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
        final GroupModuleData moduleDataObj = tmp_groupModuleDataArrayList.get(i);
        holder.group_name.setText(moduleDataObj.getGroupName());
        Glide.with(context).load(MyUrls.groupIconUrl + moduleDataObj.getGroupImage()).asBitmap().listener(new RequestListener<String, Bitmap>() {
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
                sessionManager.setCmsChildGroupId(moduleDataObj.getModuleGroupId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.CMSListFragment;
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
        return new GtroupFilter(this, groupModuleDataArrayList);
    }

    private static class GtroupFilter extends Filter {
        private final CMSChildGroupListAdapter grouoQandAseesionListAdapter;
        private final ArrayList<GroupModuleData> groupModuleDataArrayList;
        private final ArrayList<GroupModuleData> tmpgroupModuleDataArrayList;

        public GtroupFilter(CMSChildGroupListAdapter grouoQandAseesionListAdapter, ArrayList<GroupModuleData> groupModuleDataArrayList) {
            this.grouoQandAseesionListAdapter = grouoQandAseesionListAdapter;
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

                for (GroupModuleData attenObj : groupModuleDataArrayList) {
                    String title = attenObj.getGroupName().toLowerCase();
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
            grouoQandAseesionListAdapter.tmp_groupModuleDataArrayList.clear();
            grouoQandAseesionListAdapter.tmp_groupModuleDataArrayList.addAll((ArrayList<GroupModuleData>) results.values);
            grouoQandAseesionListAdapter.notifyDataSetChanged();
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
