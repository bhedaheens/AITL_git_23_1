package com.allintheloop.Adapter.MatchMaking;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.allintheloop.Bean.MatchMaking.MatchMakingModuleNameData;
import com.allintheloop.Fragment.MatchMakingAllList_Fragment;

import java.util.ArrayList;

public class MatchMakingPagerAdapter extends FragmentPagerAdapter {

    ArrayList<MatchMakingModuleNameData.ModulesName> menuData;
    ArrayList<String> titleName;

    public MatchMakingPagerAdapter(FragmentManager fm, ArrayList<MatchMakingModuleNameData.ModulesName> menuData, ArrayList<String> titleName) {
        super(fm);
        this.menuData = menuData;
        this.titleName = titleName;
    }

    @Override
    public Fragment getItem(int position) {
        return MatchMakingAllList_Fragment.getInstance(menuData, titleName, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleName.get(position);
    }

    @Override
    public int getCount() {
        return titleName.size();
    }
}
