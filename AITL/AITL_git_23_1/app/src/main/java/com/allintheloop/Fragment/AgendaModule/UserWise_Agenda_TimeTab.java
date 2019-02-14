package com.allintheloop.Fragment.AgendaModule;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Agenda.UserWiseAgendaInstantAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.AgendaData.Agenda_Time;
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
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.allintheloop.Util.GlobalData.UserwiseAgewndaRefresh;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserWise_Agenda_TimeTab extends Fragment implements VolleyInterface {


    public static String agenda_id;
    static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st"};
    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<Agenda_Time>> listDataChild;
    ArrayList<Agenda_Time> child;
    SessionManager sessionManager;
    String Eventid, Token_id, str_showTime;
    TextView textViewNoDATA;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    String agenda_kind = "allAgendaList";
    String agenda_type = "userWiseAgendaTime";
    String show_suggest_button = "";
    SmartTabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerIndicator pageIndicator;
    LinearLayout linearAgenda;
    RelativeLayout relative_staticHome, MainLayout;
    private BroadcastReceiver AgendaRefresh = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (sessionManager.isLogin())
                getUserwiseTimeData();
        }
    };

    public UserWise_Agenda_TimeTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_userwise__agenda__time_tab, container, false);
        // get the listview


        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        tabLayout = (SmartTabLayout) rootView.findViewById(R.id.tabLayout_agenda);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_agenda);
        pageIndicator = (ViewPagerIndicator) rootView.findViewById(R.id.pageIndicator);

        linearAgenda = (LinearLayout) rootView.findViewById(R.id.linear_agendaTime);
        relative_staticHome = (RelativeLayout) rootView.findViewById(R.id.relative_staticHome);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);

        sessionManager = new SessionManager(getActivity());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        Eventid = sessionManager.getEventId();
        Token_id = sessionManager.getToken();  // 71dd07494c5ee54992a27746d547e25dee01bd97
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);


        if (sessionManager.isLogin())
            getUserwiseTimeData();
        getAdvertiesment();
        return rootView;
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 5, false, this);

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

    private void getUserwiseTimeData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_UserAgenda_ByTimeUid, Param.get_UserWise_Agenda_ByTimeType1(Token_id, Eventid, sessionManager.getUserId(), sessionManager.getAgendaCategoryId()), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_UserAgenda_ByTime, Param.get_UserWise_Agenda_ByTimeType1(Token_id, Eventid, sessionManager.getUserId(), sessionManager.getAgendaCategoryId()), 0, false, this);
        } else {
            cursor = sqLiteDatabaseHandler.getAgendaListing(sessionManager.getEventId(), agenda_kind, agenda_type, sessionManager.getUserId(), "");
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsondata = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.agenda_Data)));
                        loadAgendaData(jsondata);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                textViewNoDATA.setText("You have not saved any sessions yet");
                textViewNoDATA.setVisibility(View.VISIBLE);
                linearAgenda.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {

            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        JSONObject jsonData = jsonObject.optJSONObject("data");

                        if (sqLiteDatabaseHandler.isAgendaListExist(sessionManager.getEventId(), agenda_kind, agenda_type, sessionManager.getUserId(), "")) {
                            sqLiteDatabaseHandler.UpdateAgendaListing(sessionManager.getEventId(), agenda_kind, agenda_type, jsonData.toString(), sessionManager.getUserId(), "");
                        } else {
                            sqLiteDatabaseHandler.insertAgendaListing(sessionManager.getEventId(), agenda_kind, agenda_type, jsonData.toString(), sessionManager.getUserId(), "");

                        }
                        loadAgendaData(jsonData);

                    } else {
                        ToastC.show(getActivity(), jsonObject.optString(Param.KEY_MESSAGE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
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

            sessionManager.footerView(getContext(), "0", MainLayout, relative_staticHome, linearAgenda, bottomAdverViewArrayList, getActivity());

//            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1"))
//            {
//                sessionManager.footerView(getContext(),"0",MainLayout,relative_staticHome,linearAgenda,bottomAdverViewArrayList,getActivity());
//            }
//            else
//            {
//                sessionManager.footerView(getContext(),"1",MainLayout,relative_staticHome,linearAgenda,bottomAdverViewArrayList,getActivity());
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAgendaData(JSONObject jsonData) {
        try {
            int z = 0;

            show_suggest_button = jsonData.getString("show_suggest_button");


//            if (show_suggest_button.equalsIgnoreCase("1"))
//            {
//                View_userWise_Agenda.button_layout.setVisibility(View.VISIBLE);
//            }
//            else
//            {
//                View_userWise_Agenda.button_layout.setVisibility(View.GONE);
//            }

            JSONArray jsonAgendaArray = jsonData.optJSONArray("agenda");

            JSONObject jsonEventObj = jsonData.getJSONObject("event");
            str_showTime = jsonEventObj.getString("show_session_by_time");
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, ArrayList<Agenda_Time>>();

            for (int i = 0; i < jsonAgendaArray.length(); i++) {

                JSONObject jsonAgendaObj = jsonAgendaArray.optJSONObject(i);
                child = new ArrayList<>();
                String date = jsonAgendaObj.optString("date_time");

//                if (str_showTime.equalsIgnoreCase("0"))
//                {
//                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
//                    Date dateParse = inFormat.parse(date);
//
//                    SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d");
//                    int day = Integer.parseInt(formatDayOfMonth.format(dateParse));
//                    String dayStr = day + suffixes[day];
//
//                    SimpleDateFormat outFormat = new SimpleDateFormat("EEEE ");
//                    String day1 = outFormat.format(dateParse) + dayStr;
//                    listDataHeader.add(day1);
//
//
//                } else {
                listDataHeader.add(date);
//                }
                JSONArray jsonArrayData = jsonAgendaObj.optJSONArray("data");

                for (int j = 0; j < jsonArrayData.length(); j++) {
                    JSONObject jsonDatewise = jsonArrayData.optJSONObject(j);

                    child.add(new Agenda_Time(jsonDatewise.getString("Id"), jsonDatewise.getString("Heading"), jsonDatewise.getString("Start_time"), jsonDatewise.getString("Start_date"), jsonDatewise.getString("End_time"), jsonDatewise.getString("End_date"), jsonDatewise.getString("Map_title"), jsonDatewise.getString("Address_map"), jsonDatewise.getString("time_zone"), jsonDatewise.getString("placeleft"), jsonDatewise.getString("speaker"), jsonDatewise.getString("location"), "1", jsonDatewise.getString("session_image"), jsonDatewise.getString("Types")));
                }
                listDataChild.put(listDataHeader.get(z), child);
                z++;
            }

            JSONArray jsonMettingArray = jsonData.optJSONArray("meeting");
            for (int k = 0; k < jsonMettingArray.length(); k++) {

                JSONObject jsonAgendaObj = jsonMettingArray.optJSONObject(k);
                child = new ArrayList<>();
                String date = jsonAgendaObj.optString("date");
                listDataHeader.add(date);
                JSONArray jsonArrayData = jsonAgendaObj.optJSONArray("data");

                for (int l = 0; l < jsonArrayData.length(); l++) {
                    JSONObject jsonDatewise = jsonArrayData.optJSONObject(l);

                    child.add(new Agenda_Time(jsonDatewise.getString("metting_id"), jsonDatewise.getString("exhibiotor_id"), "", jsonDatewise.getString("date"), jsonDatewise.getString("time"), jsonDatewise.getString("status"), jsonDatewise.getString("Lastname"), jsonDatewise.getString("stand_number"), "0", jsonDatewise.getString("exhi_user_id"), jsonDatewise.getString("is_exhi")));
                }
                listDataChild.put(listDataHeader.get(z), child);
                z++;
            }


            if (listDataChild.size() == 0) {
                textViewNoDATA.setText("You have not saved any sessions yet");
                textViewNoDATA.setVisibility(View.VISIBLE);
                linearAgenda.setVisibility(View.GONE);
            } else {

                UserWiseAgendaInstantAdapter ata = new UserWiseAgendaInstantAdapter(getChildFragmentManager(), listDataHeader, listDataChild);
                viewPager.setAdapter(ata);
                pageIndicator.setupWithViewPager(viewPager);

                tabLayout.setViewPager(viewPager);
                viewPager.setCurrentItem(0);

                tabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {


                        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);

                        TextView title;
                        for (int i = 0; i < vg.getChildCount(); i++) {
                            title = (TextView) vg.getChildAt(i);
                            title.setTextSize(12);
                        }

                        title = (TextView) vg.getChildAt(position);
                        title.setTextSize(16);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(AgendaRefresh, new IntentFilter(UserwiseAgewndaRefresh));
    }

    @Override
    public void onPause() {
        super.onPause();
        agenda_kind = "allAgendaList";
        agenda_type = "userWiseAgendaTime";
        getActivity().unregisterReceiver(AgendaRefresh);

    }
}
