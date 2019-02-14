package com.allintheloop.Adapter.Attendee;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeFullDirectory_Fragment;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeMyContact_Fragment;
import com.allintheloop.Util.SessionManager;

/**
 * Created by Aiyaz on 6/12/16.
 */

public class AttendeeShareFunctionPagerAdapter extends FragmentPagerAdapter {
    SessionManager sessionManager;

    public AttendeeShareFunctionPagerAdapter(FragmentManager fm, SessionManager sessionManager) {
        super(fm);
        this.sessionManager = sessionManager;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                Log.d("Call Fragment", "MSg");
                fragment = new AttendeeFullDirectory_Fragment();
                break;
            case 1:
                fragment = new AttendeeMyContact_Fragment();
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
                title = defaultLang.get2FullDirectory();
                break;
            case 1:
                title = defaultLang.get2MyContacts();
                break;
        }
        return title;
    }
}
