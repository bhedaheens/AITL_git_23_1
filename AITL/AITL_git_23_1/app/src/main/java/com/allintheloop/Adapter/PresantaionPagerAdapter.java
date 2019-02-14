package com.allintheloop.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Fragment.PresantationModule.Presantaion_TimeTab_Fragment;
import com.allintheloop.Fragment.PresantationModule.Presantation_TypeTab_Fragment;
import com.allintheloop.Util.SessionManager;

/**
 * Created by nteam on 8/6/16.
 */
public class PresantaionPagerAdapter extends FragmentPagerAdapter {
    SessionManager sessionManager;

    public PresantaionPagerAdapter(FragmentManager fm, SessionManager sessionManager) {
        super(fm);
        this.sessionManager = sessionManager;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d("Call Fragment", "MSg");
                fragment = new Presantaion_TimeTab_Fragment();
                break;
            case 1:
                fragment = new Presantation_TypeTab_Fragment();
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
        DefaultLanguage.DefaultLang defaultLang = sessionManager.getMultiLangString();
        switch (position) {
            case 0:
                title = defaultLang.get9SortByTime();
                break;
            case 1:
                title = defaultLang.get9SortByType();
                break;
        }
        return title;
    }

}
