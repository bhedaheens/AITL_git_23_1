package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 22/3/17.
 */

public class HomeImagePagerAdpater extends PagerAdapter {

    Context context;
    ArrayList<Exhibitor_DetailImage> arrayList;
    public ImageView imageView;
    ProgressBar progressBar1;

    public HomeImagePagerAdpater(Context context, ArrayList<Exhibitor_DetailImage> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();

    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.adapter_exhibitor_image, container, false);
        Exhibitor_DetailImage imgObj = arrayList.get(position);
        imageView = (ImageView) viewItem.findViewById(R.id.array_img);
        progressBar1 = (ProgressBar) viewItem.findViewById(R.id.progressBar1);

        if (imgObj.getImage_link().equalsIgnoreCase("")) {
            progressBar1.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        } else {

//            Glide.with(context)
//                    .load(imgObj.getImage_link())
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .listener(new RequestListener<String, GlideDrawable>() {
//                        @Override
//                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//
//                            progressBar1.setVisibility(View.GONE);
//                            imageView.setVisibility(View.GONE);
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource)
//                        {
//                            progressBar1.setVisibility(View.GONE);
//                            imageView.setVisibility(View.VISIBLE);
//                            return false;
//                        }
//                    })
//                    .centerCrop()
//                    .fitCenter()
//                    .into(imageView);


            if (imgObj.getImage_link().contains("gif")) {
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .asGif()
                        .skipMemoryCache(false)
                        .listener(new RequestListener<String, GifDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
                                e.printStackTrace();
                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
//                            return false;
                                return false;
                            }
                        })
                        .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);

            } else {
                Glide.with(context)
                        .load(imgObj.getImage_link())
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .centerCrop()
                        .fitCenter()
                        .into(imageView);

            }
            container.addView(viewItem);
        }
        return viewItem;
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
