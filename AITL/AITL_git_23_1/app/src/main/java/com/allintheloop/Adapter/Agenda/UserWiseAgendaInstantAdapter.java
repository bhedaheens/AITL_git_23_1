package com.allintheloop.Adapter.Agenda;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.allintheloop.Bean.AgendaData.Agenda_Time;
import com.allintheloop.Fragment.AgendaModule.UserWiseAgendaInstantFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aiyaz on 8/5/17.
 */

public class UserWiseAgendaInstantAdapter extends FragmentPagerAdapter {


    private ArrayList<String> timeList = new ArrayList<>();
    private HashMap<String, ArrayList<Agenda_Time>> listDataChild = new HashMap<>();

    public UserWiseAgendaInstantAdapter(FragmentManager fm, ArrayList<String> timeList, HashMap<String, ArrayList<Agenda_Time>> listDataChild) {
        super(fm);
        this.timeList = timeList;
        this.listDataChild = listDataChild;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<Agenda_Time> agenda_times = listDataChild.get(timeList.get(position));
        return UserWiseAgendaInstantFragment.newInstance(agenda_times);
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
