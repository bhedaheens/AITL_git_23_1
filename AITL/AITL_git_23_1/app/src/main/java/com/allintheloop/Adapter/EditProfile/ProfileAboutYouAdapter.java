package com.allintheloop.Adapter.EditProfile;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Fragment.EditProfileModule.ProfileEditAboutYou_activity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;

import java.util.List;

public class ProfileAboutYouAdapter extends RecyclerView.Adapter<ProfileAboutYouAdapter.ViewHolder> {
    Context context;
    List<AttendeeFilterList.Data> dataList;
    ProfileAboutkeywordAdapter profileGoalkeywordAdapter;
    boolean isfromSearch;
    ProfileEditAboutYou_activity aboutYou_activity;

    public ProfileAboutYouAdapter(Context context, List<AttendeeFilterList.Data> dataList, boolean isfromSearch, ProfileEditAboutYou_activity aboutYou_activity) {
        this.context = context;
        this.dataList = dataList;
        this.isfromSearch = isfromSearch;
        this.aboutYou_activity = aboutYou_activity;
    }

    @Override
    public ProfileAboutYouAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_about_you_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileAboutYouAdapter.ViewHolder holder, int position) {
        AttendeeFilterList.Data dataobj = dataList.get(position);
        holder.txt_title.setText(dataobj.getColumnName());

        profileGoalkeywordAdapter = new ProfileAboutkeywordAdapter(context, dataList, position, isfromSearch, aboutYou_activity);
        holder.recycle_keyword.setItemAnimator(new DefaultItemAnimator());
        holder.recycle_keyword.setLayoutManager(new LinearLayoutManager(context));
        holder.recycle_keyword.setAdapter(profileGoalkeywordAdapter);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void filterList(List<AttendeeFilterList.Data> filteredList, boolean isFromSearch) {
        dataList = filteredList;
        this.isfromSearch = isFromSearch;
//        Log.d("Bhavdip","Obj"+dataList.get(0).getColumnName()+ "Keyword"+dataList.get(0).getKeywords().toString());
        notifyDataSetChanged();
//        profileGoalkeywordAdapter.notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        BoldTextView txt_title;
        RecyclerView recycle_keyword;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            recycle_keyword = itemView.findViewById(R.id.recycle_keyword);
        }
    }
}
