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

import com.allintheloop.Adapter.OrderPagerAdapter;
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
public class Order_FragmentTab extends Fragment implements VolleyInterface {


    TabLayout Order_tab_layout;
    ViewPager Order_view_pager;
    OrderPagerAdapter orderPagerAdapter;
    //SharedPreferences preferences;
    SessionManager sessionManager;

    public Order_FragmentTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        sessionManager = new SessionManager(getActivity());

        // preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        Order_tab_layout = (TabLayout) rootView.findViewById(R.id.Order_tab_layout);
        Order_view_pager = (ViewPager) rootView.findViewById(R.id.Order_view_pager);
        sessionManager.strModuleId = "Orders";
        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            Log.d("AITL", "Fundraising Color");
            Order_tab_layout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopTextColor()));
            Order_tab_layout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            Order_tab_layout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            Log.d("AITL", "EvenApp Color");
            Order_tab_layout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopTextColor()));
            Order_tab_layout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            Order_tab_layout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

//        if(GlobalData.isNetworkAvailable(getActivity()))
//        {
//            if (sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
//            }
//        }


        //fragmentManager = getFragmentManager();
        orderPagerAdapter = new OrderPagerAdapter(getChildFragmentManager());

        Order_view_pager.setAdapter(orderPagerAdapter);
        Order_tab_layout.setupWithViewPager(Order_view_pager);


        Order_view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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
