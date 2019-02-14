package com.allintheloop.Fragment.PresantationModule;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.PresantaionExpanListAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Presantation;
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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Presantation_TypeTab_Fragment extends Fragment implements VolleyInterface {


    PresantaionExpanListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<Presantation>> listDataChild;
    ArrayList<Presantation> child;

    SessionManager sessionManager;
    String id, slide_name, start_time, start_date, end_time, end_date, presan_filetype, image, place_name = "";
    TextView textViewNoDATA;


    Bundle bundle;
    View view;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "PresantationType";
    String adverties_id = "";
    RelativeLayout MainLayout, relativeLayout_static;
    LinearLayout linear_content;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    public Presantation_TypeTab_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_presantation__typetab, container, false);
        // view = LayoutInflater.from(getActivity()).inflate(R.layout.footer_ads, null);
        sessionManager = new SessionManager(getActivity());

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        expListView = (ExpandableListView) rootView.findViewById(R.id.presan_lvExp);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        relativeLayout_static = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_static);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        bundle = new Bundle();
        getAdvertiesment();
        return rootView;
    }


    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 5, false, this);

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

    private void getPresantationList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            Cursor cursor = sqLiteDatabaseHandler.getPresantationList(sessionManager.getEventId(), tag, sessionManager.getUserId());

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jsonObject = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.presan_Data)));
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Presantation_By_Type, Param.getPresanByTimeType(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 0, false, this);
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getPresantationList(sessionManager.getEventId(), tag, sessionManager.getUserId());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jsonObject = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.presan_Data)));
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                expListView.setVisibility(View.GONE);
                textViewNoDATA.setVisibility(View.VISIBLE);
                textViewNoDATA.setText("No Presantation Found");
            }
        }
    }

    private void loadData(JSONArray jsonAgendaArray) {
        try {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, ArrayList<Presantation>>();

            for (int i = 0; i < jsonAgendaArray.length(); i++) {

                JSONObject jsonAgendaObj = jsonAgendaArray.optJSONObject(i);
                child = new ArrayList<>();
//                            child.add(new Presantation("hello", "time"));
                String types = jsonAgendaObj.optString("Types");

                JSONArray jsonArrayData = jsonAgendaObj.optJSONArray("data");

                for (int j = 0; j < jsonArrayData.length(); j++) {
                    JSONObject jsonDatewise = jsonArrayData.optJSONObject(j);
                    id = jsonDatewise.getString("Id");
                    slide_name = jsonDatewise.getString("Heading");
                    start_time = jsonDatewise.getString("Start_time");
                    start_date = jsonDatewise.getString("Start_date");
                    end_time = jsonDatewise.getString("End_time");
                    end_date = jsonDatewise.getString("End_date");
                    presan_filetype = jsonDatewise.getString("presentation_file_type");
                    image = jsonDatewise.getString("images");
                    place_name = jsonDatewise.getString("map_Title");

                    child.add(new Presantation(id, slide_name, start_time, start_date, end_time, end_date, presan_filetype, image, place_name));
                }


                listDataHeader.add(types);
                listDataChild.put(listDataHeader.get(i), child);
            }
            //expListView.addFooterView(view);

            if (jsonAgendaArray.length() == 0) {
                textViewNoDATA.setVisibility(View.VISIBLE);
                textViewNoDATA.setText("No Presantation Found");
                expListView.setVisibility(View.GONE);
            } else {
                listAdapter = new PresantaionExpanListAdapter(getActivity(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);

                expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                    int previousGroup = -1;

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        if (groupPosition != previousGroup)
                            expListView.collapseGroup(previousGroup);
                        previousGroup = groupPosition;
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
        getPresantationList();

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
                        JSONArray jsonAgendaArray = jsonData.optJSONArray("presentaion");
                        if (sqLiteDatabaseHandler.isPresantationList(sessionManager.getEventId(), tag, sessionManager.getUserId())) {
                            sqLiteDatabaseHandler.UpdatePresantationList(sessionManager.getEventId(), tag, jsonAgendaArray.toString(), sessionManager.getUserId());
                        } else {
                            sqLiteDatabaseHandler.insertPresantationList(sessionManager.getEventId(), tag, jsonAgendaArray.toString(), sessionManager.getUserId());
                        }
                        loadData(jsonAgendaArray);

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
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}