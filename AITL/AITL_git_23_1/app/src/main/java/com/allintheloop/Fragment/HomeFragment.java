package com.allintheloop.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.DashBoardItemListAdapter;
import com.allintheloop.Adapter.HomeBaneerImageAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.HomeData.BannerImageData;
import com.allintheloop.Bean.HomeData.DashboardIdArray;
import com.allintheloop.Bean.HomeData.DashboardItemList;
import com.allintheloop.Bean.HomePageDynamicImageArray;
import com.allintheloop.Bean.HomeScreenMapDetailData;
import com.allintheloop.Bean.Map.MapCoordinatesDetails;
import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Fragment.EditProfileModule.Attendee_ProfileEdit_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.HomeCustomViewPager;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.LAYER_TYPE_SOFTWARE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements VolleyInterface {

    RecyclerView recyclerView;
    DashBoardItemListAdapter adapter;
    NestedScrollView scrollViewHome;
    WebView webView;
    String WebStr = "", ImgStr, pTitle, imgView_status, pMId, backImg, menuBackImg, notes_status = "", top = "", left = "", right = "", bottom = "", Eventid = "", Token_id = "";
    ImageView recycle_imgView;
    FrameLayout home_linear;
    SessionManager sessionManager;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    HomeCustomViewPager home_viewPager;
    HomeBaneerImageAdapter homeImagePagerAdpater;

    Timer homeswipeTimer;
    int currentPage = 0;
    int NUM_PAGES = 0;
    Paint paint;


    ArrayList<BannerImageData> detailImagesObj;
    ArrayList<DashboardItemList> ArrayItem;
    ArrayList<DashboardIdArray> idArrays;
    ArrayList<HomePageDynamicImageArray> listImageArray;
    ArrayList<HomeScreenMapDetailData> listImageDetailArray;
    ArrayList<List<MapCoordinatesDetails>> rectanglesList = new ArrayList<>();
    ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    RelativeLayout MainLayout, relative_staticHome, layout;
    LinearLayout linear_content, linear_webView, linear_dynamicHome;
    String type = "0";
    UidCommonKeyClass uidCommonKeyClass;
    BroadcastReceiver deepLinkBroadCastRecevier = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
    private Runnable runnableCode = null;
    private Handler handler = new Handler();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        sessionManager = new SessionManager(getActivity());
        home_linear = (FrameLayout) rootView.findViewById(R.id.home_linear);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        Eventid = sessionManager.getEventId();
        Token_id = sessionManager.getToken();

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(true);

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        sessionManager.strMenuId = "0";
        sessionManager.strModuleId = "Home";

        idArrays = new ArrayList<>();
        detailImagesObj = new ArrayList<>();
        listImageArray = new ArrayList<>();
        listImageDetailArray = new ArrayList<>();
        layout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_viewMenu);
        linear_dynamicHome = (LinearLayout) rootView.findViewById(R.id.linear_dynamicHome);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        scrollViewHome = (NestedScrollView) rootView.findViewById(R.id.scrollViewHome);
        relative_staticHome = (RelativeLayout) rootView.findViewById(R.id.relative_staticHome);
        home_viewPager = (HomeCustomViewPager) rootView.findViewById(R.id.home_viewPager);
        webView = (WebView) rootView.findViewById(R.id.webViewContent);
        linear_webView = (LinearLayout) rootView.findViewById(R.id.linear_webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
        recycle_imgView = (ImageView) rootView.findViewById(R.id.recycle_imgView);
        ((MainActivity) getActivity()).updateProfilePic();


        if (sessionManager.getEventType().equalsIgnoreCase("4") || sessionManager.getEventType().equalsIgnoreCase("1")) {

            if (GlobalData.checkForUIDVersion()) {
                uidCommonKeyClass = sessionManager.getUidCommonKey();
                if (sessionManager.getFormStatus().equalsIgnoreCase("1") && uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Attendee_ProfileEdit_Fragment fragment = new Attendee_ProfileEdit_Fragment();
                    fragment.show(fm, "DialogFragment");
                }
            } else {
                if (sessionManager.getFormStatus().equalsIgnoreCase("1") && sessionManager.getRolId().equalsIgnoreCase("4")) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    Attendee_ProfileEdit_Fragment fragment = new Attendee_ProfileEdit_Fragment();
                    fragment.show(fm, "DialogFragment");
                }
            }

        }
        ArrayItem = new ArrayList<>();
        recyclerClick();

        notificationTransction();
        getHomeData();
        getAdvertiesment();

        if (sessionManager.isLogin())
            pagewiseClick();
        return rootView;
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), "0"), 5, false, this);

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), "0");
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

    private void recyclerClick() {
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DashboardItemList dashboardItemListObj = ArrayItem.get(position);
                String menuType = dashboardItemListObj.getIsCmS();

                if (menuType.equalsIgnoreCase("0")) {

                    sessionManager.strMenuId = dashboardItemListObj.getpMId();
                    sessionManager.strModuleId = "0";

                    sessionManager.setMenuid(dashboardItemListObj.getpMId());
                    sessionManager.set_isForceLogin(dashboardItemListObj.getIsforceLogin());

                    if (dashboardItemListObj.getpMId().equalsIgnoreCase("1")) {
                        if (dashboardItemListObj.getCategoryId().isEmpty()) {
                            ((MainActivity) getActivity()).isAgendaGroupAndCategoryExist();
                        } else {
                            sessionManager.setAgendaCategoryId(dashboardItemListObj.getCategoryId());
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("3")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();

                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("2")) {
                        ((MainActivity) getActivity()).attendeeRedirection(false, false);
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("7")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();

                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("6")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                        ((MainActivity) getActivity()).loadFragment();

                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("10")) {

                        ((MainActivity) getActivity()).isMapGroupExist();

                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("17")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("12")) {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("9"))  // Presantation
                    {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("11")) // Photos
                    {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("13")) // Message Wall
                    {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("15")) // Survey
                    {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("16")) // Document
                    {

                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("22")) // Silent Aucation
                    {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("43")) {

                        ((MainActivity) getActivity()).isSponsorGroupExist();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("44")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("24")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("23")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("45")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("46")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("20"))  // Fundraising
                    {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("25")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("57")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.QrcodeScannerSharecontactFragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("59")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.MatchMakingFragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("47")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("50"))  // Fundraising
                    {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("49")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("52")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("53")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("54")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (dashboardItemListObj.getpMId().equalsIgnoreCase("21")) {
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.CMSGroupListFragment;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                    else if (dashboardItemListObj.getpMId().equalsIgnoreCase("56")) {
                        if (GlobalData.checkForUIDVersion())
                        {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                        else
                        {
                            if (sessionManager.getRolId().equalsIgnoreCase("4")) {//postponed - pending
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            }
                            else if (sessionManager.getRolId().equalsIgnoreCase("6"))
                            {//postponed - pending
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            }
                            else if (sessionManager.getRolId().equalsIgnoreCase("7") && //changes applied
                                    sessionManager.isModerater().equalsIgnoreCase("1")) {//changes applied
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                                ((MainActivity) getActivity()).loadFragment();

                            }
                        }
                    }


                } else if (menuType.equalsIgnoreCase("1")) {

                    for (int id = 0; id < idArrays.size(); id++) {

                        DashboardIdArray idObj = idArrays.get(id);
                        if (idObj.getId().equals(dashboardItemListObj.getpMId())) {

                            sessionManager.cms_id(idObj.getId().toString());
                            sessionManager.strMenuId = idObj.getId().toString();
                            sessionManager.strModuleId = "0";
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    }
                }
            }
        }));
    }

    private void getHomeData() {

        if (GlobalData.isNetworkAvailable(getActivity())) {
            cursor = sqLiteDatabaseHandler.getEventHomeData(sessionManager.getEventId());
            if (cursor.moveToFirst()) {

                try {
                    JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Home_EventData)));
                    loadHomeData(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.home_pageIndexUid, Param.HomeFragment(Token_id, Eventid, sessionManager.getEventType(), sessionManager.getUserId()), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.home_pageIndex, Param.HomeFragment(Token_id, Eventid, sessionManager.getEventType(), sessionManager.getUserId()), 0, false, this);
        } else {
            cursor = sqLiteDatabaseHandler.getEventHomeData(sessionManager.getEventId());
            if (cursor.moveToFirst()) {

                try {
                    JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Home_EventData)));
                    loadHomeData(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void notificationTransction() {
        ((MainActivity) getActivity()).handleIntent(getActivity().getIntent());
        ((MainActivity) getActivity()).redirectionFromNotifcationTray(getActivity().getIntent());


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            home_viewPager.getLayoutParams().height = home_viewPager.getLayoutParams().height + 50;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            home_viewPager.getLayoutParams().height = home_viewPager.getLayoutParams().height - 50;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        getActivity().registerReceiver(deepLinkBroadCastRecevier,new IntentFilter(deepLinkbroadcast));

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
//                    JSONObject mainObject = new JSONObject(String.valueOf(volleyResponse.getJSONObject()));
                    JSONObject mainObject = new JSONObject(volleyResponse.output);
                    if (mainObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        JSONObject jsonObject = mainObject.getJSONObject("data");
                        notes_status = jsonObject.getString("note_status");

                        loadHomeData(jsonObject);
                        if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
                            if (sqLiteDatabaseHandler.isEventDataExist(sessionManager.getEventId()))  // Use To store EventHomeData
                            {
                                sqLiteDatabaseHandler.UpdateEventHomeData(sessionManager.getEventId(), jsonObject.toString());
                            } else {
                                sqLiteDatabaseHandler.insertEventHomeData(sessionManager.getEventId(), jsonObject.toString());
                            }
                        }
                        checkNoteStatus();
                    } else {
                        ToastC.show(getActivity(), mainObject.optString(Param.KEY_MESSAGE));
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

                        if (sqLiteDatabaseHandler.isAdvertiesMentExist(sessionManager.getEventId(), "0")) {
                            sqLiteDatabaseHandler.deleteAdvertiesMentData(sessionManager.getEventId(), "0");
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), "0", jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), "0", jsonObject.toString());
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
                sessionManager.footerView(getContext(), "0", MainLayout, relative_staticHome, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relative_staticHome, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relative_staticHome, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relative_staticHome, linear_content, topAdverViewArrayList, getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadHomeData(JSONObject jsonObject) {
        idArrays.clear();
        ArrayItem.clear();

        try {
            JSONArray jsonArray = jsonObject.optJSONArray("events");
            for (int e = 0; e < jsonArray.length(); e++) {
                JSONObject jObjectevent = (JSONObject) jsonArray.get(e);
                detailImagesObj.clear();
                listImageArray.clear();
                listImageDetailArray.clear();
                sessionManager.appColor(jObjectevent);
                WebStr = jObjectevent.getString("description1");
                ImgStr = jObjectevent.getString("Images");
                backImg = jObjectevent.getString("Background_img1");
                JSONArray jsonBaneerArray = jObjectevent.getJSONArray("banners_url");
                sessionManager.setPhotoFilterImage(jObjectevent.getString("photo_filter_image"));
                for (int b = 0; b < jsonBaneerArray.length(); b++) {
                    JSONObject index = jsonBaneerArray.getJSONObject(b);
                    detailImagesObj.add(new BannerImageData(index.get("image").toString(), index.get("url").toString()));
                }
            }
            JSONObject jsonObjectImage = jsonObject.getJSONObject("banner_images");
            final JSONArray jsonArrayImage = jsonObjectImage.getJSONArray("images");
            for (int img = 0; img < jsonArrayImage.length(); img++) {

                listImageDetailArray = new ArrayList<>();
                JSONObject index = jsonArrayImage.getJSONObject(img);
                JSONArray jsonArrayDetail = index.getJSONArray("details");

                if (index.getString("image_type").equalsIgnoreCase("0")) {
                    for (int det = 0; det < jsonArrayDetail.length(); det++) {
                        JSONObject jsonImageDetail = jsonArrayDetail.getJSONObject(det);
                        listImageDetailArray.add(new HomeScreenMapDetailData(jsonImageDetail.getString("coords"), jsonImageDetail.getString("menuid"), jsonImageDetail.getString("redirect_url"), jsonImageDetail.getString("cmsid"), jsonImageDetail.getString("is_force_login"), jsonImageDetail.getString("agenda_id"), jsonImageDetail.getString("group_id"), jsonImageDetail.getString("exhi_id"), jsonImageDetail.getString("super_group_id"), jsonImageDetail.getString("keyword"), jsonImageDetail.getString("all_exhi_sub_cat"), jsonImageDetail.getString("is_contains_data"), jsonImageDetail.getString("myagenda")));
                    }
                    listImageArray.add(new HomePageDynamicImageArray(index.getString("id")
                            , MyUrls.Imgurl + index.getString("Image")
                            , index.getString("Content")
                            , index.getInt("height")
                            , index.getInt("width"), listImageDetailArray));
                }
            }

            if (listImageArray.size() != 0) {
                linear_dynamicHome.setVisibility(View.VISIBLE);
                if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                    MainLayout.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                } else {
                    MainLayout.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                }

                if (linear_dynamicHome.getChildCount() != 0) {
                    linear_dynamicHome.removeAllViews();
                }

                for (int j = 0; j < listImageArray.size(); j++) {
                    List<MapCoordinatesDetails> rectangles1 = new ArrayList<>();
                    for (int det = 0; det < listImageArray.get(j).getMapDetailDatas().size(); det++) {

                        final int pos = det;
                        String[] split_coords = listImageArray.get(j).getMapDetailDatas().get(det).getCoords().split(",");
                        top = split_coords[0];
                        left = split_coords[1];
                        right = split_coords[2];
                        bottom = split_coords[3];
                        paint = new Paint();
                        paint.setStrokeWidth(5);
                        paint.setColor(Color.parseColor("#00000000"));
//                                paint.setColor(Color.parseColor("#e50000"));
                        paint.setStyle(Paint.Style.STROKE);

                        final Rect rect = new Rect(Integer.parseInt(top), Integer.parseInt(left), Integer.parseInt(right), Integer.parseInt(bottom));
                        rectangles1.add(new MapCoordinatesDetails(rect, listImageArray.get(j).getMapDetailDatas().get(det).getCoords(), listImageArray.get(j).getMapDetailDatas().get(det).getMenuid(), listImageArray.get(j).getMapDetailDatas().get(det).getRedirect_url(), listImageArray.get(j).getMapDetailDatas().get(det).getCmsid(), listImageArray.get(j).getMapDetailDatas().get(det).getIs_force_login(), listImageArray.get(j).getMapDetailDatas().get(det).getAgenda_id(), listImageArray.get(j).getMapDetailDatas().get(det).getGroup_id(), listImageArray.get(j).getMapDetailDatas().get(det).getExhi_id(), listImageArray.get(j).getMapDetailDatas().get(det).getSuper_group_id(), listImageArray.get(j).getMapDetailDatas().get(det).getExhi_sub_cat_id(), listImageArray.get(j).getMapDetailDatas().get(det).getAll_exhi_sub_cat(), listImageArray.get(j).getMapDetailDatas().get(det).getIs_contains_data(), listImageArray.get(j).getMapDetailDatas().get(det).getIs_user_agenda()));
                    }
                    rectanglesList.add(rectangles1);
                }
                for (int k = 0; k < listImageArray.size(); k++) {
                    final int position = k;
                    final RelativeLayout relativeLayout = new RelativeLayout(getActivity());
                    final RelativeLayout.LayoutParams vp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    relativeLayout.setLayoutParams(vp);
                    final ImageView image = new ImageView(getActivity());
                    image.setAdjustViewBounds(true);
                    image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    image.setCropToPadding(false);
                    image.setLayoutParams(vp);

                    if (listImageArray.get(k).getImageUrl().contains("gif")) {
                        Glide.with(getActivity())
                                .load(listImageArray.get(k).getImageUrl())
                                .asGif()
                                .override(listImageArray.get(k).getWidth(), listImageArray.get(k).getHeight()).thumbnail(0.7f)
                                .listener(new RequestListener<String, GifDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
//                                        e.printStackTrace();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        Bitmap bs = Bitmap.createBitmap(resource.getMinimumWidth(), resource.getMinimumHeight(), Bitmap.Config.RGB_565);
                                        image.setImageBitmap(bs);
                                        image.setTag(position);
                                        Canvas canvas = new Canvas(bs);
                                        final List<MapCoordinatesDetails> rectangles1 = new ArrayList<>();
                                        for (int det = 0; det < listImageArray.get(position).getMapDetailDatas().size(); det++) {

                                            final int pos = det;
                                            String[] split_coords = listImageArray.get(position).getMapDetailDatas().get(det).getCoords().split(",");
                                            top = split_coords[0];
                                            left = split_coords[1];
                                            right = split_coords[2];
                                            bottom = split_coords[3];
                                            paint = new Paint();

                                            paint.setStrokeWidth(5);
                                            paint.setAntiAlias(true);
                                            image.setLayerType(LAYER_TYPE_SOFTWARE, paint);
//                                            paint.setColor(Color.parseColor("#00000000"));
//                                            paint.setColor(Color.parseColor("#e50000"));
                                            paint.setColor(Color.parseColor("#000000"));
                                            paint.setStyle(Paint.Style.STROKE);

                                            final Rect rect = new Rect(Integer.parseInt(top), Integer.parseInt(left), Integer.parseInt(right), Integer.parseInt(bottom));
                                            canvas.drawRect(rect, paint);

                                            final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
                                                @Override
                                                public boolean onDown(MotionEvent e) {
                                                    return false;
                                                }

                                                @Override
                                                public void onShowPress(MotionEvent e) {

                                                }

                                                @Override
                                                public boolean onSingleTapUp(MotionEvent e) {


                                                    advancedDesignClick(position, image, e);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                                                    return false;
                                                }

                                                @Override
                                                public void onLongPress(MotionEvent e) {

                                                }

                                                @Override
                                                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                                                    return false;
                                                }
                                            });
                                            image.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    gestureDetector.onTouchEvent(event);

                                                    switch (event.getAction()) {
                                                        case MotionEvent.ACTION_DOWN:
                                                            return true;
                                                        case MotionEvent.ACTION_UP:
                                                            return true;
                                                        case MotionEvent.ACTION_MOVE:
                                                            return true;
                                                    }
                                                    return false;
                                                }
                                            });
                                        }
//
                                        return false;
                                    }

                                })
                                .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);

                    } else {
                        Glide.with(getActivity())
                                .load(listImageArray.get(k).getImageUrl())
                                .asBitmap()
                                .override(listImageArray.get(k).getWidth(), listImageArray.get(k).getHeight()).thumbnail(0.7f)
                                .listener(new RequestListener<String, Bitmap>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        image.setImageBitmap(resource);
                                        image.setTag(position);
                                        Canvas canvas = new Canvas(resource);
                                        List<MapCoordinatesDetails> rectangles1 = new ArrayList<>();
                                        for (int det = 0; det < listImageArray.get(position).getMapDetailDatas().size(); det++) {

                                            final int pos = det;
                                            String[] split_coords = listImageArray.get(position).getMapDetailDatas().get(det).getCoords().split(",");
                                            top = split_coords[0];
                                            left = split_coords[1];
                                            right = split_coords[2];
                                            bottom = split_coords[3];
                                            paint = new Paint();

                                            paint.setStrokeWidth(5);
                                            paint.setColor(Color.parseColor("#00000000"));
//                                paint.setColor(Color.parseColor("#e50000"));
                                            paint.setStyle(Paint.Style.STROKE);

                                            final Rect rect = new Rect(Integer.parseInt(top), Integer.parseInt(left), Integer.parseInt(right), Integer.parseInt(bottom));
                                            canvas.drawRect(rect, paint);
//                                            rectangles1.add(new MapCoordinatesDetails(rect, listImageArray.get(position).getMapDetailDatas().get(det).getCoords(),listImageArray.get(position).getMapDetailDatas().get(det).getMenuid(),listImageArray.get(position).getMapDetailDatas().get(det).getRedirect_url(),listImageArray.get(position).getMapDetailDatas().get(det).getCmsid(),listImageArray.get(position).getMapDetailDatas().get(det).getIs_force_login(),listImageArray.get(position).getMapDetailDatas().get(det).getAgenda_id()));

                                            final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
                                                @Override
                                                public boolean onDown(MotionEvent e) {
                                                    return false;
                                                }

                                                @Override
                                                public void onShowPress(MotionEvent e) {

                                                }

                                                @Override
                                                public boolean onSingleTapUp(MotionEvent e) {
                                                    Log.d("TAG", "" + position + "IMAGE PAGE" + image.getTag());

                                                    advancedDesignClick(position, image, e);
                                                    return false;
                                                }

                                                @Override
                                                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                                                    return false;
                                                }

                                                @Override
                                                public void onLongPress(MotionEvent e) {

                                                }

                                                @Override
                                                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                                                    return false;
                                                }
                                            });
                                            image.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    gestureDetector.onTouchEvent(event);

                                                    switch (event.getAction()) {
                                                        case MotionEvent.ACTION_DOWN:
                                                            return true;
                                                        case MotionEvent.ACTION_UP:
                                                            return true;
                                                        case MotionEvent.ACTION_MOVE:
                                                            return true;
                                                    }
                                                    return false;
                                                }
                                            });

                                        }
