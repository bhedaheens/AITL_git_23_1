package com.allintheloop.Adapter.Exhibitor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLinkReprenetativesData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nteam on 9/9/17.
 */

public class ExhibitorLeadRepresentatives_Adapter extends RecyclerView.Adapter<ExhibitorLeadRepresentatives_Adapter.ViewHolder> implements VolleyInterface {

    ArrayList<Object> objectArrayList;
    Context context;
    SessionManager sessionManager;
    int tmp_position;

    public ExhibitorLeadRepresentatives_Adapter(ArrayList<Object> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_exhibitor_link_representatives_list, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ExhibitorLinkReprenetativesData data = (ExhibitorLinkReprenetativesData) objectArrayList.get(position);
        GradientDrawable drawable = new GradientDrawable();
        holder.user_name.setText(data.getFirstname() + " " + data.getLastname());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteRepresentatives(data.getId());
                objectArrayList.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
            }
        });


        if (!(data.getTitle().equalsIgnoreCase(""))) {
            holder.user_desc.setVisibility(View.VISIBLE);
            if (!(data.getCompanyName().equalsIgnoreCase(""))) {
                holder.user_desc.setText(data.getTitle() + " at " + data.getCompanyName());
            } else {
                holder.user_desc.setText(data.getTitle());
            }
        } else {
            holder.user_desc.setVisibility(View.GONE);
        }

        if (data.getLogo().equalsIgnoreCase("")) {
            holder.user_image.setVisibility(View.GONE);
            holder.txt_profileName.setVisibility(View.VISIBLE);
            if (!(data.getFirstname().equalsIgnoreCase(""))) {
                if (!(data.getLastname().equalsIgnoreCase(""))) {
                    String name = data.getFirstname().charAt(0) + "" + data.getLastname().charAt(0);
                    holder.txt_profileName.setText(name);
                } else {
                    holder.txt_profileName.setText("" + data.getFirstname().charAt(0));
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
                Glide.with(context).load(MyUrls.Imgurl + data.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.user_image.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.user_image.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }
                }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.user_image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.user_image.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void deleteRepresentatives(String repId) {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest((MainActivity) context, VolleyRequest.Method.POST, MyUrls.removeExhibitorRep, Param.deleteExhibitorLeadRep(sessionManager.getEventId(), sessionManager.getUserId(), repId), 0, false, this);
        }
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_profileName, user_name, user_desc;
        ImageView user_image;
        Button btn_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            user_desc = (TextView) itemView.findViewById(R.id.user_desc);
            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            btn_delete = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }

    public void updateList(ArrayList<Object> activityInternalFeeds) {
        this.objectArrayList = activityInternalFeeds;
        notifyItemChanged(0);
    }
}
