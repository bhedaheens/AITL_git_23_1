package com.allintheloop.Adapter.AdapterActivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
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

/*
public class AdapterAcitivty_newAllInOne extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements VolleyInterface {


    ArrayList<Object> combileArrays;
    Context context;
    SessionManager sessionManager;
    ViewHolder viewHolder;

    public AdapterAcitivty_newAllInOne(ArrayList<Object> combileArrays, Context context, SessionManager sessionManager) {
        this.combileArrays = combileArrays;
        this.context = context;
        this.sessionManager = sessionManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activitynewallinone_fragment, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity_sponsorview, parent, false);
            return new ViewHolder(view);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return combileArrays.get(position) != null ? 1 : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder && combileArrays.get(position) != null) {
            ViewHolder holder = (ViewHolder) viewHolder;
            if (combileArrays.get(position) instanceof Activity_Internal_Feed)
            {
                Activity_Internal_Feed activityInternalFeedObj = (Activity_Internal_Feed) combileArrays.get(position);
                try {

                    holder.linearTopView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityInternalDetailRedirection(activityInternalFeedObj, position);
                        }
                    });
                    loadInternalFeedData(activityInternalFeedObj, holder, position);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (combileArrays.get(position) instanceof Activity_Facebook_Feed) {
                Activity_Facebook_Feed activity_facebook_feed = (Activity_Facebook_Feed) combileArrays.get(position);
                try {

                    holder.linearTopView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityFacebookDetailRedirection(activity_facebook_feed, position);
                        }
                    });


                    loadFacebookFeedData(activity_facebook_feed, holder, position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (combileArrays.get(position) instanceof Activity_Instagram_Feed) {
                Activity_Instagram_Feed activityInstagramFeed = (Activity_Instagram_Feed) combileArrays.get(position);
                try {

                    holder.linearTopView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityInstagramDetailRedirection(activityInstagramFeed, position);
                        }
                    });


                    loadInstagramFeedData(activityInstagramFeed, holder, position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (combileArrays.get(position) instanceof Activity_Twitter_Feed) {
                Activity_Twitter_Feed activityTwitterFeed = (Activity_Twitter_Feed) combileArrays.get(position);
                try {
                    holder.linearTopView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            activityTwitterDetailRedirection(activityTwitterFeed, position);
                        }
                    });

                    loadTwitterFeedData(activityTwitterFeed, holder, position);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (holder.txt_messageText.getText().length() > 120) {
                        holder.txt_seeMore.setVisibility(View.VISIBLE);
                    } else {
                        holder.txt_seeMore.setVisibility(View.GONE);
                    }
                }
            }, 1000);
        }
    }

    private void loadTwitterFeedData(Activity_Twitter_Feed activityTwitterFeed, ViewHolder holder, int position) {
        holder.txt_designaion.setVisibility(View.GONE);
        holder.frame_messageFullView.setVisibility(View.GONE);
        holder.frame_likeFullView.setVisibility(View.VISIBLE);
        holder.img_like.setClickable(false);
        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
        holder.txt_userNme.setText(activityTwitterFeed.getUser().getName());
        holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_twitter));

        holder.txt_time.setText(activityTwitterFeed.getCreatedAt());

        if (activityTwitterFeed.getText() == null || activityTwitterFeed.getText().isEmpty()) {
            holder.txt_messageText.setVisibility(View.INVISIBLE);
        } else {
            holder.txt_messageText.setVisibility(View.VISIBLE);
            holder.txt_messageText.setText("" + activityTwitterFeed.getText());
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("Bhavdip LineCount",""+holder.txt_messageText.getLineCount());
//            }
//        });

        try {
            if (activityTwitterFeed.getFavoriteCount() == null || activityTwitterFeed.getFavoriteCount().isEmpty()) {
                holder.relativeimg_likeCount.setVisibility(View.GONE);
                holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
            } else {
                int like = Integer.parseInt(activityTwitterFeed.getFavoriteCount());
                holder.txt_likeCount.setText("" + like);
                if (like > 0) {
                    holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                    holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
                } else {
                    holder.relativeimg_likeCount.setVisibility(View.GONE);
                    holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        holder.img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityTwitterDetailRedirection(activityTwitterFeed, position);
            }
        });
        holder.txt_messageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityTwitterDetailRedirection(activityTwitterFeed, position);
            }
        });


        if (activityTwitterFeed.getMedia() != null && activityTwitterFeed.getMedia().size() != 0) {
            try {
                Glide.with(context).load(activityTwitterFeed.getMedia().get(0).toString()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.img_post) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_post.setImageBitmap(resource);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_post.setVisibility(View.GONE);
        }

        if (activityTwitterFeed.getUser().getProfileImageUrl() != null) {

            try {
                Glide.with(context).load(activityTwitterFeed.getUser().getProfileImageUrl()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_userProfile.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_userProfile.setVisibility(View.VISIBLE);
                        holder.txt_profileName.setVisibility(View.GONE);
                        return false;
                    }
                }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.img_userProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_userProfile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void loadInstagramFeedData(Activity_Instagram_Feed activityInstagramFeed, ViewHolder holder, int position) {
        holder.txt_designaion.setVisibility(View.GONE);
        holder.frame_messageFullView.setVisibility(View.VISIBLE);
        holder.frame_messageFullView.setClickable(false);
        holder.frame_likeFullView.setVisibility(View.VISIBLE);
        holder.img_like.setClickable(false);
        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
        holder.img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
        holder.txt_profileName.setVisibility(View.GONE);
        holder.txt_time.setText(activityInstagramFeed.getCreatedTime());
        holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_instagram_logo));
        holder.txt_userNme.setText(activityInstagramFeed.getUser().getUsername());
        if (activityInstagramFeed.getCaption().getText() == null || activityInstagramFeed.getCaption().getText().isEmpty()) {
            holder.txt_messageText.setVisibility(View.INVISIBLE);
        } else {
            holder.txt_messageText.setVisibility(View.VISIBLE);
            holder.txt_messageText.setText(activityInstagramFeed.getCaption().getText());
        }


        try {

            if (activityInstagramFeed.getLikes().getCount() == null || activityInstagramFeed.getLikes().getCount().isEmpty()) {
                holder.relativeimg_likeCount.setVisibility(View.GONE);
                holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
            } else {
                int like = Integer.parseInt(activityInstagramFeed.getLikes().getCount());
                holder.txt_likeCount.setText("" + like);
                if (like > 0) {
                    holder.relativeimg_likeCount.setVisibility(View.VISIBLE);

                    holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
                } else {
                    holder.relativeimg_likeCount.setVisibility(View.GONE);
                    holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                }
            }


            holder.frame_messageFullView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityInstagramDetailRedirection(activityInstagramFeed, position);
                }
            });

            holder.img_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activityInstagramDetailRedirection(activityInstagramFeed, position);
                }
            });

            holder.txt_messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityInstagramDetailRedirection(activityInstagramFeed, position);
                }
            });

            if (activityInstagramFeed.getComments().getCount() == null || activityInstagramFeed.getComments().getCount().isEmpty()) {
                holder.relativeimg_messageCount.setVisibility(View.GONE);
                holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
            } else {
                int like = Integer.parseInt(activityInstagramFeed.getComments().getCount());
                holder.txt_message_count.setText("" + like);
                if (like > 0) {
                    holder.relativeimg_messageCount.setVisibility(View.VISIBLE);
                    holder.img_message.setColorFilter(context.getResources().getColor(R.color.dark_gray));
                } else {
                    holder.relativeimg_messageCount.setVisibility(View.GONE);
                    holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (activityInstagramFeed.getImages() != null && activityInstagramFeed.getImages().getStandardResolution() != null && !activityInstagramFeed.getImages().getStandardResolution().getUrl().isEmpty()) {
                Glide.with(context).load(activityInstagramFeed.getImages().getStandardResolution().getUrl()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.img_post) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_post.setImageBitmap(resource);
                    }
                });
            } else {
                holder.img_post.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (activityInstagramFeed.getUser().getProfilePicture() != null) {

            try {
                Glide.with(context).load(activityInstagramFeed.getUser().getProfilePicture()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_userProfile.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_userProfile.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.img_userProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_userProfile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadFacebookFeedData(Activity_Facebook_Feed activityFacebookFeedObj, ViewHolder holder, int position) {

        holder.txt_designaion.setVisibility(View.GONE);
        holder.frame_messageFullView.setVisibility(View.VISIBLE);
        holder.frame_likeFullView.setVisibility(View.VISIBLE);
        holder.txt_profileName.setVisibility(View.GONE);
        holder.img_like.setClickable(false);
        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
        holder.img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
        holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.facebook_activity));

        holder.txt_time.setText(activityFacebookFeedObj.getCreatedTime());

        if ((activityFacebookFeedObj.getFrom() != null)) {
            holder.txt_userNme.setVisibility(View.VISIBLE);
            holder.txt_userNme.setText(activityFacebookFeedObj.getFrom().getName());
        } else {
            holder.txt_userNme.setVisibility(View.GONE);
        }
        if (activityFacebookFeedObj.getMessage() == null || activityFacebookFeedObj.getMessage().isEmpty()) {
            holder.txt_messageText.setVisibility(View.INVISIBLE);
        } else {
            holder.txt_messageText.setVisibility(View.VISIBLE);
            holder.txt_messageText.setText(activityFacebookFeedObj.getMessage());
        }
        if (activityFacebookFeedObj.getAttachments() != null && activityFacebookFeedObj.getAttachments().getMedia() != null && activityFacebookFeedObj.getAttachments().getMedia().size() != 0) {
            try {
                Glide.with(context).load(activityFacebookFeedObj.getAttachments().getMedia().get(0).getSrc()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.img_post) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_post.setImageBitmap(resource);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_post.setVisibility(View.GONE);
        }


        holder.frame_messageFullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString("type", "2");
//                bundle.putParcelable("obj", activityFacebookFeedObj);
//                bundle.putInt("position", position);
//                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                GlobalData.CURRENT_FRAG = GlobalData.ActivityCommentView_Fragment;
//                ((MainActivity) context).loadFragment(bundle);


                activityFacebookDetailRedirection(activityFacebookFeedObj, position);
            }
        });

        try {
            if (activityFacebookFeedObj.likes == null && activityFacebookFeedObj.likes.getData() == null && activityFacebookFeedObj.likes.getData().size() == 0) {
                holder.relativeimg_likeCount.setVisibility(View.GONE);
                holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
            } else {
                int like = activityFacebookFeedObj.likes.getData().size();
                holder.txt_likeCount.setText("" + activityFacebookFeedObj.likes.getData().size());
                if (like > 0) {
                    holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                    holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
                } else {
                    holder.relativeimg_likeCount.setVisibility(View.GONE);
                    holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                }
            }


            if (activityFacebookFeedObj.comments.getData() == null || activityFacebookFeedObj.comments.getData().size() == 0) {
                holder.relativeimg_messageCount.setVisibility(View.GONE);
                holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));

            } else {
                int like = activityFacebookFeedObj.comments.getData().size();
                holder.txt_message_count.setText("" + like);
                if (like > 0) {
                    holder.relativeimg_messageCount.setVisibility(View.VISIBLE);
                    holder.img_message.setColorFilter(context.getResources().getColor(R.color.dark_gray));
                } else {
                    holder.relativeimg_messageCount.setVisibility(View.GONE);
                    holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


        if (activityFacebookFeedObj.getLogo() != null) {
            try {
                Glide.with(context).load(activityFacebookFeedObj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_userProfile.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_userProfile.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.img_userProfile) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_userProfile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder.txt_messageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityFacebookDetailRedirection(activityFacebookFeedObj, position);
            }
        });

        holder.img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityFacebookDetailRedirection(activityFacebookFeedObj, position);
            }
        });

    }



    private void loadInternalFeedData(Activity_Internal_Feed activityInternalFeedObj, ViewHolder holder, int position) {
        holder.txt_userNme.setText(activityInternalFeedObj.getFirstname() + " " + activityInternalFeedObj.getLastname());
        holder.frame_messageFullView.setVisibility(View.VISIBLE);
        holder.frame_likeFullView.setVisibility(View.VISIBLE);
        holder.img_like.setClickable(true);
        holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
        holder.img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
        if (activityInternalFeedObj.getIsAlert().equalsIgnoreCase("0")) {
            holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_internal_img));
        } else {
            holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tab_alerts));
        }

        if (activityInternalFeedObj.getIsLike().equalsIgnoreCase("1")) {
            holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
        } else {
            holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
        }

        if (activityInternalFeedObj.getTime().equalsIgnoreCase("")) {
            holder.txt_time.setVisibility(View.GONE);
        } else {
            holder.txt_time.setVisibility(View.VISIBLE);
            holder.txt_time.setText(activityInternalFeedObj.getTime());
        }

        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sessionManager.isLogin()) {

                    if (activityInternalFeedObj.getIsLike().equalsIgnoreCase("1")) {


                        likeFeed(activityInternalFeedObj.getType(), activityInternalFeedObj.getId(), sessionManager.getUserId(), sessionManager.getEventId());
                        int like = Integer.parseInt(activityInternalFeedObj.getLikeCount());
                        int temp = like - 1;
                        if (temp > 0) {
                            holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                        } else {
                            holder.relativeimg_likeCount.setVisibility(View.GONE);
                        }
                        holder.txt_likeCount.setText("" + temp);
                        activityInternalFeedObj.setLikeCount("" + temp);
                        holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                        activityInternalFeedObj.setIsLike("0");

//                        try
//                        {
//                            JSONObject jsonObject=new JSONObject();
//                            jsonObject.put("name",sessionManager.getFirstName()+" "+sessionManager.getLastName());
//                            jsonObject.put("Company_name",sessionManager.getTitle()+" "+sessionManager.getCompany_name());
//                            jsonObject.put("title","");
//                            jsonObject.put("logo",sessionManager.getImagePath());
//                            jsonObject.put("user_id",sessionManager.getUserId());
//                            jsonObject.put("datetime","");
//
//
//                        }catch (Exception e)
//                        {
//                            e.printStackTrace();
//                        }


                    } else {
                        likeFeed(activityInternalFeedObj.getType(), activityInternalFeedObj.getId(), sessionManager.getUserId(), sessionManager.getEventId());
                        int like = Integer.parseInt(activityInternalFeedObj.getLikeCount());
                        int temp = like + 1;
                        if (temp > 0) {
                            holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                        } else {
                            holder.relativeimg_likeCount.setVisibility(View.GONE);
                        }
                        holder.txt_likeCount.setText("" + temp);
                        activityInternalFeedObj.setLikeCount("" + temp);
                        holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
                        activityInternalFeedObj.setIsLike("1");
                    }
                } else {
                    sessionManager.alertDailogLogin(context);
                }
            }
        });
        if (activityInternalFeedObj.getImage() != null && activityInternalFeedObj.getImage().size() != 0) {
            try {
                Glide.with(context).load(MyUrls.Imgurl + activityInternalFeedObj.getImage().get(0).toString()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_post.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.img_post) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_post.setImageBitmap(resource);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.img_post.setVisibility(View.GONE);
        }


        holder.frame_messageFullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityInternalDetailRedirection(activityInternalFeedObj, position);
            }
        });

        holder.img_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityInternalDetailRedirection(activityInternalFeedObj, position);
            }
        });

        holder.txt_messageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityInternalDetailRedirection(activityInternalFeedObj, position);
            }
        });

        GradientDrawable drawable = new GradientDrawable();
        if (activityInternalFeedObj.getLogo() != null)
        {
            if (activityInternalFeedObj.getLogo().equalsIgnoreCase("")) {
                holder.img_userProfile.setVisibility(View.GONE);
                holder.txt_profileName.setVisibility(View.VISIBLE);

                if (!(activityInternalFeedObj.getFirstname().equalsIgnoreCase(""))) {
                    if (!(activityInternalFeedObj.getLastname().equalsIgnoreCase(""))) {
                        String name = activityInternalFeedObj.getFirstname().charAt(0) + "" + activityInternalFeedObj.getLastname().charAt(0);
                        holder.txt_profileName.setText(name);
                    } else {
                        holder.txt_profileName.setText("" + activityInternalFeedObj.getFirstname().charAt(0));
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
                    Glide.with(context).load(MyUrls.Imgurl + activityInternalFeedObj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.img_userProfile.setVisibility(View.VISIBLE);
                            holder.txt_profileName.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.img_userProfile.setVisibility(View.VISIBLE);
                            holder.txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).placeholder(context.getResources().getDrawable(R.drawable.profile)).into(new BitmapImageViewTarget(holder.img_userProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            holder.img_userProfile.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (!(activityInternalFeedObj.getAgendaId().equalsIgnoreCase(""))) {

            holder.txt_messageText.setTextColor(context.getResources().getColor(R.color.survey_question));
        } else {
            holder.txt_messageText.setTextColor(context.getResources().getColor(R.color.black));
        }


//        holder.txt_messageText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (!(activityInternalFeedObj.getAgendaId().equalsIgnoreCase(""))) {
//                    sessionManager.agenda_id(activityInternalFeedObj.getAgendaId());
//                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                    GlobalData.CURRENT_FRAG = GlobalData.View_Agenda_Fragment;
//                    ((MainActivity) context).loadFragment();
//                }
//            }
//        });


        if (activityInternalFeedObj.getMessage() == null || activityInternalFeedObj.getMessage().isEmpty()) {
            holder.txt_messageText.setVisibility(View.INVISIBLE);
            holder.txt_messageText.setText("");
        } else {
            holder.txt_messageText.setVisibility(View.VISIBLE);
            holder.txt_messageText.setText(activityInternalFeedObj.message);

        }

        if (activityInternalFeedObj.getLikeCount() == null || activityInternalFeedObj.getLikeCount().equalsIgnoreCase("")) {
            holder.relativeimg_likeCount.setVisibility(View.GONE);
            holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
        } else {

            int like = Integer.parseInt(activityInternalFeedObj.getLikeCount());
            holder.txt_likeCount.setText(activityInternalFeedObj.getLikeCount());
            if (like > 0) {
                holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
            } else {
                holder.relativeimg_likeCount.setVisibility(View.GONE);
                holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
            }
        }

        if (activityInternalFeedObj.getComments() == null) {
            holder.relativeimg_messageCount.setVisibility(View.GONE);
            holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
        } else {
            int like = activityInternalFeedObj.getComments().size();
            holder.txt_message_count.setText("" + like);
            if (like > 0) {
                holder.relativeimg_messageCount.setVisibility(View.VISIBLE);

                holder.img_message.setColorFilter(context.getResources().getColor(R.color.dark_gray));
            } else {
                holder.relativeimg_messageCount.setVisibility(View.GONE);
                holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
            }
        }
        if (activityInternalFeedObj.title != null) {
            if (!(activityInternalFeedObj.title.equalsIgnoreCase(""))) {

                holder.txt_designaion.setVisibility(View.VISIBLE);
                if (activityInternalFeedObj.companyName != null) {
                    if (!(activityInternalFeedObj.companyName.equalsIgnoreCase(""))) {
                        holder.txt_designaion.setText(activityInternalFeedObj.title + " at " + activityInternalFeedObj.companyName);
                    } else {
                        holder.txt_designaion.setText(activityInternalFeedObj.title);
                    }
                } else {
                    holder.txt_designaion.setText(activityInternalFeedObj.title);
                }
            } else {
                holder.txt_designaion.setVisibility(View.GONE);
            }
        }
    }

    private void activityInstagramDetailRedirection(Activity_Instagram_Feed activityInstagramFeed, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "3");
        bundle.putParcelable("obj", activityInstagramFeed);
        bundle.putInt("position", position);
        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
        GlobalData.CURRENT_FRAG = GlobalData.activityDetailFeed;
        ((MainActivity) context).loadFragment(bundle);
    }

    private void activityTwitterDetailRedirection(Activity_Twitter_Feed activityTwitterFeed, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "4");
        bundle.putParcelable("obj", activityTwitterFeed);
        bundle.putInt("position", position);
        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
        GlobalData.CURRENT_FRAG = GlobalData.activityDetailFeed;
        ((MainActivity) context).loadFragment(bundle);
    }

    private void activityInternalDetailRedirection(Activity_Internal_Feed activityInternalFeedObj, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "1");
        bundle.putParcelable("obj", activityInternalFeedObj);
        bundle.putInt("position", position);
        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
        GlobalData.CURRENT_FRAG = GlobalData.activityDetailFeed;
        ((MainActivity) context).loadFragment(bundle);
    }

    private void activityFacebookDetailRedirection(Activity_Facebook_Feed activityFacebookFeedObj, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("type", "2");
        bundle.putParcelable("obj", activityFacebookFeedObj);
        bundle.putInt("position", position);
        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
        GlobalData.CURRENT_FRAG = GlobalData.activityDetailFeed;
        ((MainActivity) context).loadFragment(bundle);
    }

    private void likeFeed(String type, String id, String userId, String eventid) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.ActivityLikeFeed, Param.activityLikeFeed(type, id, userId, eventid), 0, true, this);
    }


    @Override
    public int getItemCount() {
        return combileArrays.size();
    }

    public void updateList(ArrayList<Object> objectArrayList) {
        try {
//            this.combileArrays.addAll(objectArrayList);
            //  notifyItemChanged(0);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_userProfile, img_post;
        ImageView img_like, img_message;
        ImageView img_social;
        ProgressBar progressBar1;
        TextView txt_profileName, txt_userNme, txt_designaion, txt_likeCount, txt_message_count, txt_seeMore, txt_time;
        RelativeLayout relativeimg_likeCount, relativeimg_messageCount;
        FrameLayout frame_likeFullView, frame_messageFullView;
        LinearLayout linear_detailView, linearTopView;
        CardView card_internal;
        BoldTextView txt_messageText;

        public ViewHolder(View itemView) {
            super(itemView);
            img_userProfile = (ImageView) itemView.findViewById(R.id.img_userProfile);
            img_post = (ImageView) itemView.findViewById(R.id.img_post);
            img_like = (ImageView) itemView.findViewById(R.id.img_like);
            img_message = (ImageView) itemView.findViewById(R.id.img_message);
            img_social = (ImageView) itemView.findViewById(R.id.img_social);
            card_internal = (CardView) itemView.findViewById(R.id.card_internal);
            progressBar1 = (ProgressBar) itemView.findViewById(R.id.progressBar1);

            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            txt_userNme = (TextView) itemView.findViewById(R.id.txt_userNme);
            txt_designaion = (TextView) itemView.findViewById(R.id.txt_designaion);
            txt_messageText = (BoldTextView) itemView.findViewById(R.id.txt_messageText);
            txt_likeCount = (TextView) itemView.findViewById(R.id.txt_likeCount);
            txt_time = (TextView) itemView.findViewById(R.id.txt_time);
            txt_message_count = (TextView) itemView.findViewById(R.id.txt_message_count);
            txt_seeMore = (TextView) itemView.findViewById(R.id.txt_seeMore);
            relativeimg_likeCount = (RelativeLayout) itemView.findViewById(R.id.relativeimg_likeCount);
            relativeimg_messageCount = (RelativeLayout) itemView.findViewById(R.id.relativeimg_messageCount);
            linear_detailView = (LinearLayout) itemView.findViewById(R.id.linear_detailView);
            linearTopView = (LinearLayout) itemView.findViewById(R.id.linearTopView);

            frame_likeFullView = (FrameLayout) itemView.findViewById(R.id.frame_likeFullView);
            frame_messageFullView = (FrameLayout) itemView.findViewById(R.id.frame_messageFullView);
        }
    }

}
*/
