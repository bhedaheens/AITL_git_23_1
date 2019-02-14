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
import android.widget.TextView;

import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.allintheloop.Bean.AgendaData.Agenda_SpeakerList;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;


/**
 * Created by nteam on 26/5/16.
 */
public class AgendaSpeakerAdapter extends RecyclerView.Adapter<AgendaSpeakerAdapter.ViewHolder> {
    ArrayList<Agenda_SpeakerList> arrayList;
    Context context;
    SessionManager sessionManager;

    public AgendaSpeakerAdapter(ArrayList<Agenda_SpeakerList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_agenda_speaker, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Agenda_SpeakerList aspeakerObj = arrayList.get(position);
        String imgurl = MyUrls.Imgurl + aspeakerObj.getLogo();


//        Glide.with(context).load(imgurl).centerCrop().fitCenter().into(holder.circleImageView);
        holder.name.setText(aspeakerObj.getFirst_name() + " " + aspeakerObj.getLast_name());
        holder.txt_category.setText(aspeakerObj.getSpeakerCategory());


        GradientDrawable drawable = new GradientDrawable();

        if (aspeakerObj.getLogo().equalsIgnoreCase("")) {
            holder.circleImageView.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);

            if (!(aspeakerObj.getFirst_name().equalsIgnoreCase(""))) {
                if (!(aspeakerObj.getLast_name().equalsIgnoreCase(""))) {
                    String name = aspeakerObj.getFirst_name().charAt(0) + "" + aspeakerObj.getLast_name().charAt(0);
                    holder.txt_profileName.setText(name);
                } else {
                    holder.txt_profileName.setText("" + aspeakerObj.getFirst_name().charAt(0));
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
            Glide.with(context).load(imgurl).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                    holder.circleImageView.setVisibility(View.GONE);
                    holder.txt_profileName.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.circleImageView.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    return false;
                }
            }).into(new BitmapImageViewTarget(holder.circleImageView) {
                @Override
                protected void setResource(Bitmap resource) {

                    holder.circleImageView.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
//                    holder.circleImageView.setImageBitmap(resource);

                }
            });

        }
    }

    public Agenda_SpeakerList getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView circleImageView;
        TextView name, txt_category, txt_profileName;

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = (ImageView) itemView.findViewById(R.id.speaker_image);
            name = (TextView) itemView.findViewById(R.id.name);
            txt_category = (TextView) itemView.findViewById(R.id.txt_category);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
        }
    }
}
