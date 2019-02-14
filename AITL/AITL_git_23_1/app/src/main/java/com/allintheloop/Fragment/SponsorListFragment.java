package com.allintheloop.Fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.SponsorListWithSection.AdapterSponsorSectionRecyclerView;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.Speaker.SpeakerReloadedEventBus;
import com.allintheloop.Bean.SponsorClass.FavoritedSponsor;
import com.allintheloop.Bean.SponsorClass.SponsorListNewData;
import com.allintheloop.Bean.SponsorClass.SponsorReloadEventBus;
import com.allintheloop.Bean.SponsorClass.SponsorSectionHeader;
import com.allintheloop.Bean.SponsorClass.SponsorType;
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


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * A simple {@link Fragment} subclass.
 */
public class SponsorListFragment extends Fragment implements VolleyInterface {


    EditText edt_search;
    RecyclerView recyclerView;
    TextView textViewNoDATA, textViewAttendee;

    SessionManager sessionManager;
    String tag = "";


    Bundle bundle;
    boolean isClick;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String adverties_id = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    DefaultLanguage.DefaultLang defaultLang;
    LinearLayout linear_content;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    ArrayList<SponsorSectionHeader> sponsorSectionHeaders;
    AdapterSponsorSectionRecyclerView adapterSponsorSectionRecyclerView;
    Context context;

    public SponsorListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sponsor_list, container, false);
        edt_search = rootView.findViewById(R.id.edt_search);
        textViewAttendee = rootView.findViewById(R.id.textViewAttendee);
        recyclerView = rootView.findViewById(R.id.rv_viewAttendance);
//        recyclerView.setNestedScrollingEnabled(false);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        textViewNoDATA = rootView.findViewById(R.id.textViewNoDATA);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        getActivity().setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        context = getActivity();
        bundle = new Bundle();

        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        edt_search.setHint(defaultLang.get43Search().toUpperCase());

        GlobalData.currentModuleForOnResume = GlobalData.sponsorModuleid;
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (sponsorSectionHeaders.size() > 0) {
                        adapterSponsorSectionRecyclerView.filter(edt_search.getText().toString());
                        sessionManager.keyboradHidden(edt_search);
                    }
                    return true;
                }

                return false;
            }

        });


        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);

            getSponsorListData();
//            getList();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getSponsorListData();
            }

        }
        getAdvertiesment();

        new updateDatabase().execute();
        return rootView;
    }

    private void getSponsorListData() {
        sponsorSectionHeaders = new ArrayList<>();
        new getSponsorListDataAsynTask().execute();
    }

    public class getSponsorListDataAsynTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<SponsorType> sponsorTypeArrayList = sqLiteDatabaseHandler.getSponsorTypes(sessionManager.getEventId());
            for (int i = 0; i < sponsorTypeArrayList.size(); i++) {

                Log.d("AITL TypeId", i + "" + sponsorTypeArrayList.get(i).getSponsorTypeId());
                ArrayList<SponsorListNewData.SponsorNewData> newDataArrayList;
                if (sponsorTypeArrayList.get(i).getSponsorTypeId().equalsIgnoreCase("")) {
                    newDataArrayList = sqLiteDatabaseHandler.getSponsorListData(sessionManager.getEventId(), sessionManager.getSponsorPrentGroupID(), "''");
                } else {
                    newDataArrayList = sqLiteDatabaseHandler.getSponsorListData(sessionManager.getEventId(), sessionManager.getSponsorPrentGroupID(), sponsorTypeArrayList.get(i).getSponsorTypeId());
                }
                if (newDataArrayList.size() > 0) {
                    sponsorSectionHeaders.add(new SponsorSectionHeader(newDataArrayList, sponsorTypeArrayList.get(i).getSponsorType(), sponsorTypeArrayList.get(i).getSponsorTypeColor(), sponsorTypeArrayList.get(i).getSponsorPosition()));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (sponsorSectionHeaders.size() == 0) {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setText("No Sponsors Found");
                textViewNoDATA.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            } else {
                Collections.sort(sponsorSectionHeaders, new SortComparator());
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapterSponsorSectionRecyclerView = new AdapterSponsorSectionRecyclerView(context, sponsorSectionHeaders);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapterSponsorSectionRecyclerView);
            }
            super.onPostExecute(aVoid);
        }
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
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.sponsorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.sponsorModuleid);
        }
    }

    private void getFavoritedSponsor() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_FavrotiyeSponsor, Param.getNotificationListing(sessionManager.getEventId(), sessionManager.getUserId()), 2, false, this);
        }
    }

    public class updateDatabase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            boolean isSame = sqLiteDatabaseHandler.isSameSponsorUser(sessionManager.getEventId(), sessionManager.getUserId());
            if (!isSame) {
                sqLiteDatabaseHandler.updateSponsorUserID(sessionManager.getEventId(), sessionManager.getUserId());
            }

            if (sessionManager.isLogin()) {
                //if online get favorite data and sync
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    getFavoritedSponsor();
                }
            }
            return true;
        }

    }

    public class updateFav extends AsyncTask<Void, Void, Void> {
        ArrayList<FavoritedSponsor.FavoriteSponsor> exhibitors;

        public updateFav(ArrayList<FavoritedSponsor.FavoriteSponsor> exhibitors) {
            this.exhibitors = exhibitors;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sqLiteDatabaseHandler.updateSponsorFavFragment(sessionManager.getEventId(), sessionManager.getUserId(), exhibitors);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            getSponsorListData();
            super.onPostExecute(aVoid);
        }
    }

    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String time1 = "", time2 = "";
            if (o1 instanceof SponsorListNewData) {
                time1 = ((SponsorListNewData) o1).getType_position();
            }
            if (o2 instanceof SponsorListNewData) {
                time2 = ((SponsorListNewData) o2).getType_position();
            }
            return time1.compareTo(time2);
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
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        FavoritedSponsor favoritedExhibitor = gson.fromJson(jsonObject.toString(), FavoritedSponsor.class);
                        new updateFav(favoritedExhibitor.getFavoriteSponsors()).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSponsorReloadEventBus(SponsorReloadEventBus data) {
        getSponsorListData();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


}