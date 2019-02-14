package com.allintheloop.Adapter.Attendee;

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

import com.allintheloop.Bean.Attendee.AttndeeDetailExhibitorList;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 28/7/17.
 */

public class AttendeeDetailExhibitorListAdapter extends RecyclerView.Adapter<AttendeeDetailExhibitorListAdapter.ViewHolder> {

    ArrayList<AttndeeDetailExhibitorList> attndeeDetailExhibitorLists;
    Context context;
    SessionManager sessionManager;

    public AttendeeDetailExhibitorListAdapter(ArrayList<AttndeeDetailExhibitorList> attndeeDetailExhibitorLists, Context context) {
        this.attndeeDetailExhibitorLists = attndeeDetailExhibitorLists;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attendeedetail_exhibitorlist, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AttndeeDetailExhibitorList detailExhibitorListobj = attndeeDetailExhibitorLists.get(position);
        holder.exhibitor_companyName.setText(detailExhibitorListobj.getHeading());
        holder.exhibitor_standName.setText(detailExhibitorListobj.getStand_number());
        GradientDrawable drawable = new GradientDrawable();

        holder.linear_wholeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManager.exhibitor_id = detailExhibitorListobj.getExhibitor_id();
                SessionManager.exhi_pageId = detailExhibitorListobj.getExhibitor_page_id();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                ((MainActivity) context).loadFragment();
            }
        });
        if (detailExhibitorListobj.getCompany_logo().equalsIgnoreCase("")) {
            holder.exhibitor_companyLogo.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);

            if (!(detailExhibitorListobj.getHeading().equalsIgnoreCase(""))) {
                if (!(detailExhibitorListobj.getHeading().equalsIgnoreCase(""))) {
                    String name = "" + detailExhibitorListobj.getHeading().charAt(0);
                    holder.txt_profileName.setText(name);
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
            Glide.with(context).load(MyUrls.Imgurl + detailExhibitorListobj.getCompany_logo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {

                    holder.exhibitor_companyLogo.setVisibility(View.GONE);
                    holder.txt_profileName.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.exhibitor_companyLogo.setVisibility(View.VISIBLE);
                    holder.txt_profileName.setVisibility(View.GONE);
                    return false;
                }
            }).into(new BitmapImageViewTarget(holder.exhibitor_companyLogo) {
                @Override
                protected void setResource(Bitmap resource) {
                    holder.exhibitor_companyLogo.setImageBitmap(resource);
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return attndeeDetailExhibitorLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView exhibitor_companyLogo;
        TextView exhibitor_companyName, exhibitor_standName, txt_profileName;
        LinearLayout linear_wholeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            exhibitor_companyLogo = (ImageView) itemView.findViewById(R.id.exhibitor_companyLogo);
            exhibitor_companyName = (TextView) itemView.findViewById(R.id.exhibitor_companyName);
            exhibitor_standName = (TextView) itemView.findViewById(R.id.exhibitor_standName);
            linear_wholeLayout = (LinearLayout) itemView.findViewById(R.id.linear_wholeLayout);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
        }
    }
}
