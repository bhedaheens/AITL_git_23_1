package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.HomeData.DashboardItemList;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by nteam on 26/4/16.
 */
public class DashBoardItemListAdapter extends RecyclerView.Adapter<DashBoardItemListAdapter.ViewHolder> {


    ArrayList<DashboardItemList> DashObj;
    Context context;
    SessionManager sessionManager;

    public DashBoardItemListAdapter(ArrayList<DashboardItemList> dashObj, Context context) {
        DashObj = dashObj;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public DashBoardItemListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dashboard_itemlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DashBoardItemListAdapter.ViewHolder holder, int position) {
        DashboardItemList Dobj = DashObj.get(position);


        //        Log.d("AITL", "ImageView Status :-" + Dobj.getImgview_status());
        DisplayMetrics displaymetrics = new DisplayMetrics();
        float density = context.getResources().getDisplayMetrics().density;
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        holder.cardview.setContentDescription(Dobj.getpMId());
        if (sessionManager.getEventId().equalsIgnoreCase("353")) {
            holder.txtMname.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.txtMname.setTextColor(Color.parseColor("#000000"));
        }
        Log.d("Bhadvip COLOR", "COLOR:-" + Dobj.getBackColor());

        holder.frame_menuBack.setLayoutParams(new FrameLayout.LayoutParams(width / 4, width / 4));
        if (Dobj.getBackColor().equalsIgnoreCase("")) {
            holder.frame_menuBack.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            Log.d("AITL COLOR", Dobj.getBackColor());
            holder.frame_menuBack.setBackgroundColor(Color.parseColor(Dobj.getBackColor()));
        }
        if (Dobj.getIs_icon().equalsIgnoreCase("0")) {
            if (Dobj.getImgview_status().equalsIgnoreCase("1")) {
                holder.circleImageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Dobj.getImgView())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                holder.circleImageView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                holder.circleImageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })

                        .into(holder.circleImageView);
                holder.imgView.setVisibility(View.GONE);
            } else if (Dobj.getImgview_status().equalsIgnoreCase("0")) {
                holder.circleImageView.setVisibility(View.GONE);
                holder.imgView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Dobj.getImgView())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                holder.imgView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                holder.imgView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })

                        .into(holder.imgView);
            }
        } else {
            if (Dobj.getImgview_status().equalsIgnoreCase("1")) {
                holder.cardview.setBackground(context.getResources().getDrawable(R.drawable.roundedcardview));
                holder.circleImageView.setVisibility(View.VISIBLE);

                Glide.with(context)
                        .load(Dobj.getIcon_path())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                holder.circleImageView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                holder.circleImageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })

                        .into(holder.circleImageView);


                holder.imgView.setVisibility(View.GONE);
            } else if (Dobj.getImgview_status().equalsIgnoreCase("0")) {


                holder.circleImageView.setVisibility(View.GONE);
                holder.imgView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(Dobj.getIcon_path())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                holder.imgView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                holder.imgView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(holder.imgView);
            }
        }


        holder.txtMname.setText(Dobj.getpTitle());


    }

    @Override
    public int getItemCount() {
        return DashObj.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView, backImageView;
        TextView txtMname;
        CardView cardview;
        CircleImageView circleImageView;
        FrameLayout frame_menuBack;

        public ViewHolder(View itemView) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.MenuImg);
            txtMname = (TextView) itemView.findViewById(R.id.Mname);
            cardview = (CardView) itemView.findViewById(R.id.card_container);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.item_img);
            frame_menuBack = (FrameLayout) itemView.findViewById(R.id.frame_menuBack);
        }
    }
}
