package com.allintheloop.Fragment.PrivateMessage;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.R;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateMessageViewProfile_Fragment extends Fragment {


    CircleImageView img_profile;
    TextView txt_userName, txt_companyName, txt_profileName;
    Bundle bundle;
    String senderLogo, senderName, senderCompanyName;
    SessionManager sessionManager;

    public PrivateMessageViewProfile_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_private_message_view_profile, container, false);
        img_profile = (CircleImageView) rootView.findViewById(R.id.img_profile);
        txt_userName = (TextView) rootView.findViewById(R.id.txt_userName);
        txt_companyName = (TextView) rootView.findViewById(R.id.txt_companyName);
        txt_profileName = (TextView) rootView.findViewById(R.id.txt_profileName);
        sessionManager = new SessionManager(getActivity());
        bundle = getArguments();

        if (bundle.containsKey("name")) {
            senderName = bundle.getString("name");
            txt_userName.setText(senderName);
        }
        if (bundle.containsKey("imageLogo")) {
            senderLogo = bundle.getString("imageLogo");
        }
        if (bundle.containsKey("companyName")) {
            senderCompanyName = bundle.getString("companyName");
            txt_companyName.setText(senderCompanyName);
        }


        GradientDrawable drawable = new GradientDrawable();
        Random rnd = new Random();
        if (senderLogo.equalsIgnoreCase("")) {
            img_profile.setVisibility(View.GONE);
            txt_profileName.setVisibility(View.VISIBLE);

            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            if (!(senderName.equalsIgnoreCase(""))) {
                txt_profileName.setText("" + senderName.charAt(0));
            }
            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                txt_profileName.setBackgroundDrawable(drawable);
                txt_profileName.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            } else {
                drawable.setShape(GradientDrawable.OVAL);
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                txt_profileName.setBackgroundDrawable(drawable);
                txt_profileName.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            }
        } else {
            Glide.with(getActivity())
                    .load(MyUrls.Imgurl + senderLogo)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            img_profile.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            img_profile.setVisibility(View.VISIBLE);
                            txt_profileName.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .thumbnail(0.7f)
                    .into(img_profile);
        }
        return rootView;
    }

}
