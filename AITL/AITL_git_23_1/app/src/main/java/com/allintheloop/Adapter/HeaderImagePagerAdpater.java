package com.allintheloop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
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

import java.util.ArrayList;

/**
 * Created by nteam on 20/11/17.
 */

public class HeaderImagePagerAdpater extends PagerAdapter implements VolleyInterface {
    Context context;
    ArrayList<AdvertiesmentTopView> advertiesMentTopViews;
    public ImageView imageView;
    LinearLayout linear_images;
    SessionManager sessionManager;

    public HeaderImagePagerAdpater(Context context, ArrayList<AdvertiesmentTopView> advertiesMentbottomViews) {
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
        View viewItem = inflater.inflate(R.layout.adapter_footer_image, container, false);


        imageView = (ImageView) viewItem.findViewById(R.id.ivFooter);
        linear_images = (LinearLayout) viewItem.findViewById(R.id.linear_images);

        String imgUrl = "";
        Glide.with(context).load(MyUrls.Imgurl + advertiesMentTopViews.get(position).getImage()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                if (advertiesMentTopViews.get(position).getUrl().equalsIgnoreCase("")) {

                } else {
                    advertiesClick(advertiesMentTopViews.get(position).getAdvertiesmentId());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(advertiesMentTopViews.get(position).getUrl()));
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
