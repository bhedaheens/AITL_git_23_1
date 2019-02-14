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

import com.allintheloop.Adapter.Pending_AgendaPager_Adapter;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

/**
 * A simple {@link Fragment} subclass.
 */
public class View_PendingAgenda_Fragment extends Fragment implements VolleyInterface {


    TabLayout pending_Agenda;
    ViewPager pending_Agenda_view_pager;
    Pending_AgendaPager_Adapter agendaPagerAdapter;
    //SharedPreferences preferences;
    SessionManager sessionManager;
    Button btn_submitnyAgenda;

    public View_PendingAgenda_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_viewpending_agenda, container, false);


        sessionManager = new SessionManager(getActivity());

        // preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        pending_Agenda = (TabLayout) rootView.findViewById(R.id.pending_Agenda);
        pending_Agenda_view_pager = (ViewPager) rootView.findViewById(R.id.pending_Agenda_view_pager);
        btn_submitnyAgenda = (Button) rootView.findViewById(R.id.btn_submitnyAgenda);

        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {

            pending_Agenda.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopTextColor()));
            pending_Agenda.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            pending_Agenda.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_submitnyAgenda.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_submitnyAgenda.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
        } else {

            pending_Agenda.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopTextColor()));
            pending_Agenda.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            pending_Agenda.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));

            btn_submitnyAgenda.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_submitnyAgenda.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
        }

        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
//            }
        }

        agendaPagerAdapter = new Pending_AgendaPager_Adapter(getChildFragmentManager(), sessionManager);

        pending_Agenda_view_pager.setAdapter(agendaPagerAdapter);
        pending_Agenda.setupWithViewPager(pending_Agenda_view_pager);

        btn_submitnyAgenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Agenda_BookStatus;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        pending_Agenda_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
    public void onResume() {
        super.onResume();
        // ((MainActivity)getActivity()).getUpdatedDataFromParticularmodule(GlobalData.agendaModuleid);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

    }
}
