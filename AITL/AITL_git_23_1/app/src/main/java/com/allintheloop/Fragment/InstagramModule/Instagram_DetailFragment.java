package com.allintheloop.Fragment.InstagramModule;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.allintheloop.Bean.InstagramFeed;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Instagram_DetailFragment extends Fragment {

    ProgressBar progressBar1;
    SessionManager sessionManager;
    ImageView insta_img, img_video;
    CircleImageView instaUserimage;
    TextView txt_cmtCount, txt_likeCount, txt_userText, txt_cationText;
    int pos;
    InstagramFeed instagramFeedObj;
    Bundle bundle;

    public Instagram_DetailFragment() {
        // Required empty public constructor
    }

    public static Instagram_DetailFragment newInstance(int page, String title) {

        Instagram_DetailFragment fragmentFirst = new Instagram_DetailFragment();
        Log.d("AITL", "Test");
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("someInt", 0);
        //Log.i("niral", "POS ::" + pos);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_instagram__detail, container, false);
        insta_img = (ImageView) rootView.findViewById(R.id.insta_img);
        img_video = (ImageView) rootView.findViewById(R.id.img_video);
        bundle = new Bundle();
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        instaUserimage = (CircleImageView) rootView.findViewById(R.id.instaUserimage);
        txt_cmtCount = (TextView) rootView.findViewById(R.id.txt_cmtCount);
        txt_userText = (TextView) rootView.findViewById(R.id.txt_userText);
        txt_cationText = (TextView) rootView.findViewById(R.id.txt_cationText);
        txt_likeCount = (TextView) rootView.findViewById(R.id.txt_likeCount);

        instagramFeedObj = InstagramFeed_Fragment.instagramFeedArrayList.get(pos);
        if (instagramFeedObj.getType().equalsIgnoreCase("image")) {
            img_video.setVisibility(View.GONE);
        } else {
            img_video.setVisibility(View.VISIBLE);
        }

        txt_userText.setText(instagramFeedObj.getUser_name());
        txt_cationText.setText(instagramFeedObj.getCaption_text());
        txt_likeCount.setText("Likes :" + instagramFeedObj.getLikes_count());
        txt_cmtCount.setText("Comments :" + instagramFeedObj.getComment_count());

        Log.d("AITL Comments", "Count" + instagramFeedObj.getComment_count());


        insta_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(instagramFeedObj.getLink());
                Intent insta = new Intent(Intent.ACTION_VIEW, uri);
                insta.setPackage("com.instagram.android");


                if (isIntentAvailable(getActivity(), insta)) {
                    startActivity(insta);
                } else {
                    Instagram_DialogFragment.dialog.dismiss();
                    bundle.putString("Social_url", instagramFeedObj.getLink());
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                    ((MainActivity) getActivity()).loadFragment(bundle);

                }

            }
        });

        Glide.with(getActivity())
                .load(instagramFeedObj.getHigh_image())
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar1.setVisibility(View.GONE);
                        insta_img.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar1.setVisibility(View.VISIBLE);
                        insta_img.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(insta_img);

        Glide.with(getActivity()).load(instagramFeedObj.getUser_profile()).into(instaUserimage);


        return rootView;

    }

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
