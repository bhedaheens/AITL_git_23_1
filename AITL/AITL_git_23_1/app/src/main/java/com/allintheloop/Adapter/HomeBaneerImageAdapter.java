package com.allintheloop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allintheloop.Bean.HomeData.BannerImageData;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

/**
 * Created by n-team on 8/12/17.
 */

public class HomeBaneerImageAdapter extends PagerAdapter implements VolleyInterface {
    Context context;
    ArrayList<BannerImageData> advertiesMentTopViews;
    public ImageView array_img;
    ProgressBar progressBar1;

    SessionManager sessionManager;

    public HomeBaneerImageAdapter(Context context, ArrayList<BannerImageData> advertiesMentbottomViews) {
        this.context = context;
        this.advertiesMentTopViews = advertiesMentbottomViews;
        sessionManager = new SessionManager(context);
    }

    @Override
    public int getCount() {
        return advertiesMentTopViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewItem = inflater.inflate(R.layout.adapter_exhibitor_image, container, false);


        array_img = (ImageView) viewItem.findViewById(R.id.array_img);
        progressBar1 = (ProgressBar) viewItem.findViewById(R.id.progressBar1);
//        Glide.with(context).load(MyUrls.Imgurl + advertiesMentTopViews.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(array_img);
        array_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (advertiesMentTopViews.get(position).getUrl().equalsIgnoreCase("")) {

                } else {
                    pagewiseClick(advertiesMentTopViews.get(position).getImage().toString());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertiesMentTopViews.get(position).getUrl()));
                    context.startActivity(intent);
                }
            }
        });


        Glide.with(context)
                .load(MyUrls.Imgurl + advertiesMentTopViews.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        array_img.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        progressBar1.setVisibility(View.GONE);
                        array_img.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .crossFade()
                .into(array_img);

        container.addView(viewItem);
        return viewItem;
    }

    private void pagewiseClick(String name) {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.OtherPageWiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "BN", sessionManager.getMenuid(), name, "", ""), 5, false, this);
        }
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView((View) object);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

    }
}