package com.allintheloop.Fragment;


import android.database.Cursor;
import android.os.Bundle;
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

import com.allintheloop.Adapter.FavoriteList_Adapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.FavoriteList_Data;
import com.allintheloop.Bean.UidCommonKeyClass;
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
public class Favourite_FragmentList extends Fragment implements VolleyInterface {


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    RecyclerView rv_viewfavuroite;
    TextView textViewNoDATA;
    EditText edt_search;
    SessionManager sessionManager;
    String tag = "favoriteList";
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    ArrayList<Attendance> attendanceArrayList;
    ArrayList<Attendance> tmpattendanceArrayList;
    ArrayList<FavoriteList_Data> sectionHeaders;
    ArrayList<FavoriteList_Data> tmpSectionHeaders;
    FavoriteList_Adapter favoriteListAdaoter;
    DefaultLanguage.DefaultLang defaultLang;
    UidCommonKeyClass uidCommonKeyClass;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    LinearLayout Container_favuroite;

    public Favourite_FragmentList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        rv_viewfavuroite = (RecyclerView) rootView.findViewById(R.id.rv_viewfavuroite);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        attendanceArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        defaultLang = sessionManager.getMultiLangString();
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        Container_favuroite = (LinearLayout) rootView.findViewById(R.id.Container_favuroite);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        edt_search.setHint(defaultLang.get49Search());


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    tmpSectionHeaders = new ArrayList<>();
                    for (int k = 0; k < sectionHeaders.size(); k++) {
                        tmpattendanceArrayList = new ArrayList<>();
                        for (int j = 0; j < sectionHeaders.get(k).getFavoriteList().size(); j++) {

                            if (sectionHeaders.get(k).getFavoriteList().get(j).getFavrouite_user_name().toLowerCase().contains(edt_search.getText().toString().toLowerCase()))
                                tmpattendanceArrayList.add(sectionHeaders.get(k).getFavoriteList().get(j));
                        }

                        if (tmpattendanceArrayList.size() != 0) {
                            tmpSectionHeaders.add(
                                    new FavoriteList_Data(
                                            sectionHeaders.get(k).getType(),
                                            sectionHeaders.get(k).getBg_color(),
                                            tmpattendanceArrayList));
                        }
                    }


