package com.allintheloop.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.allintheloop.Fragment.FundraisingModule.Order_AuctionItem_Fragment;
import com.allintheloop.Fragment.FundraisingModule.Order_ListItem_Fragment;
import com.allintheloop.Fragment.FundraisingModule.Order_PleadgeItem_Fragment;

/**
 * Created by nteam on 10/5/16.
 */
public class OrderPagerAdapter extends FragmentPagerAdapter {
    public OrderPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d("Call Fragment", "MSg");
                fragment = new Order_ListItem_Fragment();
                break;
            case 1:
                fragment = new Order_AuctionItem_Fragment();
                break;
            case 2:
                fragment = new Order_PleadgeItem_Fragment();
                break;
            default:

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Order";
                break;
            case 1:
                title = "Auction";
                break;
            case 2:
                title = "Pledges";
                break;
        }
        return title;
    }
}
