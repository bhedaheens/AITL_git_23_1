package com.allintheloop.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.GamificationLeaderBoardList;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.RoundedImageConverter;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 16/6/17.
 */

public class GamificationLeaderBord_Adapter extends RecyclerView.Adapter<GamificationLeaderBord_Adapter.ViewHolder> {

    ArrayList<GamificationLeaderBoardList> gamificationLeaderBoardLists;
    Context context;
    SessionManager sessionManager;
    DefaultLanguage.DefaultLang defaultLang;

    public GamificationLeaderBord_Adapter(ArrayList<GamificationLeaderBoardList> gamificationLeaderBoardLists, Context context) {
        this.gamificationLeaderBoardLists = gamificationLeaderBoardLists;
        this.context = context;
        sessionManager = new SessionManager(context);
        defaultLang = sessionManager.getMultiLangString();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_gamification_leaderboard, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GamificationLeaderBoardList gamificationLeaderBoardListOnj = gamificationLeaderBoardLists.get(position);


        if (gamificationLeaderBoardListOnj.getRank().equalsIgnoreCase("1")) {
            TranslateAnimation animation = new TranslateAnimation(-500.0f, 0.0f,
                    0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            holder.cardView_firstRank.startAnimation(animation);


//            YoYo.with(Techniques.SlideInUp)
//                    .duration(1000)
//                    .playOn(holder.cardView_firstRank);

            holder.txt_name.setText(gamificationLeaderBoardListOnj.getSender_name());
            holder.txt_companyName.setText(gamificationLeaderBoardListOnj.getCompany_name());
            holder.txt_title.setText(gamificationLeaderBoardListOnj.getTitle());
            holder.txt_points.setText(gamificationLeaderBoardListOnj.getTotal());
            holder.txt_firstPoints.setText(defaultLang.get52Points());

            GradientDrawable drawableRightSelected = new GradientDrawable();
            drawableRightSelected.setColor(Color.parseColor(gamificationLeaderBoardListOnj.getColor()));
            drawableRightSelected.mutate();
            drawableRightSelected.setCornerRadii(new float[]{0, 0, 110, 110, 110, 110, 0, 0});
            holder.cardView_firstRank.setBackgroundDrawable(drawableRightSelected);
            holder.cardView_firstRank.setMaxCardElevation(getPixelsFromDPs(2));
//            holder.cardView_firstRank.setContentPadding(10, 10, 10, 10);
            holder.cardView_firstRank.setCardElevation(13);

            if (gamificationLeaderBoardListOnj.getLogo().equalsIgnoreCase("")) {

                GradientDrawable drawable = new GradientDrawable();

                if (!(gamificationLeaderBoardListOnj.getSender_name().equalsIgnoreCase(""))) {
                    holder.txt_profileName.setText("" + gamificationLeaderBoardListOnj.getSender_name().charAt(0));
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
                    Glide.with(context).load(MyUrls.Imgurl + gamificationLeaderBoardListOnj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
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
                    }).into(new BitmapImageViewTarget(holder.user_image) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            holder.user_image.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            holder.cardView_firstRank.setVisibility(View.VISIBLE);
            holder.cardView_secondRank.setVisibility(View.GONE);
            holder.cardView_thirdRank.setVisibility(View.GONE);
            holder.cardView_fourthdRank.setVisibility(View.GONE);
            holder.cardView_fifthdRank.setVisibility(View.GONE);
        }
        if (gamificationLeaderBoardListOnj.getRank().equalsIgnoreCase("2")) {
//            YoYo.with(Techniques.SlideInUp)
//                    .duration(1000)
//                    .playOn(holder.cardView_secondRank);


            TranslateAnimation animation = new TranslateAnimation(-500.0f, 0.0f,
                    0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            holder.cardView_secondRank.startAnimation(animation);

            holder.txt_secondname.setText(gamificationLeaderBoardListOnj.getSender_name());
            holder.txt_secondcompanyName.setText(gamificationLeaderBoardListOnj.getCompany_name());
            holder.txt_secondtitle.setText(gamificationLeaderBoardListOnj.getTitle());
            holder.txt_secondpoints.setText(gamificationLeaderBoardListOnj.getTotal());
            holder.txt_txtSecondPoints.setText(defaultLang.get52Points());
            if (gamificationLeaderBoardListOnj.getLogo().equalsIgnoreCase("")) {

                GradientDrawable drawable = new GradientDrawable();

                if (!(gamificationLeaderBoardListOnj.getSender_name().equalsIgnoreCase(""))) {
                    holder.txt_secondprofileName.setText("" + gamificationLeaderBoardListOnj.getSender_name().charAt(0));
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                        holder.txt_secondprofileName.setBackgroundDrawable(drawable);
                        holder.txt_secondprofileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                    } else {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                        holder.txt_secondprofileName.setBackgroundDrawable(drawable);
                        holder.txt_secondprofileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                    }
                }
            } else {
                try {
                    Glide.with(context).load(MyUrls.Imgurl + gamificationLeaderBoardListOnj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.img_secondimage.setVisibility(View.VISIBLE);
                            holder.txt_secondprofileName.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.img_secondimage.setVisibility(View.VISIBLE);
                            holder.txt_secondprofileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(holder.img_secondimage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            holder.img_secondimage.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            GradientDrawable drawableRightSelected = new GradientDrawable();
            drawableRightSelected.setColor(Color.parseColor(gamificationLeaderBoardListOnj.getColor()));
            drawableRightSelected.mutate();
            drawableRightSelected.setCornerRadii(new float[]{0, 0, 110, 110, 110, 110, 0, 0});
            holder.cardView_secondRank.setBackgroundDrawable(drawableRightSelected);
            holder.cardView_secondRank.setMaxCardElevation(getPixelsFromDPs(2));
            holder.cardView_secondRank.setCardElevation(13);


            holder.cardView_firstRank.setVisibility(View.GONE);
            holder.cardView_secondRank.setVisibility(View.VISIBLE);
            holder.cardView_thirdRank.setVisibility(View.GONE);
            holder.cardView_fourthdRank.setVisibility(View.GONE);
            holder.cardView_fifthdRank.setVisibility(View.GONE);
        }
        if (gamificationLeaderBoardListOnj.getRank().equalsIgnoreCase("3")) {
//            YoYo.with(Techniques.SlideInUp)
//                    .duration(1000)
//                    .playOn(holder.cardView_thirdRank);


            TranslateAnimation animation = new TranslateAnimation(-500.0f, 0.0f,
                    0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            holder.cardView_thirdRank.startAnimation(animation);
            holder.txt_txtThirdPoints.setText(defaultLang.get52Points());
            if (gamificationLeaderBoardListOnj.getLogo().equalsIgnoreCase("")) {

                GradientDrawable drawable = new GradientDrawable();

                if (!(gamificationLeaderBoardListOnj.getSender_name().equalsIgnoreCase(""))) {
                    holder.txt_thirdprofileName.setText("" + gamificationLeaderBoardListOnj.getSender_name().charAt(0));
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                        holder.txt_thirdprofileName.setBackgroundDrawable(drawable);
                        holder.txt_thirdprofileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                    } else {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                        holder.txt_thirdprofileName.setBackgroundDrawable(drawable);
                        holder.txt_thirdprofileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                    }
                }
            } else {
                try {
                    Glide.with(context).load(MyUrls.Imgurl + gamificationLeaderBoardListOnj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.img_thirdimage.setVisibility(View.VISIBLE);
                            holder.txt_thirdprofileName.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.img_thirdimage.setVisibility(View.VISIBLE);
                            holder.txt_thirdprofileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(holder.img_thirdimage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            holder.img_thirdimage.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            holder.txt_thirdname.setText(gamificationLeaderBoardListOnj.getSender_name());
            holder.txt_thirdcompanyName.setText(gamificationLeaderBoardListOnj.getCompany_name());
            holder.txt_thirdtitle.setText(gamificationLeaderBoardListOnj.getTitle());
            holder.txt_thirdpoints.setText(gamificationLeaderBoardListOnj.getTotal());


            GradientDrawable drawableRightSelected = new GradientDrawable();
            drawableRightSelected.setColor(Color.parseColor(gamificationLeaderBoardListOnj.getColor()));
            drawableRightSelected.mutate();
            drawableRightSelected.setCornerRadii(new float[]{0, 0, 110, 110, 110, 110, 0, 0});
            holder.cardView_thirdRank.setBackgroundDrawable(drawableRightSelected);
            holder.cardView_thirdRank.setMaxCardElevation(getPixelsFromDPs(2));
            holder.cardView_thirdRank.setCardElevation(13);


            holder.cardView_firstRank.setVisibility(View.GONE);
            holder.cardView_secondRank.setVisibility(View.GONE);
            holder.cardView_thirdRank.setVisibility(View.VISIBLE);
            holder.cardView_fourthdRank.setVisibility(View.GONE);
            holder.cardView_fifthdRank.setVisibility(View.GONE);
        }
        if (gamificationLeaderBoardListOnj.getRank().equalsIgnoreCase("4")) {


//            YoYo.with(Techniques.SlideInUp)
//                    .duration(1000)
//                    .playOn(holder.cardView_fourthdRank);


            TranslateAnimation animation = new TranslateAnimation(-500.0f, 0.0f,
                    0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            holder.cardView_fourthdRank.startAnimation(animation);
            holder.txt_txtFourthPoints.setText(defaultLang.get52Points());
            if (gamificationLeaderBoardListOnj.getLogo().equalsIgnoreCase("")) {

                GradientDrawable drawable = new GradientDrawable();

                if (!(gamificationLeaderBoardListOnj.getSender_name().equalsIgnoreCase(""))) {
                    holder.txt_fourthprofileName.setText("" + gamificationLeaderBoardListOnj.getSender_name().charAt(0));
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                        holder.txt_fourthprofileName.setBackgroundDrawable(drawable);
                        holder.txt_fourthprofileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                    } else {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                        holder.txt_fourthprofileName.setBackgroundDrawable(drawable);
                        holder.txt_fourthprofileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                    }
                }
            } else {
                try {
                    Glide.with(context).load(MyUrls.Imgurl + gamificationLeaderBoardListOnj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.img_fourthimage.setVisibility(View.VISIBLE);
                            holder.txt_fourthprofileName.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.img_thirdimage.setVisibility(View.VISIBLE);
                            holder.txt_fourthprofileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(holder.img_fourthimage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            holder.img_fourthimage.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            holder.txt_fourthname.setText(gamificationLeaderBoardListOnj.getSender_name());
            holder.txt_fourthcompanyName.setText(gamificationLeaderBoardListOnj.getCompany_name());
            holder.txt_fourthtitle.setText(gamificationLeaderBoardListOnj.getTitle());
            holder.txt_fourthpoints.setText(gamificationLeaderBoardListOnj.getTotal());
            holder.txt_txtFourthPoints.setText(defaultLang.get52Points());

            GradientDrawable drawableRightSelected = new GradientDrawable();
            drawableRightSelected.setColor(Color.parseColor(gamificationLeaderBoardListOnj.getColor()));
            drawableRightSelected.mutate();
            drawableRightSelected.setCornerRadii(new float[]{0, 0, 110, 110, 110, 110, 0, 0});
            holder.cardView_fourthdRank.setBackgroundDrawable(drawableRightSelected);
            holder.cardView_fourthdRank.setMaxCardElevation(getPixelsFromDPs(2));
            holder.cardView_fourthdRank.setCardElevation(13);


            holder.cardView_firstRank.setVisibility(View.GONE);
            holder.cardView_secondRank.setVisibility(View.GONE);
            holder.cardView_thirdRank.setVisibility(View.GONE);
            holder.cardView_fourthdRank.setVisibility(View.VISIBLE);
            holder.cardView_fifthdRank.setVisibility(View.GONE);
        }
        if (gamificationLeaderBoardListOnj.getRank().equalsIgnoreCase("5")) {

//            YoYo.with(Techniques.SlideInUp)
//                    .duration(1000)
//                    .playOn(holder.cardView_fifthdRank);

            TranslateAnimation animation = new TranslateAnimation(-500.0f, 0.0f,
                    0.0f, 0.0f);
            animation.setDuration(1000);
            animation.setFillAfter(true);
            holder.cardView_fifthdRank.startAnimation(animation);

            if (gamificationLeaderBoardListOnj.getLogo().equalsIgnoreCase("")) {

                GradientDrawable drawable = new GradientDrawable();

                if (!(gamificationLeaderBoardListOnj.getSender_name().equalsIgnoreCase(""))) {
                    holder.txt_firfthprofileName.setText("" + gamificationLeaderBoardListOnj.getSender_name().charAt(0));
                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                        holder.txt_firfthprofileName.setBackgroundDrawable(drawable);
                        holder.txt_firfthprofileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                    } else {
                        drawable.setShape(GradientDrawable.OVAL);
                        drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                        holder.txt_firfthprofileName.setBackgroundDrawable(drawable);
                        holder.txt_firfthprofileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                    }
                }
            } else {
                try {
                    Glide.with(context).load(MyUrls.Imgurl + gamificationLeaderBoardListOnj.getLogo()).asBitmap().centerCrop().listener(new RequestListener<String, Bitmap>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                            holder.img_fifthimage.setVisibility(View.VISIBLE);
                            holder.txt_firfthprofileName.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.img_fifthimage.setVisibility(View.VISIBLE);
                            holder.txt_firfthprofileName.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(new BitmapImageViewTarget(holder.img_fifthimage) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            holder.img_fifthimage.setImageDrawable(RoundedImageConverter.getRoundedCornerBitmap1(resource, Color.GRAY, 60, 0, context));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            holder.txt_firfthname.setText(gamificationLeaderBoardListOnj.getSender_name());
            holder.txt_firfthcompanyName.setText(gamificationLeaderBoardListOnj.getCompany_name());
            holder.txt_firfthtitle.setText(gamificationLeaderBoardListOnj.getTitle());
            holder.txt_firfthpoints.setText(gamificationLeaderBoardListOnj.getTotal());
            holder.txt_txtfifthPoints.setText(defaultLang.get52Points());
            GradientDrawable drawableRightSelected = new GradientDrawable();
            drawableRightSelected.setColor(Color.parseColor(gamificationLeaderBoardListOnj.getColor()));
            drawableRightSelected.mutate();
            drawableRightSelected.setCornerRadii(new float[]{0, 0, 110, 110, 110, 110, 0, 0});
            holder.cardView_fifthdRank.setBackgroundDrawable(drawableRightSelected);
            holder.cardView_fifthdRank.setMaxCardElevation(getPixelsFromDPs(2));
            holder.cardView_fifthdRank.setCardElevation(13);


            holder.cardView_firstRank.setVisibility(View.GONE);
            holder.cardView_secondRank.setVisibility(View.GONE);
            holder.cardView_thirdRank.setVisibility(View.GONE);
            holder.cardView_fourthdRank.setVisibility(View.GONE);
            holder.cardView_fifthdRank.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return gamificationLeaderBoardLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView_firstRank, cardView_secondRank, cardView_thirdRank, cardView_fourthdRank, cardView_fifthdRank;
        ImageView user_image, img_secondimage, img_thirdimage, img_fourthimage, img_fifthimage;
        TextView txt_name, txt_title, txt_companyName, txt_points, txt_profileName;
        TextView txt_secondname, txt_secondtitle, txt_secondcompanyName, txt_secondpoints, txt_secondprofileName;
        TextView txt_thirdname, txt_thirdtitle, txt_thirdcompanyName, txt_thirdpoints, txt_thirdprofileName;
        TextView txt_fourthname, txt_fourthtitle, txt_fourthcompanyName, txt_fourthpoints, txt_fourthprofileName;
        TextView txt_firfthname, txt_firfthtitle, txt_firfthcompanyName, txt_firfthpoints, txt_firfthprofileName;

        TextView txt_firstPoints, txt_txtSecondPoints, txt_txtThirdPoints, txt_txtFourthPoints, txt_txtfifthPoints;


        public ViewHolder(View itemView) {
            super(itemView);

            txt_firstPoints = (TextView) itemView.findViewById(R.id.txt_firstPoints);
            txt_txtSecondPoints = (TextView) itemView.findViewById(R.id.txt_txtSecondPoints);
            txt_txtThirdPoints = (TextView) itemView.findViewById(R.id.txt_txtThirdPoints);
            txt_txtFourthPoints = (TextView) itemView.findViewById(R.id.txt_txtFourthPoints);
            txt_txtfifthPoints = (TextView) itemView.findViewById(R.id.txt_txtfifthPoints);


            cardView_firstRank = (CardView) itemView.findViewById(R.id.cardView_firstRank);
            cardView_secondRank = (CardView) itemView.findViewById(R.id.cardView_secondRank);
            cardView_thirdRank = (CardView) itemView.findViewById(R.id.cardView_thirdRank);
            cardView_fourthdRank = (CardView) itemView.findViewById(R.id.cardView_fourthdRank);
            cardView_fifthdRank = (CardView) itemView.findViewById(R.id.cardView_fifthdRank);

            txt_profileName = (TextView) itemView.findViewById(R.id.txt_profileName);
            txt_secondprofileName = (TextView) itemView.findViewById(R.id.txt_secondprofileName);
            txt_thirdprofileName = (TextView) itemView.findViewById(R.id.txt_thirdprofileName);
            txt_fourthprofileName = (TextView) itemView.findViewById(R.id.txt_fourthprofileName);
            txt_firfthprofileName = (TextView) itemView.findViewById(R.id.txt_firfthprofileName);

            txt_points = (TextView) itemView.findViewById(R.id.txt_points);
            txt_secondpoints = (TextView) itemView.findViewById(R.id.txt_secondpoints);
            txt_thirdpoints = (TextView) itemView.findViewById(R.id.txt_thirdpoints);
            txt_fourthpoints = (TextView) itemView.findViewById(R.id.txt_fourthpoints);
            txt_firfthpoints = (TextView) itemView.findViewById(R.id.txt_firfthpoints);


            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            txt_secondtitle = (TextView) itemView.findViewById(R.id.txt_secondtitle);
            txt_thirdtitle = (TextView) itemView.findViewById(R.id.txt_thirdtitle);
            txt_fourthtitle = (TextView) itemView.findViewById(R.id.txt_fourthtitle);
            txt_firfthtitle = (TextView) itemView.findViewById(R.id.txt_firfthtitle);


            txt_companyName = (TextView) itemView.findViewById(R.id.txt_companyName);
            txt_secondcompanyName = (TextView) itemView.findViewById(R.id.txt_secondcompanyName);
            txt_thirdcompanyName = (TextView) itemView.findViewById(R.id.txt_thirdcompanyName);
            txt_fourthcompanyName = (TextView) itemView.findViewById(R.id.txt_fourthcompanyName);
            txt_firfthcompanyName = (TextView) itemView.findViewById(R.id.txt_firfthcompanyName);


            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_secondname = (TextView) itemView.findViewById(R.id.txt_secondname);
            txt_thirdname = (TextView) itemView.findViewById(R.id.txt_thirdname);
            txt_fourthname = (TextView) itemView.findViewById(R.id.txt_fourthname);
            txt_firfthname = (TextView) itemView.findViewById(R.id.txt_firfthname);

            user_image = (ImageView) itemView.findViewById(R.id.user_image);
            img_secondimage = (ImageView) itemView.findViewById(R.id.img_secondimage);
            img_thirdimage = (ImageView) itemView.findViewById(R.id.img_thirdimage);
            img_fourthimage = (ImageView) itemView.findViewById(R.id.img_fourthimage);
            img_fifthimage = (ImageView) itemView.findViewById(R.id.img_fifthimage);

        }
    }

    protected int getPixelsFromDPs(int dps) {
        Resources r = context.getResources();
        int px = (int) (TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps, r.getDisplayMetrics()));
        return px;
    }
}
