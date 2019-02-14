package com.allintheloop.Fragment.RequestMeetingModule;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Adapter.RequestMeetingInvitePagerAdapter;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestMeetingInviteMore_Fragment extends Fragment {


    TabLayout tabLayout;
    ViewPager viewPager;
    SessionManager sessionManager;
    RequestMeetingInvitePagerAdapter pagerAdapter;

    public RequestMeetingInviteMore_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_requestmeeting_invitemore, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        viewPager = rootView.findViewById(R.id.request_view_pager);
        tabLayout = rootView.findViewById(R.id.reuqest_tab);
        sessionManager = new SessionManager(getActivity());
        pagerAdapter = new RequestMeetingInvitePagerAdapter(getChildFragmentManager(), sessionManager);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            Log.d("AITL", "Fundraising Color");
            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopTextColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));

        } else {
            Log.d("AITL", "EvenApp Color");
            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopTextColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

        return rootView;
    }

}
