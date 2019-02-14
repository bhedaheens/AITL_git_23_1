package com.allintheloop.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Fragment.AgendaModule.PendingAgenda_TimeTab;
import com.allintheloop.Fragment.AgendaModule.PendingAgenda_TypeTab;
import com.allintheloop.Util.SessionManager;

/**
 * Created by nteam on 16/8/16.
 */
public class Pending_AgendaPager_Adapter extends FragmentPagerAdapter {
    SessionManager sessionManager;

    public Pending_AgendaPager_Adapter(FragmentManager fm, SessionManager sessionManager) {
        super(fm);
        this.sessionManager = sessionManager;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d("Call Fragment", "MSg");
                fragment = new PendingAgenda_TimeTab();
                break;
            case 1:
                fragment = new PendingAgenda_TypeTab();
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
                title = defaultLang.get1SortByTime();
                break;
            case 1:
                title = defaultLang.get1SortByTrack();
                break;
        }
        return title;
    }
}
