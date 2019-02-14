package com.allintheloop.Adapter.AdapterActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.ActivityModule.ActivityCommonClass;
import com.allintheloop.Bean.ActivityModule.ActivityOptionChecked;
import com.allintheloop.Bean.ActivityModule.Activity_SurveyResult;
import com.allintheloop.Bean.ActivityModule.InternalLike;
import com.allintheloop.Fragment.ActivityModule.Activtiy_ModuleAllInoneDesign_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityCommonAdapter extends RecyclerView.Adapter<ActivityCommonAdapter.ViewHolder> implements VolleyInterface {

    ArrayList<ActivityCommonClass> objectArrayList;
    Context context;
    SessionManager sessionManager;
    StaggeredGridLayoutManager.LayoutParams layoutParams;
    Activtiy_ModuleAllInoneDesign_Fragment activtiy_moduleAllInoneDesign_fragment;
    String ans = "";
    int pos = 0;

    public ActivityCommonAdapter(ArrayList<ActivityCommonClass> objectArrayList, Context context, SessionManager sessionManager, Activtiy_ModuleAllInoneDesign_Fragment activtiy_moduleAllInoneDesign_fragment) {
        this.objectArrayList = objectArrayList;
        this.context = context;
        this.sessionManager = sessionManager;
        this.activtiy_moduleAllInoneDesign_fragment = activtiy_moduleAllInoneDesign_fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activitynewallinone_fragment, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActivityCommonClass activityCommonClass = objectArrayList.get(position);
        holder.txt_userNme.setText(activityCommonClass.getName());
        loadProfileImageAndPost(holder, activityCommonClass);
        layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();

        if (activityCommonClass.getPostType().equalsIgnoreCase("1")
                || activityCommonClass.getPostType().equalsIgnoreCase("2")
                || activityCommonClass.getPostType().equalsIgnoreCase("3")
                || activityCommonClass.getPostType().equalsIgnoreCase("4")
                || activityCommonClass.getPostType().equalsIgnoreCase("7")) {
            holder.linearFeedLayout.setVisibility(View.VISIBLE);
            holder.linear_BannerView.setVisibility(View.GONE);
            holder.linear_surveyView.setVisibility(View.GONE);
            setFeedwiseData(holder, activityCommonClass);
            layoutParams.setFullSpan(false);
            holder.card_internal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("mainClassObj", activityCommonClass);
                    bundle.putInt("position", position);
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.activityDetailFeed;
                    ((MainActivity) context).loadFragment(bundle);
                }
            });

            holder.img_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("mainClassObj", activityCommonClass);
                    bundle.putInt("position", position);
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.activityDetailFeed;
                    ((MainActivity) context).loadFragment(bundle);
                }
            });

            if (activityCommonClass.getTime().equalsIgnoreCase("")) {
                holder.txt_time.setVisibility(View.GONE);
            } else {
                holder.txt_time.setVisibility(View.VISIBLE);
                holder.txt_time.setText(activityCommonClass.getTime());
            }

            if (activityCommonClass.getMessage() == null || activityCommonClass.getMessage().isEmpty()) {
                holder.txt_messageText.setVisibility(View.INVISIBLE);
                holder.txt_messageText.setText("");
            } else {
                holder.txt_messageText.setVisibility(View.VISIBLE);
                holder.txt_messageText.setText(activityCommonClass.getMessage());

            }

