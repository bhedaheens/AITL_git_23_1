package com.allintheloop.Adapter.AdapterActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.ActivityModule.ActivityCommentClass;
import com.allintheloop.Fragment.FacebookModule.FacebookDailog_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.RoundedImageConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

public class Adapter_InternalCommentMessageViewType extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int OTHER = 1;
    ArrayList<ActivityCommentClass> objectsArrayList;
    Context context;
    String userId = "";
    boolean isInternal = false;
    private int viewType;
    private int SELF = 2;

    public Adapter_InternalCommentMessageViewType(ArrayList<ActivityCommentClass> objectsArrayList, Context context, String userId, boolean isInternal) {
        this.objectsArrayList = objectsArrayList;
        this.context = context;
        this.userId = userId;
        this.isInternal = isInternal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        this.viewType = viewType;
        if (viewType == SELF) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_self_commentview, parent, false);
        } else if (viewType == OTHER) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_other_commentview, parent, false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder && objectsArrayList.get(position) != null) {

            ViewHolder viewHolder = (ViewHolder) holder;
            ActivityCommentClass dataobj = objectsArrayList.get(position);
            if (dataobj.getName() != null && !dataobj.getName().isEmpty()) {
                viewHolder.txt_name.setVisibility(View.VISIBLE);
                viewHolder.txt_name.setText(dataobj.getName() + ",");
            } else {
                viewHolder.txt_name.setVisibility(View.GONE);
            }
            viewHolder.txt_time.setText(dataobj.getDatetime());
            viewHolder.txt_message.setText(dataobj.getComment());

            String url = "";
//            if (isInternal) {
//                url = MyUrls.Imgurl + dataobj.getLogo();
//            } else {
//                url = dataobj.getLogo();
//            }
            Glide.with(context).load(dataobj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                @Override
                public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                    viewHolder.img_profile.setVisibility(View.VISIBLE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    viewHolder.img_profile.setVisibility(View.VISIBLE);
                    return false;
                }
            }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(viewHolder.img_profile) {
                @Override
                protected void setResource(Bitmap resource) {
                    viewHolder.img_profile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                }
            });


            viewHolder.img_receview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> imgStringarray = new ArrayList<>();
                    FragmentActivity activity = (FragmentActivity) (context);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    Bundle bundle = new Bundle();
                    FacebookDailog_Fragment fragment = new FacebookDailog_Fragment();
                    bundle.putInt("position", position);
                    imgStringarray.add(dataobj.getCommentImage().get(0).toString());
                    bundle.putString("isActivity", "0");
                    bundle.putStringArrayList("img_array", imgStringarray);
                    fragment.setArguments(bundle);
                    fragment.show(fm, "DialogFragment");
                }
            });

            if (dataobj.getCommentImage().size() > 0) {
                String commentImageurl = "";
                commentImageurl = dataobj.getCommentImage().get(0).toString();
                Glide.with(context).load(commentImageurl).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        viewHolder.img_receview.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        viewHolder.img_receview.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(viewHolder.img_receview) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        viewHolder.img_receview.setImageBitmap(RoundedImageConverter.getRoundedCornerBitmap(resource, Color.WHITE, 10, 0, context));
                    }
                });
            } else {
                viewHolder.img_receview.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return objectsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        ActivityCommentClass obj = objectsArrayList.get(position);

        if (obj.getUserId().equalsIgnoreCase(userId)) {
            return SELF;
        } else {
            return OTHER;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_time;
        BoldTextView txt_message;
        ImageView img_receview, img_profile;
        LinearLayout linear_imagepost;

        public ViewHolder(View view) {
            super(view);
            txt_message = (BoldTextView) itemView.findViewById(R.id.txt_message);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            img_profile = (ImageView) itemView.findViewById(R.id.img_profile);
            img_receview = (ImageView) itemView.findViewById(R.id.img_receview);
        }
    }
}
