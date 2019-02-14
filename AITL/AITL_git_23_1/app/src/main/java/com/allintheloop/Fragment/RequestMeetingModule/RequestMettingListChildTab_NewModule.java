package com.allintheloop.Fragment.RequestMeetingModule;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Adapter.RequestMetting.Adapter_RequestMettingList_ViewPagerAdapter;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewList;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewModeratorList;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestMettingListChildTab_NewModule extends Fragment {


    int position;
    ArrayList<String> stringArrayList;
    boolean isModerator = false;


    ArrayList<RequestMeetingNewList> requestMeetingNewLists;
    ArrayList<RequestMeetingNewModeratorList> moderatorListArrayList;

    TabLayout tabLayout;
    SessionManager sessionManager;
    ViewPager viewPager;
    Adapter_RequestMettingList_ViewPagerAdapter adapter;

    public RequestMettingListChildTab_NewModule() {
        // Required empty public constructor
    }

    public static Fragment getInstance(int position, ArrayList<String> arrayList, ArrayList<RequestMeetingNewList> objectArrayList) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putStringArrayList("stringArray", arrayList);
        bundle.putParcelableArrayList("data", objectArrayList);
        bundle.putBoolean("isModerator", false);
        RequestMettingListChildTab_NewModule tabFragment = new RequestMettingListChildTab_NewModule();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    public static Fragment getInstance(int position, ArrayList<String> arrayList, ArrayList<RequestMeetingNewModeratorList> objectArrayList, boolean ismoderator) {

        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putStringArrayList("stringArray", arrayList);
        bundle.putParcelableArrayList("data", objectArrayList);
        bundle.putBoolean("isModerator", ismoderator);
        RequestMettingListChildTab_NewModule tabFragment = new RequestMettingListChildTab_NewModule();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        stringArrayList = getArguments().getStringArrayList("stringArray");
        isModerator = getArguments().getBoolean("isModerator");

        if (isModerator) {
            moderatorListArrayList = getArguments().getParcelableArrayList("data");
        } else {
            requestMeetingNewLists = getArguments().getParcelableArrayList("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request_metting_list_tab_fragment__new_module, container, false);
        SessionManager sessionManager = new SessionManager(getActivity());


        ArrayList<String> datetime = new ArrayList<>();
        datetime.add("All");
        datetime.add("Accepted");
        datetime.add("Pending");
        datetime.add("Rejected");
        String text;
        text = stringArrayList.get(position).toString().replaceAll("\\n", "");
        viewPager = rootView.findViewById(R.id.mettingType_viewpager);


        if (isModerator) {
            adapter = new Adapter_RequestMettingList_ViewPagerAdapter(getChildFragmentManager(), datetime, "child", text, moderatorListArrayList, position, true);
        } else {
            adapter = new Adapter_RequestMettingList_ViewPagerAdapter(getChildFragmentManager(), datetime, "child", text, requestMeetingNewLists, position, "", false);
        }
        viewPager.setAdapter(adapter);
        tabLayout = rootView.findViewById(R.id.mettingType_tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.setElevation(6);
        } else {
            // do something for phones running an SDK before lollipop
        }

        return rootView;
    }
}
