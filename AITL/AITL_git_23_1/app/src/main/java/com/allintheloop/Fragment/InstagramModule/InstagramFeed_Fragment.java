package com.allintheloop.Fragment.InstagramModule;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.Adapter_instagram;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.InstagramFeed;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
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
public class InstagramFeed_Fragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewInstagram;
    Button btn_load_more;
    TextView txt_Nofeed;
    Adapter_instagram adapterInstagram;
    int page_count = 1;
    SessionManager sessionManager;
    public static ArrayList<InstagramFeed> instagramFeedArrayList;

    String str_link, str_likesCnt, str_commCnt, str_captiontext, str_userName, str_profilePic, str_type, str_id;
    String str_lowResolutionImg, str_stndResolutionImg;
    int total_pages = 0;
    GridLayoutManager linearLayoutManager;
    boolean btn_load;
    CardView card_NoData;
    boolean isFirstTime = false;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "InstagramList";
    boolean isLoading;
    Handler handler;
    NestedScrollView scrollView;
    ProgressBar progressBar1;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    LinearLayout linear_content;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    public InstagramFeed_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        View rootView = inflater.inflate(R.layout.fragment_instagram_feed, container, false);
        btn_load_more = (Button) rootView.findViewById(R.id.btn_load_more);
        rv_viewInstagram = (RecyclerView) rootView.findViewById(R.id.rv_viewInstagram);
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        txt_Nofeed = (TextView) rootView.findViewById(R.id.txt_Nofeed);
        card_NoData = (CardView) rootView.findViewById(R.id.card_NoData);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        rv_viewInstagram.setNestedScrollingEnabled(false);
        instagramFeedArrayList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        handler = new Handler();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());

//        sessionManager.setMenuid("46");
        sessionManager.strMenuId = "46";
        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();


        rv_viewInstagram.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                InstagramFeed InstaObj = instagramFeedArrayList.get(position);
                Log.d("AITL ID", InstaObj.getId());

                FragmentManager fm = getActivity().getSupportFragmentManager();
                Instagram_DialogFragment fragment = new Instagram_DialogFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                fragment.setArguments(bundle);
                fragment.show(fm, "DialogFragment");

            }
        }));

        boolean tabletSize = getActivity().getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            linearLayoutManager = new GridLayoutManager(getActivity(), 1);
        }
        rv_viewInstagram.setLayoutManager(linearLayoutManager);
        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getInstagramFeed();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getInstagramFeed();
            }
        }

        getAdvertiesment();
        return rootView;
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

    private void buttongetFeed() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getInstagramFeed, Param.getInstagramFeed(sessionManager.getEventId(), page_count), 1, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void getInstagramFeed() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            instagramFeedArrayList.clear();
