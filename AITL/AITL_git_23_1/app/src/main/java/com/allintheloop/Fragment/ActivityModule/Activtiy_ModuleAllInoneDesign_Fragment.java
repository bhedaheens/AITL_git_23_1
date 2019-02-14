package com.allintheloop.Fragment.ActivityModule;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.allintheloop.Adapter.AdapterActivity.ActivityCommonAdapter;
import com.allintheloop.Bean.ActivityModule.ActivityCommonClass;
import com.allintheloop.Bean.ActivityModule.ActivityCommonList;
import com.allintheloop.Bean.ActivityModule.EventBusAcitivtyLikeCountRefresh;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
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
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import static com.allintheloop.Util.GlobalData.newactivityReloadedFromSharePost;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activtiy_ModuleAllInoneDesign_Fragment extends Fragment implements VolleyInterface {

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    RecyclerView rv_viewActivity;
    ProgressBar progressBar1;
    ArrayList<ActivityCommonClass> allData;
    ArrayList<ActivityCommonClass> allDataReverse;
    RelativeLayout MainLayout, relativeLayout_Data, relativeLayout_forceLogin;
    LinearLayout linear_content;
    SessionManager sessionManager;
    DefaultLanguage.DefaultLang defaultLang;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    boolean islatPage;
    String facebookNextpage = "", twitterNextpage = "", instagramNextUrl = "";
    //    AdapterAcitivty_newAllInOne adapter;
    StaggeredGridLayoutManager mLayoutManager;
    RecyclerView.OnScrollListener onScrollListener = null;
    boolean loading = false;
    int visibleItemCount, totalItemCount, pastVisibleItems;
    int current_page = 1;
    int postcountbeforead;
    String survey_id = "", advertise_id = "";
    BoldTextView txt_postButton;
    ActivityCommonList activityCommonList;
    ActivityCommonAdapter activityCommonAdapter;
    private BroadcastReceiver broadcastReceiverReloadData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setupView(true);
        }
    };


    public Activtiy_ModuleAllInoneDesign_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activtiy__module_all_inone_design, container, false);
        initView(rootView);
        setupView(true);
        return rootView;
    }

    private void setupView(boolean isLoader) {
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();


        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.OVAL);

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        allData = new ArrayList<>();
        allDataReverse = new ArrayList<>();
        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        activityCommonAdapter = new ActivityCommonAdapter(allData, getActivity(), sessionManager, Activtiy_ModuleAllInoneDesign_Fragment.this);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv_viewActivity.setLayoutManager(mLayoutManager);
        rv_viewActivity.setItemAnimator(null);
        rv_viewActivity.setAdapter(activityCommonAdapter);
        paginationMethod();
