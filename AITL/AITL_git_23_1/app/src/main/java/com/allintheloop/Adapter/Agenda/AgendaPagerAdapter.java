package com.allintheloop.Adapter.Agenda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Fragment.AgendaModule.Agenda_TimeTab_Fragment;
import com.allintheloop.Fragment.AgendaModule.Agenda_TypeTab_Fragment;
import com.allintheloop.Fragment.AgendaModule.UserWise_Agenda_TimeTab;
import com.allintheloop.Util.SessionManager;

/**
 * Created by nteam on 10/5/16.
 */
public class AgendaPagerAdapter extends FragmentStatePagerAdapter {

    SessionManager sessionManager;

    public AgendaPagerAdapter(FragmentManager fm, SessionManager sessionManager) {
        super(fm);
        this.sessionManager = sessionManager;

    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                Agenda_TimeTab_Fragment tab1 = new Agenda_TimeTab_Fragment();
                return tab1;
            case 1:
                Agenda_TypeTab_Fragment tab2 = new Agenda_TypeTab_Fragment();
                return tab2;
            case 2:
                UserWise_Agenda_TimeTab tab3 = new UserWise_Agenda_TimeTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
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
            case 2:
                title = defaultLang.get1ViewMyAgenda();
        }
        return title;
    }
}
