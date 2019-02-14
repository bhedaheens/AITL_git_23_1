package com.allintheloop.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.allintheloop.Adapter.MatchMaking.MatchMakingListAdapter;
import com.allintheloop.Bean.MatchMaking.MatchMakingListingData;
import com.allintheloop.Bean.MatchMaking.MatchMakingModuleNameData;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchMakingAllList_Fragment extends Fragment implements VolleyInterface {


    ArrayList<String> titleName;
    ArrayList<MatchMakingModuleNameData.ModulesName> modulesNameArrayList;
    ArrayList<MatchMakingListingData> matchMakingListingData;
    int position;
    SessionManager sessionManager;
    int page_count = 1, total_page = 0;
    TextView textViewNoDATA;
    RecyclerView rv_viewData;
    String menuId = "";
    String strTitle = "", strSubTitle = "", strLogo = "", strId = "", strExhiPageId = "", strModuleId = "", strModuleName = "";
    MatchMakingListAdapter matchMakingListAdapter;

    boolean isLoadMore = true, loading = false;
    int totalitemcount, lastVisibleItempagecount;
    LinearLayoutManager layoutManager;
    EditText edt_search;
    String keyword = "";
    SwipeRefreshLayout swipeRefreshLayout;
    TextView txt_clear;

    public MatchMakingAllList_Fragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(ArrayList<MatchMakingModuleNameData.ModulesName> modulesNameArrayList, ArrayList<String> stringArrayList, int pos) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("title", stringArrayList);
        bundle.putSerializable("moduleData", modulesNameArrayList);
        bundle.putInt("pos", pos);
        MatchMakingAllList_Fragment makingAllList_fragment = new MatchMakingAllList_Fragment();
        makingAllList_fragment.setArguments(bundle);
        return makingAllList_fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleName = getArguments().getStringArrayList("title");
        modulesNameArrayList = (ArrayList<MatchMakingModuleNameData.ModulesName>) getArguments().getSerializable("moduleData");
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_match_making_all_list_, container, false);
        initView(rootView);
        getData(false, false);
        return rootView;
    }

    private void initView(View rootView) {
        sessionManager = new SessionManager(getActivity());
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        txt_clear = (TextView) rootView.findViewById(R.id.txt_clear);
        rv_viewData = (RecyclerView) rootView.findViewById(R.id.rv_viewData);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        swiperTorefresh();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    swiperTorefresh();
                    getData(false, false);
                } else {
                    ToastC.show(getActivity(), getString(R.string.noInernet));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (GlobalData.isNetworkAvailable(getActivity())) {

                        if (edt_search.getText().length() > 0) {
                            keyword = edt_search.getText().toString();
                            getData(false, true);
                            sessionManager.keyboradHidden(edt_search);
                        } else {
                            swiperTorefresh();
                            getData(false, true);
                            sessionManager.keyboradHidden(edt_search);
                        }
                    }
                    return true;
                }
                return false;
            }

        });


        txt_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    sessionManager.keyboradHidden(edt_search);
                    swiperTorefresh();
                    Log.d("AITL ClearDataText", keyword);
                    getData(false, true);
                }
            }
        });

        rv_viewData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalitemcount = layoutManager.getItemCount();
                lastVisibleItempagecount = layoutManager.findLastVisibleItemPosition();
                if (!loading && totalitemcount <= (lastVisibleItempagecount + 5) && isLoadMore) {

                    loading = true;
                    if (total_page > page_count) {
                        page_count = page_count + 1;
                        getData(true, false);
                    } else {
                        isLoadMore = false;
                    }

                }
            }
        });

    }


    private void swiperTorefresh() {
        layoutManager = new LinearLayoutManager(getActivity());
        matchMakingListingData = new ArrayList<>();
        rv_viewData.setLayoutManager(layoutManager);
        rv_viewData.setItemAnimator(new DefaultItemAnimator());
        matchMakingListAdapter = new MatchMakingListAdapter(getActivity(), matchMakingListingData, sessionManager);
        rv_viewData.setAdapter(matchMakingListAdapter);
        edt_search.setText("");
        keyword = "";
    }

    private void getData(boolean isLoad, boolean isLoder) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            String url = "";
            if (!isLoad) {
                page_count = 1;
                matchMakingListingData = new ArrayList<>();
            }
            menuId = modulesNameArrayList.get(position).getId();
            if (menuId.equalsIgnoreCase("2")) {
                url = MyUrls.getMathcMakingModuleAttendeeData;
            } else if (menuId.equalsIgnoreCase("3")) {
                url = MyUrls.getMathcMakingModuleExhibitorData;
            } else if (menuId.equalsIgnoreCase("7")) {
                url = MyUrls.getMathcMakingModuleSpeakerData;
            } else if (menuId.equalsIgnoreCase("43")) {
                url = MyUrls.getMathcMakingModuleSponsorData;
            }

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, url, Param.getMatchMakingAllData(
                    sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getRolId(),
                    page_count, keyword), 0, isLoder, this);//postponed - pending
        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        total_page = jsonObject.getInt("total_page");
                        loadData(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void loadData(JSONObject jsonObject) {
        try {
            ArrayList<MatchMakingListingData> matchMakingListingData_temp = new ArrayList<>();
            if (menuId.equalsIgnoreCase("2")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() != 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject index = jsonArray.getJSONObject(i);
                        strTitle = index.getString("Firstname") + " " + index.getString("Lastname");
                        if (!(index.getString("Title").equalsIgnoreCase(""))) {
                            if (!(index.getString("Company_name").equalsIgnoreCase(""))) {
                                strSubTitle = index.getString("Title") + " " + "at" + " " + index.getString("Company_name");
                            } else {
                                strSubTitle = index.getString("Title");
                            }
                        } else {
                            strSubTitle = "";
                        }
                        strLogo = index.getString("Logo");
                        strId = index.getString("Id");
                        strModuleId = menuId;
                        matchMakingListingData_temp.add(new MatchMakingListingData(strTitle, strSubTitle, strLogo, strId, "", strModuleId, ""));
                    }
                }
            } else if (menuId.equalsIgnoreCase("3")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() != 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject index = jsonArray.getJSONObject(i);
                        strTitle = index.getString("Heading");
                        strSubTitle = index.getString("stand_number");
                        strLogo = index.getString("company_logo");
                        strId = index.getString("exhibitor_id");
                        strExhiPageId = index.getString("exhibitor_page_id");
                        strModuleId = menuId;
                        matchMakingListingData_temp.add(new MatchMakingListingData(strTitle, strSubTitle, strLogo, strId, strExhiPageId, strModuleId, ""));
                    }
                }
            } else if (menuId.equalsIgnoreCase("7")) {

                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() != 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject index = jsonArray.getJSONObject(i);
                        strTitle = index.getString("Firstname") + " " + index.getString("Lastname");
                        if (!(index.getString("Title").equalsIgnoreCase(""))) {
                            if (!(index.getString("Company_name").equalsIgnoreCase(""))) {
                                strSubTitle = index.getString("Title") + " " + "at" + " " + index.getString("Company_name");
                            } else {
                                strSubTitle = index.getString("Title");
                            }
                        } else {
                            strSubTitle = "";
                        }
                        strLogo = index.getString("Logo");
                        strId = index.getString("Id");
                        strModuleId = menuId;
                        matchMakingListingData_temp.add(new MatchMakingListingData(strTitle, strSubTitle, strLogo, strId, "", strModuleId, ""));
                    }
                }

            } else if (menuId.equalsIgnoreCase("43")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray.length() != 0) {

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject index = jsonArray.getJSONObject(i);
                        strTitle = index.getString("Sponsors_name");
                        strSubTitle = index.getString("Company_name");
                        strLogo = index.getString("company_logo");
                        strId = index.getString("Id");
                        strExhiPageId = "";
                        strModuleId = menuId;
                        matchMakingListingData_temp.add(new MatchMakingListingData(strTitle, strSubTitle, strLogo, strId, strExhiPageId, strModuleId, ""));
                    }
                }
            }

            if (page_count != 1) {
                if (matchMakingListingData.size() != 0) {
                    matchMakingListingData.remove(matchMakingListingData.size() - 1);
                }
            }
            if (total_page > 1) {
                if (page_count != total_page) {
                    if (matchMakingListingData_temp.size() != 0) {
                        matchMakingListingData_temp.add(null);
                    }
                }
            }
            if (matchMakingListingData_temp.size() != 0) {
                matchMakingListingData.addAll(matchMakingListingData_temp);
            }

            if (matchMakingListingData.size() != 0) {
                rv_viewData.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);

            } else {
                rv_viewData.setVisibility(View.GONE);
                textViewNoDATA.setVisibility(View.VISIBLE);
            }
            new setListview().execute();
            loading = false;
        } catch (Exception e) {
            e.printStackTrace();
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
            matchMakingListAdapter.updateList(matchMakingListingData);
        }
    }

}
