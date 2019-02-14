package com.allintheloop.Fragment.FacebookModule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.allintheloop.Bean.FacebookFeedData;
import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookDetail_Fragment extends Fragment {

    int pos;
    FacebookFeedData facebookDataObj;
    Bundle bundle;
    ImageView img_facebookImage;
    ArrayList<String> img_array;
    String isActivity = "";

    public FacebookDetail_Fragment() {
        // Required empty public constructor
    }


    public static FacebookDetail_Fragment newInstance(int page, String title, ArrayList<String> img_array, String isActivity) {

        FacebookDetail_Fragment fragmentFirst = new FacebookDetail_Fragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putStringArrayList("img_array", img_array);
        args.putString("isActivity", isActivity);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("someInt", 0);
        Log.d("AITL POSION", "" + pos);
        img_array = getArguments().getStringArrayList("img_array");
        isActivity = getArguments().getString("isActivity");
        Collections.reverse(img_array);
        //Log.i("niral", "POS ::" + pos);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_facebook_detail, container, false);
        img_facebookImage = (ImageView) rootView.findViewById(R.id.img_facebookImage);

        if (isActivity.equalsIgnoreCase("1")) {
            Glide.with(getActivity())
                    .load(MyUrls.Imgurl + img_array.get(pos))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            img_facebookImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            img_facebookImage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .crossFade()
                    .into(img_facebookImage);
        } else {
            Glide.with(getActivity())
                    .load(img_array.get(pos))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            img_facebookImage.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            img_facebookImage.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .crossFade()
                    .into(img_facebookImage);
        }
        return rootView;
    }

}
