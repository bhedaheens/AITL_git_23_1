package com.allintheloop.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.allintheloop.Fragment.RequestMeetingModule.RequestMeetingInviteMoreTabFragment;
import com.allintheloop.Fragment.RequestMeetingModule.RequestMeetingInvitedTabFragment;
import com.allintheloop.Util.SessionManager;

/**
 * Created by nteam on 13/3/18.
 */

public class RequestMeetingInvitePagerAdapter extends FragmentStatePagerAdapter {
    SessionManager sessionManager;

    public RequestMeetingInvitePagerAdapter(FragmentManager fm, SessionManager sessionManager) {
        super(fm);
        this.sessionManager = sessionManager;

    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                RequestMeetingInviteMoreTabFragment tab1 = new RequestMeetingInviteMoreTabFragment();
                return tab1;
            case 1:
                RequestMeetingInvitedTabFragment tab2 = new RequestMeetingInvitedTabFragment();
                return tab2;
            default:
                return null;
        }
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
                title = "All Attendees";
                break;
            case 1:
                title = "Invited Attendees";
                break;
        }
        return title;
    }
}
