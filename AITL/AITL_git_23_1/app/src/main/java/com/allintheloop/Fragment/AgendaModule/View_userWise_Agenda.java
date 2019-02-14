package com.allintheloop.Fragment.AgendaModule;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.allintheloop.Adapter.Agenda.UserWiseAgendaPager;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequestResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class View_userWise_Agenda extends Fragment implements VolleyInterface {

    TabLayout tabLayout;
    ViewPager viewPager;
    UserWiseAgendaPager agendaPagerAdapter;
    //SharedPreferences preferences;
    SessionManager sessionManager;
    public static LinearLayout button_layout;
    public Button btn_suggested;

    public View_userWise_Agenda() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_userwise__agenda, container, false);

        sessionManager = new SessionManager(getActivity());

        // preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        tabLayout = (TabLayout) rootView.findViewById(R.id.userWise_Agenda);
        viewPager = (ViewPager) rootView.findViewById(R.id.userWise_Agenda_view_pager);
        button_layout = (LinearLayout) rootView.findViewById(R.id.button_layout);
        btn_suggested = (Button) rootView.findViewById(R.id.btn_suggested);
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {

            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopTextColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_suggested.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_suggested.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopTextColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_suggested.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_suggested.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

//        if (GlobalData.isNetworkAvailable(getActivity()))
//        {
//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),1, false, this);
//            }
//        }

        agendaPagerAdapter = new UserWiseAgendaPager(getChildFragmentManager(), sessionManager);

        viewPager.setAdapter(agendaPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        btn_suggested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.viewSuggestedTimeList;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
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
        // ((MainActivity)getActivity()).getUpdatedDataFromParticularmodule(GlobalData.agendaModuleid);
    }
}
