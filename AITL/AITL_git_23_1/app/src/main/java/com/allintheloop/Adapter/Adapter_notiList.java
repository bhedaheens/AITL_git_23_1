package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.NotificationData;
import com.allintheloop.R;

import java.util.ArrayList;


/**
 * Created by nteam on 29/9/16.
 */
public class Adapter_notiList extends RecyclerView.Adapter<Adapter_notiList.ViewHolder> {
    ArrayList<NotificationData> notificationDatas;
    Context context;

    public Adapter_notiList(ArrayList<NotificationData> notificationDatas, Context context) {
        this.notificationDatas = notificationDatas;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_noti_dialog, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final NotificationData notificationData = notificationDatas.get(position);
        holder.user_name.setText(notificationData.getFirstname() + " " + notificationData.getLastname());
        holder.txt_notimessage.setText(notificationData.getMessage());

        Glide.with(context)
                .load(notificationData.getLogo())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progressBar1.setVisibility(View.GONE);
                        holder.noti_logo.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar1.setVisibility(View.GONE);
                        holder.noti_logo.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .placeholder(R.drawable.noimage)
                .into(holder.noti_logo);

    }

    @Override
    public int getItemCount() {
        return notificationDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar1;
        ImageView noti_logo;
        TextView user_name, txt_notimessage;

        public ViewHolder(View itemView) {
            super(itemView);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            noti_logo = (ImageView) itemView.findViewById(R.id.noti_logo);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            txt_notimessage = (TextView) itemView.findViewById(R.id.txt_notimessage);
        }
    }
}
