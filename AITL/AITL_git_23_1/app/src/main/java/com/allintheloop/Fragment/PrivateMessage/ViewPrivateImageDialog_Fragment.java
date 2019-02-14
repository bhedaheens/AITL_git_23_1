package com.allintheloop.Fragment.PrivateMessage;


import android.app.Dialog;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 */
public class ViewPrivateImageDialog_Fragment extends DialogFragment {


    Dialog dialog;
    ImageView imgclose, full_Imageview;
    Bundle bundle;
    SessionManager sessionManager;
    String imgUrl = "";
    ProgressBar progressBar1;

    public ViewPrivateImageDialog_Fragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transperent)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setWindowAnimations(R.style.dialog_animation);


//        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        dialog = new Dialog(getActivity());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(root);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////        dialog.getWindow().setWindowAnimations(R.style.dialog_animation);
        return dialog;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_private_image_dialog, container, false);
        sessionManager = new SessionManager(getActivity());
        imgclose = (ImageView) rootView.findViewById(R.id.imgclose);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        full_Imageview = (ImageView) rootView.findViewById(R.id.full_Imageview);
        bundle = getArguments();
        if (bundle.containsKey("imageurl")) {
            imgUrl = bundle.getString("imageurl");
        }
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });


        Glide.with(getActivity())
                .load(imgUrl)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        full_Imageview.setVisibility(View.GONE);
                        progressBar1.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        full_Imageview.setVisibility(View.VISIBLE);
                        progressBar1.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(full_Imageview);


        return rootView;

    }

}
