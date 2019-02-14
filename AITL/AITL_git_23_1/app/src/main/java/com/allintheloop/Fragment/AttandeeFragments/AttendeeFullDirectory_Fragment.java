package com.allintheloop.Fragment.AttandeeFragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Attendee.AttendeeFilterExpantableListAdapter;
import com.allintheloop.Adapter.Attendee.AttendeeFullDirectoryAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Bean.Attendee.AttendeeKeywordData;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.allintheloop.Util.GlobalData.attendeeFavListingData;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendeeFullDirectory_Fragment extends Fragment implements VolleyInterface {


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    public ArrayList<String> attendeeFilterList;

    TextView textViewNoDATA;
    BoldTextView txt_filter, btn_cancel;
    EditText edt_search;
    RecyclerView rv_viewFullAttendance, rv_cateogry;
    SessionManager sessionManager;
    List<Attendance> attendanceArrayList;
    AttendeeFullDirectoryAdapter attandeeAdatpter_temp;
    String id, first_name, last_name, company_name, title, email, logo, full_name;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "attendanceAllDirectory";
    String adverties_id = "";
    String keyword = "";
    String tag_search = "0";
    Handler handler;
    int total_pages, page_count = 1;
    int search_page_count = 1, search_total_pages;
    //    NestedScrollView scrollView;
    DefaultLanguage.DefaultLang defaultLanguage;
    RelativeLayout MainLayout, relativeLayout_static;
    LinearLayout linear_content, linear_filter;

    ArrayList<String> selectedCategory;
    String filterString = "";
    Button btn_clearSearch, btn_filter;
    boolean isLoadMore = true, loading = false;
    int totalitemcount, lastVisibleItempagecount;
    LinearLayoutManager layoutManager;
    LinearLayout linear_filterdropdown, linear_button, linear_edtRecyclerview;
    RelativeLayout linear_filerdailog;
    boolean isFilterOpen = false;
    ImageView img_arrow;
    List<AttendeeFilterList.Data> aDataList;
    JSONArray jsonArray = new JSONArray();
    RecyclerView.OnScrollListener onScrollListener = null;


    ExpandableListView category_expand;
    ArrayList<AttendeeFilterList.Data> listDataHeader;
    HashMap<AttendeeFilterList.Data, ArrayList<AttendeeKeywordData>> listDataChild;
    ArrayList<AttendeeKeywordData> child;
    AttendeeFilterExpantableListAdapter expantableListAdapter;

    private BroadcastReceiver attendeeFavListingDataBroadCast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            for (int i = 0; i < attendanceArrayList.size(); i++) {
//                if (intent.getStringExtra("attnedeeId").equalsIgnoreCase(attendanceArrayList.get(i).getId())) {
//                    attendanceArrayList.get(i).setIs_fav(intent.getStringExtra("status"));
//                    attandeeAdatpter_temp.notifyDataSetChanged();
//                }
//            }
            resetValue();
            getList();
        }
    };

    public AttendeeFullDirectory_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_attendee_full_directory, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        initView(rootView);
        refreshFragment();


        linear_filterdropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isFilterOpen) {
                    isFilterOpen = false;

                    YoYo.with(Techniques.SlideInUp).duration(500).playOn(linear_edtRecyclerview);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            linear_filerdailog.setVisibility(View.GONE);
                            img_arrow.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
                        }
                    }, 10);

