package com.allintheloop.Fragment.RequestMeetingModule;


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
import android.widget.TextView;

import com.allintheloop.Adapter.RequestMetting.Adapter_RequestMettingList_ViewPagerAdapter;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingListNewDataList;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingListNewModeratorDataList;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewList;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewModeratorList;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.allintheloop.Util.GlobalData.updateRequestMeetingReloadData;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestMettingListFragment_newModule extends Fragment implements VolleyInterface {


    TabLayout tabLayout;
    SessionManager sessionManager;
    ViewPager viewPager;
    UidCommonKeyClass uidCommonKeyClass;
    RequestMeetingListNewDataList requestMeetingNewList;
    ArrayList<Object> requestMeetingListNewDataListArrayList;
    ArrayList<RequestMeetingNewList> requeChildList;

    RequestMeetingListNewModeratorDataList listNewModeratorDataListDataList;
    ArrayList<Object> modertorArrayList;
    ArrayList<RequestMeetingNewModeratorList> requestMeetingNewModeratorLists;


    Adapter_RequestMettingList_ViewPagerAdapter adapter;

    TextView txt_data;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshData();
        }
    };


    public RequestMettingListFragment_newModule() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_metting_list, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        requestMeetingListNewDataListArrayList = new ArrayList<>();
        requeChildList = new ArrayList<>();

        modertorArrayList = new ArrayList<>();
        requestMeetingNewModeratorLists = new ArrayList<>();

        viewPager = rootView.findViewById(R.id.request_view_pager);
        tabLayout = rootView.findViewById(R.id.reuqest_tab);
        txt_data = rootView.findViewById(R.id.txt_data);

        sessionManager.strModuleId = "Meetings";


        setUpData();
        getListData();
        return rootView;
    }

    private void setUpData() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopBackColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition()).setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                } else {
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopBackColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition()).setBackgroundColor(Color.parseColor(sessionManager.getTopTextColor()));
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getFunTopTextColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition()).setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                } else {
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopTextColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition()).setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void getListData() {
        requestMeetingListNewDataListArrayList.clear();
        requeChildList.clear();

        modertorArrayList.clear();
        requestMeetingNewModeratorLists.clear();

        if (GlobalData.isNetworkAvailable(getActivity())) {

            if (sessionManager.getRolId().equalsIgnoreCase("6")) {//postponed - pending
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exhibitorgetAllRequestMeetingwithDateNew, Param.getAllRequestMettingList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
            } else if (sessionManager.getRolId().equalsIgnoreCase("4"))//postponed - pending
            {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeegetAllRequestMeetingwithDate, Param.getAllRequestMettingList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
            } else if (sessionManager.getRolId().equalsIgnoreCase("7") &&//postponed - pending
                    sessionManager.isModerater().equalsIgnoreCase("1")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.moderatorGetAllRequestMeetingWithDate, Param.getAllRequestMettingList(sessionManager.getEventId(), sessionManager.getUserId()), 1, false, this);
            }

        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }

    private void refreshData() {


        GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
        ((MainActivity) getActivity()).reloadFragment();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        requestMeetingNewList = gson.fromJson(jsonObject.toString(), RequestMeetingListNewDataList.class);
                        requestMeetingListNewDataListArrayList.addAll(requestMeetingNewList.getRequestMeetingNewLists());
                        requeChildList.addAll(requestMeetingNewList.getRequestMeetingNewLists());
                        loadData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        listNewModeratorDataListDataList = gson.fromJson(jsonObject.toString(), RequestMeetingListNewModeratorDataList.class);
                        modertorArrayList.addAll(listNewModeratorDataListDataList.getRequestMeetingNewLists());
                        requestMeetingNewModeratorLists.addAll(listNewModeratorDataListDataList.getRequestMeetingNewLists());
                        loadData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void loadData() {
        ArrayList<String> datetime = new ArrayList<>();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        SimpleDateFormat newFormat = new SimpleDateFormat("MMM\nEEE dd");
        if (sessionManager.getRolId().equalsIgnoreCase("7") && //postponed - pending
                sessionManager.isModerater().equalsIgnoreCase("1")) {
            for (int i = 0; i < modertorArrayList.size(); i++) {
                RequestMeetingNewModeratorList dateTimeList = (RequestMeetingNewModeratorList) modertorArrayList.get(i);
                Date date = null;
                try {
                    date = simpleDateFormat.parse(dateTimeList.getDate());
                    datetime.add(newFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            if (modertorArrayList.size() > 0) {

                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                txt_data.setVisibility(View.GONE);
                adapter = new Adapter_RequestMettingList_ViewPagerAdapter(getChildFragmentManager(), datetime, "parent", requestMeetingNewModeratorLists, "", true);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
                    Log.d("AITL", "Fundraising Color");
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopBackColor()));
                    tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));

                } else {
                    Log.d("AITL", "EvenApp Color");
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopBackColor()));
                    tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setBackgroundColor(Color.parseColor(sessionManager.getTopTextColor()));
                }
            } else {
                viewPager.setVisibility(View.GONE);
                txt_data.setVisibility(View.VISIBLE);
                tabLayout.setVisibility(View.GONE);
            }
        } else {
            for (int i = 0; i < requestMeetingListNewDataListArrayList.size(); i++) {
                RequestMeetingNewList dateTimeList = (RequestMeetingNewList) requestMeetingListNewDataListArrayList.get(i);
                Date date = null;
                try {
                    date = simpleDateFormat.parse(dateTimeList.getDate());
                    datetime.add(newFormat.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (requestMeetingListNewDataListArrayList.size() > 0) {
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
                txt_data.setVisibility(View.GONE);

                adapter = new Adapter_RequestMettingList_ViewPagerAdapter(getChildFragmentManager(), datetime, "parent", requeChildList, false);
                viewPager.setAdapter(adapter);
                tabLayout.setupWithViewPager(viewPager);
                if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
                    Log.d("AITL", "Fundraising Color");
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopBackColor()));
                    tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));

                } else {
                    Log.d("AITL", "EvenApp Color");
                    tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopBackColor()));
                    tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));
                    ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setBackgroundColor(Color.parseColor(sessionManager.getTopTextColor()));
                }
            } else {
                tabLayout.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                txt_data.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(updateRequestMeetingReloadData));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);

    }
}
