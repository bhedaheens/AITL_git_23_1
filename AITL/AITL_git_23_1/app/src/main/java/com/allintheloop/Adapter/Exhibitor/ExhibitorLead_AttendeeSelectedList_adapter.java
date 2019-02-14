package com.allintheloop.Adapter.Exhibitor;

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

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLeadAttendeeSelectedList;
import com.allintheloop.Fragment.ExhibitorFragment.ExhibitorLead_AttendeeSelectList;
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
 * Created by nteam on 11/9/17.
 */

public class ExhibitorLead_AttendeeSelectedList_adapter extends RecyclerView.Adapter<ExhibitorLead_AttendeeSelectedList_adapter.ViewHolder> {
    ArrayList<ExhibitorLeadAttendeeSelectedList> objectArrayList;
    Context context;
    SessionManager sessionManager;
    int tmp_position;
    ExhibitorLead_AttendeeSelectList fragment;

    public ExhibitorLead_AttendeeSelectedList_adapter(ArrayList<ExhibitorLeadAttendeeSelectedList> objectArrayList, Context context, ExhibitorLead_AttendeeSelectList fragment) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.fragment = fragment;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exhibitorlead_attendeeselectedlist, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExhibitorLeadAttendeeSelectedList attendance = objectArrayList.get(position);
        this.tmp_position = position;
        final ViewHolder itemViewHolder = (ViewHolder) holder;

        GradientDrawable drawable = new GradientDrawable();
        itemViewHolder.imageView.setVisibility(View.GONE);
        itemViewHolder.user_sqrimage.setVisibility(View.VISIBLE);
        itemViewHolder.userDesc.setVisibility(View.GONE);
        itemViewHolder.userName.setText(attendance.getFirstname() + " " + attendance.getLastname());
        itemViewHolder.imageView.setVisibility(View.VISIBLE);
        itemViewHolder.user_sqrimage.setVisibility(View.GONE);

        if (attendance.getIsSelected() == 2) {
            itemViewHolder.check.setChecked(false);

        } else {
            if (attendance.getIsSelected() == 0) {
                itemViewHolder.check.setChecked(false);
            } else {
                itemViewHolder.check.setChecked(true);

            }
        }

        itemViewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;

                boolean chek;
                if (cb.isChecked()) {

                    chek = fragment.getGroupList(attendance);
                    if (chek) {
                        attendance.setIsSelected(1);
                    } else {
                        cb.setChecked(false);
                    }
                } else {
                    attendance.setIsSelected(0);
                    fragment.removeGroupList(attendance);
                }

            }
        });

        if (attendance.getLogo().equalsIgnoreCase("")) {
            itemViewHolder.imageView.setVisibility(View.GONE);
            itemViewHolder.txt_profileName.setVisibility(View.VISIBLE);

            if (!(attendance.getFirstname().equalsIgnoreCase(""))) {
                if (!(attendance.getLastname().equalsIgnoreCase(""))) {
                    String name = attendance.getFirstname().charAt(0) + "" + attendance.getLastname().charAt(0);
                    holder.txt_profileName.setText(name);
                } else {
                    holder.txt_profileName.setText("" + attendance.getFirstname().charAt(0));
                }
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    holder.txt_profileName.setBackgroundDrawable(drawable);
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    drawable.setShape(GradientDrawable.OVAL);
                    drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                    holder.txt_profileName.setBackgroundDrawable(drawable);
                    holder.txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                }
            }

        } else {
            try {
                Glide.with(context).load(MyUrls.Imgurl + attendance.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        itemViewHolder.imageView.setVisibility(View.VISIBLE);
                        itemViewHolder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        itemViewHolder.imageView.setVisibility(View.VISIBLE);
                        itemViewHolder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(itemViewHolder.imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        itemViewHolder.imageView.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView userName, userDesc, txt_profileName;
        ImageView user_sqrimage;
        RelativeLayout layout_relative;
        CardView app_back;
        CheckBox check;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.user_image);
            user_sqrimage = (ImageView) itemView.findViewById(R.id.user_sqrimage);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            userDesc = (TextView) itemView.findViewById(R.id.user_desc);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            layout_relative = (RelativeLayout) itemView.findViewById(R.id.layout_relative);
            app_back = (CardView) itemView.findViewById(R.id.app_back);
            check = (CheckBox) itemView.findViewById(R.id.check_group);
        }
    }
}
