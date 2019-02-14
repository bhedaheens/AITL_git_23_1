package com.allintheloop.Adapter.Exhibitor;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Fragment.FundraisingModule.Fundrising_Home_Fragment;
import com.allintheloop.Fragment.Fundraising_auctionModule.LiveAuctionDetail;
import com.allintheloop.Fragment.Fundraising_auctionModule.OnlineShop_detail;
import com.allintheloop.Fragment.Sponsor_Detail_Fragment;
import com.allintheloop.Fragment.ViewImageDialog;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * Created by nteam on 6/6/16.
 */
public class Exhibitor_ImageAdapter extends PagerAdapter {

    Context context;
    ArrayList<Exhibitor_DetailImage> arrayList;
    public ImageView imageView;
    ProgressBar progressBar1;
    SessionManager sessionManager;

    public Exhibitor_ImageAdapter(Context context, ArrayList<Exhibitor_DetailImage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sessionManager = new SessionManager(context);
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.adapter_exhibitor_image, container, false);
        Exhibitor_DetailImage imgObj = arrayList.get(position);
        Log.d("ArrayImg", imgObj.getImage_link());
        imageView = (ImageView) viewItem.findViewById(R.id.array_img);
        progressBar1 = (ProgressBar) viewItem.findViewById(R.id.progressBar1);

        if (imgObj.getImage_link().equalsIgnoreCase("")) {
            progressBar1.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        } else {
            if (imgObj.getTag().equalsIgnoreCase("Exhibitor")) {
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                imageView.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .into(imageView);

                container.addView(viewItem);
//                Exhibitor_Detail_Fragment.headerViewPager.addView(viewItem);
            } else if (imgObj.getTag().equalsIgnoreCase("Fundrising")) {

                //    Glide.with(context).load(imgObj.getImage_link()).placeholder(R.drawable.allinloop).centerCrop().fitCenter().into(imageView);
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                imageView.setVisibility(View.GONE);
                                Fundrising_Home_Fragment.viewPager.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                Fundrising_Home_Fragment.viewPager.setVisibility(View.VISIBLE);
                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .centerCrop()
                        .fitCenter()
                        .into(imageView);
                Fundrising_Home_Fragment.viewPager.addView(viewItem);
            } else if (imgObj.getTag().equalsIgnoreCase("silent_productDetail")) {

                //    Glide.with(context).load(imgObj.getImage_link()).placeholder(R.drawable.allinloop).centerCrop().fitCenter().into(imageView);
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                imageView.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .centerCrop()
                        .fitCenter()
                        .into(imageView);
                Log.d("AITL  Image", imgObj.getImage_link());
//                SilentAuction_ProductDetail.silent_viewPager.addView(viewItem);
                container.addView(viewItem);
            } else if (imgObj.getTag().equalsIgnoreCase("live_productDetail")) {

                //    Glide.with(context).load(imgObj.getImage_link()).placeholder(R.drawable.allinloop).centerCrop().fitCenter().into(imageView);
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                imageView.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                LiveAuctionDetail.frame_viewpager.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                LiveAuctionDetail.frame_viewpager.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .centerCrop()
                        .fitCenter()
                        .into(imageView);
                Log.d("AITL  Image", imgObj.getImage_link());
                LiveAuctionDetail.silent_viewPager.addView(viewItem);
            } else if (imgObj.getTag().equalsIgnoreCase("Online_shop")) {

                //    Glide.with(context).load(imgObj.getImage_link()).placeholder(R.drawable.allinloop).centerCrop().fitCenter().into(imageView);
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                imageView.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                OnlineShop_detail.frame_viewpager.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                OnlineShop_detail.frame_viewpager.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .centerCrop()
                        .fitCenter()
                        .into(imageView);
                OnlineShop_detail.online_viewPager.addView(viewItem);
            } else if (imgObj.getTag().equalsIgnoreCase("pledge")) {

                //    Glide.with(context).load(imgObj.getImage_link()).placeholder(R.drawable.allinloop).centerCrop().fitCenter().into(imageView);
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                imageView.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .centerCrop()
                        .fitCenter()
                        .into(imageView);

                container.addView(viewItem);
//                Pledge_Detail_Fragment.pledge_viewPager.addView(viewItem);
            } else if (imgObj.getTag().equalsIgnoreCase("imagedialog")) {

                //    Glide.with(context).load(imgObj.getImage_link()).placeholder(R.drawable.allinloop).centerCrop().fitCenter().into(imageView);
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                imageView.setVisibility(View.GONE);
                                progressBar1.setVisibility(View.GONE);
                                ViewImageDialog.frame_viewpager.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                ViewImageDialog.frame_viewpager.setVisibility(View.VISIBLE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .crossFade()
                        .centerCrop()
                        .fitCenter()
                        .into(imageView);

                ViewImageDialog.viewPager.addView(viewItem);
            }
        }

        return viewItem;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

}