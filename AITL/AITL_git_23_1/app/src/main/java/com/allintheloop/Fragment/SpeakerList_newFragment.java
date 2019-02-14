package com.allintheloop.Fragment;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.allintheloop.Adapter.SpeakerListNewAdapter;
import com.allintheloop.Adapter.SpeakerListWithSection.AdapterSpeakerSectionRecyclerView;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.FavoritedSpeaker;
import com.allintheloop.Bean.PhotoFilter.EventBusgetPhotoFilterData;
import com.allintheloop.Bean.Speaker.SpeakerListClass;
import com.allintheloop.Bean.Speaker.SpeakerReloadedEventBus;
import com.allintheloop.Bean.Speaker.SpeakerSectionHeader;
import com.allintheloop.Bean.Speaker.SpeakerType;
import com.allintheloop.Bean.Speaker.SpeakerList;
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
public class SpeakerList_newFragment extends Fragment implements VolleyInterface {

    EditText edt_search;
    RecyclerView recyclerView;
    TextView textViewNoDATA, textViewAttendee;
    //    SharedPreferences preferences;
    SessionManager sessionManager;
    ArrayList<SpeakerSectionHeader> speakerSectionHeaders;
    //    ArrayList<SpeakerList.SpeakerData> speakerDataArrayList;
    AdapterSpeakerSectionRecyclerView adapterSpeakerSectionRecyclerView;
    Bundle bundle;
    boolean isClick;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String adverties_id = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    DefaultLanguage.DefaultLang defaultLang;
    LinearLayout linear_content;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    Context context;

    public SpeakerList_newFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_speaker_list_new, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        context = getActivity();

        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
//        frame_footer = (RelativeLayout) rootView.findViewById(R.id.frame_footer);
        textViewAttendee = (TextView) rootView.findViewById(R.id.textViewAttendee);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_viewAttendance);
        recyclerView.setNestedScrollingEnabled(false);
//        speakerDataArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        sessionManager = new SessionManager(context);

        //preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);

        defaultLang = sessionManager.getMultiLangString();
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        GlobalData.currentModuleForOnResume = GlobalData.speakerModuleid;
        bundle = new Bundle();


        edt_search.setHint(defaultLang.get7Search().toUpperCase());


        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (speakerSectionHeaders.size() > 0) {
                        adapterSpeakerSectionRecyclerView.filter(edt_search.getText().toString());
                        sessionManager.keyboradHidden(edt_search);
                    }
                    return true;
                }

                return false;
            }

        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (speakerSectionHeaders.size() > 0) {
                    if (s.toString().length() == 0) {
                        adapterSpeakerSectionRecyclerView.filter(edt_search.getText().toString());
                        sessionManager.keyboradHidden(edt_search);
                    }
                }
            }
        });

        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getListOfllineData();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getListOfllineData();
            }

        }
        getAdvertiesment();

        new updateDatabase().execute();
        return rootView;
    }

    public class getSpeakerAsyncTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            speakerSectionHeaders.clear();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<SpeakerType> typeArrayList = new ArrayList<>();
            typeArrayList = sqLiteDatabaseHandler.getSpeakerTypes(sessionManager.getEventId());
            for (SpeakerType type : typeArrayList) {
                ArrayList<SpeakerListClass.SpeakerListNew> speakerListNewArrayList;
                if (type.getSpeakerTypeId().equalsIgnoreCase("")) {
                    speakerListNewArrayList = sqLiteDatabaseHandler.getSpeakerListDataByType(sessionManager.getEventId(), "''");
                } else {
                    speakerListNewArrayList = sqLiteDatabaseHandler.getSpeakerListDataByType(sessionManager.getEventId(), type.getSpeakerTypeId());
                }
                if (speakerListNewArrayList.size() > 0) {
                    speakerSectionHeaders.add(new SpeakerSectionHeader(speakerListNewArrayList, type.getSpeakerType(), type.getSpeakerTypeColor(), type.getSpeakerPosition()));
                }
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);


            if (speakerSectionHeaders.size() == 0) {
//                                ToastC.show(getActivity(), "No Speaker List Found");
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setText("No Speakers Found");
                textViewNoDATA.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                Collections.sort(speakerSectionHeaders, new SortComparator());
                edt_search.setVisibility(View.VISIBLE);
                adapterSpeakerSectionRecyclerView = new AdapterSpeakerSectionRecyclerView(context, speakerSectionHeaders);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                textViewNoDATA.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapterSpeakerSectionRecyclerView);
            }
        }
    }


    private void getListOfllineData() {
        speakerSectionHeaders = new ArrayList<>();
//        speakerDataArrayList = new ArrayList<>();
        new getSpeakerAsyncTask().execute();
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 1, false, this);

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


    public class updateDatabase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            sqLiteDatabaseHandler.updateSpeakerUserID(sessionManager.getEventId(), sessionManager.getUserId());
            if (sessionManager.isLogin()) {
                if (GlobalData.isNetworkAvailable(context)) {
                    getFavoritedSpeaker();
                }
            }
            return true;
        }

    }


    private void getFavoritedSpeaker() {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.get_FavoriteSpeaker, Param.getNotificationListing(sessionManager.getEventId(), sessionManager.getUserId()), 2, false, this);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 1:
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
                    pagewiseClick();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        FavoritedSpeaker favoritedExhibitor = gson.fromJson(jsonObject.toString(), FavoritedSpeaker.class);
                        new updateFav(favoritedExhibitor.getFavoriteSpeakers()).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    public class updateFav extends AsyncTask<Void, Void, Void> {
        ArrayList<FavoritedSpeaker.FavoriteSpeaker> exhibitors;

        public updateFav(ArrayList<FavoritedSpeaker.FavoriteSpeaker> exhibitors) {
            this.exhibitors = exhibitors;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sqLiteDatabaseHandler.updateSpeakerFavFragment(sessionManager.getEventId(), sessionManager.getUserId(), exhibitors);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            getListOfllineData();
            super.onPostExecute(aVoid);
        }
    }


    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(context)) {

            new VolleyRequest(context, VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.speakerModuleid)) {
            ((MainActivity) context).getUpdatedDataFromParticularmodule(GlobalData.speakerModuleid);
        }
    }

    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {

            String time1 = "", time2 = "";

            if (o1 instanceof SpeakerList.SpeakerData) {
                if (sessionManager.getSpeakerIssortBy().equalsIgnoreCase("1")) {
                    time1 = ((SpeakerList.SpeakerData) o1).getLastname();
                } else {
                    time1 = ((SpeakerList.SpeakerData) o1).getFirstname();
                }
            }
            if (o2 instanceof SpeakerList.SpeakerData) {
                if (sessionManager.getSpeakerIssortBy().equalsIgnoreCase("1")) {
                    time1 = ((SpeakerList.SpeakerData) o2).getLastname();
                } else {
                    time2 = ((SpeakerList.SpeakerData) o2).getFirstname();
                }
            }
            return time1.compareTo(time2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeakerReloadedEventBus(SpeakerReloadedEventBus data) {
        getListOfllineData();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
