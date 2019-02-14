package com.allintheloop.Fragment.AgendaModule;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Agenda.AgendaTimeAdapter;
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
public class PendingAgenda_TypeTab extends Fragment implements VolleyInterface {


    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<Agenda_Time>> listDataChild;
    ArrayList<Agenda_Time> child;
    //    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
    String Agenda_Id, Heading, Start_date, Start_time, Map_title, Time, EndTime;
    SessionManager sessionManager;
    String Eventid, Token_id;
    TextView textViewNoDATA;
    public static String agenda_id;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    String agenda_kind = "pendingUserAgenda";
    String agenda_type = "Type";

    SmartTabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerIndicator pageIndicator;

    LinearLayout linear_agendaType;

    public PendingAgenda_TypeTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pendingagenda__typetab, container, false);

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
        linear_agendaType = (LinearLayout) rootView.findViewById(R.id.linear_agendaType);


        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_PendingAgendaByType, Param.get_UserWise_Agenda_ByTimeType(Token_id, Eventid, sessionManager.getUserId()), 0, false, this);

        } else {
            cursor = sqLiteDatabaseHandler.getAgendaListing(sessionManager.getEventId(), agenda_kind, agenda_type, sessionManager.getUserId(), "");
            if (cursor.moveToFirst()) {
                Log.d("AITL ", cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.agenda_Data)));
                try {
                    JSONObject jsondata = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.agenda_Data)));
                    loadAgendaData(jsondata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

                    child.add(new Agenda_Time(jsonDatewise.getString("Id"), jsonDatewise.getString("Heading"), jsonDatewise.getString("Start_time"), jsonDatewise.getString("Start_date"), jsonDatewise.getString("End_time"), jsonDatewise.getString("End_date"), jsonDatewise.getString("Map_title"), jsonDatewise.getString("Address_map"), jsonDatewise.getString("time_zone"), jsonDatewise.getString("placeleft"), jsonDatewise.getString("speaker"), jsonDatewise.getString("location"), jsonDatewise.getString("session_image"), jsonDatewise.getString("Types")));

                }

                listDataHeader.add(type);
                Log.d("ListDataHeader", listDataHeader.toString());
                listDataChild.put(listDataHeader.get(i), child);

                Log.d("ListDataChild", listDataChild.toString());
            }

            if (jsonAgendaArray.length() == 0) {
                textViewNoDATA.setText("No Agenda available for this Category.");
                textViewNoDATA.setVisibility(View.VISIBLE);
                linear_agendaType.setVisibility(View.GONE);
            } else {
                textViewNoDATA.setVisibility(View.GONE);
                linear_agendaType.setVisibility(View.VISIBLE);
                AgendaTimeAdapter ata = new AgendaTimeAdapter(getChildFragmentManager(), listDataHeader, listDataChild);
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