//                    YoYo.with(Techniques.SlideOutUp)
//                            .duration(500)
//                            .withListener(new Animator.AnimatorListener() {
//                                @Override
//                                public void onAnimationStart(Animator animation)
//                                {
//
//                                }
//
//                                @Override
//                                public void onAnimationEnd(Animator animation)
//                                {
////                                    GlobalData.Viewanimation(linear_edtRecyclerview,Techniques.SlideInUp);
//                                }
//
//                                @Override
//                                public void onAnimationCancel(Animator animation) {
//                                }
//
//                                @Override
//                                public void onAnimationRepeat(Animator animation) {
//                                }
//                            }).playOn(linear_filerdailog);

                } else {
                    isFilterOpen = true;
                    sessionManager.keyboradHidden(edt_search);

                    GlobalData.Viewanimation(linear_edtRecyclerview, Techniques.SlideInUp);
                    YoYo.with(Techniques.SlideInDown)
                            .duration(500)
                            .withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                    linear_filerdailog.setVisibility(View.VISIBLE);
                                    img_arrow.setImageDrawable(getResources().getDrawable(R.drawable.up_arrow));
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {

//                                    linear_filerdailog.setVisibility(View.GONE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            }).playOn(linear_filerdailog);


//                    GlobalData.Viewanimation(linear_filerdailog,Techniques.SlideInDown);


                }
                setButton();
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        if (edt_search.getText().length() > 0) {
                            page_count = 1;
                            isLoadMore = true;
                            search_page_count = 1;
                            keyword = edt_search.getText().toString();
                            getAttendeeeByKeyword();
                            sessionManager.keyboradHidden(edt_search);
                        } else {
                            resetValue();
                            clearSearchList();
                            sessionManager.keyboradHidden(edt_search);
                        }
                    } else {
                    }
                    return true;
                }

                return false;
            }

        });

        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listDataChild.size() != 0) {
                    jsonArray = expantableListAdapter.getSelectableList();
                    resetValue();
                    setRecyclerView();
                    getList();
                    hideFilterLayout();
                }

            }
        });

        linear_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aDataList.size() != 0) {
                    refreshFragment();
                }
            }
        });
        getAdvertiesment();
        return rootView;
    }


    public void linearFilterAnimation(boolean isVisible) {
        if (isVisible) {
//            YoYo.with(Techniques.SlideInDown)
//                    .onStart(new YoYo.AnimatorCallback() {
//                        @Override
//                        public void call(Animator animator) {
//
//                        }
//                    }).playOn(linear_filter);


            linear_filter.setVisibility(View.VISIBLE);

        } else {
//            YoYo.with(Techniques.SlideInDown)
//                    .onStart(new YoYo.AnimatorCallback() {
//                        @Override
//                        public void call(Animator animator) {
//
//                        }
//                    }).playOn(linear_filter);

            linear_filter.setVisibility(View.GONE);

        }


    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 6, false, this);

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

    private void resetValue() {
        isFilterOpen = false;
        filterString = "";
        page_count = 1;
        isLoadMore = true;
        search_page_count = 1;
        keyword = "";
        edt_search.getText().clear();
    }

    public void refreshFragment() {

        linear_filter.setVisibility(View.GONE);
        sessionManager = new SessionManager(getActivity());
        tag = "attendanceAllDirectory" + sessionManager.getAttendeeMainCategoryData();
        defaultLanguage = sessionManager.getMultiLangString();
        attendanceArrayList = new ArrayList<>();
        handler = new Handler();
        jsonArray = new JSONArray();
        isLoadMore = true;
        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        selectedCategory = new ArrayList<>();
        attendeeFilterList = new ArrayList<>();
        aDataList = new ArrayList<>();

        edt_search.setHint(defaultLanguage.get2Search().toUpperCase());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        setButton();
        resetValue();
        setRecyclerView();
        getList();
        getAttendeeFilerdata();
        hideFilterLayout();
    }

    private void setButton() {
        GradientDrawable drawableFulllist = new GradientDrawable();
        drawableFulllist.setShape(GradientDrawable.RECTANGLE);
        drawableFulllist.setCornerRadius(13.0f);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            drawableFulllist.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
//            btn_cancel.setBackgroundDrawable(drawableFulllist);
//            btn_cancel.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_filter.setBackgroundDrawable(drawableFulllist);
            btn_filter.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            txt_filter.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            linear_filterdropdown.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            img_arrow.setColorFilter(Color.parseColor(sessionManager.getFunTopTextColor()));

        } else {
            drawableFulllist.setColor(Color.parseColor(sessionManager.getTopBackColor()));
//            btn_cancel.setBackgroundDrawable(drawableFulllist);
//            btn_cancel.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_filter.setBackgroundDrawable(drawableFulllist);
            btn_filter.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            txt_filter.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            linear_filterdropdown.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            img_arrow.setColorFilter(Color.parseColor(sessionManager.getTopTextColor()));


        }
    }

    private void hideFilterLayout() {
        isFilterOpen = false;
        linear_filerdailog.setVisibility(View.GONE);
        rv_viewFullAttendance.setVisibility(View.VISIBLE);
        img_arrow.setImageDrawable(getResources().getDrawable(R.drawable.down_arrow));
        edt_search.setVisibility(View.VISIBLE);
    }

    private void paginationMethod() {
        if (onScrollListener != null)
            rv_viewFullAttendance.removeOnScrollListener(onScrollListener);
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalitemcount = layoutManager.getItemCount();
                lastVisibleItempagecount = layoutManager.findLastVisibleItemPosition();
                if (!loading && totalitemcount <= (lastVisibleItempagecount + 5) && isLoadMore) {

                    loading = true;
                    if (tag_search.equalsIgnoreCase("0")) {
                        if (total_pages > page_count) {
                            page_count = page_count + 1;
                            getButtonList();
                        } else {
                            isLoadMore = false;
                        }

                    } else {
                        if (search_total_pages > search_page_count) {
                            search_page_count = search_page_count + 1;
                            buttonLoadGulfoodSearch();
                        } else {
                            isLoadMore = false;
                        }
                    }
                }
            }
        };
        rv_viewFullAttendance.addOnScrollListener(onScrollListener);
    }

    private void setRecyclerView() {
        layoutManager = new LinearLayoutManager(getActivity());
        rv_viewFullAttendance.setLayoutManager(layoutManager);
        rv_viewFullAttendance.setItemAnimator(new DefaultItemAnimator());
        attandeeAdatpter_temp = new AttendeeFullDirectoryAdapter(attendanceArrayList, getActivity(), AttendeeFullDirectory_Fragment.this);
        rv_viewFullAttendance.setAdapter(attandeeAdatpter_temp);
        paginationMethod();
    }

    private void initView(View rootView) {
        page_count = 1;
        search_page_count = 1;
        rv_viewFullAttendance = rootView.findViewById(R.id.rv_viewFullAttendance);
        category_expand = rootView.findViewById(R.id.category_expand);
        txt_filter = rootView.findViewById(R.id.txt_filter);
        textViewNoDATA = rootView.findViewById(R.id.textViewNoDATA);
        relativeLayout_static = rootView.findViewById(R.id.relativeLayout_static);
        MainLayout = rootView.findViewById(R.id.MainLayout);
        linear_content = rootView.findViewById(R.id.linear_content);
        linear_filterdropdown = rootView.findViewById(R.id.linear_filterdropdown);
        linear_button = rootView.findViewById(R.id.linear_button);
        linear_edtRecyclerview = rootView.findViewById(R.id.linear_edtRecyclerview);
        linear_filerdailog = rootView.findViewById(R.id.linear_filerdailog);
        linear_filter = rootView.findViewById(R.id.linear_filter);
//        btn_clearSearch = rootView.findViewById(R.id.btn_clearSearch);
        btn_cancel = rootView.findViewById(R.id.btn_cancel);
        btn_filter = rootView.findViewById(R.id.btn_filter);
        img_arrow = rootView.findViewById(R.id.img_arrow);
        edt_search = rootView.findViewById(R.id.edt_search);


    }

    private void getAttendeeFilerdata() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeFiterData, Param.getAttendeeFilterListData(sessionManager.getEventId(), sessionManager.getAttendeeMainCategoryData()), 7, false, this);
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 10, false, this);
        }
    }

    private void getAttendeeeByKeyword() {

        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNewUid, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, search_page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 4, true, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNew, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, search_page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 4, true, this);
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
                        JSONArray jArrayevent = jsonObject.getJSONArray("attendee_list");
