package com.allintheloop.Fragment.FacebookModule;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allintheloop.Adapter.Facebook_Dailog_PagerAdapter;
import com.allintheloop.R;
import com.eftimoff.viewpagertransformers.DrawFromBackTransformer;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacebookDailog_Fragment extends DialogFragment {


    public static Dialog dialog;
    ImageView dailog_close;
    ViewPager facebook_dialogview;
    Bundle bundle;
    int pos;
    String isActivity = "";
    ArrayList<String> img_arrayList;

    public FacebookDailog_Fragment() {
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_facebook_dailog, container, false);
        dailog_close = (ImageView) rootView.findViewById(R.id.dailog_close);
        facebook_dialogview = (ViewPager) rootView.findViewById(R.id.facebook_dialogview);

        bundle = getArguments();

        if (bundle.containsKey("isActivity")) {
            isActivity = bundle.getString("isActivity");
        }
        if (bundle.containsKey("position")) {
            pos = bundle.getInt("position");
        }

        if (bundle.containsKey("img_array")) {
            img_arrayList = bundle.getStringArrayList("img_array");
        }

        dailog_close = (ImageView) rootView.findViewById(R.id.dailog_close);
        facebook_dialogview = (ViewPager) rootView.findViewById(R.id.facebook_dialogview);
        setupViewPager(facebook_dialogview);
        dailog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        facebook_dialogview.setCurrentItem(0);
        facebook_dialogview.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                facebook_dialogview.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        Facebook_Dailog_PagerAdapter adapter = new Facebook_Dailog_PagerAdapter(getChildFragmentManager(), img_arrayList, isActivity);
        for (int i = 0; i < img_arrayList.size(); i++) {
            adapter.addFragment(new FacebookDetail_Fragment(), "");
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(img_arrayList.size());
        viewPager.setPageTransformer(true, new DrawFromBackTransformer());
    }
}
