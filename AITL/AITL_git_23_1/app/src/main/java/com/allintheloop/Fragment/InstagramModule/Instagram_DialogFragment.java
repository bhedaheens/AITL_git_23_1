package com.allintheloop.Fragment.InstagramModule;


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

import com.eftimoff.viewpagertransformers.DrawFromBackTransformer;
import com.allintheloop.Adapter.Instagram_dialog_PagerAdapter;
import com.allintheloop.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Instagram_DialogFragment extends DialogFragment {
    public static Dialog dialog;
    ViewPager instagram_dialogviewPager;
    ImageView dailog_insta_close;
    Bundle bundle;
    int pos;

    public Instagram_DialogFragment() {
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_instagram__dialog, container, false);
        bundle = getArguments();
        if (bundle.containsKey("position")) {
            pos = bundle.getInt("position");
        }

        dailog_insta_close = (ImageView) rootView.findViewById(R.id.dailog_insta_close);
        instagram_dialogviewPager = (ViewPager) rootView.findViewById(R.id.instagram_dialogview);
        setupViewPager(instagram_dialogviewPager);
        dailog_insta_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        instagram_dialogviewPager.setCurrentItem(pos);
        instagram_dialogviewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                instagram_dialogviewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {
        Instagram_dialog_PagerAdapter adapter = new Instagram_dialog_PagerAdapter(getChildFragmentManager());
        for (int i = 0; i < InstagramFeed_Fragment.instagramFeedArrayList.size(); i++) {
            adapter.addFragment(new Instagram_DetailFragment(), "");
        }
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(InstagramFeed_Fragment.instagramFeedArrayList.size());
        viewPager.setPageTransformer(true, new DrawFromBackTransformer());
    }

}
