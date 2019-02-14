package com.allintheloop.Adapter.AdapterActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aiyaz on 3/8/17.
 */

public class Adapter_Activity_CommentView extends RecyclerView.Adapter<Adapter_Activity_CommentView.ViewHolder> {
    ArrayList<Object> objectsArrayList;
    Context context;


    public Adapter_Activity_CommentView(ArrayList<Object> objectsArrayList, Context context) {
        this.objectsArrayList = objectsArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity_commentview, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //

//
//           Activity_Facebook_Feed.Datum_ datumObj= (Activity_Facebook_Feed.Datum_)objectsArrayList.get(position);
//            holder.txt_time.setText(datumObj.getCreatedTime());
//            holder.txt_comment.setText(datumObj.getMessage());
//            holder.user_sender.setText(datumObj.getFrom().getName());
//        try
//        {
//            Glide.with(context).load(datumObj.getFrom().getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
//                @Override
//                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
//                    holder.sender_image.setVisibility(View.VISIBLE);
//
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                    holder.sender_image.setVisibility(View.VISIBLE);
//                    return false;
//                }
//            }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.sender_image) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    holder.sender_image.setImageBitmap(resource);
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public int getItemCount() {
        return objectsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView sender_image;
        ImageView delete_img, img_comment;
        TextView txt_time, txt_comment, user_sender;

        public ViewHolder(View itemView) {
            super(itemView);
            sender_image = (CircleImageView) itemView.findViewById(R.id.sender_image);
            delete_img = (ImageView) itemView.findViewById(R.id.delete_img);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            user_sender = (TextView) itemView.findViewById(R.id.user_sender);

        }
    }
}
