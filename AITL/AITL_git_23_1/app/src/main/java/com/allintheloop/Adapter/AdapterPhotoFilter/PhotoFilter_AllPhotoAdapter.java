package com.allintheloop.Adapter.AdapterPhotoFilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allintheloop.Bean.PhotoFilter.PhotoFilter_seeAllPhotoBean;
import com.allintheloop.Fragment.PhotoFilter.PhotoFilter_seeMyPhotoFragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

public class PhotoFilter_AllPhotoAdapter extends RecyclerView.Adapter<PhotoFilter_AllPhotoAdapter.ViewHolder> {

    List<PhotoFilter_seeAllPhotoBean.Listing> objectList;
    Context context;
    SessionManager sessionManager;
    PhotoFilter_seeMyPhotoFragment photoFilter_seeMyPhotoFragment;

    public PhotoFilter_AllPhotoAdapter(List<PhotoFilter_seeAllPhotoBean.Listing> objectList, Context context, SessionManager sessionManager, PhotoFilter_seeMyPhotoFragment photoFilter_seeMyPhotoFragment) {
        this.objectList = objectList;
        this.context = context;
        this.sessionManager = sessionManager;
        this.photoFilter_seeMyPhotoFragment = photoFilter_seeMyPhotoFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_photofilter_see_allphoto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoFilter_seeAllPhotoBean.Listing data = objectList.get(position);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        holder.frame_back.setLayoutParams(new FrameLayout.LayoutParams(width / 4, width / 4));
        Glide.with(context).load(data.getImage()).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                holder.img_photo.setVisibility(View.GONE);
                holder.progress_image.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.img_photo.setVisibility(View.VISIBLE);
                holder.progress_image.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.img_photo);

        if (data.isChecked()) {
            holder.img_checked.setVisibility(View.VISIBLE);
        } else {
            holder.img_checked.setVisibility(View.INVISIBLE);
        }

        holder.img_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.isChecked()) {
                    data.setChecked(false);
                    holder.img_checked.setVisibility(View.INVISIBLE);
                } else {
                    data.setChecked(true);
                    holder.img_checked.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_photo, img_checked;
        ProgressBar progress_image;
        FrameLayout frame_back;

        public ViewHolder(View itemView) {
            super(itemView);
            progress_image = itemView.findViewById(R.id.progress_image);
            img_photo = itemView.findViewById(R.id.img_photo);
            img_checked = itemView.findViewById(R.id.img_checked);
            frame_back = itemView.findViewById(R.id.frame_back);
        }
    }

    public ArrayList<String> getSelectedData() {
        ArrayList<String> data = new ArrayList<>();
        for (PhotoFilter_seeAllPhotoBean.Listing dataSelected : objectList) {
            if (dataSelected.isChecked()) {
                data.add(dataSelected.getImage());
            }
        }
        return data;
    }

}