//        if (sessionManager.getActivity_share_icon_setting().equalsIgnoreCase("0"))
//
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
//        }

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {

            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            txt_postButton.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            txt_postButton.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }
        txt_postButton.setBackgroundDrawable(drawable);

        if (sessionManager.isLogin()) {
            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getAdvertiesment();
            setUpValue();
            getAllFeed(isLoader);
        } else {
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getAdvertiesment();
                getAllFeed(isLoader);
            }
        }

        txt_postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLogin()) {
                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                    GlobalData.CURRENT_FRAG = GlobalData.ActivitySharePost_Fragment;
                    ((MainActivity) getActivity()).loadFragment();
                } else {
                    sessionManager.alertDailogLogin(getActivity());
                }

            }
        });
    }

    private void setUpValue() {
        facebookNextpage = "";
        twitterNextpage = "";
        instagramNextUrl = "";
        current_page = 1;
        survey_id = "";
        advertise_id = "";
        loading = false;
    }


    public void getCountPostBeforeAd() {

        if (current_page >= 1) {
            postcountbeforead = 0;
            for (int i = 0; i < allDataReverse.size(); i++) {
                ActivityCommonClass activityCommon;
                activityCommon = allDataReverse.get(i);

                if (activityCommon.getPostType().equalsIgnoreCase("5") || activityCommon.getPostType().equalsIgnoreCase("6")) {
                    for (int j = 0; j < allDataReverse.size(); j++) {
                        ActivityCommonClass activityCommonClass;
                        activityCommonClass = allDataReverse.get(j);

                        if (activityCommonClass.getPostType().equalsIgnoreCase("5") || activityCommonClass.getPostType().equalsIgnoreCase("6")) {
                            Log.d("PostCountBeforeAd", "" + postcountbeforead);
                            break;
                        } else {
                            postcountbeforead++;
                        }
                    }
                    break;
                }
            }
        }
    }


    public void getSurveyId() {
        for (int i = 0; i < allDataReverse.size(); i++) {
            ActivityCommonClass activityCommonClass;
            activityCommonClass = allDataReverse.get(i);

            if (activityCommonClass.getPostType().equalsIgnoreCase("6")) {
                survey_id = activityCommonClass.getId();
                break;
            }
        }
    }

    public void getAdvertiseId() {
        for (int i = 0; i < allDataReverse.size(); i++) {
            ActivityCommonClass activityCommonClass;
            activityCommonClass = allDataReverse.get(i);

            if (activityCommonClass.getPostType().equalsIgnoreCase("5")) {
                advertise_id = activityCommonClass.getId();
                break;
            }
        }
    }

    private void paginationMethod() {
        onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                int[] firstVisibleItems = null;
                firstVisibleItems = mLayoutManager.findFirstVisibleItemPositions(firstVisibleItems);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }

                if (!loading) {
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {

                        if (islatPage && facebookNextpage.equalsIgnoreCase("1") && twitterNextpage.equalsIgnoreCase("1") && instagramNextUrl.equalsIgnoreCase("1")) {
                            progressBar1.setVisibility(View.GONE);
                        } else {
                            current_page = current_page + 1;
                            loadMore();
                        }
                        loading = true;
                        Log.d("tag", "LOAD NEXT ITEM");
                    }
                }
            }
        };
        rv_viewActivity.addOnScrollListener(onScrollListener);
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 1, false, this);
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

    public void getAllFeed(boolean isLoder) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllActivityFeedUid, Param.getActivityAllFeedPageWisewithpostcount(sessionManager.getEventId(), sessionManager.getUserId(), current_page, facebookNextpage, twitterNextpage, instagramNextUrl, postcountbeforead, survey_id, advertise_id), 0, isLoder, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllActivityFeed, Param.getActivityAllFeedPageWisewithpostcount(sessionManager.getEventId(), sessionManager.getUserId(), current_page, facebookNextpage, twitterNextpage, instagramNextUrl, postcountbeforead, survey_id, advertise_id), 0, isLoder, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void loadMore() {
        progressBar1.setVisibility(View.GONE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllActivityFeedUid, Param.getActivityAllFeedPageWisewithpostcount(sessionManager.getEventId(), sessionManager.getUserId(), current_page, facebookNextpage, twitterNextpage, instagramNextUrl, postcountbeforead, survey_id, advertise_id), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllActivityFeed, Param.getActivityAllFeedPageWisewithpostcount(sessionManager.getEventId(), sessionManager.getUserId(), current_page, facebookNextpage, twitterNextpage, instagramNextUrl, postcountbeforead, survey_id, advertise_id), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void initView(View rootView) {
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        rv_viewActivity = (RecyclerView) rootView.findViewById(R.id.rv_viewActivity);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        txt_postButton = rootView.findViewById(R.id.txt_postButton);

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 0:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
//                        progressBar1.setVisibility(View.GONE);
                        JSONObject jsonObjectPagination = jsonObject.getJSONObject("pagination");

                        Gson gson = new Gson();
                        activityCommonList = gson.fromJson(jsonObject.toString(), ActivityCommonList.class);
                        allData.addAll(activityCommonList.getData());

                        if (activityCommonList.getData().size() == 0) {
                            islatPage = true;
                        } else {
                            islatPage = false;
                        }
                        if (allDataReverse.size() > 0) {
                            allDataReverse.removeAll(allDataReverse);
                        }
                        allDataReverse.addAll(activityCommonList.getData());
                        Collections.reverse(allDataReverse);
                        getCountPostBeforeAd();
                        getSurveyId();
                        getAdvertiseId();

                        if (jsonObjectPagination.has("fb")) {
                            String fbUrl = jsonObjectPagination.getString("fb");

                            if (fbUrl.length() != 0) {
                                facebookNextpage = fbUrl;
                            } else {
                                facebookNextpage = "1";
                            }
                        } else {
                            facebookNextpage = "1";
                        }


                        if (jsonObjectPagination.has("tw")) {
                            String twitterUrl = jsonObjectPagination.getString("tw");

                            if (twitterUrl.length() != 0) {
                                twitterNextpage = twitterUrl;
                            } else {
                                twitterNextpage = "1";
                            }
                        } else {
                            twitterNextpage = "1";
                        }

                        if (jsonObjectPagination.has("ig")) {

                            String instagramUrl = jsonObjectPagination.getString("ig");

                            if (instagramUrl.length() != 0) {
                                instagramNextUrl = instagramUrl;
                            } else {
                                instagramNextUrl = "1";
                            }
                        } else {
                            instagramNextUrl = "1";
                        }


                        if (allData.size() != 0) {
                            relativeLayout_forceLogin.setVisibility(View.GONE);
                            relativeLayout_Data.setVisibility(View.VISIBLE);
                        } else {
                            relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                            relativeLayout_Data.setVisibility(View.GONE);
                        }
//
//                        Collections.sort(allData, new TimestampComparator());
//                        progressBar1.setVisibility(View.GONE);

//                        adapter.notifyDataSetChanged();

//                        for (int i = 0; i <allData.size()/6 ; i++)
//                        {
//                            Log.d("Bhavdip data",allData.get(i).toString());
//                        }

                        new setListview().execute();

                        loading = false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Log.d("AITL  Advertiesment", jsonObject.toString());

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
            sessionManager.setisFooterAdvertiesment("0");
            sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            //   sessionManager.NotefooterView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openSurveyDialog(ActivityCommonClass activityCommonClass) {
        if (activityCommonClass.getAns_submitted().equalsIgnoreCase("0")) {
            Bundle bundle = new Bundle();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Activity_SurveyFragment_Dialog fragment = new Activity_SurveyFragment_Dialog();
            bundle.putParcelable("activitySurvey", activityCommonClass);
            fragment.setArguments(bundle);
            fragment.show(fm, "DialogFragment");
        } else {
            Bundle bundle = new Bundle();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Activity_surveyResultDialog_fragment fragment = new Activity_surveyResultDialog_fragment();
            bundle.putParcelable("activitySurvey", activityCommonClass);
            fragment.setArguments(bundle);
            fragment.show(fm, "DialogFragment");
        }

    }

    public void openPostUrl(ActivityCommonClass activityCommonClass) {
        if (activityCommonClass.getAdvertUrl() != null && !activityCommonClass.getAdvertUrl().isEmpty()) {
            Intent tel = new Intent(Intent.ACTION_VIEW, Uri.parse(activityCommonClass.getAdvertUrl()));
            startActivity(tel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiverReloadData, new IntentFilter(newactivityReloadedFromSharePost));

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusAcitivtyLikeCountRefresh(EventBusAcitivtyLikeCountRefresh data) {
        activityCommonAdapter.notifyDataSetChanged();
    }


    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiverReloadData);
        EventBus.getDefault().unregister(this);
    }

    private class setListview extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            activityCommonAdapter.updateList(allData);
        }
    }
}