                    if (tmpSectionHeaders.size() == 0) {

                        textViewNoDATA.setText(defaultLang.get49YouHaveNotSavedAnyFavoritesYet());
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        rv_viewfavuroite.setVisibility(View.GONE);
                    } else {

                        edt_search.setVisibility(View.VISIBLE);
                        favoriteListAdaoter = new FavoriteList_Adapter(tmpSectionHeaders, getActivity());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        textViewNoDATA.setVisibility(View.GONE);
                        rv_viewfavuroite.setVisibility(View.VISIBLE);
                        rv_viewfavuroite.setLayoutManager(mLayoutManager);
                        rv_viewfavuroite.setItemAnimator(new DefaultItemAnimator());
                        rv_viewfavuroite.setAdapter(favoriteListAdaoter);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        if (sessionManager.isLogin())
//        {
//
//            relativeLayout_forceLogin.setVisibility(View.GONE);
//            relativeLayout_Data.setVisibility(View.VISIBLE);
//        } else {
//            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
//            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1"))
//            {
//                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
//                relativeLayout_Data.setVisibility(View.GONE);
//            } else {
//                relativeLayout_forceLogin.setVisibility(View.GONE);
//                relativeLayout_Data.setVisibility(View.VISIBLE);
//            }
//
//        }


        if (sessionManager.isLogin()) {
            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getFavoriteList();

        } else {
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                edt_search.setVisibility(View.GONE);
                textViewNoDATA.setText(defaultLang.get49YouHaveNotSavedAnyFavoritesYet());
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewfavuroite.setVisibility(View.GONE);
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
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, Container_favuroite, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, Container_favuroite, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_Data, Container_favuroite, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, Container_favuroite, topAdverViewArrayList, getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST,
                    MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(),
                    sessionManager.getUserId(), "", "", "",
                    "OT", sessionManager.getMenuid()), 5, false,
                    this);
        }
    }

    private void getFavoriteList() {
        if (GlobalData.checkForUIDVersion()) {
            if (uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    attendanceArrayList.clear();
                    Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
                    Log.d("AITL Cursor Size", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                Log.d("AITL  Oflline", jArrayevent.toString());
                                loadData(jArrayevent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (GlobalData.checkForUIDVersion())
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteListUid, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                        else
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteList, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                    } else {
                        if (GlobalData.checkForUIDVersion())
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteListUid, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                        else
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteList, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                    }
                } else {
                    attendanceArrayList.clear();
                    Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
                    Log.d("AITL Cursor Size", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                loadData(jArrayevent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        edt_search.setVisibility(View.GONE);
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        textViewNoDATA.setText(defaultLang.get49YouHaveNotSavedAnyFavoritesYet());
                        rv_viewfavuroite.setVisibility(View.GONE);
                    }
                }
            } else {
                edt_search.setVisibility(View.GONE);
                textViewNoDATA.setText("No favorites contact available for this user");
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewfavuroite.setVisibility(View.GONE);

            }
        } else {
            if (sessionManager.getRolId().equalsIgnoreCase("4")) //changes applied
            {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    attendanceArrayList.clear();
                    Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
                    Log.d("AITL Cursor Size", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                Log.d("AITL  Oflline", jArrayevent.toString());
                                loadData(jArrayevent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (GlobalData.checkForUIDVersion())
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteListUid, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                        else
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteList, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                    } else {
                        if (GlobalData.checkForUIDVersion())
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteListUid, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                        else
                            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavoriteList, Param.getFavoriteList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
                    }
                } else {
                    attendanceArrayList.clear();
                    Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
                    Log.d("AITL Cursor Size", "" + cursor.getCount());
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                JSONObject jArrayevent = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                                Log.d("AITL  Oflline", jArrayevent.toString());
                                loadData(jArrayevent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        edt_search.setVisibility(View.GONE);
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        textViewNoDATA.setText(defaultLang.get49YouHaveNotSavedAnyFavoritesYet());
                        rv_viewfavuroite.setVisibility(View.GONE);
                    }
                }
            } else {
                edt_search.setVisibility(View.GONE);
                textViewNoDATA.setText("No favorites contact available for this user");
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewfavuroite.setVisibility(View.GONE);

            }
        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        attendanceArrayList.clear();
                        sectionHeaders = new ArrayList<>();
                        tmpattendanceArrayList = new ArrayList<>();

                        if (sessionManager.isLogin())
                            pagewiseClick();

                        loadData(jsonObject);
                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObject.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObject.toString(), tag);
                        }

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

    private void loadData(JSONObject jsonData) {
        try {
            sectionHeaders = new ArrayList<>();
            tmpattendanceArrayList = new ArrayList<>();

            JSONArray jsonArray = jsonData.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject index = jsonArray.getJSONObject(i);
                JSONArray jArrayData = index.getJSONArray("data");
                attendanceArrayList = new ArrayList<>();
                for (int k = 0; k < jArrayData.length(); k++) {
                    JSONObject object = jArrayData.getJSONObject(k);
                    attendanceArrayList.add(new Attendance(object.getString("Id")
                            , object.getString("module_id")
                            , object.getString("module_type")
                            , object.getString("user_name")
                            , object.getString("logo")
                            , object.getString("extra")
                            , tag));
                }
                sectionHeaders.add(new FavoriteList_Data(index.getString("type"), index.getString("color"), attendanceArrayList));
                //arrayListHashMap.put(sectionHeaders.get(i), attendanceArrayList);
            }

            if (sectionHeaders.size() == 0) {
                edt_search.setVisibility(View.GONE);
                textViewNoDATA.setText(defaultLang.get49YouHaveNotSavedAnyFavoritesYet());
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewfavuroite.setVisibility(View.GONE);

            } else {
                edt_search.setVisibility(View.VISIBLE);
                favoriteListAdaoter = new FavoriteList_Adapter(sectionHeaders, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                textViewNoDATA.setVisibility(View.GONE);
                rv_viewfavuroite.setVisibility(View.VISIBLE);
                rv_viewfavuroite.setLayoutManager(mLayoutManager);
                rv_viewfavuroite.setItemAnimator(new DefaultItemAnimator());
                rv_viewfavuroite.setAdapter(favoriteListAdaoter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
