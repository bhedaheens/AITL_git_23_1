package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.PrivateMessage_Sppiner;
import com.allintheloop.Fragment.PrivateMessage.Private_Message_Fragment;
import com.allintheloop.R;

import java.util.ArrayList;


/**
 * Created by nteam on 1/7/16.
 */
public class PrivateMessage_Sppr_Adapter extends RecyclerView.Adapter<PrivateMessage_Sppr_Adapter.ViewHolder> {

    ArrayList<PrivateMessage_Sppiner> sppinerArrayList;
    Context context;

    public PrivateMessage_Sppr_Adapter(ArrayList<PrivateMessage_Sppiner> sppinerArrayList, Context context) {
        this.sppinerArrayList = sppinerArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_privatemessage_sppr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        PrivateMessage_Sppiner messageSppinerObj = sppinerArrayList.get(position);
        holder.txt_user.setText(messageSppinerObj.getName());
        holder.linear_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Private_Message_Fragment.sppinerArrayList.remove(position);
                Private_Message_Fragment.arrayReqid.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, sppinerArrayList.size());
                notifyDataSetChanged();
                Log.d("AITL Adapter", Private_Message_Fragment.sppinerArrayList.toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return sppinerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linear_user;
        TextView txt_user;

        public ViewHolder(View itemView) {
            super(itemView);
            linear_user = (LinearLayout) itemView.findViewById(R.id.linear_user);
            txt_user = (TextView) itemView.findViewById(R.id.txt_user);
        }
    }
}