//                                        rectanglesList.add(rectangles1);
                                        return false;
                                    }
                                })
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(image);

                    }
                    relativeLayout.addView(image);
                    TextView textView = new TextView(getActivity());
                    textView.setText(Html.fromHtml(listImageArray.get(k).getContent()));
                    relativeLayout.addView(textView);
                    linear_dynamicHome.addView(relativeLayout);
                }
            } else {
                MainLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                linear_dynamicHome.setVisibility(View.GONE);
            }

            if (detailImagesObj.size() == 0) {
                home_viewPager.setVisibility(View.GONE);
            } else {
                home_viewPager.setVisibility(View.VISIBLE);
                homeImagePagerAdpater = new HomeBaneerImageAdapter(getActivity(), detailImagesObj);
                home_viewPager.setAdapter(homeImagePagerAdpater);
            }
            NUM_PAGES = detailImagesObj.size();
            home_viewPager.setCurrentItem(0);
            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                        home_viewPager.setCurrentItem(0, false);
                        return;
                    }
                    home_viewPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 5000, 5000);

            // Pager listener over indicator
            home_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {
                }
            });

            ((MainActivity) getActivity()).setDrawerBackColor();

            if (WebStr.equalsIgnoreCase("")) {
                linear_webView.setVisibility(View.GONE);
            } else {
                linear_webView.setVisibility(View.VISIBLE);
                String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
                String pas = "</body></html>";
                String myHtmlString = pish + WebStr + pas;
                webView.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);
            }

            JSONArray homeMenu = jsonObject.getJSONArray("aitl_home_page_menu");
            for (int h = 0; h < homeMenu.length(); h++) {

                JSONObject homeObj = homeMenu.getJSONObject(h);
                pTitle = homeObj.getString("Menu_name");
                pMId = homeObj.getString("id");
//                    pCheck = homeObj.getString("is_feture_products");
                imgView_status = homeObj.getString("img_view");
                menuBackImg = homeObj.getString("img");
                String menuBackimgUrl = MyUrls.Imgurl + menuBackImg;

                ArrayItem.add(new DashboardItemList(pTitle, imgView_status, pMId, "", menuBackimgUrl, homeObj.getString("is_icon"), homeObj.getString("icon_path"), homeObj.getString("Background_color"), homeObj.getString("is_cms"), homeObj.getString("is_force_login"), homeObj.getString("category_id")));
                idArrays.add(new DashboardIdArray(pMId));

            }
            adapter = new DashBoardItemListAdapter(ArrayItem, getActivity());
            if (ArrayItem.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                recycle_imgView.setVisibility(View.GONE);
            } else {

                recyclerView.setVisibility(View.VISIBLE);
                if (backImg.equalsIgnoreCase("")) {
                    recycle_imgView.setVisibility(View.GONE);
                    if (sessionManager.getEventId().equalsIgnoreCase("353")) {
                        home_linear.setBackgroundColor(Color.parseColor(sessionManager.getBackGroundColor()));
                    } else {
                        home_linear.setBackgroundColor(Color.parseColor("#ffffff"));
                    }
                } else {
                    recycle_imgView.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(MyUrls.Imgurl + backImg).into(recycle_imgView);
                }

            }
            GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
            mLayoutManager.setSmoothScrollbarEnabled(true);
            recyclerView.setLayoutManager(mLayoutManager);

            recyclerView.setAdapter(adapter);

            ((MainActivity) getActivity()).setAppcolor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void advancedDesignClick(int position, ImageView image, MotionEvent e) {
        try {
            final List<MapCoordinatesDetails> rectangles = rectanglesList.get(position);
            for (int i = 0; i < rectangles.size(); i++) {
                Rect rect = rectangles.get(i).getRect();
                RectF rectF = new RectF(rect);
                Matrix currentMatrix = image.getImageMatrix();
                currentMatrix.mapRect(rectF);

                if (rectF.contains((int) e.getX(), (int) e.getY())) {

                    if (!(rectangles.get(i).getCmsid().equalsIgnoreCase(""))) {

                        sessionManager.strMenuId = rectangles.get(i).getCmsid().toString();
                        sessionManager.strModuleId = "0";

                        sessionManager.cms_id(rectangles.get(i).getCmsid().toString());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                        GlobalData.CURRENT_FRAG = GlobalData.CMS_FRAGMENT_TEST1;
                        ((MainActivity) getActivity()).loadFragment();
                    } else if (!(rectangles.get(i).getMenuid().equalsIgnoreCase(""))) {
                        sessionManager.strMenuId = rectangles.get(i).getMenuid();
                        sessionManager.strModuleId = "0";
                        sessionManager.setMenuid(rectangles.get(i).getMenuid());
                        sessionManager.set_isForceLogin(rectangles.get(i).getIs_force_login());

                        if (rectangles.get(i).getMenuid().equalsIgnoreCase("1")) {
                            if (rectangles.get(i).getIs_user_agenda().equalsIgnoreCase("1")) {
                                GlobalData.is_user_agenda = true;
                                sessionManager.setAgendaCategoryId(rectangles.get(i).getAgenda_id());
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                                ((MainActivity) getActivity()).loadFragment();
                            } else {
                                GlobalData.is_user_agenda = false;
                                if (rectangles.get(i).getGroup_id().isEmpty() && rectangles.get(i).getAgenda_id().isEmpty()) {
                                    ((MainActivity) getActivity()).isAgendaGroupAndCategoryExist();
                                } else if (!rectangles.get(i).getGroup_id().isEmpty()) {
                                    sessionManager.setAgendagroupID(rectangles.get(i).getGroup_id());
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.AgendaGroupWiseFragment;
                                    ((MainActivity) getActivity()).loadFragment();
                                } else {
                                    sessionManager.setAgendaCategoryId(rectangles.get(i).getAgenda_id());
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                                    ((MainActivity) getActivity()).loadFragment();
                                }
                            }
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("3")) {

                            if (rectangles.get(i).getExhi_id().isEmpty() && rectangles.get(i).getAll_exhi_sub_cat().equalsIgnoreCase("1")) {
                                sessionManager.setExhibitorParentCategoryId(rectangles.get(i).getExhi_id());
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.ExhibitorSubCategoryListFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            } else if (!rectangles.get(i).getExhi_id().isEmpty()) {
                                sessionManager.setExhibitorParentCategoryId(rectangles.get(i).getExhi_id());
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.ExhibitorSubCategoryListFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            } else if ((!rectangles.get(i).getExhi_sub_cat_id().isEmpty())) {
                                sessionManager.setExhibitorSubCategoryDesc(rectangles.get(i).getExhi_sub_cat_id());
                                sessionManager.setExhibitorSponsorCategoryId("");
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.ExhibitorSubCategoryWiseListFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            } else {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.EXHIBITOR_FRAGMENT;
                                ((MainActivity) getActivity()).loadFragment();
                            }

                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("2")) {
                            ((MainActivity) getActivity()).attendeeRedirection(false, false);
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("7")) {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SPEAKER_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();

                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("6")) {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.View_Note_Fragment;
                            ((MainActivity) getActivity()).loadFragment();

                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("10")) {
                            sessionManager.Map_coords = "";
                            sessionManager.exhibitor_standNumber = "";
                            ((MainActivity) getActivity()).isMapGroupExist();

                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("27")) {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.notificationListingFragment;
                            ((MainActivity) getActivity()).loadFragment();

                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("17")) {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.SOCIAL_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("12")) {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PRIVATE_MESSAGE;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("9"))  // Presantation
                        {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Presantation_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("11")) // Photos
                        {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Photo_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("13")) // Message Wall
                        {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PublicMessage_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("15")) // Survey
                        {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Survey_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("16")) // Document
                        {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Document_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("22")) // Silent Aucation
                        {

                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Silent_Auction;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("43")) {

                            ((MainActivity) getActivity()).isSponsorGroupExist();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("44")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.TweitterFeed;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("24")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Online_shop;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("23")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.live_Auction;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("45")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.activityFeed;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("46")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.InstagramFeed;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("20"))  // Fundraising
                        {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("25")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.DonateFor_veteran;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("57")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.QrcodeScannerSharecontactFragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("47")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.facebookFeed;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("59")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.MatchMakingFragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("50"))  // Fundraising
                        {
                            sessionManager.setQandAgroupID("");
                            if (rectangles.get(i).getIs_contains_data().equalsIgnoreCase("1")) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.QAList_Fragment;
                                ((MainActivity) getActivity()).loadFragment();
                            } else {

                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.QandAGroupWiseFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            }
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("49")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.FavoriteList_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("52")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.Gamification_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("53")) {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.ExhibiorLead_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                        else if (rectangles.get(i).getMenuid().equalsIgnoreCase("54"))
                        {
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.PhotoFilter_Fragment;
                            ((MainActivity) getActivity()).loadFragment();
                        } else if (rectangles.get(i).getMenuid().equalsIgnoreCase("21")) {
                            if (rectangles.get(i).getSuper_group_id().isEmpty() && rectangles.get(i).getGroup_id().isEmpty()) {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.CMSGroupListFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            } else if (!(rectangles.get(i).getSuper_group_id().isEmpty())) {
                                sessionManager.setCmsSuperGroupId(rectangles.get(i).getSuper_group_id());
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.CMSGroupChildListFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            } else if (!(rectangles.get(i).getGroup_id().isEmpty())) {
                                sessionManager.setCmsChildGroupId(rectangles.get(i).getGroup_id());
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.CMSListFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            }
                        }
                        else if (rectangles.get(i).getMenuid().equalsIgnoreCase("56")) {
                            if (GlobalData.checkForUIDVersion())
                            {
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            }
                            else
                            {
                                if (sessionManager.getRolId().equalsIgnoreCase("4")) {//postponed - pending
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.attendeeRequestMettingFragment;
                                    ((MainActivity) getActivity()).loadFragment();
                                }
                                else if (sessionManager.getRolId().equalsIgnoreCase("6"))
                                {//postponed - pending
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
                                    ((MainActivity) getActivity()).loadFragment();
                                }
                                else if (sessionManager.getRolId().equalsIgnoreCase("7") && //changes applied
                                        sessionManager.isModerater().equalsIgnoreCase("1")) {//changes applied
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.moderatorRequestMettingFragment;
                                    ((MainActivity) getActivity()).loadFragment();

                                }
                            }
                        }
                    } else if (!(rectangles.get(i).getRedirect_url().equalsIgnoreCase(""))) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Social_url", rectangles.get(i).getRedirect_url());
                        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);

                        GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                        ((MainActivity) getActivity()).loadFragment(bundle);
                    }

                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.OtherPageWiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", "19", "", "", ""), 10, false, this);
        }
    }

    private void checkNoteStatus() {
        ((MainActivity) getActivity()).updateCartCountFromHome();
    }

}