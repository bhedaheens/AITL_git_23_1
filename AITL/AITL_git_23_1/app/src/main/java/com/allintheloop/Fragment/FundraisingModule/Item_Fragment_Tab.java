package com.allintheloop.Fragment.FundraisingModule;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.allintheloop.Adapter.ItemPagerAdapter;
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
public class Item_Fragment_Tab extends Fragment implements VolleyInterface {


    TabLayout item_tab_layout;
    ViewPager item_view_pager;
    ItemPagerAdapter itemPagerAdapter;
    //SharedPreferences preferences;
    SessionManager sessionManager;
    Button btn_addItem;

    public Item_Fragment_Tab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_item_tab, container, false);
        sessionManager = new SessionManager(getActivity());

        // preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        sessionManager.strModuleId = "Items";
        item_tab_layout = (TabLayout) rootView.findViewById(R.id.item_tab_layout);
        item_view_pager = (ViewPager) rootView.findViewById(R.id.item_view_pager);
        btn_addItem = (Button) rootView.findViewById(R.id.btn_addItem);
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            Log.d("AITL", "Fundraising Color");
            item_tab_layout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopTextColor()));
            item_tab_layout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            item_tab_layout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_addItem.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_addItem.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            Log.d("AITL", "EvenApp Color");
            item_tab_layout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopTextColor()));
            item_tab_layout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            item_tab_layout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_addItem.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_addItem.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

        btn_addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.Fragment_Stack.add(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.AdditemFragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

//        if(GlobalData.isNetworkAvailable(getActivity()))
//        {
//            if (sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),0, false, this);
//            }
//        }
        itemPagerAdapter = new ItemPagerAdapter(getChildFragmentManager());

        item_view_pager.setAdapter(itemPagerAdapter);
        item_tab_layout.setupWithViewPager(item_view_pager);


        item_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

    }
}
