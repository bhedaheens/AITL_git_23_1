package com.allintheloop.Fragment.PrivateMessage;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.NewStartConverionAdapter;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.R;
import com.allintheloop.Util.EndlessScrollListener;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartPrivateMessageConversion_Fragment extends Fragment implements VolleyInterface {

    TextView textViewAttendee, textViewNoDATA;
    EditText edt_search;
    RecyclerView rv_viewFullAttendance;
    SessionManager sessionManager;
    ArrayList<Attendance> attendanceArrayList;
    NewStartConverionAdapter attendanceAdapter;
    String id, first_name, last_name, company_name, title, email, logo, full_name;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;

    Bundle bundle;
    String adverties_id = "";
    String keyword = "";

    String tag_search = "0";
    boolean isLoading;
    Handler handler;
    ProgressBar progressBar;
    int total_pages, page_count = 1;
    int search_page_count = 1, search_total_pages;
    NestedScrollView scrollView;
    WrapContentLinearLayoutManager linearLayoutManager;
    LinearLayout linear_filter;

    public StartPrivateMessageConversion_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_startnew_conversion, container, false);
        rv_viewFullAttendance = (RecyclerView) rootView.findViewById(R.id.rv_viewFullAttendance);
        rv_viewFullAttendance.setNestedScrollingEnabled(false);
        textViewAttendee = (TextView) rootView.findViewById(R.id.textViewAttendee);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        sessionManager = new SessionManager(getActivity());
        attendanceArrayList = new ArrayList<>();
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rv_viewFullAttendance.setLayoutManager(linearLayoutManager);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        handler = new Handler();

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());

        linear_filter = (LinearLayout) rootView.findViewById(R.id.linear_filter);
        linear_filter.setVisibility(View.GONE);
        bundle = new Bundle();


        edt_search.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (GlobalData.isNetworkAvailable(getActivity())) {


                    if (edt_search.getText().length() > 0) {

                        page_count = 1;
                        search_page_count = 1;
                        keyword = edt_search.getText().toString();
                        getAttendeeeByKeyword(keyword);
                        sessionManager.keyboradHidden(edt_search);
                        Log.d("AITL FillDataCalled", keyword);
                    } else {
                        page_count = 1;
                        search_page_count = 1;
                        keyword = "";
                        getList();
                        Log.d("AITL ClearDataACTION", keyword);
                        sessionManager.keyboradHidden(edt_search);
                    }
                } else {
                    sessionManager.keyboradHidden(edt_search);
                    if (attendanceArrayList.size() > 0) {

                        attendanceAdapter.getFilter().filter(edt_search.getText().toString());
                    }
                }

                return true;
            }

            return false;
        });


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {

                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        sessionManager.keyboradHidden(edt_search);
                        page_count = 1;
                        search_page_count = 1;
                        keyword = "";
                        Log.d("AITL ClearDataText", keyword);
                        getList();

                    } else {
                        sessionManager.keyboradHidden(edt_search);
                        if (attendanceArrayList.size() > 0) {

                            attendanceAdapter.getFilter().filter(edt_search.getText().toString());
                        }
                    }
                }
            }
        });


        getList();
        return rootView;
    }


    private void getAttendeeeByKeyword(String s) {

        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_AttendanceList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, search_page_count, ""), 4, false, this);
        }
    }


    private void getList() {

        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_AttendanceList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 0, false, this);

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            attendanceArrayList.clear();
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("attendee_list");
                            total_pages = jsonData.getInt("list_total_pages");
                            //  if (!(sessionManager.isattendeeStarclick)) {
                            loadAttendeeData(jsonArray);
                            //  }
                            //  sessionManager.isattendeeStarclick = false;
                            tag_search = "0";
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("attendee_list");
                            loadAttendeeData(jsonArray);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            attendanceArrayList = new ArrayList<>();
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("attendee_list");
                            search_total_pages = jsonData.getInt("list_total_pages");
                            tag_search = "1";
                            loadAttendeeData(jsonArray);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {

                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("attendee_list");
                            loadAttendeeData(jsonArray);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void getButtonList() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_AttendanceList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 1, false, this);
    }

    private void buttonLoadGulfoodSearch() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_AttendanceList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 5, false, this);
        }
    }


    private void loadAttendeeData(JSONArray jsonArray) {

        try {
            if (!isLoading) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    id = object.getString("Id");
                    first_name = object.getString("Firstname");
                    last_name = object.getString("Lastname");
                    company_name = object.getString("Company_name");
                    title = object.getString("Title");
                    email = object.getString("Email");
                    logo = object.getString("Logo");
                    full_name = first_name + " " + last_name;
                    attendanceArrayList.add(new Attendance(id, first_name, last_name, company_name, title, email, logo, "attendance", full_name, object.getString("is_favorites"), "1", ""));
                }
            } else {
                ArrayList<Attendance> tmp_attendanceArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    id = object.getString("Id");
                    first_name = object.getString("Firstname");
                    last_name = object.getString("Lastname");
                    company_name = object.getString("Company_name");
                    title = object.getString("Title");
                    email = object.getString("Email");
                    logo = object.getString("Logo");
                    full_name = first_name + " " + last_name;
                    attendanceArrayList.add(new Attendance(id, first_name, last_name, company_name, title, email, logo, "attendance", full_name, object.getString("is_favorites"), "1", ""));
                }
                attendanceArrayList.addAll(tmp_attendanceArrayList);
            }
            set_recycler();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private BroadcastReceiver attendeeFavListingDataBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            tag="exhibitor";
            Log.d("AITL attnedeeId", intent.getStringExtra("attnedeeId"));
            Log.d("AITL STATUS", intent.getStringExtra("status"));

            for (int i = 0; i < attendanceArrayList.size(); i++) {

                if (intent.getStringExtra("attnedeeId").equalsIgnoreCase(attendanceArrayList.get(i).getId())) {
                    Log.d("AAKASH ATTENDEE NAME", attendanceArrayList.get(i).getId());
                    attendanceArrayList.get(i).setIs_fav(intent.getStringExtra("status"));
                    attendanceAdapter.notifyDataSetChanged();
                }

            }
            getList();
        }
    };


    private void set_recycler() {

        if (tag_search.equalsIgnoreCase("0")) {
            Log.d("AITL TAG", tag_search);
            try {
                if (!isLoading) {
                    if (attendanceArrayList.size() == 0) {
                        //edt_search.setVisibility(View.GONE);
                        textViewNoDATA.setText("No Attendees Found");
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        rv_viewFullAttendance.setVisibility(View.GONE);
                    } else {
                        Log.d("AITL FULL", "DATA");
                        Log.d("AITL FULL", "DATA SIZE " + attendanceArrayList.size());
                        textViewNoDATA.setVisibility(View.GONE);
                        rv_viewFullAttendance.setVisibility(View.VISIBLE);
                        attendanceAdapter = new NewStartConverionAdapter(attendanceArrayList, getActivity(), rv_viewFullAttendance, linearLayoutManager, scrollView, getActivity(), edt_search);
                        rv_viewFullAttendance.setAdapter(attendanceAdapter);
                    }
                } else {
                    Log.d("AITL EMPTY", "NOT LOADING");
                    attendanceAdapter = new NewStartConverionAdapter(attendanceArrayList, getActivity(), rv_viewFullAttendance, linearLayoutManager, scrollView, getActivity(), edt_search);
                    rv_viewFullAttendance.setAdapter(attendanceAdapter);
                    attendanceAdapter.setLoaded();
                    isLoading = false;
                }

                if (attendanceArrayList.size() != 0)
                    attendanceAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                        @Override
                        public void onLoadMore() {
                            page_count++;
                            try {
                                if (page_count <= total_pages) {

//                                message_adapter.addFooter();
                                    progressBar.setVisibility(View.VISIBLE);
                                    handler.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
//                                        message_adapter.removeFooter();
                                            progressBar.setVisibility(View.GONE);
                                            isLoading = true;
                                            try {
                                                if (GlobalData.isNetworkAvailable(getActivity()))
                                                    getButtonList();
                                                else
                                                    Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                                            } catch (NullPointerException e) {
                                                e.printStackTrace();
                                            }


                                        }
                                    }, 2000);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (tag_search.equalsIgnoreCase("1")) {
            Log.d("AITL TAG", tag_search);
            try {
                if (!isLoading) {
                    if (attendanceArrayList.size() == 0) {
                        //edt_search.setVisibility(View.GONE);
                        textViewNoDATA.setText("No Attendees Found");
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        rv_viewFullAttendance.setVisibility(View.GONE);
                    } else {
                        Log.d("AITL FULL", "DATA");

                        textViewNoDATA.setVisibility(View.GONE);
                        rv_viewFullAttendance.setVisibility(View.VISIBLE);
                        attendanceAdapter = new NewStartConverionAdapter(attendanceArrayList, getActivity(), rv_viewFullAttendance, linearLayoutManager, scrollView, getActivity(), edt_search);
                        rv_viewFullAttendance.setAdapter(attendanceAdapter);
                    }
                } else {
                    Log.d("AITL EMPTY", "NOT LOADING");
                    attendanceAdapter = new NewStartConverionAdapter(attendanceArrayList, getActivity(), rv_viewFullAttendance, linearLayoutManager, scrollView, getActivity(), edt_search);
                    rv_viewFullAttendance.setAdapter(attendanceAdapter);
                    attendanceAdapter.setLoaded();
                    isLoading = false;
                }

                scrollView.setOnScrollChangeListener(new EndlessScrollListener(linearLayoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {

                        search_page_count++;
                        try {
                            if (search_page_count <= search_total_pages) {

//                                message_adapter.addFooter();
                                progressBar.setVisibility(View.VISIBLE);
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
//                                        message_adapter.removeFooter();
                                        progressBar.setVisibility(View.GONE);
                                        isLoading = true;

                                        try {
                                            if (GlobalData.isNetworkAvailable(getActivity()))
                                                buttonLoadGulfoodSearch();
                                            else
                                                Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, 2000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
