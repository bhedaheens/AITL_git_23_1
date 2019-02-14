package com.allintheloop.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.allintheloop.Fragment.FundraisingModule.Item_BuyNow_Fragment;
import com.allintheloop.Fragment.FundraisingModule.Item_SlientAuction_Fragment;

/**
 * Created by nteam on 22/8/16.
 */
public class ItemPagerAdapter extends FragmentPagerAdapter {
    public ItemPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d("Call Fragment", "MSg");
                fragment = new Item_SlientAuction_Fragment();
                break;
            case 1:
                fragment = new Item_BuyNow_Fragment();
                break;
            default:

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Silent Auction";
                break;
            case 1:
                title = "Buynow";
                break;
        }
        return title;
    }
}
