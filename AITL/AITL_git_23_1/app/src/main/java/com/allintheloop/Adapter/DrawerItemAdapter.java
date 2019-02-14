package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.DrawerItem;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


/**
 * Created by nteam on 25/4/16.
 */
public class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {
    Context mContext;
    int layoutResourceId;
    ArrayList<DrawerItem> Objdata;
    SessionManager sessionManager;
    Context context;
    String color;

    public DrawerItemAdapter(Context context, int resource, String color) {
        super(context, resource);
        this.color = color;
    }

    public DrawerItemAdapter(Context mContext, int layoutResourceId, ArrayList<DrawerItem> objdata, Context context, String color) {
        super(mContext, layoutResourceId, objdata);
        this.mContext = mContext;
        this.layoutResourceId = layoutResourceId;
        this.Objdata = objdata;
        this.context = context;
        this.color = color;
        sessionManager = new SessionManager(context);
        // preferences=context.getSharedPreferences(GlobalData.USER_PREFRENCE,Context.MODE_PRIVATE);
    }

    public void changeColorText(String color) {
        this.color = color;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
            holder.imageView = (ImageView) convertView.findViewById(R.id.EventLogo);
            holder.event_Name = (TextView) convertView.findViewById(R.id.event_Name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ViewHolder finalHolder = holder;

        if (position == 0) {

            holder.textViewName.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                Glide.with(context)
                        .load(sessionManager.getfun_logo_image())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                finalHolder.imageView.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                finalHolder.imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(finalHolder.imageView);

            } else {
                //Glide.with(context).load(sessionManager.getEvent_Logo()).centerCrop().fitCenter().placeholder(R.drawable.allinloop).into(imageView);
                Glide.with(context)
                        .load(sessionManager.getEvent_Logo())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                finalHolder.imageView.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                finalHolder.imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(finalHolder.imageView);
            }
            //Log.d("Logo",preferences.getString("Logo_Img","img"));
        } else if (position == 1) {
            finalHolder.textViewName.setVisibility(View.GONE);
            finalHolder.imageView.setVisibility(View.GONE);
            finalHolder.event_Name.setVisibility(View.VISIBLE);
            finalHolder.event_Name.setText(sessionManager.getEventName());
        } else {
            DrawerItem drawerItem = Objdata.get(position);
            finalHolder.textViewName.setVisibility(View.VISIBLE);
            finalHolder.imageView.setVisibility(View.GONE);
            finalHolder.event_Name.setVisibility(View.GONE);
            finalHolder.textViewName.setText(drawerItem.name);

            finalHolder.textViewName.setContentDescription(drawerItem.getId());
            try {
                if (sessionManager.getMenuTextColor() == null || sessionManager.getMenuTextColor().equals("")) {
                    finalHolder.textViewName.setTextColor(Color.parseColor("#FFFFFF"));

                } else {
                    finalHolder.textViewName.setTextColor(Color.parseColor(sessionManager.getMenuTextColor()));
                    finalHolder.textViewName.setBackgroundColor(Color.parseColor(sessionManager.getMenuBackColor()));

                }

            } catch (Exception e) {
                finalHolder.textViewName.setTextColor(Color.parseColor("#FFFFFF"));
            }
            if (position == MainActivity.mSelectedItem) {
                try {
                    if (sessionManager.getMenuHoveColor() == null || sessionManager.getMenuHoveColor().equals("")) {
                        finalHolder.textViewName.setBackgroundColor(Color.parseColor(sessionManager.getMenuHoveColor()));
                    } else {

                        finalHolder.textViewName.setBackgroundColor(Color.parseColor(sessionManager.getMenuHoveColor()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    finalHolder.textViewName.setBackgroundColor(Color.parseColor("#000000"));

                }

            } else {
                finalHolder.textViewName.setBackgroundColor(Color.parseColor(sessionManager.getMenuBackColor()));
            }

        }
        notifyDataSetChanged();
        return convertView;
    }

    static class ViewHolder {
        private ImageView imageView;
        private TextView event_Name;
        private TextView textViewName;
    }
}
