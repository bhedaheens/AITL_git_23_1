package com.allintheloop.Adapter.Exhibitor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_MyLead_Fragment;
import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_ScanLead_Fragment;
import com.allintheloop.Fragment.ExhibitorFragment.Exhibitor_SettingLead_Fragment;

/**
 * Created by Aiyaz on 29/8/17.
 */

public class ExhibitorLeadPagerAdapter extends FragmentStatePagerAdapter {
    String isThree = "";
    String leadCount = "";

    public ExhibitorLeadPagerAdapter(FragmentManager fm, String isThree, String leadCount) {
        super(fm);
        this.isThree = isThree;
        this.leadCount = leadCount;
    }

    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        if (isThree.equalsIgnoreCase("1")) {
            switch (position) {
                case 0:
                    Exhibitor_ScanLead_Fragment tab1 = new Exhibitor_ScanLead_Fragment();
                    return tab1;
                case 1:
                    Exhibitor_MyLead_Fragment tab2 = new Exhibitor_MyLead_Fragment();
                    return tab2;
                case 2:
                    Exhibitor_SettingLead_Fragment tab3 = new Exhibitor_SettingLead_Fragment();
                    return tab3;

            }
        } else {
            switch (position) {
                case 0:
                    Exhibitor_ScanLead_Fragment tab1 = new Exhibitor_ScanLead_Fragment();
                    return tab1;
                case 1:
                    Exhibitor_MyLead_Fragment tab2 = new Exhibitor_MyLead_Fragment();
                    return tab2;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        if (isThree.equalsIgnoreCase("1")) {
            return 3;
        } else {
            return 2;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (isThree.equalsIgnoreCase("1")) {
            switch (position) {
                case 0:
                    title = "Scan Leads\nTap here to scan Leads";
                    break;
                case 1:
                    title = "My Leads\nNumber of Leads:" + leadCount;
                    break;
                case 2:
                    title = "Settings";
            }
        } else {
            switch (position) {
                case 0:
                    title = "Scan Leads\nTap here to scan Leads";
                    break;
                case 1:
                    title = "My Leads\nNumber of Leads:" + leadCount;
                    break;
            }
        }
        return title;
    }
}
