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

import com.allintheloop.Bean.AgendaData.Agenda;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Aiyaz on 8/5/17.
 */

public class AgendaListAdapterNew extends RecyclerView.Adapter<AgendaListAdapterNew.MyViewHolder> {
    private Context context;
    private ArrayList<Agenda> arrayList;
    private SessionManager sessionManager;

    public AgendaListAdapterNew(Context context, ArrayList<Agenda> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sessionManager = new SessionManager(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = infalInflater.inflate(R.layout.agenda_list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Agenda ageObj = arrayList.get(position);


        holder.linear_agendaView.setVisibility(View.VISIBLE);
        holder.linear_agendaHeader.setContentDescription(ageObj.getAgendaId());

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
            if (sessionManager.getAgendaSpeakerColumn().equalsIgnoreCase("0")) {
                holder.txt_speaker_name.setVisibility(View.GONE);
            } else {
                if (!(ageObj.getSpeaker().equalsIgnoreCase(""))) {
                    holder.txt_speaker_name.setVisibility(View.VISIBLE);
                    holder.txt_speaker_name.setText(ageObj.getSpeaker());
                } else {
                    holder.txt_speaker_name.setVisibility(View.GONE);
                }
            }

            if (ageObj.getSessionImage().equalsIgnoreCase("")) {
                holder.layout_image.setVisibility(View.GONE);
            } else {
                holder.layout_image.setVisibility(View.VISIBLE);
                try {
                    Glide.with(context)
                            .load(MyUrls.Imgurl + ageObj.getSessionImage())
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
//                        holder.iv_sessionImage.setImageBitmap(resource);

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (ageObj.getTypes().equalsIgnoreCase("")) {
                holder.txt_placeLeft.setVisibility(View.INVISIBLE);
            } else {
                holder.txt_placeLeft.setVisibility(View.VISIBLE);
                holder.txt_placeLeft.setText(ageObj.getTypes());
            }

            if (ageObj.getLocation().equalsIgnoreCase("")) {
                holder.txt_locationName.setVisibility(View.GONE);
            } else {
                holder.txt_locationName.setVisibility(View.VISIBLE);
                holder.txt_locationName.setText(ageObj.getLocation());
            }

            holder.txt_name.setText(ageObj.getHeading());
            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm:ss");
            Date start = f1.parse(ageObj.getStartTime());
            Date end = f1.parse(ageObj.getEndTime());
            if (sessionManager.getTimeFormat().equalsIgnoreCase("1")) {
                SimpleDateFormat f2 = new SimpleDateFormat("HH:mm");
                holder.txt_time.setText(f2.format(start) + " - " + f2.format(end));

            } else {
                DateFormat f2 = new SimpleDateFormat("h:mm a");
                holder.txt_time.setText(f2.format(start) + " - " + f2.format(end));
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    sessionManager.agenda_id(ageObj.getAgendaId());
//                                    sessionManager.setAgendaStatus("Pending");
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
                    ((MainActivity) context).loadFragment();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_speaker_name, txt_time, txt_placeLeft, txt_locationName;
        LinearLayout linear_agendaHeader, layout_image, linear_agendaView;
        ImageView iv_sessionImage;

        public MyViewHolder(View v) {
            super(v);

            txt_name = (TextView) v.findViewById(R.id.txt_name);
            txt_time = (TextView) v.findViewById(R.id.txt_time);
            txt_speaker_name = (TextView) v.findViewById(R.id.txt_speaker_name);
            txt_placeLeft = (TextView) v.findViewById(R.id.txt_placeLeft);
            txt_locationName = (TextView) v.findViewById(R.id.txt_locationName);
            linear_agendaHeader = (LinearLayout) v.findViewById(R.id.linear_agendaHeader);
            linear_agendaView = (LinearLayout) v.findViewById(R.id.linear_agendaView);
            layout_image = (LinearLayout) v.findViewById(R.id.layout_image);
            iv_sessionImage = (ImageView) v.findViewById(R.id.iv_sessionImage);
//            img_topAgenda = (ImageView) v.findViewById(R.id.img_topAgenda);
        }
    }
}
