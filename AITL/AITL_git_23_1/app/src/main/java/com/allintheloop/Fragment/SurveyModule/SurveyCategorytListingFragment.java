package com.allintheloop.Fragment.SurveyModule;


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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Adapter.SurveyCategoryListingAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.SurveyCategoryListing;
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
public class SurveyCategorytListingFragment extends Fragment implements VolleyInterface {


    RecyclerView recycler_categoryListing;
    SessionManager sessionManager;
    ArrayList<SurveyCategoryListing> surveyCategoryListings;
    RelativeLayout relativeLayout_forceLogin, MainLayout, relativeLayout_static;
    LinearLayout relativeLayout_Data;


    Bundle bundle;
    SurveyCategoryListingAdapter categoryListingAdapter;
    TextView textViewNoDATA;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "SurveyCategoryLisitng";

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;


    public SurveyCategorytListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");

        View rootView = inflater.inflate(R.layout.fragment_survey_categoryt_listing, container, false);
        sessionManager = new SessionManager(getActivity());

        relativeLayout_Data = (LinearLayout) rootView.findViewById(R.id.relativeLayout_Data);
        recycler_categoryListing = (RecyclerView) rootView.findViewById(R.id.recycler_categoryListing);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        relativeLayout_static = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_static);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);

        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        surveyCategoryListings = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();


        recycler_categoryListing.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                SurveyCategoryListing mapObj = surveyCategoryListings.get(position);
                sessionManager.setCategoryId(mapObj.getCategory_id());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.SurveyCategoryWiseFragment;
                ((MainActivity) getActivity()).loadFragment();
                //startActivity(new Intent(getActivity(), Map_Detail_Activity.class).putExtra("map_id",mapObj.getId()));
            }
        }));

        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getsurveyListingFragment();

        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getsurveyListingFragment();
            }
        }
        getAdvertiesment();
        bundle = new Bundle();
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


    private void getsurveyListingFragment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getSurveyCategoryListing, Param.getSurveyListingFragment(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId()), 0, false, this);
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL  Map Oflline", jsonObject.toString());
                        dataLoad(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL JsonData", jsonObject.toString());
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        surveyCategoryListings.clear();

                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObjectData.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonObjectData.toString(), tag);
                        }
                        dataLoad(jsonObjectData);

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
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, relativeLayout_Data, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_static, relativeLayout_Data, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_static, relativeLayout_Data, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_static, relativeLayout_Data, topAdverViewArrayList, getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dataLoad(JSONObject jsonObject) {
        try {
            JSONArray jsonArraySurvey = jsonObject.getJSONArray("survey");
            for (int i = 0; i < jsonArraySurvey.length(); i++) {
                JSONObject index = jsonArraySurvey.getJSONObject(i);
                surveyCategoryListings.add(new SurveyCategoryListing(index.getString("category_id")
                        , index.getString("survey_name")));
            }
            if (surveyCategoryListings.size() != 0) {

                if (surveyCategoryListings.size() > 1) {
                    recycler_categoryListing.setVisibility(View.VISIBLE);
                    categoryListingAdapter = new SurveyCategoryListingAdapter(surveyCategoryListings, getContext());
                    recycler_categoryListing.setItemAnimator(new DefaultItemAnimator());
                    recycler_categoryListing.setLayoutManager(new LinearLayoutManager(getContext()));
                    recycler_categoryListing.setAdapter(categoryListingAdapter);
                } else {
                    Log.d("AITL CategoryId", surveyCategoryListings.get(0).getCategory_id());
                    sessionManager.setCategoryId(surveyCategoryListings.get(0).getCategory_id());
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.SurveyCategoryWiseFragment;
                    ((MainActivity) getActivity()).loadFragment();
                    recycler_categoryListing.setVisibility(View.VISIBLE);
                    categoryListingAdapter = new SurveyCategoryListingAdapter(surveyCategoryListings, getContext());
                    recycler_categoryListing.setItemAnimator(new DefaultItemAnimator());
                    recycler_categoryListing.setLayoutManager(new LinearLayoutManager(getContext()));
                    recycler_categoryListing.setAdapter(categoryListingAdapter);

                }
            } else {
                recycler_categoryListing.setVisibility(View.GONE);
                textViewNoDATA.setText(jsonObject.getString("message"));
                textViewNoDATA.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
