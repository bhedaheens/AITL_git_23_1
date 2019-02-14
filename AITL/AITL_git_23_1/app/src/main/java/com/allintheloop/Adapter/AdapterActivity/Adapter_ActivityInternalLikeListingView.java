package com.allintheloop.Adapter.AdapterActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.ActivityModule.InternalLike;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.RoundedImageConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.List;

public class Adapter_ActivityInternalLikeListingView extends RecyclerView.Adapter<Adapter_ActivityInternalLikeListingView.ViewHolder> {
    List<InternalLike> internalLikes;
    Context context;

    public Adapter_ActivityInternalLikeListingView(List<InternalLike> internalLikes, Context context) {
        this.internalLikes = internalLikes;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_internallike_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InternalLike internalLikeObj = internalLikes.get(position);
        if (internalLikeObj.getName().isEmpty()) {
            holder.txt_name.setVisibility(View.GONE);
        } else {
            holder.txt_name.setVisibility(View.VISIBLE);
            holder.txt_name.setText(internalLikeObj.getName());
        }

        if (internalLikeObj.getCompanyName().isEmpty()) {
            holder.txt_destination.setVisibility(View.GONE);
        } else {
            holder.txt_destination.setVisibility(View.VISIBLE);
            holder.txt_destination.setText(internalLikeObj.getCompanyName());
        }
        Glide.with(context).load(internalLikeObj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                holder.img_profile.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.img_profile.setVisibility(View.VISIBLE);
                return false;
            }
        }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.img_profile) {
            @Override
            protected void setResource(Bitmap resource) {
                holder.img_profile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
            }
        });

    }

    @Override
    public int getItemCount() {
        return internalLikes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_profile;
        TextView txt_name, txt_destination;

        public ViewHolder(View itemView) {
            super(itemView);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_destination = (TextView) itemView.findViewById(R.id.txt_destination);
        }
    }
}
