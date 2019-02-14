package com.allintheloop.Adapter.Agenda;

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

import com.allintheloop.Bean.AgendaData.Agenda_Time;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 8/5/17.
 */

public class UserWiseAgendaListAdapter extends RecyclerView.Adapter<UserWiseAgendaListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Agenda_Time> arrayList;
    private SessionManager sessionManager;

    public UserWiseAgendaListAdapter(Context context, ArrayList<Agenda_Time> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sessionManager = new SessionManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = infalInflater.inflate(R.layout.agenda_expandablelist_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Agenda_Time agendaObj = arrayList.get(position);

        holder.linear_agendaHeader.setContentDescription(agendaObj.getAgenda_id());
        if (agendaObj.getIsAgenda().equalsIgnoreCase("1")) {


            holder.agenda_layout.setVisibility(View.VISIBLE);
            holder.layout_metting.setVisibility(View.GONE);

            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(13.0f);
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                holder.linear_agendaHeader.setBackgroundDrawable(drawable);
                holder.txt_name.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                holder.txt_time.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            } else {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(13.0f);
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                holder.linear_agendaHeader.setBackgroundDrawable(drawable);
                holder.txt_name.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                holder.txt_time.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            }
            try {
                if (!(agendaObj.getSpeaker().equalsIgnoreCase(""))) {
                    holder.txt_speaker_name.setVisibility(View.VISIBLE);
                    holder.txt_speaker_name.setText(agendaObj.getSpeaker());
                } else {
                    holder.txt_speaker_name.setVisibility(View.GONE);
                }

                if (agendaObj.getSessionType().equalsIgnoreCase("")) {
                    holder.txt_placeLeft.setVisibility(View.GONE);
                } else {
                    holder.txt_placeLeft.setVisibility(View.VISIBLE);
                    holder.txt_placeLeft.setText(agendaObj.getSessionType());
                }
                if (agendaObj.getSessionImage().equalsIgnoreCase("")) {
                    holder.layout_image.setVisibility(View.GONE);
                } else {
                    holder.layout_image.setVisibility(View.VISIBLE);
                    try {
                        Glide.with(context)
                                .load(MyUrls.Imgurl + agendaObj.getSessionImage())
                                .asBitmap()
                                .skipMemoryCache(false)
                                .listener(new RequestListener<String, Bitmap>() {

                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                                        holder.iv_sessionImage.setVisibility(View.VISIBLE);

                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        holder.iv_sessionImage.setVisibility(View.VISIBLE);
                                        return false;
                                    }
                                }).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                holder.iv_sessionImage.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, 30));

                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (agendaObj.getLocation().equalsIgnoreCase("")) {
                    holder.txt_locationName.setVisibility(View.GONE);
                } else {
                    holder.txt_locationName.setVisibility(View.VISIBLE);
                    holder.txt_locationName.setText(agendaObj.getLocation());
                }

                holder.txt_name.setText(agendaObj.getHeading());

                if (agendaObj.getAgenda_timezone().equalsIgnoreCase("")) {

                    holder.txt_time.setText(agendaObj.getStart_time() + " - " + agendaObj.getEnd_time());
                } else {
                    holder.txt_time.setText(agendaObj.getStart_time() + " - " + agendaObj.getEnd_time() + " (" + agendaObj.getAgenda_timezone() + ")");
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        sessionManager.agenda_id(agendaObj.getAgenda_id());
//                                    sessionManager.setAgendaStatus("Pending");
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                        ((MainActivity) context).loadFragment();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.layout_metting.setVisibility(View.VISIBLE);

            holder.layout_metting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (agendaObj.getIs_exhi().equalsIgnoreCase("1")) {
                        sessionManager.exhibitor_id = agendaObj.getExhi_user_id();
                        sessionManager.exhi_pageId = agendaObj.getExhibiotor_id();
                        GlobalData.temp_Fragment = 0;
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    } else {
                        SessionManager.AttenDeeId = agendaObj.getExhibiotor_id();
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                        ((MainActivity) context).loadFragment();
                    }
                }
            });

            holder.agenda_layout.setVisibility(View.GONE);
            holder.txt_Mettingname.setText(agendaObj.getExhiHeading());
            holder.txt_Mettingtime.setText(agendaObj.getTime());
            if (agendaObj.getIs_exhi().equalsIgnoreCase("1")) {
                holder.txt_standNumber.setVisibility(View.VISIBLE);
                holder.txt_standNumber.setText(agendaObj.getStand_number());
            } else {
                holder.txt_standNumber.setVisibility(View.GONE);
            }

            if (agendaObj.getStatus().equalsIgnoreCase("1")) {
                holder.txt_MettingStatus.setText("Confirmed");
                holder.txt_MettingStatus.setBackground(context.getResources().getDrawable(R.drawable.btn_accpet_layout));
            } else {
                holder.txt_MettingStatus.setText("Pending");
                holder.txt_MettingStatus.setBackground(context.getResources().getDrawable(R.drawable.btn_pending));
            }

            holder.txt_MettingStatus.setTextColor(context.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_time, txt_speaker_name, txt_placeLeft, txt_locationName;
        TextView txt_Mettingname, txt_Mettingtime, txt_standNumber, txt_MettingStatus;
        LinearLayout linear_agendaHeader, layout_image, agenda_layout, layout_metting;
        ImageView iv_sessionImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_Mettingname = (TextView) itemView.findViewById(R.id.txt_Mettingname);
            txt_Mettingtime = (TextView) itemView.findViewById(R.id.txt_Mettingtime);
            txt_standNumber = (TextView) itemView.findViewById(R.id.txt_standNumber);
            txt_MettingStatus = (TextView) itemView.findViewById(R.id.txt_MettingStatus);
            txt_speaker_name = (TextView) itemView.findViewById(R.id.txt_speaker_name);
//        TextView lbl_placeLeft = (TextView) convertView.findViewById(R.id.lbl_placeLeft);
            txt_placeLeft = (TextView) itemView.findViewById(R.id.txt_placeLeft);
            linear_agendaHeader = (LinearLayout) itemView.findViewById(R.id.linear_agendaHeader);
            layout_image = (LinearLayout) itemView.findViewById(R.id.layout_image);
            agenda_layout = (LinearLayout) itemView.findViewById(R.id.agenda_layout);
            layout_metting = (LinearLayout) itemView.findViewById(R.id.layout_metting);
            txt_locationName = (TextView) itemView.findViewById(R.id.txt_locationName);
            iv_sessionImage = (ImageView) itemView.findViewById(R.id.iv_sessionImage);
        }
    }
}
