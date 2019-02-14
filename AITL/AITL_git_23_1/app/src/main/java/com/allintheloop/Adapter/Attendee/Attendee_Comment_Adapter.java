package com.allintheloop.Adapter.Attendee;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.Attendee.Attendee_Comment;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by nteam on 4/7/16.
 */
public class Attendee_Comment_Adapter extends RecyclerView.Adapter<Attendee_Comment_Adapter.ViewHolder> implements VolleyInterface {

    ArrayList<Attendee_Comment> commentArrayList;
    Context context;
    SessionManager sessionManager;

    public Attendee_Comment_Adapter(ArrayList<Attendee_Comment> commentArrayList, Context context) {
        this.commentArrayList = commentArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_attendee_comment, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final Attendee_Comment commentObj = commentArrayList.get(position);
        holder.user_sender.setText(commentObj.getUser_name());
        holder.txt_time.setText(commentObj.getTime_stamp());
        holder.txt_comment.setText(commentObj.getComment());


        if (commentObj.getComment().equalsIgnoreCase("")) {
            holder.txt_comment.setVisibility(View.GONE);
        }
        Log.d("AITL CommentImg", commentObj.getImage());
        Log.d("AITL LogoImg", commentObj.getLogo());

        if (sessionManager.isLogin()) {
            if (commentObj.getUser_id().equalsIgnoreCase(sessionManager.getUserId())) {
                holder.img_delete.setVisibility(View.VISIBLE);
            } else {
                holder.img_delete.setVisibility(View.GONE);
            }

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialDialog dialog = new MaterialDialog.Builder(context)
                            .title(context.getResources().getString(R.string.txtDelete))
                            .items("Are you sure you want to Delete this Comment?")
                            .positiveColor(context.getResources().getColor(R.color.colorAccent))
                            .positiveText(context.getResources().getString(R.string.txtDelete))
                            .negativeText(context.getResources().getString(R.string.cancelText))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(MaterialDialog dialog, DialogAction which) {

                                    DeleteMessage(commentObj.getComment_id(), commentObj.getTag());
                                    commentArrayList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, commentArrayList.size());
                                    notifyDataSetChanged();


                                }
                            })
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
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

        } else {
            holder.img_delete.setVisibility(View.GONE);
        }


        Glide.with(context)
                .load(commentObj.getLogo())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progress_senderImage.setVisibility(View.GONE);
                        holder.sender_image.setVisibility(View.VISIBLE);
                        Glide.with(context).load(commentObj.getLogo()).centerCrop().fitCenter().placeholder(R.drawable.defult_attende).into(holder.sender_image);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progress_senderImage.setVisibility(View.GONE);
                        holder.sender_image.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.sender_image);


        if (commentObj.getImage().equalsIgnoreCase("")) {
            holder.progressBar1.setVisibility(View.GONE);
            holder.img_comment.setVisibility(View.GONE);
        } else {
            Glide.with(context)
                    .load(commentObj.getImage())
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progressBar1.setVisibility(View.GONE);
                            holder.img_comment.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progressBar1.setVisibility(View.GONE);
                            holder.img_comment.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(holder.img_comment);
        }
    }

    private void DeleteMessage(String comm_id, String tag) {
        if (tag.equalsIgnoreCase("attendee_detail")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.attendee_delete_comment, Param.attendance_delete_comment(sessionManager.getToken(), comm_id, sessionManager.getEventId()), 0, true, this);
        } else if (tag.equalsIgnoreCase("public_message")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.public_commentDelete, Param.attendance_delete_comment(sessionManager.getToken(), comm_id, sessionManager.getEventId()), 0, true, this);
        } else if (tag.equalsIgnoreCase("speaker_detail")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.speaker_DeleteComment, Param.attendance_delete_comment(sessionManager.getToken(), comm_id, sessionManager.getEventId()), 0, true, this);
        } else if (tag.equalsIgnoreCase("private_message")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.public_commentDelete, Param.attendance_delete_comment(sessionManager.getToken(), comm_id, sessionManager.getEventId()), 0, true, this);
        } else if (tag.equalsIgnoreCase("exhibitor_detail")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.exhibitor_deleteComment, Param.exhibitor_DeleteComment(sessionManager.getEventId(), sessionManager.getToken(), comm_id), 0, true, this);
        } else if (tag.equalsIgnoreCase("photo_section")) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.photo_deleteComment, Param.photo_DeleteComment(sessionManager.getEventId(), sessionManager.getToken(), comm_id), 0, true, this);
        }
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL", jsonObject.toString());
                    } else {
                        Log.d("AITL", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_comment, img_delete;
        TextView user_sender, txt_time, txt_comment;
        CircleImageView sender_image;
        ProgressBar progressBar1, progress_senderImage;

        public ViewHolder(View itemView) {
            super(itemView);

            sender_image = (CircleImageView) itemView.findViewById(R.id.sender_image);
            img_comment = (ImageView) itemView.findViewById(R.id.img_comment);
            img_delete = (ImageView) itemView.findViewById(R.id.delete_img);
            user_sender = (TextView) itemView.findViewById(R.id.user_sender);
            txt_comment = (TextView) itemView.findViewById(R.id.txt_comment);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);
            progress_senderImage = (ProgressBar) itemView.findViewById(R.id.progress_senderImage);
        }
    }
}
