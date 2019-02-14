package com.allintheloop.Fragment.AgendaModule;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Agenda.UserWiseAgendaInstantAdapter;
import com.allintheloop.Adapter.Agenda.userWiseAgendaExpaListAdapter;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class UserWise_Agenda_TypeTab extends Fragment implements VolleyInterface {

    userWiseAgendaExpaListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<Agenda_Time>> listDataChild;
    ArrayList<Agenda_Time> child;
    ArrayList<Agenda_Time> meetingChild;
    //    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
    String Agenda_Id, Heading, Start_date, Start_time, Map_title, Time, EndTime;
    SessionManager sessionManager;
    String Eventid, Token_id;
    TextView textViewNoDATA;
    public static String agenda_id;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    String agenda_kind = "userWiseAgenda";
    String agenda_type = "Type";

    SmartTabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerIndicator pageIndicator;

    LinearLayout linearAgenda;

    static String[] suffixes =
            //    0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //    20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //    30    31
                    "th", "st"};

    public UserWise_Agenda_TypeTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_wise__agenda__type_tab, container, false);

        Log.d("Call TimeTab", "Call TimeTab");

        sessionManager = new SessionManager(getActivity());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        //   preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);
        Eventid = sessionManager.getEventId();
        Token_id = sessionManager.getToken();  // 71dd07494c5ee54992a27746d547e25dee01bd97


        tabLayout = (SmartTabLayout) rootView.findViewById(R.id.tabLayout_agenda);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_agenda);
        pageIndicator = (ViewPagerIndicator) rootView.findViewById(R.id.pageIndicator);


        if (GlobalData.isNetworkAvailable(getActivity())) {
            cursor = sqLiteDatabaseHandler.getAgendaListing(sessionManager.getEventId(), agenda_kind, agenda_type, sessionManager.getUserId(), "");
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    Log.d("AITL  ", cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.agenda_Data)));
                    try {
                        JSONObject jsondata = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.agenda_Data)));
                        loadAgendaData(jsondata);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_UserAgenda_ByType, Param.get_UserWise_Agenda_ByTimeType(Token_id, Eventid, sessionManager.getUserId()), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_UserAgenda_ByType, Param.get_UserWise_Agenda_ByTimeType(Token_id, Eventid, sessionManager.getUserId()), 0, false, this);
            }
        } else {
            cursor = sqLiteDatabaseHandler.getAgendaListing(sessionManager.getEventId(), agenda_kind, agenda_type, sessionManager.getUserId(), "");
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    Log.d("AITL ", cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.agenda_Data)));
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


        return rootView;
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {

            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Data", jsonObject.toString());
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
        }
    }

    private void loadAgendaData(JSONObject jsonData) {
        try {

            int z = 0;

            JSONArray jsonAgendaArray = jsonData.optJSONArray("agenda");

            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, ArrayList<Agenda_Time>>();


            for (int i = 0; i < jsonAgendaArray.length(); i++) {
                JSONObject jsonAgendaObj = jsonAgendaArray.optJSONObject(i);
                child = new ArrayList<>();
                String type = jsonAgendaObj.optString("Types");

                JSONArray jsonArrayData = jsonAgendaObj.optJSONArray("data");

                for (int j = 0; j < jsonArrayData.length(); j++) {
                    JSONObject jsonDatewise = jsonArrayData.optJSONObject(j);

                    child.add(new Agenda_Time(jsonDatewise.getString("Id"), jsonDatewise.getString("Heading"), jsonDatewise.getString("Start_time"), jsonDatewise.getString("Start_date"), jsonDatewise.getString("End_time"), jsonDatewise.getString("End_date"), jsonDatewise.getString("Map_title"), jsonDatewise.getString("Address_map"), jsonDatewise.getString("time_zone"), jsonDatewise.getString("placeleft"), jsonDatewise.getString("speaker"), jsonDatewise.getString("location"), "1", jsonDatewise.getString("session_image"), jsonDatewise.getString("Types")));

                }
                listDataHeader.add(type);
                Log.d("ListDataHeader", listDataHeader.toString());
                listDataChild.put(listDataHeader.get(z), child);
                z++;

                Log.d("ListDataChild", listDataChild.toString());
            }
            JSONArray jsonMettingArray = jsonData.optJSONArray("meeting");
            for (int k = 0; k < jsonMettingArray.length(); k++) {

                JSONObject jsonAgendaObj = jsonMettingArray.optJSONObject(k);
                child = new ArrayList<>();
                String date = jsonAgendaObj.optString("Types");
                Log.d("AITL  UserWise Date", date);
                listDataHeader.add(date);
                JSONArray jsonArrayData = jsonAgendaObj.optJSONArray("data");

                for (int l = 0; l < jsonArrayData.length(); l++) {
                    JSONObject jsonDatewise = jsonArrayData.optJSONObject(l);

                    child.add(new Agenda_Time(jsonDatewise.getString("metting_id"), jsonDatewise.getString("exhibiotor_id"), jsonDatewise.getString("attendee_id"), jsonDatewise.getString("date"), jsonDatewise.getString("time"), jsonDatewise.getString("status"), jsonDatewise.getString("Heading"), jsonDatewise.getString("stand_number"), "0", jsonDatewise.getString("exhi_user_id"), ""));
                }
                Log.d("ListDataHeaderMETTING", listDataHeader.toString());
                listDataChild.put(listDataHeader.get(z), child);
                z++;
                Log.d("ListDataChildMETTING", listDataChild.toString());
            }
            listAdapter = new userWiseAgendaExpaListAdapter(getActivity(), listDataHeader, listDataChild);

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

}
