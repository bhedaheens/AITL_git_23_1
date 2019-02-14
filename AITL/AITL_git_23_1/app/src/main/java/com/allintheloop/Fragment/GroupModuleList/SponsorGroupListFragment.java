package com.allintheloop.Fragment.GroupModuleList;


import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.GroupModuleListAdapter.GroupMapListAdapter;
import com.allintheloop.Adapter.GroupModuleListAdapter.GroupSponsorListAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.GroupingData.GroupModuleData;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SponsorGroupListFragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewMapGroup;
    EditText edt_search;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    ArrayList<GroupModuleData> groupModuleDataArrayList;
    SessionManager sessionManager;
    GroupSponsorListAdapter sponsorListAdapter;
    TextView txt_agendaNoTxt;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    LinearLayout linear_content;
    Context context;


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    public SponsorGroupListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sponsor_group_list, container, false);


        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        rv_viewMapGroup = (RecyclerView) rootView.findViewById(R.id.rv_viewMapGroup);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getContext());
        sessionManager = new SessionManager(getActivity());
        txt_agendaNoTxt = (TextView) rootView.findViewById(R.id.txt_agendaNoTxt);
        context = getActivity();
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);

        groupModuleDataArrayList = new ArrayList<>();
        GlobalData.currentModuleForOnResume = GlobalData.sponsorModuleid;
        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getGropListData();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getGropListData();
            }

        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (groupModuleDataArrayList.size() > 0) {
                    sponsorListAdapter.getFilter().filter(s);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void afterTextChanged(Editable s) {

                if (groupModuleDataArrayList.size() > 0) {
                    sponsorListAdapter.getFilter().filter(s);
                }
            }
        });

        getAdvertiesment();

        return rootView;
    }


    private void getGropListData() {
        groupModuleDataArrayList = new ArrayList<>();
        groupModuleDataArrayList = sqLiteDatabaseHandler.getMainGroupData(sessionManager.getEventId(), sessionManager.getMenuid());
        if (groupModuleDataArrayList.size() != 0) {
            txt_agendaNoTxt.setVisibility(View.GONE);
            rv_viewMapGroup.setVisibility(View.VISIBLE);
            sponsorListAdapter = new GroupSponsorListAdapter(groupModuleDataArrayList, context);
            rv_viewMapGroup.setItemAnimator(new DefaultItemAnimator());
            rv_viewMapGroup.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_viewMapGroup.setAdapter(sponsorListAdapter);
        } else {
            txt_agendaNoTxt.setVisibility(View.VISIBLE);
            rv_viewMapGroup.setVisibility(View.GONE);
            txt_agendaNoTxt.setText("No Sponsor Group Found");
            sessionManager.setSponsorPrentGroupID("");
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.SponsorGroupWiseFragment;
            ((MainActivity) getActivity()).loadFragment();
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

            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1")) {
                sessionManager.footerView(context, "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "0", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(context, "1", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 2, false, this);

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


    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.sponsorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.sponsorModuleid);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 2:
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
