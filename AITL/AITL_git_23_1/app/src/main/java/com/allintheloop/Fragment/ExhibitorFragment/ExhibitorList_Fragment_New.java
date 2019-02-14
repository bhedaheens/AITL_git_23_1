package com.allintheloop.Fragment.ExhibitorFragment;

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
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Activity.ExhibitorCountryListActivity;
import com.allintheloop.Activity.ExhibitorParentCatListActivity;
import com.allintheloop.Adapter.Exhibitor.ExhibitorCategoryListingAdapter;
import com.allintheloop.Adapter.ExhibitorListWithSection.AdapterSectionRecyclerNew;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCountry;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorParentCatGroup;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorType;
import com.allintheloop.Bean.ExhibitorListClass.FavoritedExhibitor;
import com.allintheloop.Bean.ExhibitorListClass.SectionHeader;
import com.allintheloop.Bean.SectionHeaderParentGroup;
import com.allintheloop.Bean.Speaker.SpeakerReloadedEventBus;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

import static com.allintheloop.Util.GlobalData.Update_Profile;

/**
 * Created by nteam on 17/11/17.
 */

public class ExhibitorList_Fragment_New extends Fragment implements VolleyInterface {


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    EditText edt_search;
    RecyclerView recyclerView, rv_viewCategoryListing;
    TextView textViewNoDATA, textViewAttendee;
    SessionManager sessionManager;
    String tag;
    Button btn_seeRequestExhibitor, btn_Clearsearch;
    Bundle bundle;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String keyword = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    String category_id = "";
    WrapContentLinearLayoutManager linearLayoutManager;
    DefaultLanguage.DefaultLang defaultLang;
    ExhibitorCategoryListingAdapter exhibitorCategoryListingAdapter;
    ArrayList<ExhibitorCategoryListing> categoryListings;
    ArrayList<SectionHeaderParentGroup> sectionHeaderParentGroups;
    ArrayList<Attendance> attendanceArrayList;
    List<SectionHeader> sections;
    AdapterSectionRecyclerNew adapterRecycler;
    LinearLayout Container_Attendance;
    Context context;
    //NewChanges
    LinearLayout filterLayout;
    TextView txtSelectCountry, txtSelectProduct;
    HorizontalScrollView horizontalScrollView1;
    ArrayList<ExhibitorCountry> exhibitorCountries = new ArrayList<>();
    ArrayList<String> selectedCountries = new ArrayList<>();
    ArrayList<String> selectedParentCat = new ArrayList<>();
    ArrayList<String> selectedCategoryId = new ArrayList<>();
    boolean isButtonClick = false;
    boolean isSliderShow = false;
    ArrayList<ExhibitorParentCatGroup> groupData;
    ProgressBar progressBar;
    UidCommonKeyClass uidCommonKeyClass;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d("niral", "Reciver Called : " + sessionManager.getUserProfile());
            // TODO Auto-generated method stub
            adapterRecycler.notifyDataSetChanged();
        }
    };

    public ExhibitorList_Fragment_New() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor_list_new, container, false);
        context = getActivity();
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        initView(rootView);
        arrayInitlize();
        rv_viewCategoryListing.setHorizontalFadingEdgeEnabled(false);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        sessionManager.setIsLastCategoryName("");
        tag = getTag() + sessionManager.getExhibitorParentCategoryId();
        GlobalData.currentModuleForOnResume = GlobalData.exhibitorModuleid;
        getcategoryForLeftToright();
        viewVisibleGone();
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        onClick();
        getAdvertiesment();
        defaultLang = sessionManager.getMultiLangString();
        edt_search.setHint(defaultLang.get3Search().toUpperCase());
        buttonSet();
        return rootView;
    }

    private void onClick() {
        rv_viewCategoryListing.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ExhibitorCategoryListing exhibitorGulfoodSector = categoryListings.get(position);
                Log.d("AITL PRODUCT NAME", exhibitorGulfoodSector.getSector());
//                txt_productName.setVisibility(View.GONE);
//                txt_productName.setText(exhibitorGulfoodSector.getSector() + " " + "Category Selected");

                sessionManager.keyboradHidden(edt_search);
                sessionManager.setIsLastCategoryName("");
                getExhibitorBySubCat(exhibitorGulfoodSector.getShortDesc());
                isSelected(position);
            }
        }));

        btn_Clearsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isButtonClick = true;
                sessionManager.keyboradHidden(edt_search);

                sessionManager.setIsLastCategoryName("");
                if (!(edt_search.getText().toString().equalsIgnoreCase(""))) {
                    edt_search.setText("");
                }
                category_id = "";
                keyword = "";
                if (isSliderShow) {
                    isSelected(-1);
                } else {
                    getcategoryForLeftToright();
                    selectedCountries.clear();
                    selectedParentCat.clear();
                    selectedCategoryId.clear();
                    txtSelectCountry.setText("");
                    txtSelectProduct.setText("");
                }
                new getExhibitorAsyncTask(true).execute();
            }
        });


        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (sections.size() > 0) {
                        sessionManager.setIsLastCategoryName("");
                        keyword = edt_search.getText().toString();
//                            getExhibitorByKeyword();
                        adapterRecycler.filter(keyword);
                        sessionManager.keyboradHidden(edt_search);
                        Log.d("AITL FillDataCalled", keyword);
                    }
                    return true;
                }

                return false;
            }

        });


        btn_seeRequestExhibitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        txtSelectCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalData.currentFragment = GlobalData.CurrentFragment.ExhibitorList_Fragment_New;

                Intent intent = new Intent(((MainActivity) getActivity()), ExhibitorCountryListActivity.class);
                intent.putExtra("countries", exhibitorCountries);
                startActivityForResult(intent, 1000);

            }
        });

        txtSelectProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isGroupData = "";
                if (groupData.size() > 0) {
                    isGroupData = "1";
                } else {
                    isGroupData = "0";
                }
                GlobalData.currentFragment = GlobalData.CurrentFragment.ExhibitorList_Fragment_New;
                Intent intent = new Intent(((MainActivity) getActivity()), ExhibitorParentCatListActivity.class);
                intent.putExtra("categories", sectionHeaderParentGroups);
                intent.putExtra("selected", selectedParentCat);
                intent.putExtra("isGroupData", isGroupData);
                startActivityForResult(intent, 2000);
            }
        });
    }

    private void viewVisibleGone() {
        if (exhibitorCountries.size() != 0) {
            txtSelectCountry.setVisibility(View.VISIBLE);
        } else {
            txtSelectCountry.setVisibility(View.GONE);
        }
        if (sectionHeaderParentGroups.size() != 0) {
            txtSelectProduct.setVisibility(View.VISIBLE);
        } else {
            txtSelectProduct.setVisibility(View.GONE);
        }
        if (sessionManager.getRequestMettingButton().equalsIgnoreCase("1")) {

            if (GlobalData.checkForUIDVersion()) {
                if (uidCommonKeyClass.getIsOnlyExhibitorUser().equalsIgnoreCase("1")) {
                    btn_seeRequestExhibitor.setVisibility(View.VISIBLE);
                    buttonSet();
                } else {
                    btn_seeRequestExhibitor.setVisibility(View.GONE);
                }
            } else {
                if (sessionManager.getRolId().equalsIgnoreCase("6")) {//changes applied
                    btn_seeRequestExhibitor.setVisibility(View.VISIBLE);
                    buttonSet();
                } else {
                    btn_seeRequestExhibitor.setVisibility(View.GONE);
                }
            }

        } else {
            btn_seeRequestExhibitor.setVisibility(View.GONE);
        }

        if (sessionManager.isLogin()) {
            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getCategoryListing();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
//                new getCategoryInAsyncTask().execute();
                getCategoryListing();
            }
        }

    }

    private void initView(View rootView) {
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        btn_seeRequestExhibitor = (Button) rootView.findViewById(R.id.btn_seeRequestExhibitor);
        btn_Clearsearch = (Button) rootView.findViewById(R.id.btn_Clearsearch);
        textViewAttendee = (TextView) rootView.findViewById(R.id.textViewAttendee);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_viewAttendance);
        rv_viewCategoryListing = (RecyclerView) rootView.findViewById(R.id.rv_viewCategoryListing);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);

        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        Container_Attendance = (LinearLayout) rootView.findViewById(R.id.Container_Attendance);

        filterLayout = (LinearLayout) rootView.findViewById(R.id.filterLayout);
        txtSelectCountry = (TextView) rootView.findViewById(R.id.txtSelectCountry);
        txtSelectProduct = (TextView) rootView.findViewById(R.id.txtSelectProduct);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        horizontalScrollView1 = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView1);
        sessionManager = new SessionManager(getActivity());

        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
    }

    private void arrayInitlize() {
        categoryListings = new ArrayList<>();

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        groupData = new ArrayList<>();
        attendanceArrayList = new ArrayList<>();
        sections = new ArrayList<>();
        sectionHeaderParentGroups = new ArrayList<>();

    }

    private void getcategoryForLeftToright() {
        categoryListings = sqLiteDatabaseHandler.getSubCategoryList1(sessionManager.getExhibitorParentCategoryId(), sessionManager.getEventId());
        exhibitorCountries = sqLiteDatabaseHandler.getExhibitorCountries(sessionManager.getEventId());
        sectionHeaderParentGroups = new ArrayList<>();
        groupData = sqLiteDatabaseHandler.getExhibitorParentGroupData(sessionManager.getExhibitorParentCategoryId(), sessionManager.getEventId());
        if (groupData.size() > 0) {
            for (int i = 0; i < groupData.size(); i++) {
                ArrayList<ExhibitorCategoryListing> attendanceArrayList = sqLiteDatabaseHandler.getSubCategoryList1GroupWise(sessionManager.getExhibitorParentCategoryId(), sessionManager.getEventId(), groupData.get(i).getId());
                Collections.sort(attendanceArrayList, new SortComparator());
                if (attendanceArrayList.size() > 0) {
                    sectionHeaderParentGroups.add(new SectionHeaderParentGroup(attendanceArrayList, groupData.get(i).getName(), groupData.get(i).getId()));
                }
            }
        } else {
            ArrayList<ExhibitorCategoryListing> attendanceArrayList = sqLiteDatabaseHandler.getSubCategoryList1GroupWise(sessionManager.getExhibitorParentCategoryId(), sessionManager.getEventId(), "");
            ArrayList<String> singleLatter = new ArrayList<>();
            Collections.sort(attendanceArrayList, new SortComparator());
            if (attendanceArrayList.size() > 0) {
                ArrayList<String> stringfirstLatter = new ArrayList<>();
                for (ExhibitorCategoryListing categoryListing : attendanceArrayList) {
                    if (!categoryListing.getSector().isEmpty()) {
                        String index = categoryListing.getSector().substring(0, 1);
                        stringfirstLatter.add(index);
                    }
                }
                stringfirstLatter = new ArrayList<String>(new LinkedHashSet<String>(stringfirstLatter));
                for (String name : stringfirstLatter) {
                    singleLatter.add(name);
                }
                for (int i = 0; i < singleLatter.size(); i++) {
                    ArrayList<ExhibitorCategoryListing> attendanceArrayListtemp = getAttendeeListFromEmptyGroup(singleLatter.get(i).toString(), attendanceArrayList);
                    if (attendanceArrayList.size() > 0) {
                        sectionHeaderParentGroups.add(new SectionHeaderParentGroup(attendanceArrayListtemp, singleLatter.get(i).toString(), ""));
                    }
                }
            }
        }
    }

    private ArrayList<ExhibitorCategoryListing> getAttendeeListFromEmptyGroup(String latter, ArrayList<ExhibitorCategoryListing> temparrayList) {
        ArrayList<ExhibitorCategoryListing> parentCategoryData = new ArrayList<ExhibitorCategoryListing>();
        for (int i = 0; i < temparrayList.size(); i++) {
            if (latter.equalsIgnoreCase(temparrayList.get(i).getSector().substring(0, 1))) {
                ExhibitorCategoryListing parentCategoryData1 = new ExhibitorCategoryListing();
                parentCategoryData1.setId(temparrayList.get(i).getId());
                parentCategoryData1.setImg(temparrayList.get(i).getImg());
                parentCategoryData1.setSector(temparrayList.get(i).getSector());
                parentCategoryData1.setShortDesc(temparrayList.get(i).getShortDesc());
                parentCategoryData1.setExhi_cat_group_id(temparrayList.get(i).getExhi_cat_group_id());

                parentCategoryData.add(parentCategoryData1);
            }
        }
        return parentCategoryData;
    }

    private void buttonSet() {
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(7.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

            btn_seeRequestExhibitor.setBackgroundDrawable(drawable);
            btn_seeRequestExhibitor.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            btn_Clearsearch.setBackgroundDrawable(drawable);
            btn_Clearsearch.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(7.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));

            btn_seeRequestExhibitor.setBackgroundDrawable(drawable);
            btn_seeRequestExhibitor.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

            btn_Clearsearch.setBackgroundDrawable(drawable);
            btn_Clearsearch.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }

    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 2, false, this);

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    private void getCategoryListing() {

        if (sessionManager.getShowSlider().equalsIgnoreCase("1")) {
            if (categoryListings.size() != 0) {
                rv_viewCategoryListing.setVisibility(View.VISIBLE);
                exhibitorCategoryListingAdapter = new ExhibitorCategoryListingAdapter(categoryListings, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                rv_viewCategoryListing.setLayoutManager(mLayoutManager);
                rv_viewCategoryListing.setItemAnimator(new DefaultItemAnimator());
                rv_viewCategoryListing.setAdapter(exhibitorCategoryListingAdapter);
            } else {
                rv_viewCategoryListing.setVisibility(View.GONE);
            }
            setCountryFilterVisibility(false);
            isSliderShow = true;
        } else {
            setCountryFilterVisibility(true);
            isSliderShow = false;
        }


        new getExhibitorAsyncTask(true).execute();
//        new getExhibitorInAsyncTask().execute();
        new updateDatabase().execute();
        //  getExhibitors();

    }

    private void getExhibitorFromChildProduct() {
        try {
            new getExhibitorFromChildProductAsyncTask().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            sections.clear();

//                            loadAttendeeData(jsonData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    pagewiseClick();
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
            case 5: {
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        FavoritedExhibitor favoritedExhibitor = gson.fromJson(jsonObject.toString(), FavoritedExhibitor.class);
                        new updateFav(favoritedExhibitor.getFavoriteExhibitors()).execute();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            }
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 10, false, this);
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
                sessionManager.footerView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(context, "1", MainLayout, relativeLayout_Data, Container_Attendance, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "1", MainLayout, relativeLayout_Data, Container_Attendance, topAdverViewArrayList, getActivity());
            }
//            sessionManager.footerView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, bottomAdverViewArrayList, getActivity());
//            sessionManager.HeaderView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, topAdverViewArrayList, getActivity());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.e("ExhibitorList", "onResume");
        super.onResume();
        Log.e("ExhibitorList", "onResume");
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Update_Profile));
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.exhibitorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.exhibitorModuleid);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSpeakerReloadedEventBus(SpeakerReloadedEventBus data) {
        getCategoryListing();
    }

    private void isSelected(int pos) {
        try {

            for (int i = 0; i < categoryListings.size(); i++) {
                if (i == pos) {
                    categoryListings.get(i).setSelected(true);
                } else {
                    categoryListings.get(i).setSelected(false);
                }
            }
            if (exhibitorCategoryListingAdapter != null)
                exhibitorCategoryListingAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExhibitorBySubCat(String shortDesc) {
        try {

            sections.clear();
            ArrayList<ExhibitorType> types = sqLiteDatabaseHandler.getExhibitorTypes(sessionManager.getEventId());
            for (int i = 0; i < types.size(); i++) {
                ArrayList<Attendance> attendanceArrayList = sqLiteDatabaseHandler.getExhibitorListBySubCatType(sessionManager.getExhibitorParentCategoryId(), sessionManager.getEventId(), sessionManager.getUserId(), shortDesc, types.get(i).getExhibitorId());

                if (attendanceArrayList.size() > 0) {
                    sections.add(new SectionHeader(attendanceArrayList, types.get(i).getExhibitorType(), types.get(i).getExhibitorTypeColor()));
                }
            }
            if (sections.size() > 0) {

                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapterRecycler = new AdapterSectionRecyclerNew(getActivity(), sections, true);
                recyclerView.setAdapter(adapterRecycler);
            } else {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.VISIBLE);
                textViewNoDATA.setText("No Exhibitors Found");
                recyclerView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCountryFilterVisibility(boolean isVisible) {
        if (isVisible) {
            filterLayout.setVisibility(View.VISIBLE);
            horizontalScrollView1.setVisibility(View.GONE);
//            horizontalScrollView1.setVisibility(View.VISIBLE);
        } else {
            filterLayout.setVisibility(View.GONE);
//            filterLayout.setVisibility(View.VISIBLE);
            horizontalScrollView1.setVisibility(View.VISIBLE);
        }
    }

    private void getFavoritedExhibitor() {
        try {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getFavouritedExhibitors, Param.getNotificationListing(sessionManager.getEventId(), sessionManager.getUserId()), 5, false, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                exhibitorCountries.clear();
                selectedCountries.clear();
                selectedCountries = (ArrayList<String>) data.getExtras().getSerializable("selected");
                exhibitorCountries = (ArrayList<ExhibitorCountry>) data.getExtras().getSerializable("myData");
                ArrayList<String> selectedName = (ArrayList<String>) data.getExtras().getSerializable("selectedName");
                setFilterText(txtSelectCountry, selectedName);

//                if (selectedCountries.size()!=0)
//                {
//                    selectedCategoryId.clear();
//                }
                getExhibitorFromChildProduct();
//                getExhibitorCountryProduct();
            } else if (requestCode == 2000) {
                sectionHeaderParentGroups.clear();
                selectedParentCat.clear();
                selectedCategoryId.clear();
                ArrayList<String> slectd = (ArrayList<String>) data.getExtras().getSerializable("selected");

                sectionHeaderParentGroups = (ArrayList<SectionHeaderParentGroup>) data.getExtras().getSerializable("myData");
                ArrayList<String> selectedName = (ArrayList<String>) data.getExtras().getSerializable("selectedName");
                ArrayList<String> selectedId = (ArrayList<String>) data.getExtras().getSerializable("categoryId");
                selectedCategoryId = (ArrayList<String>) data.getExtras().getSerializable("categoryId");


                for (int i = 0; i < slectd.size(); i++) {
                    String[] categoryKeywords = slectd.get(i).split(",");
                    selectedParentCat.addAll(new ArrayList<String>(Arrays.asList(categoryKeywords)));
                }
                setParentCatFilterText(txtSelectProduct, selectedName, selectedId);
                getExhibitorFromChildProduct();
            }
        }
    }

    public void setFilterText(TextView textView, ArrayList<String> list) {
        String s = list.toString();
        s = s.substring(1, s.length() - 1);
        textView.setText(s);
    }

    public void setParentCatFilterText(TextView textView, ArrayList<String> list, ArrayList<String> listCatId) {
        String s = list.toString();
        s = s.substring(1, s.length() - 1);
        textView.setText(s);

        String catId = listCatId.toString();
        catId = catId.substring(1, catId.length() - 1);
        pagewiseClickForCategory(catId);

    }

    private void pagewiseClickForCategory(String param) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.OtherPageWiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "EXHI_CAT", "", "", sessionManager.get_cmsId(), param), 6, false, this);
        }
    }

    public class getExhibitorAsyncTask extends AsyncTask<Void, Void, Boolean> {

        boolean isVisibleDialog = false;

        public getExhibitorAsyncTask(boolean isVisibleDialog) {
            this.isVisibleDialog = isVisibleDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            keyword = "";
            sections.clear();
            sessionManager.setIsLastCategoryName("");
            if (isVisibleDialog) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
//            boolean isSame = sqLiteDatabaseHandler.isSameExhibitorUser(sessionManager.getEventId(), sessionManager.getUserId());
//            if (!isSame)
//            {
            ArrayList<ExhibitorType> types = sqLiteDatabaseHandler.getExhibitorTypes(sessionManager.getEventId());
            for (int i = 0; i < types.size(); i++) {
                ArrayList<Attendance> attendanceArrayList = sqLiteDatabaseHandler.getExhibitorListBySubCat(sessionManager.getExhibitorParentCategoryId(), sessionManager.getEventId(), sessionManager.getUserId(), types.get(i).getExhibitorId());
                if (attendanceArrayList.size() > 0) {
                    sections.add(new SectionHeader(attendanceArrayList, types.get(i).getExhibitorType(), types.get(i).getExhibitorTypeColor()));
                }
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressBar.isShown())
                progressBar.setVisibility(View.GONE);
            if (sections.size() > 0) {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                btn_Clearsearch.setVisibility(View.VISIBLE);
                adapterRecycler = new AdapterSectionRecyclerNew(context, sections, false);
                recyclerView.setAdapter(adapterRecycler);
            } else {
                edt_search.setVisibility(View.VISIBLE);
                btn_Clearsearch.setVisibility(View.GONE);
                textViewNoDATA.setVisibility(View.GONE);
                textViewNoDATA.setText("No Exhibitors Found");
                recyclerView.setVisibility(View.GONE);
            }
//            buttonSet();
        }
    }

    public class getExhibitorFromChildProductAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            keyword = "";
            sections.clear();
            sessionManager.setIsLastCategoryName("");
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
//            boolean isSame = sqLiteDatabaseHandler.isSameExhibitorUser(sessionManager.getEventId(), sessionManager.getUserId());
//            if (!isSame)
//            {
            ArrayList<ExhibitorType> types = sqLiteDatabaseHandler.getExhibitorParentCateogryTypes(sessionManager.getEventId());
            ArrayList<Attendance> attendanceArrayList = sqLiteDatabaseHandler.getExhibitorFromChildProductFilterWholeFragment(sessionManager.getEventId(),
                    sessionManager.getUserId(), types, selectedCountries, selectedParentCat, selectedCategoryId, sessionManager.getExhibitorParentCategoryId());
            if (attendanceArrayList.size() > 0) {
                sections.add(new SectionHeader(attendanceArrayList, "", ""));
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressBar.setVisibility(View.GONE);
            if (sections.size() > 0) {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                adapterRecycler = new AdapterSectionRecyclerNew(getActivity(), sections, true);
                recyclerView.setAdapter(adapterRecycler);
            } else {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                textViewNoDATA.setText("No Exhibitors Found");
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    public class updateDatabase extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
//            boolean isSame = sqLiteDatabaseHandler.isSameExhibitorUser(sessionManager.getEventId(), sessionManager.getUserId());
//            if (!isSame)
//            {
            sqLiteDatabaseHandler.updateExhibitorUserID(sessionManager.getEventId(), sessionManager.getUserId());
//            }
            if (sessionManager.isLogin()) {
                //if online get favorite data and sync
                getFavoritedExhibitor();
            }
            return true;
        }
    }

    public class updateFav extends AsyncTask<Void, Void, Void> {
        ArrayList<FavoritedExhibitor.FavoriteExhibitor> exhibitors;

        public updateFav(ArrayList<FavoritedExhibitor.FavoriteExhibitor> exhibitors) {
            this.exhibitors = exhibitors;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            sqLiteDatabaseHandler.updateExhibitorFav(sessionManager.getEventId(), sessionManager.getUserId(), exhibitors);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            new getExhibitorInAsyncTask().execute();
            //  getExhibitors();
            new getExhibitorAsyncTask(false).execute();
            super.onPostExecute(aVoid);
        }
    }

    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String time1 = "", time2 = "";
            Date date1, date2;
            if (o1 instanceof ExhibitorCategoryListing) {
                time1 = ((ExhibitorCategoryListing) o1).getSector();
            }
            if (o2 instanceof ExhibitorCategoryListing) {
                time2 = ((ExhibitorCategoryListing) o2).getSector();
            }
            return time1.compareTo(time2);
        }
    }

}