package com.allintheloop.Fragment.AttandeeFragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Attendee.AttandeeMyContactAdaper;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.R;
import com.allintheloop.Util.EndlessScrollListener;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.allintheloop.Util.GlobalData.attendeeFavListingData;

public class AttendeeMyContact_Fragment extends Fragment implements VolleyInterface {

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    TextView textViewAttendee, textViewNoDATA;
    EditText edt_search;
    RecyclerView rv_viewFullAttendance;
    SessionManager sessionManager;
    ArrayList<Attendance> attendanceArrayList;
    AttandeeMyContactAdaper attandeeAdatpter_temp;
    String id, first_name, last_name, company_name, title, email, logo, full_name, type, exhibitor_id, exhibitor_page_id, is_favorites;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "attendanceContact";
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
    EndlessScrollListener endlessScrollListener;
    DefaultLanguage.DefaultLang defaultLang;
    RelativeLayout MainLayout, relativeLayout_static;
    LinearLayout linear_content;
    private BroadcastReceiver attendeeFavListingDataBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            tag="exhibitor";


            for (int i = 0; i < attendanceArrayList.size(); i++) {

                if (intent.getStringExtra("attnedeeId").equalsIgnoreCase(attendanceArrayList.get(i).getId())) {
                    attendanceArrayList.get(i).setIs_fav(intent.getStringExtra("status"));
                    attandeeAdatpter_temp.notifyDataSetChanged();
                }
            }
            getList();
        }
    };

    public AttendeeMyContact_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attendee_my_contact, container, false);
        initView(rootView);
        refreshFragment();
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

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

                            attandeeAdatpter_temp.getFilter().filter(edt_search.getText().toString());
                        }
                    }
                    return true;
                }
                return false;
            }

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
//                        endlessScrollListener.setpriTotalCount(0);
                        sessionManager.keyboradHidden(edt_search);
                        page_count = 1;
                        search_page_count = 1;
                        keyword = "";
                        Log.d("AITL ClearDataText", keyword);
                        getList();

                    } else {
                        sessionManager.keyboradHidden(edt_search);
                        if (attendanceArrayList.size() > 0) {

                            attandeeAdatpter_temp.getFilter().filter(edt_search.getText().toString());
                        }
                    }
                }
            }
        });
        getAdvertiesment();
        return rootView;
    }

    private void setUpRecyclerView() {
        rv_viewFullAttendance.setNestedScrollingEnabled(false);
        WrapContentLinearLayoutManager mLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rv_viewFullAttendance.setLayoutManager(mLayoutManager);
        rv_viewFullAttendance.setItemAnimator(new DefaultItemAnimator());

        endlessScrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (tag_search.equalsIgnoreCase("0")) {
                    if (total_pages > page) {
                        page_count = page + 1;
                        getButtonList();
                    }
                } else {
                    if (search_total_pages > page) {
                        search_page_count = page + 1;
                        buttonLoadGulfoodSearch();
                    }
                }
            }
        };
        scrollView.setOnScrollChangeListener(endlessScrollListener);
    }

    public void refreshFragment() {
        keyword = "";
        tag_search = "0";
        page_count = 1;
        search_page_count = 1;
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        attendanceArrayList = new ArrayList<>();
        handler = new Handler();
        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        edt_search.setHint(defaultLang.get2Search());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        setUpRecyclerView();
        getList();
    }

    private void initView(View rootView) {
        rv_viewFullAttendance = (RecyclerView) rootView.findViewById(R.id.rv_viewFullAttendance);
        textViewAttendee = (TextView) rootView.findViewById(R.id.textViewAttendee);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        relativeLayout_static = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_static);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 6, false, this);

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

    private void getAttendeeeByKeyword(String s) {

        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.
                        getAttendeeContactListUid, Param.getAttendanceList(sessionManager.
                                getToken(), sessionManager.getEventId(), sessionManager.getEventType()
                        , sessionManager.getUserId(), keyword, search_page_count,
                        ""), 4, true, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, search_page_count, ""), 4, true, this);
        }
    }

    private void getList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            attendanceArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {

                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jArrayevent = jsonObject.getJSONArray("contact_attendee");
                        loadAttendeeData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (GlobalData.checkForUIDVersion())
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactListUid, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 0, false, this);
                else
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 0, false, this);

            } else {
                if (GlobalData.checkForUIDVersion())
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactListUid, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 0, false, this);
                else
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 0, false, this);
            }
        } else {
            attendanceArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {

                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        JSONArray jArrayevent = jsonObject.getJSONArray("contact_attendee");

                        loadAttendeeData(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                edt_search.setVisibility(View.GONE);
                textViewNoDATA.setText("No Attendees Found");
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewFullAttendance.setVisibility(View.GONE);
            }
        }
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
                            JSONArray jsonArray = jsonData.getJSONArray("contact_attendee");
                            total_pages = jsonData.getInt("list_total_pages");
                            tag_search = "0";
                            if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                                sqLiteDatabaseHandler.updateListingData(sessionManager.getEventId(), tag, sessionManager.getUserId(), jsonData.toString());
                                sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                            } else {
                                sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                            }
                            Log.d("AAkash STATUS", "STARUS" + sessionManager.isattendeeStarclick);
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
                            progressBar.setVisibility(View.GONE);
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("contact_attendee");
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
                            JSONArray jsonArray = jsonData.getJSONArray("contact_attendee");
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
                            progressBar.setVisibility(View.GONE);
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("contact_attendee");
                            loadAttendeeData(jsonArray);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
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

            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1")) {
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loaddataFromAdapter(JSONObject jsonData) {
        try {
//            page_count = 1;
//            search_page_count = 1;
//            keyword = "";
//            JSONArray jsonArray = jsonData.getJSONArray("contact_attendee");
//            attendanceArrayList.clear();
            if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
            } else {
                sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
            }
//            loadAttendeeData(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getButtonList() {

        if (GlobalData.isNetworkAvailable(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactListUid, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 1, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 1, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void buttonLoadGulfoodSearch() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            progressBar.setVisibility(View.VISIBLE);
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactListUid, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 5, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeContactList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, ""), 5, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void loadAttendeeData(JSONArray jsonArray) {

        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                id = object.getString("Id");
                first_name = object.getString("Firstname");
                last_name = object.getString("Lastname");
                company_name = object.getString("Company_name");
                title = object.getString("Title");
                email = object.getString("Email");
                logo = object.getString("Logo");
                is_favorites = object.getString("is_favorites");
                exhibitor_id = object.getString("exhibitor_id");
                exhibitor_page_id = object.getString("exhibitor_page_id");
                type = object.getString("type");
                full_name = first_name + " " + last_name;
                attendanceArrayList.add(new Attendance(id, first_name, last_name, company_name, title, email, logo, "attendance", full_name, is_favorites, type, exhibitor_id, exhibitor_page_id, "0"));

            }

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
                attandeeAdatpter_temp = new AttandeeMyContactAdaper(attendanceArrayList, getActivity(), AttendeeMyContact_Fragment.this);
                rv_viewFullAttendance.setAdapter(attandeeAdatpter_temp);
            }

//            set_recycler();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(attendeeFavListingDataBroadCast, new IntentFilter(attendeeFavListingData));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(attendeeFavListingDataBroadCast);
    }
}
