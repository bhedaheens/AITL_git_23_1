package com.allintheloop.Adapter.AdapterPhotoFilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allintheloop.Bean.PhotoFilter.EventBusgetPhotoFilterData;
import com.allintheloop.Bean.PhotoFilter.PhotoFilterListing;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class PhotoFilter_FilterListing_Adapter extends RecyclerView.Adapter<PhotoFilter_FilterListing_Adapter.ViewHolder> {
    List<Object> objectList;
    Context context;
    SessionManager sessionManager;

    public PhotoFilter_FilterListing_Adapter(List<Object> objectList, Context context, SessionManager sessionManager) {
        this.objectList = objectList;
        this.context = context;
        this.sessionManager = sessionManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_photofilter_filterlisting, parent, false);
        return new ViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PhotoFilterListing photoFilterListing = (PhotoFilterListing) objectList.get(position);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            holder.img_photoFilter.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
        }

        holder.img_photoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBusgetPhotoFilterData(photoFilterListing.getImage()));
                ((MainActivity) context).fragmentBackStackMaintain();
            }
        });


        Glide.with(context).load(photoFilterListing.getImage()).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                holder.img_photoFilter.setVisibility(View.GONE);
                holder.progress_image.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                holder.img_photoFilter.setVisibility(View.VISIBLE);
                holder.progress_image.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.img_photoFilter);
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_photoFilter;
        ProgressBar progress_image;
        FrameLayout frame_image;

        public ViewHolder(View itemView) {
            super(itemView);
            img_photoFilter = itemView.findViewById(R.id.img_photoFilter);
            progress_image = itemView.findViewById(R.id.progress_image);
            frame_image = itemView.findViewById(R.id.frame_image);
        }
    }
}
