package com.allintheloop.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.GamificationLeaderBord_Adapter;
import com.allintheloop.Adapter.GamificationPointListAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.GamificationLeaderBoardList;
import com.allintheloop.Bean.GamificationPointListing;
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

import static com.allintheloop.Util.GlobalData.updateGamticationModule;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamiiFication_Fragment extends Fragment implements VolleyInterface {


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    TextView txt_earnPointNow;
    WebView txt_leaderBoard;
    RecyclerView recycler_leaderBoradView, recycler_pointView;
    LinearLayout linear_leaderBoard;
    SessionManager sessionManager;
    ArrayList<GamificationLeaderBoardList> leaderBoardLists;
    ArrayList<GamificationPointListing> pointListings;
    GamificationLeaderBord_Adapter gamificationLeaderBord_adapter;
    GamificationPointListAdapter pointListAdapter;
    String myHtmlString, WebStr;
    ImageView image1, imaga2;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ImageView btn_refresh;
    LinearLayout layout_refresh;
    RotateAnimation rotate;
    DefaultLanguage.DefaultLang defaultLang;
    RelativeLayout MainLayout, relativeLayout_static;
    LinearLayout linear_content;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    private BroadcastReceiver updateData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getListData();
        }
    };


    public GamiiFication_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gaminification, container, false);
        txt_leaderBoard = (WebView) rootView.findViewById(R.id.txt_leaderBoard);
        txt_earnPointNow = (TextView) rootView.findViewById(R.id.txt_earnPointNow);
        recycler_leaderBoradView = (RecyclerView) rootView.findViewById(R.id.recycler_leaderBoradView);
        recycler_pointView = (RecyclerView) rootView.findViewById(R.id.recycler_pointView);
        linear_leaderBoard = (LinearLayout) rootView.findViewById(R.id.linear_leaderBoard);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        relativeLayout_static = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_static);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        btn_refresh = (ImageView) rootView.findViewById(R.id.btn_refresh);
        layout_refresh = (LinearLayout) rootView.findViewById(R.id.layout_refresh);
        image1 = (ImageView) rootView.findViewById(R.id.image1);
        imaga2 = (ImageView) rootView.findViewById(R.id.imaga2);
        leaderBoardLists = new ArrayList<>();
        pointListings = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        txt_earnPointNow.setText(defaultLang.get52EarnPointsNow());

        rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatCount(Animation.INFINITE);


        recycler_leaderBoradView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GamificationLeaderBoardList boardListObj = leaderBoardLists.get(position);
                if (boardListObj.getRole_id().equalsIgnoreCase("4")) {//changes applied
                    SessionManager.AttenDeeId = boardListObj.getId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Attendance_Detail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (boardListObj.getRole_id().equalsIgnoreCase("6")) {//changes applied
                    sessionManager.exhibitor_id = boardListObj.getId();

                    if (GlobalData.checkForUIDVersion())
                        sessionManager.exhi_pageId = boardListObj.getExhibitor_page_id();
                    else
                        sessionManager.exhi_pageId = boardListObj.getId();

                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Exhibitor_Detail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else if (boardListObj.getRole_id().equalsIgnoreCase("7")) {//changes applied
                    SessionManager.speaker_id = boardListObj.getId();
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.Speaker_Detail_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                }

            }
        }));


        layout_refresh.setOnClickListener(view -> {
            getListData();
            btn_refresh.startAnimation(rotate);
        });

        txt_leaderBoard.getSettings().setJavaScriptEnabled(true);
        txt_leaderBoard.getSettings().setAllowContentAccess(true);
        txt_leaderBoard.getSettings().setUseWideViewPort(true);
        txt_leaderBoard.getSettings().setLoadWithOverviewMode(true);


        getListData();

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            txt_earnPointNow.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
        } else {
            txt_earnPointNow.setTextColor(Color.parseColor(sessionManager.getTopBackColor()));
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (GlobalData.isNetworkAvailable(getActivity())) {
//                    messageArrayList.clear();
                    getListData();
                } else {
                    ToastC.show(getActivity(), "No Internet Connection");
                }
            }
        });


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

            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1")) {
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_static, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_static, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_static, linear_content, topAdverViewArrayList, getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getListData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getGamificationDataUid, Param.getGamificationData(sessionManager.getEventId()), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getGamificationData, Param.getGamificationData(sessionManager.getEventId()), 0, false, this);

        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        leaderBoardLists.clear();
                        pointListings.clear();

                        btn_refresh.clearAnimation();
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONObject jsonImage = jsonData.getJSONObject("image");

                        Glide.with(getActivity()).load(jsonImage.getString("L")).into(image1);
                        Glide.with(getActivity()).load(jsonImage.getString("R")).into(imaga2);

                        JSONArray jsonArray = jsonData.getJSONArray("top_five");
                        JSONArray JsonPointListing = jsonData.getJSONArray("info");
                        WebStr = jsonData.getString("header");

                        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: center;}</style></head><body>";
                        String pas = "</body></html>";
                        String myHtmlString = pish + WebStr + pas;
                        txt_leaderBoard.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);

                        if (GlobalData.checkForUIDVersion()) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject index = jsonArray.getJSONObject(i);
                                leaderBoardLists.add(new GamificationLeaderBoardList(index.getString("id")
                                        , index.getString("Sender_name")
                                        , index.getString("Company_name")
                                        , index.getString("Title")
                                        , index.getString("logo")
                                        , index.getString("total")
                                        , index.getString("Role_id")
                                        , index.getString("color")
                                        , index.getString("rank")
                                        , index.getString("exhibitor_page_id")));
                            }
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject index = jsonArray.getJSONObject(i);
                                leaderBoardLists.add(new GamificationLeaderBoardList(index.getString("id")
                                        , index.getString("Sender_name")
                                        , index.getString("Company_name")
                                        , index.getString("Title")
                                        , index.getString("logo")
                                        , index.getString("total")
                                        , index.getString("Role_id")
                                        , index.getString("color")
                                        , index.getString("rank")));
                            }
                        }

                        for (int i = 0; i < JsonPointListing.length(); i++) {
                            JSONObject index1 = JsonPointListing.getJSONObject(i);
                            pointListings.add(new GamificationPointListing(index1.getString("point")
                                    , index1.getString("rank")));
                        }

                        if (pointListings.size() == 0) {
                            linear_leaderBoard.setVisibility(View.GONE);
                        } else {
                            linear_leaderBoard.setVisibility(View.VISIBLE);
                            pointListAdapter = new GamificationPointListAdapter(pointListings, getActivity());
                            recycler_pointView.setItemAnimator(new DefaultItemAnimator());
                            recycler_pointView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recycler_pointView.setAdapter(pointListAdapter);
                        }

                        if (leaderBoardLists.size() == 0) {
                            recycler_leaderBoradView.setVisibility(View.GONE);
                        } else {
                            recycler_leaderBoradView.setVisibility(View.VISIBLE);
                            gamificationLeaderBord_adapter = new GamificationLeaderBord_Adapter(leaderBoardLists, getActivity());
                            recycler_leaderBoradView.setItemAnimator(new DefaultItemAnimator());
                            recycler_leaderBoradView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recycler_leaderBoradView.setAdapter(gamificationLeaderBord_adapter);
                        }

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

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateData);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(updateData, new IntentFilter(updateGamticationModule));
    }
}
