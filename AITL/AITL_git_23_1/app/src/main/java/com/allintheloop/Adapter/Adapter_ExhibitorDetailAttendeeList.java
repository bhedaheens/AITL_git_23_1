package com.allintheloop.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorDetailAttendeeList;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 28/7/17.
 */

public class Adapter_ExhibitorDetailAttendeeList extends RecyclerView.Adapter<Adapter_ExhibitorDetailAttendeeList.ViewHolder> {
    ArrayList<ExhibitorDetailAttendeeList> exhibitorDetailAttendeeLists;
    Context context;
    SessionManager sessionManager;

    public Adapter_ExhibitorDetailAttendeeList(ArrayList<ExhibitorDetailAttendeeList> exhibitorDetailAttendeeLists, Context context) {
        this.exhibitorDetailAttendeeLists = exhibitorDetailAttendeeLists;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exhibitordetail_attendeelist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ExhibitorDetailAttendeeList exhibitorDetailAttendeeListObj = exhibitorDetailAttendeeLists.get(position);
        GradientDrawable drawable = new GradientDrawable();
        holder.attendee_name.setText(exhibitorDetailAttendeeListObj.getFirstname() + "\n" + exhibitorDetailAttendeeListObj.getLastname());

        holder.linear_wholeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager.AttenDeeId = exhibitorDetailAttendeeListObj.getId();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                ((MainActivity) context).loadFragment();
            }
        });

        if (exhibitorDetailAttendeeListObj.getLogo().equalsIgnoreCase("")) {
            holder.attendee_imag.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);
            if (!(exhibitorDetailAttendeeListObj.getFirstname().equalsIgnoreCase(""))) {
                if (!(exhibitorDetailAttendeeListObj.getLastname().equalsIgnoreCase(""))) {
                    String name = exhibitorDetailAttendeeListObj.getFirstname().charAt(0) + "" + exhibitorDetailAttendeeListObj.getLastname().charAt(0);
                    holder.txt_profileName.setText(name);
                } else {
                    holder.txt_profileName.setText("" + exhibitorDetailAttendeeListObj.getFirstname().charAt(0));
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
            Glide.with(context).load(MyUrls.Imgurl + exhibitorDetailAttendeeListObj.getLogo()).asBitmap()
                    .override(90, 90).centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                    holder.attendee_imag.setVisibility(View.GONE);
                    holder.txt_profileName.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.attendee_imag.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    return false;
                }
            }).into(new BitmapImageViewTarget(holder.attendee_imag) {
                @Override
                protected void setResource(Bitmap resource) {
                    holder.attendee_imag.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return exhibitorDetailAttendeeLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView attendee_imag;
        TextView txt_profileName;
        BoldTextView attendee_name;
        LinearLayout linear_wholeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            attendee_imag = (ImageView) itemView.findViewById(R.id.attendee_image);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            attendee_name = itemView.findViewById(R.id.attendee_name);
            linear_wholeLayout = (LinearLayout) itemView.findViewById(R.id.linear_wholeLayout);
        }
    }
}
