package com.allintheloop.Adapter.Attendee;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeFullDirectory_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class AttendeeFilterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<AttendeeFilterList.Data> attandeeGroupList;
    Context context;
    SessionManager sessionManager;
    AttendeeFullDirectory_Fragment attendeeFullDirectory_fragment;

    public AttendeeFilterListAdapter(List<AttendeeFilterList.Data> attandeeGroupList, Context context, AttendeeFullDirectory_Fragment attendeeFullDirectory_fragment) {
        this.context = context;
        this.attandeeGroupList = attandeeGroupList;
        this.attendeeFullDirectory_fragment = attendeeFullDirectory_fragment;
        sessionManager = new SessionManager(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attendeefilterlist, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final ViewHolder viewHolder = (ViewHolder) holder;
        AttendeeFilterList.Data data = attandeeGroupList.get(position);
        viewHolder.groupName.setText(data.getColumnName());


        if (data.isCheck()) {

            viewHolder.img_arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.img_right));
        } else {
            viewHolder.img_arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow));
        }


//        viewHolder.groupName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                if (!data.isCheck()) {
//                    viewHolder.img_arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.img_right));
//                    data.setCheck(true);
//                } else {
//                    viewHolder.img_arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow));
//                    data.setCheck(false);
//                }
//            }
//        });


//        viewHolder.groupName.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                attendeeFullDirectory_fragment.selectedData(data);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return attandeeGroupList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        ImageView img_arrow;

        public ViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.group_name);
            img_arrow = (ImageView) itemView.findViewById(R.id.img_arrow);
        }
    }

    public ArrayList<AttendeeFilterList.Data> getselectedData() {
        ArrayList<AttendeeFilterList.Data> data = new ArrayList<>();

        for (AttendeeFilterList.Data obj1 : attandeeGroupList) {
            if (obj1.isCheck()) {
                data.add(obj1);
            }
        }
        return data;
    }

    public void clearFiler() {
        ArrayList<AttendeeFilterList.Data> data = new ArrayList<>();

        for (AttendeeFilterList.Data obj1 : attandeeGroupList) {
            if (obj1.isCheck()) {
                obj1.setCheck(false);
            }
        }
        notifyDataSetChanged();
    }

}
