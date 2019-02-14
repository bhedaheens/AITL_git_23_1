package com.allintheloop.Fragment.ExhibitorFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.allintheloop.Adapter.Exhibitor.ExhibitorLeadPagerAdapter;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequestResponse;

import static com.allintheloop.Util.GlobalData.exhibitorMyLeadCount;

/**
 * A simple {@link Fragment} subclass.
 */
public class Exhibitor_Lead_Fragment extends Fragment implements VolleyInterface {


    TabLayout tabLayout;
    ViewPager viewPager;
    ExhibitorLeadPagerAdapter exhibitorLeadPagerAdapter;
    SessionManager sessionManager;
    Bundle bundle;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data;
    String leadCount = "";

    private BroadcastReceiver leadCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                leadCount = String.valueOf(intent.getIntExtra("count", -1));
                Log.d("bhv", "onReceive: " + leadCount);
                tabLayout.getTabAt(1).setText("My Leads\nNumber of Leads : " + leadCount);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    public Exhibitor_Lead_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor__lead, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager = new SessionManager(getActivity());
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);

        tabLayout = (TabLayout) rootView.findViewById(R.id.exhibitor_leadTab);
        viewPager = (ViewPager) rootView.findViewById(R.id.exhibitor_leadViewpager);

        bundle = new Bundle();
//        linear_header=(FrameLayout)rootView.findViewById(R.id.linear_header);
//        linear_footer=(FrameLayout)rootView.findViewById(R.id.linear_footer);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
        } else {
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
            }

        }

        setTabAdapter();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopTextColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));

        } else {
            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopTextColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));

        }

        return rootView;
    }

    private void setTabAdapter() {
        if (sessionManager.getshowLeadRetrivalSettingTab().equalsIgnoreCase("1")) {
            exhibitorLeadPagerAdapter = new ExhibitorLeadPagerAdapter(getChildFragmentManager(), "0", leadCount);
        } else {
            exhibitorLeadPagerAdapter = new ExhibitorLeadPagerAdapter(getChildFragmentManager(), "1", leadCount);
        }

        viewPager.setAdapter(exhibitorLeadPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void removeTab(int position) {
        if (tabLayout.getTabCount() >= 1 && position < tabLayout.getTabCount()) {
            tabLayout.removeTabAt(position);
//            viewPager.removeViewAt(position);

        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 3:
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(leadCountReceiver, new IntentFilter(exhibitorMyLeadCount));
    }
    //    public static void changeTab()
//    {
//        viewPager.setCurrentItem(1);
//    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(leadCountReceiver);
    }
}
