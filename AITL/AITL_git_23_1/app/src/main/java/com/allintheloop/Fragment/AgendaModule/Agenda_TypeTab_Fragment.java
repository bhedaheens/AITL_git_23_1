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
import com.allintheloop.Bean.AgendaData.AgendaType;
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

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Agenda_TypeTab_Fragment extends Fragment implements VolleyInterface {

    public static String agenda_id;
    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<Agenda>> listDataChild;
    ArrayList<Agenda> child;
    //    SharedPreferences preferences;
//    SharedPreferences.Editor editor;
    String Agenda_Id, Heading, Start_date, Start_time, Map_title, Time, EndTime;
    SessionManager sessionManager;
    String Eventid, Token_id;
    TextView textViewNoDATA;
    String str_pending_agenda_status;
    String agenda_kind = "allAgendaList";
    String agenda_type = "Type";
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    Bundle bundle;
    View view;
    String adverties_id = "";
    SmartTabLayout tabLayout;
    ViewPager viewPager;
    //    CirclePageIndicator pageIndicator;
    ViewPagerIndicator pageIndicator;
    LinearLayout linearAgenda;
    RelativeLayout MainLayout, relative_staticHome;

    public Agenda_TypeTab_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agenda__typetab, container, false);
        //view = LayoutInflater.from(getActivity()).inflate(R.layout.footer_ads, null);

        sessionManager = new SessionManager(getActivity());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        //   preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);
        Eventid = sessionManager.getEventId();
        Token_id = sessionManager.getToken();  // 71dd07494c5ee54992a27746d547e25dee01bd97


        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        tabLayout = (SmartTabLayout) rootView.findViewById(R.id.tabLayout_agenda);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_agenda);
        pageIndicator = (ViewPagerIndicator) rootView.findViewById(R.id.pageIndicator);

        bundle = new Bundle();

        linearAgenda = (LinearLayout) rootView.findViewById(R.id.lauout_agendaType);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        relative_staticHome = (RelativeLayout) rootView.findViewById(R.id.relative_staticHome);

        /* Create an Intent that will start the Menu-Activity. */
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

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
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

    private void getAgendaData() {
        try {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, ArrayList<Agenda>>();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new getAgendaData().execute();
                }
            }, 2000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
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
            ArrayList<AgendaType> agendaTypes = sqLiteDatabaseHandler.getAgendaTypeList(sessionManager.getEventId());
            for (int i = 0; i < agendaTypes.size(); i++) {

                child = new ArrayList<>();
                child = sqLiteDatabaseHandler.getAgendaListDataByType(sessionManager.getEventId(), agendaTypes.get(i).getTypeId(), sessionManager.getAgendaCategoryId());

                if (child.size() > 0) {
                    listDataHeader.add(agendaTypes.get(i).getTypeName());
                    listDataChild.put(agendaTypes.get(i).getTypeName(), child);
                }
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

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}