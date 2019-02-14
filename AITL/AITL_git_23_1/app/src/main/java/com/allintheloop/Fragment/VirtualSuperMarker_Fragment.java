package com.allintheloop.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.VirtualSupermarketAdapter;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorListdata;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.EndlessScrollListener;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VirtualSuperMarker_Fragment extends Fragment implements VolleyInterface {


    EditText edt_search;
    RecyclerView recyclerView;
    TextView textViewNoDATA;
    ArrayList<ExhibitorListdata> sectionHeaders;
    ArrayList<ExhibitorListdata> tmpSectionHeaders;

    ArrayList<Attendance> attendanceArrayList;
    ArrayList<Attendance> tmpattendanceArrayList;

    VirtualSupermarketAdapter attendanceAdapter;
    String ex_id, ex_pageId, ex_heading, ex_desc, ex_images, ex_comapnyLogo, ex_websiteUrl, ex_facebookUrl, ex_twitterUrl, ex_linkinUrl, ex_phoneNumber, ex_emailId, ex_stand_number;
    RelativeLayout reltive_edtsearch;

    NestedScrollView scrollView;

    boolean isLoading;
    Handler handler;
    ProgressBar progressBar2;
    int total_pages, page_count = 1;
    int search_page_count = 1, search_total_pages;

    WrapContentLinearLayoutManager linearLayoutManager;
    SessionManager sessionManager;
    String tag_search = "0";
    String keyword = "";
    ImageView img_scan;
    String res_email = "", banner = "";
    ImageView img_banner;

    public VirtualSuperMarker_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_virtual_super_marker, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_viewAttendance);

        reltive_edtsearch = (RelativeLayout) rootView.findViewById(R.id.reltive_edtsearch);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        img_scan = (ImageView) rootView.findViewById(R.id.img_scan);
        img_banner = (ImageView) rootView.findViewById(R.id.img_banner);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        sessionManager = new SessionManager(getActivity());
        progressBar2 = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        sectionHeaders = new ArrayList<>();
        attendanceArrayList = new ArrayList<>();

        recyclerView.setNestedScrollingEnabled(false);
        handler = new Handler();


        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openScanActivity();
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (GlobalData.isNetworkAvailable(getActivity())) {
                    if (s.toString().trim().length() > 0) {
                        Log.d("AITL STRING", s.toString());
                        keyword = s.toString();
//                        Log.d("BVhavdip KEYWORD",keyword);
                        search_page_count = 1;
                        getExhibitorByKeyword(s.toString());
                        // GulfoodSearch();
//                        GulfoodSearch();
                    } else {
                        page_count = 1;
                        getList();
                        sessionManager.keyboradHidden(edt_search);
                    }
                } else {
                    try {
                        Log.d("AITL ARRAYLIST SIZE", "" + sectionHeaders.size());

                        tmpSectionHeaders = new ArrayList<>();
                        for (int k = 0; k < sectionHeaders.size(); k++) {
                            tmpattendanceArrayList = new ArrayList<>();
                            Log.d("AITL EXHIBITOR SIZE", "DATA" + sectionHeaders.get(k).getExhibitorList().size());
                            for (int j = 0; j < sectionHeaders.get(k).getExhibitorList().size(); j++) {

                                if (sectionHeaders.get(k).getExhibitorList().get(j).getEx_heading().toLowerCase().contains(s.toString().toLowerCase()))
                                    tmpattendanceArrayList.add(sectionHeaders.get(k).getExhibitorList().get(j));
                            }

                            if (tmpattendanceArrayList.size() != 0) {
                                tmpSectionHeaders.add(
                                        new ExhibitorListdata(
                                                sectionHeaders.get(k).getType(),
                                                sectionHeaders.get(k).getBg_color(),
                                                tmpattendanceArrayList));
                            }
                        }

                        if (tmpSectionHeaders.size() == 0) {

                            textViewNoDATA.setText("No Exhibitor Found");
                            textViewNoDATA.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {

                            textViewNoDATA.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            attendanceAdapter = new VirtualSupermarketAdapter(tmpSectionHeaders, recyclerView, linearLayoutManager, getActivity(), getActivity(), scrollView);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(attendanceAdapter);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
            getList();
            pagewiseClick();
//            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 0, false, this);
        }

        return rootView;
    }


    private void openScanActivity() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    private void getExhibitorByKeyword(String s) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getVirtualMarketList, Param.getVirtualMarketList(sessionManager.getEventId(), s.toString(), search_page_count), 3, false, this);
        }
    }

    private void buttonLoadGulfoodSearch() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getVirtualMarketList, Param.getVirtualMarketList(sessionManager.getEventId(), keyword, search_page_count), 4, false, this);
        }
    }

    private void getButtonList() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getVirtualMarketList, Param.getVirtualMarketList(sessionManager.getEventId(), "", page_count), 2, false, this);
    }


    private void getList() {

        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getVirtualMarketList, Param.getVirtualMarketList(sessionManager.getEventId(), "", page_count), 1, false, this);

        } else {

            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 5, false, this);
        }
    }


    private void getExhibitorProfile() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getVirtualMarketExhibitorDetail, Param.getVirtualMarketDetail(sessionManager.getEventId(), res_email), 5, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void loadAttendeeData(JSONArray jsonData) {
        try {
            // sectionHeaders = new ArrayList<>();
            //tmpattendanceArrayList = new ArrayList<>();

            if (!isLoading) {
                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject index = jsonData.getJSONObject(i);
                    JSONArray jArrayData = index.getJSONArray("data");
                    attendanceArrayList = new ArrayList<>();
                    for (int k = 0; k < jArrayData.length(); k++) {
                        JSONObject object = jArrayData.getJSONObject(k);
                        ex_id = object.getString("exhibitor_id");
                        ex_pageId = object.getString("exhibitor_page_id");
                        ex_heading = object.getString("Heading");
                        ex_desc = object.getString("Short_desc");
                        ex_images = object.getString("Images");

                        ex_comapnyLogo = object.getString("company_logo");
                        ex_websiteUrl = object.getString("website_url");
                        ex_facebookUrl = object.getString("facebook_url");
                        ex_linkinUrl = object.getString("linkedin_url");
                        ex_twitterUrl = object.getString("twitter_url");
                        ex_phoneNumber = object.getString("phone_number1");
                        ex_stand_number = object.getString("stand_number");
                        ex_emailId = object.getString("email_address");
                        attendanceArrayList.add(new Attendance(ex_id, ex_pageId, ex_heading, ex_desc, ex_images, ex_comapnyLogo, ex_websiteUrl, ex_facebookUrl, ex_twitterUrl, ex_linkinUrl, ex_phoneNumber, ex_emailId, getTag(), ex_stand_number, "0"));
                    }
                    sectionHeaders.add(new ExhibitorListdata(index.getString("type"), index.getString("bg_color"), attendanceArrayList));
                    //arrayListHashMap.put(sectionHeaders.get(i), attendanceArrayList);
                }
            } else {
                ArrayList<ExhibitorListdata> tmp_sectionHeaders = new ArrayList<>();
                for (int i = 0; i < jsonData.length(); i++) {
                    JSONObject index = jsonData.getJSONObject(i);
                    JSONArray jArrayData = index.getJSONArray("data");
                    attendanceArrayList = new ArrayList<>();
                    for (int k = 0; k < jArrayData.length(); k++) {
                        JSONObject object = jArrayData.getJSONObject(k);
                        ex_id = object.getString("exhibitor_id");
                        ex_pageId = object.getString("exhibitor_page_id");
                        ex_heading = object.getString("Heading");
                        ex_desc = object.getString("Short_desc");
                        ex_images = object.getString("Images");

                        ex_comapnyLogo = object.getString("company_logo");
                        ex_websiteUrl = object.getString("website_url");
                        ex_facebookUrl = object.getString("facebook_url");
                        ex_linkinUrl = object.getString("linkedin_url");
                        ex_twitterUrl = object.getString("twitter_url");
                        ex_phoneNumber = object.getString("phone_number1");
                        ex_stand_number = object.getString("stand_number");
                        ex_emailId = object.getString("email_address");
                        attendanceArrayList.add(new Attendance(ex_id, ex_pageId, ex_heading, ex_desc, ex_images, ex_comapnyLogo, ex_websiteUrl, ex_facebookUrl, ex_twitterUrl, ex_linkinUrl, ex_phoneNumber, ex_emailId, getTag(), ex_stand_number, "0"));
                    }

                    tmp_sectionHeaders.add(new ExhibitorListdata(index.getString("type"), index.getString("bg_color"), attendanceArrayList));
                    //arrayListHashMap.put(sectionHeaders.get(i), attendanceArrayList);
                }
                sectionHeaders.addAll(tmp_sectionHeaders);
            }
            set_recycler();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void set_recycler() {

        if (tag_search.equalsIgnoreCase("0")) {
            Log.d("AITL TAG", tag_search);
            try {
                if (!isLoading) {
                    if (sectionHeaders.size() == 0) {
                        Log.d("AITL IF EMPTY", "NOT LOADING");
                        reltive_edtsearch.setVisibility(View.VISIBLE);
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        textViewNoDATA.setText("Tap the scan button above and scan a product’s QR code to learn more");
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        Log.d("AITL FULL", "DATA");

                        textViewNoDATA.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        attendanceAdapter = new VirtualSupermarketAdapter(sectionHeaders, recyclerView, linearLayoutManager, getActivity(), getActivity(), scrollView);
                        recyclerView.setAdapter(attendanceAdapter);
                    }
                } else {
                    Log.d("AITL EMPTY", "NOT LOADING");
                    attendanceAdapter = new VirtualSupermarketAdapter(sectionHeaders, recyclerView, linearLayoutManager, getActivity(), getActivity(), scrollView);
                    recyclerView.setAdapter(attendanceAdapter);
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
                                    progressBar2.setVisibility(View.VISIBLE);
                                    handler.postDelayed(new Runnable() {

                                        @Override
                                        public void run() {
//                                        message_adapter.removeFooter();
                                            progressBar2.setVisibility(View.GONE);
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
                        Log.d("AITL IF EMPTY", "NOT LOADING");
                        reltive_edtsearch.setVisibility(View.VISIBLE);
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        textViewNoDATA.setText("Tap the scan button above and scan a product’s QR code to learn more");
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        Log.d("AITL FULL", "DATA");

                        textViewNoDATA.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        attendanceAdapter = new VirtualSupermarketAdapter(sectionHeaders, recyclerView, linearLayoutManager, getActivity(), getActivity(), scrollView);
                        recyclerView.setAdapter(attendanceAdapter);
                    }
                } else {
                    Log.d("AITL EMPTY", "NOT LOADING");
                    attendanceAdapter = new VirtualSupermarketAdapter(sectionHeaders, recyclerView, linearLayoutManager, getActivity(), getActivity(), scrollView);
                    recyclerView.setAdapter(attendanceAdapter);
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
                                progressBar2.setVisibility(View.VISIBLE);
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
//                                        message_adapter.removeFooter();
                                        progressBar2.setVisibility(View.GONE);
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


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {


            case 0:
                try {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {

                            banner = jsonObject.getString("banner");

                            Log.d("AITL BANNER", banner);
                            Glide.with(getActivity()).load(banner).into(img_banner);
                            sectionHeaders.clear();
                            attendanceArrayList.clear();
                            tag_search = "0";
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            total_pages = jsonObject.getInt("total_pages");
                            loadAttendeeData(jsonData);

                        } else {
//                            ToastC.show(getActivity(), "No Attendance Found");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            loadAttendeeData(jsonData);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            sectionHeaders.clear();
                            attendanceArrayList.clear();
                            search_total_pages = jsonObject.getInt("total_pages");
                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            tag_search = "1";
                            loadAttendeeData(jsonData);

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

                            JSONArray jsonData = jsonObject.getJSONArray("data");
                            loadAttendeeData(jsonData);

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
                        //JSONObject jsonObject1=jsonObject.getJSONObject("data");
                        sessionManager.cms_id(jsonObject.getString("data"));
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                        ((MainActivity) getActivity()).loadFragment();

//                        sessionManager.exhibitor_id = jsonObject1.getString("exhibitor_id");
//                        sessionManager.exhi_pageId = jsonObject1.getString("exhibitor_page_id");
//                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//                        GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
//                        ((MainActivity)getActivity()).loadFragment();
//                        Log.d("AITL DETAIL",jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
            } else {
                res_email = result.getContents();
                getExhibitorProfile();
//                Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
