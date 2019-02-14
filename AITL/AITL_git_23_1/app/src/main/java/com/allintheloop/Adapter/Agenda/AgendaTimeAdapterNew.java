package com.allintheloop.Adapter.Agenda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.allintheloop.Bean.AgendaData.Agenda;
import com.allintheloop.Fragment.AgendaModule.AgendaInstantFragmentNew;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aiyaz on 8/5/17.
 */

public class AgendaTimeAdapterNew extends FragmentPagerAdapter {


    private ArrayList<String> timeList = new ArrayList<>();
    private HashMap<String, ArrayList<Agenda>> listDataChild = new HashMap<>();

    public AgendaTimeAdapterNew(FragmentManager fm, ArrayList<String> timeList, HashMap<String, ArrayList<Agenda>> listDataChild) {
        super(fm);
        this.timeList = timeList;
        this.listDataChild = listDataChild;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<Agenda> agenda_times = listDataChild.get(timeList.get(position));
        return AgendaInstantFragmentNew.newInstance(agenda_times);
    }

    @Override
    public int getCount() {
        return timeList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return timeList.get(position);
    }

}