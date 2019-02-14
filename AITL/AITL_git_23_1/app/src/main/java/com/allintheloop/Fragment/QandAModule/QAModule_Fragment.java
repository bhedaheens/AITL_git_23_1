package com.allintheloop.Fragment.QandAModule;


import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.allintheloop.Adapter.QaModuleDataAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.QAList.QaModuleData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class QAModule_Fragment extends Fragment implements VolleyInterface {


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    RecyclerView rv_viewQaData;
    QaModuleDataAdapter qaModuleDataAdapter;
    ArrayList<QaModuleData> qaModuleDataArrayList;
    TextView textViewNoDATA, txt_label;
    SessionManager sessionManager;
    RelativeLayout relativeLayout_forceLogin;
    LinearLayout relativeLayout_Data;
    EditText edt_search;
    String tag = "";
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DefaultLanguage.DefaultLang defaultLang;
    RelativeLayout MainLayout, relativeLayout_static;
    LinearLayout linear_content;

    public QAModule_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qamodule, container, false);
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        txt_label = (TextView) rootView.findViewById(R.id.txt_label);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        rv_viewQaData = (RecyclerView) rootView.findViewById(R.id.rv_viewQaData);
        qaModuleDataArrayList = new ArrayList<>();

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        sessionManager = new SessionManager(getActivity());
        tag = "Q&AListModule" + sessionManager.getQandAgroupID();
        defaultLang = sessionManager.getMultiLangString();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        relativeLayout_Data = (LinearLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        relativeLayout_static = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_static);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        edt_search.setHint(defaultLang.get50SearchQA());
        txt_label.setText(defaultLang.get50TapASessionToAskAQuestionAndInteract());

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (qaModuleDataArrayList.size() > 0) {
                    qaModuleDataAdapter.getFilter().filter(s);
                }


            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void afterTextChanged(Editable s) {

                if (qaModuleDataArrayList.size() > 0) {
                    qaModuleDataAdapter.getFilter().filter(s);
                }

            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    updateData();
                } else {
                    ToastC.show(getActivity(), "No Internet Connection");
                }
            }
        });


        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getListData();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getListData();
            }

        }


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

    private void getListData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            qaModuleDataArrayList = new ArrayList<>();
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL  Q&A  Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getQaListSession, Param.getQandAListData(sessionManager.getEventId(), sessionManager.getQandAgroupID(), sessionManager.getUserId()), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getQaListSession, Param.getQandAListData(sessionManager.getEventId(), sessionManager.getQandAgroupID(), sessionManager.getUserId()), 0, false, this);
            }


        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL  Q&A  Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        qaModuleDataArrayList = new ArrayList<>();
                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObject.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObject.toString(), tag);
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                        loadData(jsonObject);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    pagewiseClick();
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


            sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, relativeLayout_Data, bottomAdverViewArrayList, getActivity());
            sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_static, relativeLayout_Data, topAdverViewArrayList, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateData() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getQaListSession, Param.getQandAListData(sessionManager.getEventId(), sessionManager.getQandAgroupID(), sessionManager.getUserId()), 0, false, this);
    }

    private void loadData(JSONObject jsonObject) {
        try {

            JSONArray jsonArrayData = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArrayData.length(); i++) {
                JSONObject index = jsonArrayData.getJSONObject(i);
                qaModuleDataArrayList.add(new QaModuleData(
                        index.getString("session_id")
                        , index.getString("Session_name")
                        , index.getString("Event_id")
                        , index.getString("session_date")
                        , index.getString("start_time")
                        , index.getString("end_time")
                        , index.getString("time")
                        , index.getString("total_question")));
            }

            if (sessionManager.isLogin())
                pagewiseClick();


            if (qaModuleDataArrayList.size() == 0) {
                textViewNoDATA.setText("No Data Found");
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewQaData.setVisibility(View.GONE);
            } else {
                qaModuleDataAdapter = new QaModuleDataAdapter(qaModuleDataArrayList, getActivity(), sessionManager);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                textViewNoDATA.setVisibility(View.GONE);
                rv_viewQaData.setVisibility(View.VISIBLE);
                rv_viewQaData.setLayoutManager(mLayoutManager);
                rv_viewQaData.setItemAnimator(new DefaultItemAnimator());
                rv_viewQaData.setAdapter(qaModuleDataAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
