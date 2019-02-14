package com.allintheloop.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.allintheloop.Fragment.FacebookModule.FacebookDetail_Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiyaz on 30/1/17.
 */

public class Facebook_Dailog_PagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    ArrayList<String> img_arryList;
    Context context;
    String isActivity = "";

    public Facebook_Dailog_PagerAdapter(FragmentManager manager, ArrayList<String> img_arryList, String isActivity) {
        super(manager);
        this.img_arryList = img_arryList;
        this.isActivity = isActivity;
        Log.d("AITL PAGERADAPTER", "SIZE" + img_arryList.size());
    }

    @Override
    public Fragment getItem(int position) {
        return FacebookDetail_Fragment.newInstance(position, "POSITION " + position, img_arryList, isActivity);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

}