//            if (activityCommonClass.getIsLike().equalsIgnoreCase("1"))
//            {
//                holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
//            } else if (activityCommonClass.getIsLike().equalsIgnoreCase("0")) {
//                holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
//            }


            if (activityCommonClass.getSubTitle() != null && !activityCommonClass.getSubTitle().isEmpty()) {
                holder.txt_designaion.setVisibility(View.VISIBLE);
                holder.txt_designaion.setText(activityCommonClass.getSubTitle());
            } else {
                holder.txt_designaion.setVisibility(View.GONE);
            }
            if (activityCommonClass.getPostType().equalsIgnoreCase("1") || activityCommonClass.getPostType().equalsIgnoreCase("2")) {
                holder.img_like.setVisibility(View.VISIBLE);
                holder.img_message.setVisibility(View.VISIBLE);

                holder.frame_likeFullView.setVisibility(View.VISIBLE);
                holder.frame_messageFullView.setVisibility(View.VISIBLE);

                if (activityCommonClass.getComments() == null) {
                    holder.relativeimg_messageCount.setVisibility(View.GONE);
                    holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                } else {
                    int like = activityCommonClass.getComments().size();
                    holder.txt_message_count.setText("" + like);
                    if (like > 0) {
                        holder.relativeimg_messageCount.setVisibility(View.VISIBLE);
                        holder.img_message.setColorFilter(context.getResources().getColor(R.color.dark_gray));
                    } else {
                        holder.relativeimg_messageCount.setVisibility(View.GONE);
                        holder.img_message.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                    }
                }


                if (activityCommonClass.getLikes() == null && activityCommonClass.getLikes().size() == 0) {
                    holder.relativeimg_likeCount.setVisibility(View.GONE);
                    holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                } else {
                    int like = activityCommonClass.getLikes().size();
                    holder.txt_likeCount.setText(activityCommonClass.getLikeCount());
                    if (like > 0) {
                        holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                        if (activityCommonClass.getIsLike().equalsIgnoreCase("1")) {
                            holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
                        } else {
                            holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                        }

                    } else {
                        holder.relativeimg_likeCount.setVisibility(View.GONE);
                        holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                    }
                }
            } else {
                holder.relativeimg_likeCount.setVisibility(View.GONE);
                holder.img_like.setVisibility(View.GONE);
                holder.frame_likeFullView.setVisibility(View.GONE);

                holder.relativeimg_messageCount.setVisibility(View.GONE);
                holder.img_message.setVisibility(View.GONE);
                holder.frame_messageFullView.setVisibility(View.GONE);
            }


            holder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sessionManager.isLogin()) {
                        if (activityCommonClass.getPostType().equalsIgnoreCase("1") || activityCommonClass.getPostType().equalsIgnoreCase("2")) {
                            if (activityCommonClass.getIsLike().equalsIgnoreCase("1")) {
                                int like = Integer.parseInt(activityCommonClass.getLikeCount());
                                int temp = like - 1;
                                if (temp > 0) {
                                    holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                                } else {
                                    holder.relativeimg_likeCount.setVisibility(View.GONE);
                                }
                                holder.txt_likeCount.setText("" + temp);
                                activityCommonClass.setLikeCount("" + temp);
                                holder.img_like.setColorFilter(context.getResources().getColor(R.color.activity_heartcolor));
                                activityCommonClass.setIsLike("0");

                                if (activityCommonClass.getLikes().size() != 0) {
                                    activityCommonClass.getLikes().remove(activityCommonClass.getLikes().size() - 1);
                                }

                            } else {

                                int like = Integer.parseInt(activityCommonClass.getLikeCount());
                                int temp = like + 1;
                                if (temp > 0) {
                                    holder.relativeimg_likeCount.setVisibility(View.VISIBLE);
                                } else {
                                    holder.relativeimg_likeCount.setVisibility(View.GONE);
                                }
                                holder.txt_likeCount.setText("" + temp);
                                activityCommonClass.setLikeCount("" + temp);
                                holder.img_like.setColorFilter(context.getResources().getColor(R.color.red));
                                activityCommonClass.setIsLike("1");

                                InternalLike internalLike = new InternalLike();
                                if (sessionManager.getTitle() != null && !sessionManager.getTitle().isEmpty()) {
                                    if (sessionManager.getCompany_name() != null && !sessionManager.getCompany_name().isEmpty()) {
                                        internalLike.setCompanyName(sessionManager.getTitle() + " at " + sessionManager.getCompany_name());
                                    } else {
                                        internalLike.setCompanyName(sessionManager.getTitle());
                                    }
                                } else {
                                    internalLike.setCompanyName("");
                                }

                                internalLike.setDatetime("");
                                internalLike.setLogo(MyUrls.thumImgUrl + sessionManager.getImagePath());
                                internalLike.setName(sessionManager.getFirstName() + " " + sessionManager.getLastName());
                                activityCommonClass.getLikes().add(internalLike);

                            }
                            likeFeed(activityCommonClass.getType(), activityCommonClass.getId(), sessionManager.getUserId(), sessionManager.getEventId());
                        }
                    } else {
                        sessionManager.alertDailogLogin(context);
                    }
                }
            });


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
        } else {
            layoutParams.setFullSpan(true);
            if (activityCommonClass.getPostType().equalsIgnoreCase("6")) {

                holder.linearFeedLayout.setVisibility(View.GONE);
                holder.linear_BannerView.setVisibility(View.GONE);
                holder.linear_surveyView.setVisibility(View.VISIBLE);

                holder.txt_surveyQuestion.setText(activityCommonClass.getSurveyQuestion());
                if (activityCommonClass.getAns_submitted().equalsIgnoreCase("0")) {
                    setUpSurvey(activityCommonClass, holder, position);
                } else {
                    setUpSurveyResult(activityCommonClass, holder);
                }


            } else {
                holder.linearFeedLayout.setVisibility(View.GONE);
                holder.linear_BannerView.setVisibility(View.VISIBLE);
                holder.linear_surveyView.setVisibility(View.GONE);

                Glide.with(context).load(activityCommonClass.getAdvertImage()).asBitmap().listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        holder.img_bannerView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.img_bannerView.setVisibility(View.VISIBLE);
                        return false;
                    }
                }).into(new BitmapImageViewTarget(holder.img_bannerView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        holder.img_bannerView.setImageBitmap(resource);
                    }
                });

            }
            holder.linear_BannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activityCommonClass.getPostType().equalsIgnoreCase("6")) {
                        activtiy_moduleAllInoneDesign_fragment.openSurveyDialog((
                                ActivityCommonClass) objectArrayList.get(position));
                    } else {
                        activtiy_moduleAllInoneDesign_fragment.openPostUrl((ActivityCommonClass) objectArrayList.get(position));
                    }
                }
            });

        }

    }


    private void setUpSurvey(ActivityCommonClass activityCommonClass, ViewHolder holder, int position) {

        final String strRadioAns = "";
        List<String> optionlist = new ArrayList<>();
        List<ActivityOptionChecked> checkedArrayList = new ArrayList<>();
        optionlist.addAll(activityCommonClass.getSurveyOptions());
        if (holder.linear_option.getChildCount() != 0) {
            holder.linear_option.removeAllViews();
        }

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(80.0f);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {


            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            holder.btn_submit.setBackground(drawable);
            holder.btn_submit.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        } else {

            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            holder.btn_submit.setBackground(drawable);
            holder.btn_submit.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }
        try {
            RadioGroup radioGroup = new RadioGroup(context);
            ActivityOptionChecked optionChecked = new ActivityOptionChecked();
            for (int l = 0; l < optionlist.size(); l++) {
                RadioButton radioButton = new RadioButton(context);
                radioButton.setText(optionlist.get(l).toString());

                radioButton.setPadding(15, 10, 0, 10);
                radioButton.setTextSize(15);
                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                    radioButton.setTextColor(Color.parseColor(sessionManager.getTopBackColor()));
//                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                } else {
                    radioButton.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
//                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GlobalData.customeRadioColorForActivitySurveyChange(radioButton, sessionManager);
                }
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.leftMargin = 50;
                params1.topMargin = 30;
                params1.rightMargin = 50;
                radioGroup.addView(radioButton);
                radioButton.setLayoutParams(params1);


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                        ans = radioButton.getText().toString();
                    }
                });
            }
            holder.linear_option.addView(radioGroup);
            holder.linear_question_page.setVisibility(View.VISIBLE);
            holder.piechart.setVisibility(View.GONE);
            holder.txt_noResult.setVisibility(View.GONE);


            holder.btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (sessionManager.isLogin()) {
                        if (ans.isEmpty()) {
                            ToastC.show(context, "Please Select at least one option");
                        } else {
                            pos = position;
                            submitQuestion(activityCommonClass.getId(), ans);
                        }
                    } else {
                        sessionManager.alertDailogLogin((MainActivity) context);
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void submitQuestion(String id, String ans) {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.ansActivityFeedSurvey, Param.submitActivitySurvey(sessionManager.getEventId(), sessionManager.getUserId(), id, ans), 1, true, this);
        } else {
            ToastC.show(context, context.getString(R.string.noInernet));
        }

    }

    private void setUpSurveyResult(ActivityCommonClass activityCommonClass, ViewHolder holder) {
        ArrayList ansKey = new ArrayList<>();
        ArrayList ansValue = new ArrayList<>();
        ArrayList colorVaLUE = new ArrayList<>();
        float totalAnsValue = 0f;
        holder.linear_question_page.setVisibility(View.GONE);
        if (activityCommonClass.getSurveyResult().size() != 0) {
            holder.piechart.setVisibility(View.VISIBLE);
            holder.txt_noResult.setVisibility(View.GONE);
            for (int i = 0; i < activityCommonClass.getSurveyResult().size(); i++) {
                Activity_SurveyResult index = activityCommonClass.getSurveyResult().get(i);
                ansKey.add(index.getAnswerKey());
                ansValue.add(index.getAnswerValue());
                totalAnsValue = totalAnsValue + Float.parseFloat(index.getAnswerValue());
                colorVaLUE.add(Color.parseColor(index.getColor()));
            }
            setPiechart(ansKey, ansValue, colorVaLUE, totalAnsValue, holder);
        } else {
            holder.txt_noResult.setText(activityCommonClass.getResult_message());
            holder.piechart.setVisibility(View.GONE);
            holder.txt_noResult.setVisibility(View.VISIBLE);
        }
    }


    private void setPiechart(ArrayList ansKey, ArrayList ansValue, ArrayList colorVaLUE, float totalAnsValue, ViewHolder holder) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float mult = 100;
        for (int i = 0; i < ansValue.size(); i++) {
            Float value = Float.parseFloat(ansValue.get(i).toString()) * mult / totalAnsValue;
            entries.add(new PieEntry(value, ansKey.get(i).toString()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorVaLUE);
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(17f);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(1f);
        dataSet.setValueLinePart2Length(0.9f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextColor(Color.parseColor("#ffffff"));

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        holder.piechart.getLegend().setWordWrapEnabled(true);
        holder.piechart.setData(data);
        holder.piechart.setDrawEntryLabels(false);
        holder.piechart.setDrawHoleEnabled(false);
        holder.piechart.setRotationEnabled(false);
        holder.piechart.getDescription().setEnabled(false);
    }


    private void setFeedwiseData(ViewHolder holder, ActivityCommonClass activityCommonClass) {

        switch (activityCommonClass.getPostType()) {
            case "1":
                holder.frame_messageFullView.setVisibility(View.VISIBLE);
                holder.frame_likeFullView.setVisibility(View.VISIBLE);
                holder.img_like.setClickable(true);
                holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
                holder.img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
                holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_internal_img));
                break;
            case "2":
                holder.frame_messageFullView.setVisibility(View.VISIBLE);
                holder.frame_likeFullView.setVisibility(View.VISIBLE);
                holder.img_like.setClickable(true);
                holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
                holder.img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
                holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tab_alerts));
                break;
            case "3":
                holder.txt_designaion.setVisibility(View.GONE);
                holder.frame_messageFullView.setVisibility(View.VISIBLE);
                holder.frame_likeFullView.setVisibility(View.VISIBLE);
                holder.txt_profileName.setVisibility(View.GONE);
                holder.img_like.setClickable(false);
                holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
                holder.img_message.setImageDrawable(context.getResources().getDrawable(R.drawable.message_activity));
                holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.facebook_activity));
                break;
            case "4":
                holder.txt_designaion.setVisibility(View.GONE);
                holder.frame_messageFullView.setVisibility(View.GONE);
                holder.frame_likeFullView.setVisibility(View.VISIBLE);
                holder.img_like.setClickable(false);
                holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
                holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_twitter));
                break;
            case "5":
