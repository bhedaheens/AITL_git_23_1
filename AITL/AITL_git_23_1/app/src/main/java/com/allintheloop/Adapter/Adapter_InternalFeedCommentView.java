package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Bean.ActivityModule.ActivityCommentClass;
import com.allintheloop.Fragment.FacebookModule.FacebookDailog_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aiyaz on 3/8/17.
 */

public class Adapter_InternalFeedCommentView extends RecyclerView.Adapter<Adapter_InternalFeedCommentView.ViewHolder> implements VolleyInterface {

    ArrayList<ActivityCommentClass> objectArrayList;
    Context context;
    SessionManager sessionManager;

    public Adapter_InternalFeedCommentView(ArrayList<ActivityCommentClass> objectArrayList, Context context) {
        this.objectArrayList = objectArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity_commentview, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ActivityCommentClass feed_comment = objectArrayList.get(position);

        Log.d("AITL In Adapter", "" + feed_comment.getComment());
        holder.txt_time.setText(feed_comment.getDatetime());
        holder.txt_comment.setText(feed_comment.getComment());
        holder.user_sender.setText(feed_comment.getName());

        if (feed_comment.getShowDelete().equalsIgnoreCase("0")) {
            holder.delete_img.setVisibility(View.GONE);
        } else {
            holder.delete_img.setVisibility(View.VISIBLE);
        }

        holder.img_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> imgStringarray = new ArrayList<>();
                imgStringarray.add(feed_comment.getCommentImage().get(0).toString());
                FragmentActivity activity = (FragmentActivity) (context);
                FragmentManager fm = activity.getSupportFragmentManager();
                Bundle bundle = new Bundle();
                FacebookDailog_Fragment fragment = new FacebookDailog_Fragment();
                bundle.putInt("position", position);
                bundle.putString("isActivity", "1");
                bundle.putStringArrayList("img_array", imgStringarray);
                fragment.setArguments(bundle);
                fragment.show(fm, "DialogFragment");
            }
        });

        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialDialog dialog = new MaterialDialog.Builder(context)
                        .title("Delete Comment")
                        .items("Are you sure want to delete this Comment?")
                        .positiveColor(context.getResources().getColor(R.color.colorAccent))
                        .positiveText(context.getResources().getString(R.string.yes_string))
                        .negativeText(context.getResources().getString(R.string.cancelText))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                deleteComment(feed_comment.getCommentId());
                                objectArrayList.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(MaterialDialog dialog, DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .cancelable(false)
                        .build();
                dialog.show();

            }
        });

        try {
            Glide.with(context).load(MyUrls.Imgurl + feed_comment.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    holder.sender_image.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    holder.sender_image.setVisibility(View.VISIBLE);
                    return false;
                }
            }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.sender_image) {
                @Override
                protected void setResource(Bitmap resource) {
                    holder.sender_image.setImageBitmap(resource);
                }
            });

            if (feed_comment.getCommentImage().size() > 0) {
                Glide.with(context).load(MyUrls.Imgurl + feed_comment.getCommentImage().get(0)).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_comment.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_comment.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.img_comment) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_comment.setImageBitmap(resource);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void deleteComment(String commentId) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.DeleteCommentActivity, Param.deleteCommentView(commentId), 0, true, this);
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
                        GlobalData.activiyReloadedfromSharePicture(context);
                        ((MainActivity) context).fragmentBackStackMaintain();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
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