//            btn_load_more.setVisibility(View.GONE);
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        Log.d("AITL    Oflline", jsonArray.toString());
                        loadInstagramData(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                isFirstTime = true;
                if (isFirstTime) {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getInstagramFeed, Param.getInstagramFeed(sessionManager.getEventId(), page_count), 0, false, this);
                    isFirstTime = false;
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getInstagramFeed, Param.getInstagramFeed(sessionManager.getEventId(), page_count), 0, false, this);
                }
            } else {
                isFirstTime = true;
                if (isFirstTime) {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getInstagramFeed, Param.getInstagramFeed(sessionManager.getEventId(), page_count), 0, false, this);
                    isFirstTime = false;
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getInstagramFeed, Param.getInstagramFeed(sessionManager.getEventId(), page_count), 0, false, this);
                }
            }
        } else {
            instagramFeedArrayList.clear();
            btn_load_more.setVisibility(View.GONE);
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        Log.d("AITL    Oflline", jsonArray.toString());
                        loadInstagramData(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {

                rv_viewInstagram.setVisibility(View.GONE);
                card_NoData.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void loadInstagramData(JSONArray jArrayData) {
        try {

            if (!isLoading) {
                for (int i = 0; i < jArrayData.length(); i++) {
                    JSONObject jObjData = jArrayData.getJSONObject(i);
                    str_link = jObjData.getString("link");
                    str_type = jObjData.getString("type");
                    str_id = jObjData.getString("id");

                    JSONObject jlikeObj = jObjData.getJSONObject("likes");
                    str_likesCnt = jlikeObj.getString("count");

                    JSONObject jCommObj = jObjData.getJSONObject("comments");
                    str_commCnt = jCommObj.getString("count");

                    JSONObject jCaptionObj = jObjData.getJSONObject("caption");
                    str_captiontext = jCaptionObj.getString("text");

                    JSONObject jUserObj = jObjData.getJSONObject("user");
                    str_userName = jUserObj.getString("username");
                    str_profilePic = jUserObj.getString("profile_picture");

                    JSONObject jImageObj = jObjData.getJSONObject("images");
                    JSONObject jLowImageObj = jImageObj.getJSONObject("low_resolution");
                    str_lowResolutionImg = jLowImageObj.getString("url");

                    JSONObject jStandImageObj = jImageObj.getJSONObject("standard_resolution");
                    str_stndResolutionImg = jStandImageObj.getString("url");

                    instagramFeedArrayList.add(new InstagramFeed(str_id, str_commCnt, str_likesCnt, str_lowResolutionImg, str_stndResolutionImg, str_link, str_userName, str_profilePic, str_captiontext, str_type));
                }
            } else {
                ArrayList<InstagramFeed> tmp_instagramFeedArrayList = new ArrayList<>();
                for (int i = 0; i < jArrayData.length(); i++) {
                    JSONObject jObjData = jArrayData.getJSONObject(i);
                    str_link = jObjData.getString("link");
                    str_type = jObjData.getString("type");
                    str_id = jObjData.getString("id");

                    JSONObject jlikeObj = jObjData.getJSONObject("likes");
                    str_likesCnt = jlikeObj.getString("count");

                    JSONObject jCommObj = jObjData.getJSONObject("comments");
                    str_commCnt = jCommObj.getString("count");

                    JSONObject jCaptionObj = jObjData.getJSONObject("caption");
                    str_captiontext = jCaptionObj.getString("text");

                    JSONObject jUserObj = jObjData.getJSONObject("user");
                    str_userName = jUserObj.getString("username");
                    str_profilePic = jUserObj.getString("profile_picture");

                    JSONObject jImageObj = jObjData.getJSONObject("images");
                    JSONObject jLowImageObj = jImageObj.getJSONObject("low_resolution");
                    str_lowResolutionImg = jLowImageObj.getString("url");

                    JSONObject jStandImageObj = jImageObj.getJSONObject("standard_resolution");
                    str_stndResolutionImg = jStandImageObj.getString("url");

                    tmp_instagramFeedArrayList.add(new InstagramFeed(str_id, str_commCnt, str_likesCnt, str_lowResolutionImg, str_stndResolutionImg, str_link, str_userName, str_profilePic, str_captiontext, str_type));
                }
                instagramFeedArrayList.addAll(tmp_instagramFeedArrayList);
            }
            set_recycler();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void set_recycler() {
        try {
            if (!isLoading) {

                if (instagramFeedArrayList.size() == 0) {

                    rv_viewInstagram.setVisibility(View.GONE);
                    card_NoData.setVisibility(View.VISIBLE);
                } else {
                    rv_viewInstagram.setVisibility(View.VISIBLE);
                    card_NoData.setVisibility(View.GONE);
                    adapterInstagram = new Adapter_instagram(instagramFeedArrayList, rv_viewInstagram, linearLayoutManager, getActivity(), getActivity(), scrollView);
                    rv_viewInstagram.setAdapter(adapterInstagram);
                }
            } else {
                adapterInstagram.notifyDataSetChanged();
                adapterInstagram.setLoaded();
                isLoading = false;
            }

            if (instagramFeedArrayList.size() != 0)
                adapterInstagram.setOnLoadMoreListener(new OnLoadMoreListener() {

                    @Override
                    public void onLoadMore() {

                        page_count++;

                        if (page_count <= total_pages) {

                            progressBar1.setVisibility(View.VISIBLE);

                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    progressBar1.setVisibility(View.GONE);
                                    isLoading = true;
                                    try {
                                        if (GlobalData.isNetworkAvailable(getActivity()))
                                            buttongetFeed();
                                        else
                                            Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, 2000);
                        }

                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {

                    JSONObject jmainObject = new JSONObject(volleyResponse.output);
                    if (jmainObject.getString("success").equalsIgnoreCase("true")) {
                        if (sessionManager.isLogin()) {
                            pagewiseClick();
                        }
                        instagramFeedArrayList.clear();
                        total_pages = jmainObject.getInt("page_count");
                        JSONArray jArrayData = jmainObject.getJSONArray("data");

                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jArrayData.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jArrayData.toString(), tag);
                        }
                        loadInstagramData(jArrayData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jmainObject = new JSONObject(volleyResponse.output);
                    if (jmainObject.getString("success").equalsIgnoreCase("true")) {
                        total_pages = jmainObject.getInt("page_count");
                        JSONArray jArrayData = jmainObject.getJSONArray("data");
                        loadInstagramData(jArrayData);
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
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
