package com.allintheloop.Fragment.AgendaModule;


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Agenda.AgendaTimeAdapterNew;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.AgendaData.Agenda;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.github.vivchar.viewpagerindicator.ViewPagerIndicator;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Agenda_TimeTab_Fragment extends Fragment implements VolleyInterface {

    public ArrayList<String> listDataHeader, agendaTimeList;
    public HashMap<String, ArrayList<Agenda>> listDataChild;
    ArrayList<Agenda> child;

    SessionManager sessionManager;
    String str_pending_agenda_status, str_showTime;
    TextView textViewNoDATA;
    public static String agenda_id;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    String agenda_kind = "allAgendaList";
    String agenda_type = "Time";


    Bundle bundle;
    View view;

    DefaultLanguage.DefaultLang defaultLanguage;


    SmartTabLayout tabLayout;
    ViewPager viewPager;
    //    CirclePageIndicator pageIndicator;
    ViewPagerIndicator pageIndicator;

    LinearLayout linearAgenda;
    RelativeLayout relative_staticHome, MainLayout;

    static String[] suffixes =
            // 0     1     2     3     4     5     6     7     8     9
            {"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    //10    11    12    13    14    15    16    17    18    19
                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
                    //20    21    22    23    24    25    26    27    28    29
                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
                    // 30    31
                    "th", "st"};


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    public Agenda_TimeTab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agenda__timetab, container, false);
        sessionManager = new SessionManager(getActivity());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);


        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();


        tabLayout = (SmartTabLayout) rootView.findViewById(R.id.tabLayout_agenda);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_agenda);
        pageIndicator = (ViewPagerIndicator) rootView.findViewById(R.id.pageIndicator);

        linearAgenda = (LinearLayout) rootView.findViewById(R.id.linear_agendaTime);
        relative_staticHome = (RelativeLayout) rootView.findViewById(R.id.relative_staticHome);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);

        bundle = new Bundle();

        defaultLanguage = sessionManager.getMultiLangString();

        getAgendaData();

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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {


            case 3:
                try {

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

            sessionManager.footerView(getActivity(), "0", MainLayout, relative_staticHome, linearAgenda, bottomAdverViewArrayList, getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getAgendaData() {
        try {
            listDataChild = new HashMap<String, ArrayList<Agenda>>();
            listDataHeader = new ArrayList<>();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getAgendaData().execute();
                }
            }, 1000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class getAgendaData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listDataChild.clear();
            listDataHeader.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            agendaTimeList = sqLiteDatabaseHandler.getAgendaTimeList(sessionManager.getEventId(), sessionManager.getAgendaCategoryId());
            for (int i = 0; i < agendaTimeList.size(); i++) {
                child = new ArrayList<>();
                if (sessionManager.getAgendaSortOrder().equalsIgnoreCase("1")) {
                    child = sqLiteDatabaseHandler.getAgendaListDataByTimeOrderByTime(sessionManager.getEventId(), agendaTimeList.get(i), sessionManager.getAgendaCategoryId());
                } else if (sessionManager.getAgendaSortOrder().equalsIgnoreCase("0")) {
                    child = sqLiteDatabaseHandler.getAgendaListDataByTime(sessionManager.getEventId(), agendaTimeList.get(i), sessionManager.getAgendaCategoryId());
                    Collections.sort(child, new SortComparator());
                }
                listDataHeader.add(getDayOfWeek(agendaTimeList.get(i)));
                listDataChild.put(getDayOfWeek(agendaTimeList.get(i)), child);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                if (listDataHeader.size() == 0) {
                    textViewNoDATA.setText("No Agenda Found");
                    textViewNoDATA.setVisibility(View.VISIBLE);
                    linearAgenda.setVisibility(View.GONE);

                } else {
                    try {
                        AgendaTimeAdapterNew ata = new AgendaTimeAdapterNew(getChildFragmentManager(), listDataHeader, listDataChild);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public String getDayOfWeek(String stringDate) {
        String dayOfWeek;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        Calendar calender = new GregorianCalendar();

        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
            dayOfWeek = null;
        }

        calender.setTime(date);

        switch (calender.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.MONDAY:
                dayOfWeek = defaultLanguage.getTimeAndDateMonday();
                break;
            case Calendar.TUESDAY:
                dayOfWeek = defaultLanguage.getTimeAndDateTuesday();
                break;
            case Calendar.WEDNESDAY:
                dayOfWeek = defaultLanguage.getTimeAndDateWednesday();
                break;
            case Calendar.THURSDAY:
                dayOfWeek = defaultLanguage.getTimeAndDateThursday();
                break;
            case Calendar.FRIDAY:
                dayOfWeek = defaultLanguage.getTimeAndDateFriday();
                break;
            case Calendar.SATURDAY:
                dayOfWeek = defaultLanguage.getTimeAndDateSaturday();
                break;
            case Calendar.SUNDAY:
                dayOfWeek = defaultLanguage.getTimeAndDateSunday();
                break;

            default:
                dayOfWeek = null;
                break;
        }


        SimpleDateFormat formatDayOfMonth = new SimpleDateFormat("d");
        int day = Integer.parseInt(formatDayOfMonth.format(date));
        String dayStr = day + suffixes[day];

        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE ");
        String day1 = dayOfWeek + " " + day;

        Log.e("AgendaTime", "DAte: " + day1);
        return day1;
    }

    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            int time1 = 0, time2 = 0;
            if (o1 instanceof Agenda) {
                if (!((Agenda) o1).getSort_order().isEmpty()) {
                    time1 = Integer.parseInt(((Agenda) o1).getSort_order());
                } else {
                    time1 = 9999;
                }

            }
            if (o2 instanceof Agenda) {
                if (!((Agenda) o2).getSort_order().isEmpty()) {
                    time2 = Integer.parseInt(((Agenda) o2).getSort_order());
                } else {
                    time2 = 9999;
                }
            }
            return time1 - time2;
        }
    }

}