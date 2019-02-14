package com.allintheloop.Fragment.GroupModuleList;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.CmsListDataAdapter;
import com.allintheloop.Adapter.GroupModuleListAdapter.CMSChildGroupListAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.CmsGroupData.CMSList;
import com.allintheloop.Bean.CmsGroupData.CmsChildGroupData;
import com.allintheloop.Bean.CmsGroupData.CmsChildGroupList;
import com.allintheloop.Bean.CmsGroupData.CmsListData;
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
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class CMSListFragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewCMSList;
    EditText edt_search;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    ArrayList<CmsListData> cmsListData;
    SessionManager sessionManager;
    CmsListDataAdapter cmsListDataAdapter;
    TextView txt_agendaNoTxt;
    String tag = "";
    Context context;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    RelativeLayout MainLayout, relativeLayout_Data;
    LinearLayout linear_content;

    public CMSListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cmslist, container, false);

        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        rv_viewCMSList = (RecyclerView) rootView.findViewById(R.id.rv_viewCMSList);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        txt_agendaNoTxt = (TextView) rootView.findViewById(R.id.txt_agendaNoTxt);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getContext());
        sessionManager = new SessionManager(getActivity());
        cmsListData = new ArrayList<>();
        context = getActivity();

        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        GlobalData.currentModuleForOnResume = GlobalData.cmsModuleid;

//        getCMSListData();

        getCmsListOffline();
        getAdvertiesment();
        return rootView;
    }

    private void getCmsListOffline() {
        cmsListData = new ArrayList<>();
        cmsListData = sqLiteDatabaseHandler.getCMSListData(sessionManager.getEventId(), sessionManager.getCmsChildGroupId());

        if (cmsListData.size() > 0) {

            txt_agendaNoTxt.setVisibility(View.GONE);
            rv_viewCMSList.setVisibility(View.VISIBLE);
            Collections.sort(cmsListData, new SortComparator());
            cmsListDataAdapter = new CmsListDataAdapter(cmsListData, context);
            rv_viewCMSList.setItemAnimator(new DefaultItemAnimator());
            rv_viewCMSList.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_viewCMSList.setAdapter(cmsListDataAdapter);
        } else {
            txt_agendaNoTxt.setVisibility(View.VISIBLE);
            rv_viewCMSList.setVisibility(View.GONE);
        }


    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObject.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObject.toString(), tag);
                        }

                        loadData(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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


    private void loadData(JSONObject jsonObject) {
        try {
            cmsListData.clear();
            Gson gson = new Gson();
            CMSList qandAGroupListData = gson.fromJson(jsonObject.getString("data"), CMSList.class);
            cmsListData.addAll(qandAGroupListData.getGroupModuleData());

            if (cmsListData.size() > 0) {
                txt_agendaNoTxt.setVisibility(View.GONE);
                rv_viewCMSList.setVisibility(View.VISIBLE);
                cmsListDataAdapter = new CmsListDataAdapter(cmsListData, context);
                rv_viewCMSList.setItemAnimator(new DefaultItemAnimator());
                rv_viewCMSList.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_viewCMSList.setAdapter(cmsListDataAdapter);
            } else {
                txt_agendaNoTxt.setVisibility(View.VISIBLE);
                rv_viewCMSList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.cmsModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.cmsModuleid);
        }
    }

    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String time1 = "", time2 = "";
            if (o1 instanceof CmsListData) {
                time1 = ((CmsListData) o1).getMenuName();
            }
            if (o2 instanceof CmsListData) {
                time2 = ((CmsListData) o2).getMenuName();
            }
            return time1.compareTo(time2);
        }
    }

}
