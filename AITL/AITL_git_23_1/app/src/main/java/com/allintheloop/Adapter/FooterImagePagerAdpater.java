package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allintheloop.Bean.AdvertiesMentbottomView;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Aiyaz on 22/3/17.
 */

public class FooterImagePagerAdpater extends PagerAdapter implements VolleyInterface {

    Context context;
    ArrayList<AdvertiesMentbottomView> advertiesMentbottomViews;
    public ImageView imageView;
    SessionManager sessionManager;

    public FooterImagePagerAdpater(Context context, ArrayList<AdvertiesMentbottomView> advertiesMentbottomViews) {
        this.context = context;
        this.advertiesMentbottomViews = advertiesMentbottomViews;
        sessionManager = new SessionManager(context);
    }

    @Override
    public int getCount() {
        return advertiesMentbottomViews.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View viewItem = inflater.inflate(R.layout.adapter_footer_image, container, false);


        imageView = (ImageView) viewItem.findViewById(R.id.ivFooter);

        String imgUrl = "";
        Glide.with(context).load(MyUrls.Imgurl + advertiesMentbottomViews.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                if (advertiesMentbottomViews.get(position).getUrl().equalsIgnoreCase("")) {

                } else {
                    advertiesClick(advertiesMentbottomViews.get(position).getAdvertiesmentId());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertiesMentbottomViews.get(position).getUrl()));
                    context.startActivity(intent);
                }
            }
        });

        container.addView(viewItem);
        return viewItem;
    }


    private void advertiesClick(String adverties_id) {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", adverties_id, "AD", ""), 5, false, this);
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
        switch (volleyResponse.type) {
            case 0:
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}
