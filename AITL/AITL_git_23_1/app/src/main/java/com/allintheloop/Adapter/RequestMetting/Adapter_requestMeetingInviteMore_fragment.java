package com.allintheloop.Adapter.RequestMetting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.AttendeeInviteMoreList;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by nteam on 9/3/18.
 */

public class Adapter_requestMeetingInviteMore_fragment extends RecyclerView.Adapter<Adapter_requestMeetingInviteMore_fragment.ViewHolder> {
    ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> objectArrayList;
    Context context;
    SessionManager sessionManager;
    ViewHolder holder;

    public Adapter_requestMeetingInviteMore_fragment(ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_requestmeetinginvitemore_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AttendeeInviteMoreList.AttendeeinviteMoreData moreData = objectArrayList.get(position);


        this.holder = holder;
        holder.userName.setText(moreData.getFirstname() + " " + moreData.getLastname());
        GradientDrawable drawable = new GradientDrawable();
        holder.check.setVisibility(View.VISIBLE);
        if (moreData.getTitle().isEmpty()) {
            holder.userDesc.setVisibility(View.GONE);
        } else {
            holder.userDesc.setVisibility(View.VISIBLE);
            if (moreData.getCompanyName().isEmpty()) {
                holder.userDesc.setText(moreData.getTitle());
            } else {
                holder.userDesc.setText(moreData.getTitle() + " at " + moreData.getCompanyName());
            }
        }


        if (moreData.Ischeck()) {
            holder.check.setChecked(true);
        } else {
            holder.check.setChecked(false);
        }

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.check.isChecked()) {
                    holder.check.setChecked(true);
                    moreData.setIscheck(true);
                } else {
                    holder.check.setChecked(false);
                    moreData.setIscheck(false);
                }
            }
        });
        if (moreData.getLogo().equalsIgnoreCase("")) {
            holder.user_sqrimage.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);
            if (moreData.getFirstname().isEmpty() && moreData.getLastname().isEmpty()) {
                holder.txt_profileName.setVisibility(View.GONE);
            } else {
                holder.txt_profileName.setVisibility(View.VISIBLE);
                if (!moreData.getFirstname().isEmpty()) {
                    if (!moreData.getLastname().isEmpty()) {
                        holder.txt_profileName.setText("" + moreData.getFirstname().charAt(0) + "" + moreData.getLastname().charAt(0));
                    } else {
                        holder.txt_profileName.setText("" + moreData.getFirstname().charAt(0));
                    }
                }

            }
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(10);
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {

                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.txt_profileName.setBackgroundDrawable(drawable);
                holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.txt_profileName.setBackgroundDrawable(drawable);
                holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
        } else {
            Glide.with(context).load(MyUrls.Imgurl + moreData.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    holder.user_sqrimage.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.user_sqrimage.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    return false;
                }
            }).into(new BitmapImageViewTarget(holder.user_sqrimage) {
                @Override
                protected void setResource(Bitmap resource) {
                    holder.user_sqrimage.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.TRANSPARENT, 10, 0, context));
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userDesc, txt_profileName;
        ImageView user_sqrimage;
        RelativeLayout layout_relative;
        CardView app_back;
        CheckBox check;

        public ViewHolder(View itemView) {
            super(itemView);
            user_sqrimage = (ImageView) itemView.findViewById(R.id.user_sqrimage);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
            check = (CheckBox) itemView.findViewById(R.id.check_group);
        }
    }

    public ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> getSelectedList() {
        ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> selected = new ArrayList<>();
        for (int i = 0; i < objectArrayList.size(); i++) {
            if (objectArrayList.get(i).Ischeck()) {
                selected.add(objectArrayList.get(i));
            }
        }
        return selected;
    }

    public void updateList(ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> objectArrayList) {
        this.objectArrayList = objectArrayList;
        notifyItemChanged(0);
        notifyDataSetChanged();
    }

    public void setAllCheckboxfalse() {

        for (int i = 0; i < objectArrayList.size(); i++) {
            if (objectArrayList.get(i).Ischeck()) {
                objectArrayList.get(i).setIscheck(false);
            }
        }
        notifyDataSetChanged();
    }

}