//                        loadAttendeeFromDataBase(jArrayevent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNewUid, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNew, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 0, false, this);
        } else {
            attendanceArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {

                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        JSONArray jArrayevent = jsonObject.getJSONArray("attendee_list");

                        loadAttendeeFromDataBase(jArrayevent);
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

    private void clearSearchList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            attendanceArrayList.clear();
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNewUid,
                        Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(),
                                sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count,
                                filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString())
                        , 0, true, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNew,
                        Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(),
                                sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count,
                                filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray
                                        .toString()), 0, true, this);
            }
        } else {
            attendanceArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {

                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        JSONArray jArrayevent = jsonObject.getJSONArray("attendee_list");

                        loadAttendeeFromDataBase(jArrayevent);
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

    private void loadAttendeeFromDataBase(JSONArray jsonArray) {
        List<Attendance> attendanceArrayListTemp = new ArrayList<>();
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
                full_name = first_name + " " + last_name;
                attendanceArrayListTemp.add(new Attendance(id, first_name, last_name, company_name, title, email, logo, "attendance", full_name, object.getString("is_favorites"), "1", ""));
            }
            if (attendanceArrayListTemp.size() == 0) {
                textViewNoDATA.setText("No Attendees Found");
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewFullAttendance.setVisibility(View.GONE);
            } else {
                textViewNoDATA.setVisibility(View.GONE);
                rv_viewFullAttendance.setVisibility(View.VISIBLE);
                attendanceArrayList.addAll(attendanceArrayListTemp);
                new setListview().execute();
            }
            loading = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAttendeeData(JSONArray jsonArray) {
        List<Attendance> attendanceArrayListTemp = new ArrayList<>();
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
                full_name = first_name + " " + last_name;
                attendanceArrayListTemp.add(new Attendance(id, first_name, last_name, company_name, title, email, logo, "attendance", full_name, object.getString("is_favorites"), "1", ""));
            }

            if (tag_search.equalsIgnoreCase("0")) {
                if (page_count != 1) {
                    if (attendanceArrayList.size() != 0) {
                        attendanceArrayList.remove(attendanceArrayList.size() - 1);
                    }
                }
                if (total_pages > 1) {
                    if (page_count != total_pages) {
                        if (attendanceArrayListTemp.size() != 0) {
                            attendanceArrayListTemp.add(null);
                        }
                    }
                }

            } else {
                if (search_page_count != 1) {
                    if (attendanceArrayList.size() != 0) {
                        attendanceArrayList.remove(attendanceArrayList.size() - 1);
                    }
                }
                if (search_total_pages > 1) {
                    if (search_page_count != search_total_pages) {
                        if (attendanceArrayListTemp.size() != 0) {
                            attendanceArrayListTemp.add(null);
                        }
                    }

                }
            }
            if (attendanceArrayListTemp.size() != 0) {
                attendanceArrayList.addAll(attendanceArrayListTemp);
            }
            if (attendanceArrayList.size() == 0) {
                textViewNoDATA.setText("No Attendees Found");
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewFullAttendance.setVisibility(View.GONE);
            } else {
                textViewNoDATA.setVisibility(View.GONE);
                rv_viewFullAttendance.setVisibility(View.VISIBLE);
            }
            new setListview().execute();
            loading = false;
        } catch (Exception e) {
            e.printStackTrace();
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
                            attendanceArrayList = new ArrayList<>();
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("attendee_list");
                            total_pages = jsonData.getInt("list_total_pages");

                            if (filterString.isEmpty()) {
                                if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                                    sqLiteDatabaseHandler.updateListingData(sessionManager.getEventId(), tag, sessionManager.getUserId(), jsonData.toString());
                                    sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                                } else {
                                    sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                                }
                            }
//                            scrollView.setScrollY(0);
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

//                            scrollView.setScrollY(0);
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
                            search_total_pages = jsonData.getInt("list_total_pages");
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
                    pagewiseClick();
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
            case 7:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Gson gson = new Gson();
                        AttendeeFilterList data = gson.fromJson(jsonObject.toString(), AttendeeFilterList.class);

                        aDataList = data.getData();
                        if (aDataList.size() == 0) {
                            linearFilterAnimation(false);
                        } else {
                            linearFilterAnimation(true);
                            listDataHeader = new ArrayList<>();
                            listDataChild = new HashMap<AttendeeFilterList.Data, ArrayList<AttendeeKeywordData>>();
                            child = new ArrayList<>();
                            for (AttendeeFilterList.Data header : aDataList) {
                                child = new ArrayList<>();
                                for (String keyword : header.getKeywords()) {
                                    if (!keyword.trim().isEmpty()) {
                                        child.add(new AttendeeKeywordData(keyword.trim(), false));
                                    }
                                }
                                listDataHeader.add(header);
                                Collections.sort(child, new childSorting());
                                listDataChild.put(header, child);
                            }
                            if (listDataHeader.size() != 0)
                                Collections.sort(listDataHeader, new headerSorting());
                            expantableListAdapter = new AttendeeFilterExpantableListAdapter(getActivity(), listDataHeader, listDataChild, AttendeeFullDirectory_Fragment.this, sessionManager);
                            category_expand.setDividerHeight(0);
                            category_expand.setAdapter(expantableListAdapter);
                            category_expand.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                                int previousGroup = -1;

                                @Override
                                public void onGroupExpand(int groupPosition) {
                                    if (groupPosition != previousGroup)
                                        category_expand.collapseGroup(previousGroup);
                                    previousGroup = groupPosition;
                                }
                            });
                        }

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
            sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
            sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, topAdverViewArrayList, getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loaddataFromAdapter(JSONObject jsonData) {
        try {
            JSONArray jsonArray = jsonData.getJSONArray("attendee_list");
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
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNewUid, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 1, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNew, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 1, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void buttonLoadGulfoodSearch() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNewUid, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, search_page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 5, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAttendeeListNew, Param.getAttendeeListNew(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), keyword, search_page_count, filterString, sessionManager.getAttendeeMainCategoryData(), jsonArray.toString()), 5, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {

//                listDataChild.put(data.getExtras().getString("categoryid"),(ArrayList<String>)data.getExtras().getSerializable("selectedName"));
//                ArrayList<String> fullArray=(ArrayList<String>) data.getExtras().getSerializable("myData");
//                JSONArray jsonArray=new JSONArray();
//                Log.d("Bhavdip Data",listDataChild.toString());


//                attendeeCategoryArrayList.clear();
//                selectedCategory.clear();
//                selectedCategory = (ArrayList<String>) data.getExtras().getSerializable("selected");
//                attendeeCategoryArrayList = (ArrayList<AttendeeCategoryList.AttendeeCategory>) data.getExtras().getSerializable("myData");
//                ArrayList<String> selectedName = (ArrayList<String>) data.getExtras().getSerializable("selectedName");
//                setFilterText(txt_filter, selectedName, selectedCategory);
//
//
//                try {
//                    filterString = "";
//                    filterString = selectedCategory.toString();
//                    filterString = filterString.replaceAll("\\[", "").replaceAll("\\]", "");
//                    Log.d("Bhavdip Keyword", filterString);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                if (filterString.isEmpty()) {
//
//                } else {
//                    getFilterList();
//                }
            }

        }
    }

    public void setFilterText(TextView textView, ArrayList<String> list, ArrayList<String> selectedData) {
        String s = list.toString();
        s = s.substring(1, s.length() - 1);
        textView.setText(s);


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

    public class headerSorting implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String time1 = "", time2 = "";
            Date date1, date2;
            if (o1 instanceof AttendeeFilterList.Data) {
                time1 = ((AttendeeFilterList.Data) o1).getColumnName();
            }
            if (o2 instanceof AttendeeFilterList.Data) {
                time2 = ((AttendeeFilterList.Data) o2).getColumnName();
            }
            return time1.compareTo(time2);
        }
    }

    public class childSorting implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String time1 = "", time2 = "";
            Date date1, date2;
            if (o1 instanceof AttendeeKeywordData) {
                time1 = ((AttendeeKeywordData) o1).getKeyword();
            }
            if (o2 instanceof AttendeeKeywordData) {
                time2 = ((AttendeeKeywordData) o2).getKeyword();
            }
            return time1.compareTo(time2);
        }
    }

    private class setListview extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            attandeeAdatpter_temp.updateList(attendanceArrayList);
        }
    }
}