package com.allintheloop.Fragment.ExhibitorFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.ExhibitorListWithSection.AdapterSectionRecyclerNew;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorType;
import com.allintheloop.Bean.ExhibitorListClass.FavoritedExhibitor;
import com.allintheloop.Bean.ExhibitorListClass.SectionHeader;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.allintheloop.Util.GlobalData.Update_Profile;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorListingFromSubCategoryList extends Fragment implements VolleyInterface {


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    EditText edt_search;
    SessionManager sessionManager;
    Button btn_seeRequestExhibitor, btn_Clearsearch;
    Bundle bundle;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    TextView textViewNoDATA, textViewAttendee;
    LinearLayout Container_Attendance;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    String adverties_id = "", keyword = "";
    Context context;
    RecyclerView rv_viewAttendance;
    ArrayList<String> selectedCountries;
    ArrayList<String> selectedParentCat;
    ArrayList<String> dataShortDesc;
    ArrayList<String> selectedCategoryId;
    ArrayList<String> listCatId;
    List<SectionHeader> sections;
    AdapterSectionRecyclerNew adapterRecycler;
    UidCommonKeyClass uidCommonKeyClass;

    WrapContentLinearLayoutManager linearLayoutManager;
    private BroadcastReceiver exhibitorListingData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getExhibitorCountryProduct();
        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d("niral", "Reciver Called : " + sessionManager.getUserProfile());
            // TODO Auto-generated method stub
            adapterRecycler.notifyDataSetChanged();
        }
    };


    public ExhibitorListingFromSubCategoryList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor_listing_from_sub_category_list, container, false);

        context = getActivity();
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        GlobalData.currentModuleForOnResume = GlobalData.exhibitorModuleid;

        selectedCountries = new ArrayList<>();
        selectedParentCat = new ArrayList<>();
        dataShortDesc = new ArrayList<>();
        selectedCategoryId = new ArrayList<>();
        listCatId = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        sections = new ArrayList<>();
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        btn_seeRequestExhibitor = (Button) rootView.findViewById(R.id.btn_seeRequestExhibitor);
        sessionManager = new SessionManager(context);

        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();

        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        Container_Attendance = (LinearLayout) rootView.findViewById(R.id.Container_Attendance);

        rv_viewAttendance = (RecyclerView) rootView.findViewById(R.id.rv_viewAttendance);

        rv_viewAttendance.setNestedScrollingEnabled(false);
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rv_viewAttendance.setLayoutManager(linearLayoutManager);
        rv_viewAttendance.setItemAnimator(new DefaultItemAnimator());

        if (sessionManager.getRequestMettingButton().equalsIgnoreCase("1")) {

            if (GlobalData.checkForUIDVersion()) {

                if (uidCommonKeyClass.getIsOnlyExhibitorUser().equalsIgnoreCase("1")) {
                    btn_seeRequestExhibitor.setVisibility(View.VISIBLE);
                } else {
                    btn_seeRequestExhibitor.setVisibility(View.GONE);
                }

            } else {

                if (sessionManager.getRolId().equalsIgnoreCase("6")) {//changes applied
                    btn_seeRequestExhibitor.setVisibility(View.VISIBLE);
                } else {
                    btn_seeRequestExhibitor.setVisibility(View.GONE);
                }

            }

        } else {
            btn_seeRequestExhibitor.setVisibility(View.GONE);
        }


        btn_seeRequestExhibitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        if (!sessionManager.getExhibitorSponsorCategoryId().isEmpty()) {
            listCatId.add(sessionManager.getExhibitorSponsorCategoryId());
            String catId = listCatId.toString();
            catId = catId.substring(1, catId.length() - 1);
            pagewiseClickForCategory(catId);
        }

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (sections.size() > 0) {

                        keyword = edt_search.getText().toString();
                        adapterRecycler.filter(keyword);
                        sessionManager.keyboradHidden(edt_search);
                    }
                    return true;
                }

                return false;
            }

        });

        getAdvertiesment();
        getExhibitorCountryProduct();
        new updateDatabase().execute();
        return rootView;
    }

    private void getExhibitorCountryProduct() {
        try {

            dataShortDesc.clear();
            selectedParentCat.clear();
            sections.clear();

            if (!(sessionManager.getExhibitorSubCategoryDesc().isEmpty())) {
                dataShortDesc.add(sessionManager.getExhibitorSubCategoryDesc());
            }
            if (!(sessionManager.getExhibitorSponsorCategoryId().isEmpty())) {
                selectedCategoryId.add(sessionManager.getExhibitorSponsorCategoryId());
            }
            for (int i = 0; i < dataShortDesc.size(); i++) {
                String[] categoryKeywords = dataShortDesc.get(i).split(",");
                selectedParentCat.addAll(new ArrayList<String>(Arrays.asList(categoryKeywords)));
            }

            ArrayList<ExhibitorType> types = sqLiteDatabaseHandler.getExhibitorParentCateogryTypes(sessionManager.getEventId());
//            for (int i = 0; i < types.size(); i++)
//            {
            ArrayList<Attendance> attendanceArrayList = sqLiteDatabaseHandler.getExhibitorFromChildProductFiltersponsor(sessionManager.getEventId(),
                    sessionManager.getUserId(), types, selectedCountries, selectedParentCat, selectedCategoryId);
            if (attendanceArrayList.size() > 0) {
                sections.add(new SectionHeader(attendanceArrayList, "", ""));
            }
//            }

            if (sections.size() > 0) {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                rv_viewAttendance.setVisibility(View.VISIBLE);
                adapterRecycler = new AdapterSectionRecyclerNew(context, sections, true);
                rv_viewAttendance.setAdapter(adapterRecycler);
            } else {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                textViewNoDATA.setText("No Exhibitors Found");
                rv_viewAttendance.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {


            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    pagewiseClick();
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
            case 5: {
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        FavoritedExhibitor favoritedExhibitor = gson.fromJson(jsonObject.toString(), FavoritedExhibitor.class);
                        new updateFav(favoritedExhibitor.getFavoriteExhibitors()).execute();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Update_Profile));
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.exhibitorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.exhibitorModuleid);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void getFavoritedExhibitor() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavouritedExhibitors, Param.getNotificationListing(sessionManager.getEventId(), sessionManager.getUserId()), 5, false, this);
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
                sessionManager.footerView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(context, "1", MainLayout, relativeLayout_Data, Container_Attendance, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "1", MainLayout, relativeLayout_Data, Container_Attendance, topAdverViewArrayList, getActivity());
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

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(context)) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 10, false, this);
        }
    }

    private void pagewiseClickForCategory(String param) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.OtherPageWiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "EXHI_CAT", "", "", sessionManager.get_cmsId(), param), 6, false, this);
        }
    }

    public class updateDatabase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

//            boolean isSame = sqLiteDatabaseHandler.isSameExhibitorUser(sessionManager.getEventId(), sessionManager.getUserId());
//            if (!isSame) {
            sqLiteDatabaseHandler.updateExhibitorUserID(sessionManager.getEventId(), sessionManager.getUserId());
//            }

            if (sessionManager.isLogin()) {
                //if online get favorite data and sync
                getFavoritedExhibitor();
            }
            return true;
        }

    }

    public class updateFav extends AsyncTask<Void, Void, Void> {
        ArrayList<FavoritedExhibitor.FavoriteExhibitor> exhibitors;

        public updateFav(ArrayList<FavoritedExhibitor.FavoriteExhibitor> exhibitors) {
            this.exhibitors = exhibitors;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            sqLiteDatabaseHandler.updateExhibitorFav(sessionManager.getEventId(), sessionManager.getUserId(), exhibitors);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            getExhibitorCountryProduct();
            Log.e("getExhibitorsPagination", "onPostExecute");
            super.onPostExecute(aVoid);
        }
    }

}

