package com.allintheloop.Fragment;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.MatchMaking.MatchMakingPagerAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.MatchMaking.MatchMakingModuleNameData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchmakingTab_Fragment extends Fragment implements VolleyInterface {


    TabLayout tabLayout;
    SessionManager sessionManager;
    ViewPager viewPager;
    TextView txt_data;

    MatchMakingPagerAdapter adapter;
    ArrayList<String> titleName;
    ArrayList<MatchMakingModuleNameData.ModulesName> dataArrayList;
    MatchMakingModuleNameData moduleNameData;
    CardView card_infoMsg;
    LinearLayout linear_tabdata;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;


    RelativeLayout relativeLayout_Data, MainLayout;

    public MatchmakingTab_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_matchmaking_module, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        initView(rootView);
        getAdvertiesment();
        return rootView;
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 1, false, this);

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.adverties_Data)));
                        Log.d("AITL  Map Oflline", jsonObject.toString());
                        getAdvertiesment(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void getAdvertiesment(JSONObject jsonObject) {

        try {
            JSONObject jsonObjectAdavertiesment = jsonObject.getJSONObject("data");
            JSONArray jArrayHeader = jsonObjectAdavertiesment.getJSONArray("header");
            JSONArray jArrayFooter = jsonObjectAdavertiesment.getJSONArray("footer");

            topAdverViewArrayList = new ArrayList<>();
            bottomAdverViewArrayList = new ArrayList<>();
            for (int i = 0; i < jArrayHeader.length(); i++) {
                JSONObject index = jArrayHeader.getJSONObject(i);
                topAdverViewArrayList.add(new AdvertiesmentTopView(index.getString("image"), index.getString("url"), index.getString("id")));
            }

            for (int i = 0; i < jArrayFooter.length(); i++) {
                JSONObject index = jArrayFooter.getJSONObject(i);
                bottomAdverViewArrayList.add(new AdvertiesMentbottomView(index.getString("image"), index.getString("url"), index.getString("id")));
            }

            sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, linear_tabdata, bottomAdverViewArrayList, getActivity());
            sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_tabdata, topAdverViewArrayList, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getModuleData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            card_infoMsg.setVisibility(View.GONE);
            linear_tabdata.setVisibility(View.VISIBLE);
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getMathcMakingModuleName, Param.geMatchMakingModuleName(sessionManager.getEventId()), 0, true, this);
        } else {
            card_infoMsg.setVisibility(View.VISIBLE);
            linear_tabdata.setVisibility(View.GONE);
            txt_data.setText(getString(R.string.noInernet));
        }
    }

    private void initView(View rootView) {
        sessionManager = new SessionManager(getActivity());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        dataArrayList = new ArrayList<>();
        titleName = new ArrayList<>();

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        viewPager = rootView.findViewById(R.id.matchmaking_view_pager);

        tabLayout = rootView.findViewById(R.id.matchmaking_tab);
        txt_data = (TextView) rootView.findViewById(R.id.txt_data);
        card_infoMsg = (CardView) rootView.findViewById(R.id.card_infoMsg);
        linear_tabdata = (LinearLayout) rootView.findViewById(R.id.linear_tabdata);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        if (sessionManager.isLogin()) {
            getModuleData();
        } else {
            card_infoMsg.setVisibility(View.VISIBLE);
            linear_tabdata.setVisibility(View.GONE);
            txt_data.setText(getString(R.string.login_message));
        }
    }

    private void loadData(JSONObject jsonObject) {
        try {
            Gson gson = new Gson();

            moduleNameData = gson.fromJson(jsonObject.toString(), MatchMakingModuleNameData.class);
            dataArrayList.addAll(moduleNameData.getModulesName());
            if (dataArrayList.size() != 0) {
                tabLayout.setVisibility(View.VISIBLE);
                for (MatchMakingModuleNameData.ModulesName name : dataArrayList) {
                    titleName.add(name.getMenuname());
                }
                adapter = new MatchMakingPagerAdapter(getChildFragmentManager(), dataArrayList, titleName);
                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(0);
                tabLayout.setupWithViewPager(viewPager);
                setUpTabLayout();

            } else {
                tabLayout.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setUpTabLayout() {

        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getFunTopTextColor()), Color.parseColor(sessionManager.getFunTopBackColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));

        } else {
            tabLayout.setTabTextColors(Color.parseColor(sessionManager.getTopTextColor()), Color.parseColor(sessionManager.getTopBackColor()));
            tabLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(sessionManager.getTopTextColor()));
            ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(0).setBackgroundColor(Color.parseColor(sessionManager.getTopTextColor()));
        }

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
                ((MainActivity) getActivity()).selectedTabCount = tabLayout.getSelectedTabPosition();
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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        loadData(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Log.d("AITL Advertiesment", jsonObject.toString());

                        if (sqLiteDatabaseHandler.isAdvertiesMentExist(sessionManager.getEventId(), sessionManager.getMenuid())) {
                            sqLiteDatabaseHandler.deleteAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        }

                        getAdvertiesment(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