//                holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.activity_internal_img));
                break;
            case "6":

                break;
            case "7":
                holder.txt_designaion.setVisibility(View.GONE);
                holder.frame_messageFullView.setVisibility(View.VISIBLE);
                holder.frame_messageFullView.setClickable(false);
                holder.frame_likeFullView.setVisibility(View.VISIBLE);
                holder.img_like.setClickable(false);
                holder.img_like.setImageDrawable(context.getResources().getDrawable(R.drawable.hearts_acitvity));
                holder.img_social.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_instagram_logo));

                break;
            default:
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        ActivityCommonClass activityCommonClass = (ActivityCommonClass) objectArrayList.get(position);
        return activityCommonClass.getPostType().equalsIgnoreCase("5") ? 0 : 1;
    }

    private void likeFeed(String type, String id, String userId, String eventid) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.ActivityLikeFeed, Param.activityLikeFeed(type, id, userId, eventid), 0, false, this);
    }

    @Override
    public int getItemCount() {
        return objectArrayList.size();
    }


    private void loadProfileImageAndPost(ViewHolder holder, ActivityCommonClass activityCommonClass) {
        GradientDrawable drawable = new GradientDrawable();
        if (activityCommonClass.getLogo() != null) {
            if (activityCommonClass.getLogo().equalsIgnoreCase("")) {
                holder.img_userProfile.setVisibility(View.GONE);
                holder.txt_profileName.setVisibility(View.VISIBLE);

                if (!(activityCommonClass.getName().equalsIgnoreCase(""))) {
                    holder.txt_profileName.setText(String.valueOf(activityCommonClass.getName().charAt(0)));
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
                    Glide.with(context).load(activityCommonClass.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
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


        if (activityCommonClass.getImage() != null && activityCommonClass.getImage().size() != 0) {
            try {
                Glide.with(context).load(activityCommonClass.getImage().get(0).toString()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
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
    }

    public void updateList(ArrayList<ActivityCommonClass> activityArrayList) {
        try {

            this.objectArrayList = activityArrayList;
            notifyItemChanged(0);
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
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        List<Activity_SurveyResult> activity_surveyResults = new ArrayList<>();
                        JSONObject jsonObject1Result = jsonObject.getJSONObject("data");

                        JSONArray jsonArray = jsonObject1Result.getJSONArray("survey_result");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                Activity_SurveyResult activity_surveyResult = new Activity_SurveyResult();
                                activity_surveyResult.setAnswerKey(jsonObject1.getString("answer_key"));
                                activity_surveyResult.setAnswerValue(jsonObject1.getString("answer_value"));
                                activity_surveyResult.setColor(jsonObject1.getString("color"));
                                activity_surveyResults.add(activity_surveyResult);
                            }
                        }
                        objectArrayList.get(pos).setResult_message(jsonObject1Result.getString("result_message"));
                        objectArrayList.get(pos).setSurveyResult(activity_surveyResults);
                        objectArrayList.get(pos).setPostType("6");
                        objectArrayList.get(pos).setAns_submitted("1");
                        notifyDataSetChanged();
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
        LinearLayout linearFeedLayout, linear_BannerView, linear_surveyView;
        ImageView img_bannerView;


        BoldTextView txt_surveyQuestion;
        LinearLayout linear_question_page, linear_option;
        Button btn_submit;
        PieChart piechart;
        BoldTextView txt_noResult;

        public ViewHolder(View itemView) {
            super(itemView);

            img_userProfile = itemView.findViewById(R.id.img_userProfile);
            img_post = itemView.findViewById(R.id.img_post);
            img_like = itemView.findViewById(R.id.img_like);
            img_message = itemView.findViewById(R.id.img_message);
            img_social = itemView.findViewById(R.id.img_social);
            img_bannerView = itemView.findViewById(R.id.img_bannerView);
            card_internal = itemView.findViewById(R.id.card_internal);
            progressBar1 = itemView.findViewById(R.id.progressBar1);

            txt_profileName = itemView.findViewById(R.id.txt_profileName);
            txt_userNme = itemView.findViewById(R.id.txt_userNme);
            txt_designaion = itemView.findViewById(R.id.txt_designaion);
            txt_messageText = itemView.findViewById(R.id.txt_messageText);
            txt_likeCount = itemView.findViewById(R.id.txt_likeCount);
            txt_time = itemView.findViewById(R.id.txt_time);
            txt_message_count = itemView.findViewById(R.id.txt_message_count);
            txt_seeMore = itemView.findViewById(R.id.txt_seeMore);
            relativeimg_likeCount = itemView.findViewById(R.id.relativeimg_likeCount);
            relativeimg_messageCount = itemView.findViewById(R.id.relativeimg_messageCount);
            linear_detailView = itemView.findViewById(R.id.linear_detailView);
            linearTopView = itemView.findViewById(R.id.linearTopView);
            linearFeedLayout = itemView.findViewById(R.id.linearFeedLayout);
            linear_BannerView = itemView.findViewById(R.id.linear_BannerView);
            linear_surveyView = itemView.findViewById(R.id.linear_surveyView);

            frame_likeFullView = itemView.findViewById(R.id.frame_likeFullView);
            frame_messageFullView = itemView.findViewById(R.id.frame_messageFullView);


            txt_surveyQuestion = itemView.findViewById(R.id.txt_surveyQuestion);
            txt_noResult = itemView.findViewById(R.id.txt_noResult);
            linear_question_page = itemView.findViewById(R.id.linear_question_page);
            linear_option = itemView.findViewById(R.id.linear_option);
            btn_submit = itemView.findViewById(R.id.btn_submit);
            piechart = itemView.findViewById(R.id.piechart);

        }
    }


}
