package com.allintheloop.Fragment.AttandeeFragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Attendee.AttendeeMainCategoryAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Attendee.AttendeeCategoryList;
import com.allintheloop.Bean.DefaultLanguage;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AttendeeMainCategoryFragment extends Fragment implements VolleyInterface {

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    TextView textViewNoDATA;
    EditText edt_search;
    RecyclerView rv_viewMainGroup;
    RelativeLayout MainLayout, relativeLayout_static;
    LinearLayout linear_content;
    DefaultLanguage.DefaultLang defaultLanguage;
    SessionManager sessionManager;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    AttendeeMainCategoryAdapter attendeeGroupAdapter;
    LinearLayoutManager layoutManager;


    AttendeeCategoryList attendeeCategory;
    ArrayList<AttendeeCategoryList.AttendeeCategory> attendeeCategoryArrayList;

    public AttendeeMainCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attandee_main_groups, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        initView(rootView);
        getAttendeeCategorydata();
        getAdvertiesment();
        return rootView;
    }

    public void initView(View rootView) {

        rv_viewMainGroup = (RecyclerView) rootView.findViewById(R.id.rv_viewFullAttendance);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        relativeLayout_static = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_static);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);

        sessionManager = new SessionManager(getActivity());
        sessionManager.setAttendeeMainCategoryData("");
        defaultLanguage = sessionManager.getMultiLangString();
        edt_search.setHint(defaultLanguage.get2Search());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    attendeeGroupAdapter.getFilter().filter("");
                    sessionManager.keyboradHidden(edt_search);
                } else {
                    attendeeGroupAdapter.getFilter().filter(editable.toString());
                }
            }
        });
    }


    private void getAttendeeCategorydata() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_AttendanceCategoryList, Param.getCMS_superGroupData(sessionManager.getEventId()), 0, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 6, false, this);
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.adverties_Data)));
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

            sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
            sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, topAdverViewArrayList, getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        Gson gson = new Gson();
                        attendeeCategoryArrayList = new ArrayList<>();
                        attendeeCategory = gson.fromJson(jsonObject.getString("data"), AttendeeCategoryList.class);
                        attendeeCategoryArrayList.addAll(attendeeCategory.getAttendeeCategories());

                        if (attendeeCategoryArrayList.size() > 0) {


                            layoutManager = new LinearLayoutManager(getActivity());
                            rv_viewMainGroup.setLayoutManager(layoutManager);
                            rv_viewMainGroup.setItemAnimator(new DefaultItemAnimator());
                            attendeeGroupAdapter = new AttendeeMainCategoryAdapter(attendeeCategoryArrayList, getActivity());
                            rv_viewMainGroup.setAdapter(attendeeGroupAdapter);
                        } else {
                            sessionManager.setAttendeeMainCategoryData("");
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
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
