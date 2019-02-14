package com.allintheloop.Adapter.RequestMetting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewList;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewModeratorList;
import com.allintheloop.Fragment.RequestMeetingModule.RequestMettingListChildTab_NewModule;
import com.allintheloop.Fragment.RequestMeetingModule.RequestMettingList_loadFragment;

import java.util.ArrayList;

/**
 * Created by nteam on 7/3/18.
 */

public class Adapter_RequestMettingList_ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> datetime;
    String tag;
    boolean isModerator = false;
    String dateData;
    ArrayList<RequestMeetingNewList> objectArrayList;
    ArrayList<RequestMeetingNewModeratorList> moderatorListArrayList;
    int parentPosition;


    // For AttendeeAndExhibitor
    public Adapter_RequestMettingList_ViewPagerAdapter(FragmentManager fm, ArrayList<String> title, String tag, ArrayList<RequestMeetingNewList> objectArrayList, boolean isModerator) {
        super(fm);
        this.datetime = title;
        this.tag = tag;
        this.objectArrayList = objectArrayList;
        this.isModerator = isModerator;
    }

    // For MoreratorData
    public Adapter_RequestMettingList_ViewPagerAdapter(FragmentManager fm, ArrayList<String> title, String tag, ArrayList<RequestMeetingNewModeratorList> moderatorLists, String temp, boolean isModerator) {
        super(fm);
        this.datetime = title;
        this.tag = tag;
        this.moderatorListArrayList = moderatorLists;
        this.isModerator = isModerator;
    }

    // For ChildAttendeeData
    public Adapter_RequestMettingList_ViewPagerAdapter(FragmentManager fm, ArrayList<String> title, String tag, String dateData, ArrayList<RequestMeetingNewList> objectArrayList, int parentPosition, String temp, boolean isModerator) {
        super(fm);
        this.datetime = title;
        this.tag = tag;
        this.dateData = dateData;
        this.objectArrayList = objectArrayList;
        this.parentPosition = parentPosition;
        this.isModerator = isModerator;
    }

    public Adapter_RequestMettingList_ViewPagerAdapter(FragmentManager fm, ArrayList<String> title, String tag, String dateData, ArrayList<RequestMeetingNewModeratorList> objectArrayList, int parentPosition, boolean isModerator) {
        super(fm);
        this.datetime = title;
        this.tag = tag;
        this.dateData = dateData;
        this.moderatorListArrayList = objectArrayList;
        this.parentPosition = parentPosition;
        this.isModerator = isModerator;
    }


    @Override
    public Fragment getItem(int position) {
        if (tag.equalsIgnoreCase("parent")) {
            if (isModerator) {
                return RequestMettingListChildTab_NewModule.getInstance(position, datetime, moderatorListArrayList, isModerator);
            } else {

                return RequestMettingListChildTab_NewModule.getInstance(position, datetime, objectArrayList);
            }
        } else if (tag.equalsIgnoreCase("child")) {
            if (isModerator) {
                return RequestMettingList_loadFragment.getInstance(position, datetime, dateData, moderatorListArrayList, parentPosition, isModerator);
            } else {
                return RequestMettingList_loadFragment.getInstance(position, datetime, dateData, objectArrayList, parentPosition);
            }

        }
        return null;
    }

    @Override
    public int getCount() {
        return datetime.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return datetime.get(position);
    }
}
