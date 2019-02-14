package com.allintheloop.Adapter;

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

import com.allintheloop.Adapter.GroupModuleListAdapter.GroupMapListAdapter;
import com.allintheloop.Bean.CmsGroupData.CmsListData;
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

public class CmsListDataAdapter extends RecyclerView.Adapter<CmsListDataAdapter.ViewHolder> {

    ArrayList<CmsListData> groupModuleDataArrayList;
    Context context;
    SessionManager sessionManager;

    public CmsListDataAdapter(ArrayList<CmsListData> groupModuleDataArrayList, Context context) {
        this.groupModuleDataArrayList = groupModuleDataArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_group_list_fragment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        final CmsListData moduleDataObj = groupModuleDataArrayList.get(i);
        holder.group_name.setText(moduleDataObj.getMenuName());

        Glide.with(context).load(moduleDataObj.getCms_icon()).asBitmap().listener(new RequestListener<String, Bitmap>() {
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
                sessionManager.cms_id(moduleDataObj.getId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                ((MainActivity) context).loadFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupModuleDataArrayList.size();
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