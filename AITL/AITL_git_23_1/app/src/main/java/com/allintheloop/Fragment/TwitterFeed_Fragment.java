package com.allintheloop.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.ViewActivityAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.ViewActivity;
import com.allintheloop.Util.OnLoadMoreListener;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.bumptech.glide.Glide;
import com.allintheloop.Adapter.TwitterFeedAdapter;
import com.allintheloop.Bean.twitterFeed;
import com.allintheloop.MainActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TwitterFeed_Fragment extends Fragment implements VolleyInterface {


    TextView txt_tagName, txt_Nofeed;
    RecyclerView rv_viewtweet;
    ArrayList<twitterFeed> feedArrayList;
    TwitterFeedAdapter feedAdapter;
    String id, name, time, screen_name, profile_image, media_url, feed_desc, tag_name;
    SessionManager sessionManager;
    CardView card_NoData;
    Button btn_load_more;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "twitterList";
    int total_pages, page_count = 1;
    boolean isFirstTime = false;
    boolean btn_load;
    WrapContentLinearLayoutManager linearLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    NestedScrollView scrollView;

    boolean isLoading;
    Handler handler;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data,MainLayout;
    LinearLayout linear_content;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    public TwitterFeed_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_twitterfeed, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager = new SessionManager(getActivity());
        txt_tagName = (TextView) rootView.findViewById(R.id.txt_tagName);
        txt_Nofeed = (TextView) rootView.findViewById(R.id.txt_Nofeed);
        btn_load_more = (Button) rootView.findViewById(R.id.btn_load_more);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        rv_viewtweet = (RecyclerView) rootView.findViewById(R.id.rv_viewtweet);
        rv_viewtweet.setNestedScrollingEnabled(false);
        feedArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        card_NoData = (CardView) rootView.findViewById(R.id.card_NoData);
        handler = new Handler();
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rv_viewtweet.setLayoutManager(linearLayoutManager);


        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);

        btn_load_more.setOnClickListener(v -> {

            if (page_count == total_pages) {
                btn_load_more.setVisibility(View.GONE);
            }
            if (GlobalData.isNetworkAvailable(getActivity())) {
                page_count++;
                btnLoadviewFeedApi();
            } else {
                ToastC.show(getActivity(), "No Internet Connection");
            }
        });


        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getTwitterList();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getTwitterList();
            }

        }
        getAdvertiesment();

        return rootView;
    }


    private void getAdvertiesment()
    {
        if(GlobalData.isNetworkAvailable(getActivity()))
        {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()),2, false, this);

        }
        else
        {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0)
            {
                if (cursor.moveToFirst())
                {
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

    private void getAdvertiesment(JSONObject jsonObject)
    {

        try {
            JSONObject jsonObjectAdavertiesment = jsonObject.getJSONObject("data");
            JSONArray jArrayHeader = jsonObjectAdavertiesment.getJSONArray("header");
            JSONArray jArrayFooter = jsonObjectAdavertiesment.getJSONArray("footer");

            topAdverViewArrayList = new ArrayList<>();
            bottomAdverViewArrayList = new ArrayList<>();
            for (int i = 0; i < jArrayHeader.length(); i++) {
                JSONObject index = jArrayHeader.getJSONObject(i);
                topAdverViewArrayList.add(new AdvertiesmentTopView(index.getString("image"), index.getString("url"),index.getString("id")));
            }

            for (int i = 0; i < jArrayFooter.length(); i++) {
                JSONObject index = jArrayFooter.getJSONObject(i);
                bottomAdverViewArrayList.add(new AdvertiesMentbottomView(index.getString("image"), index.getString("url"),index.getString("id")));
            }

            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1"))
            {
                sessionManager.footerView(getContext(),"0",MainLayout,relativeLayout_Data,linear_content,bottomAdverViewArrayList,getActivity());
                sessionManager.HeaderView(getContext(),"0",MainLayout,relativeLayout_Data,linear_content,topAdverViewArrayList,getActivity());
            }
            else
            {
                sessionManager.footerView(getContext(),"1",MainLayout,relativeLayout_Data,linear_content,bottomAdverViewArrayList,getActivity());
                sessionManager.HeaderView(getContext(),"1",MainLayout,relativeLayout_Data,linear_content,topAdverViewArrayList,getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btnLoadviewFeedApi() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.TwitterFeed, Param.getTwitterFeed(sessionManager.getEventId(), sessionManager.getEventType(), page_count, sessionManager.twitterHashTagName), 1, false, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (sessionManager.isLogin()) {
                        pagewiseClick();
                    }
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        feedArrayList.clear();
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                        }

                        loadData(jsonData);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        loadData(jsonData);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try
                {
                    JSONObject jsonObject=new JSONObject(volleyResponse.output);
                    pagewiseClick();
                    if (jsonObject.getString("success").equalsIgnoreCase("true"));
                    {
                        Log.d("AITL Advertiesment",jsonObject.toString());

                        if (sqLiteDatabaseHandler.isAdvertiesMentExist(sessionManager.getEventId(), sessionManager.getMenuid()))
                        {
                            sqLiteDatabaseHandler.deleteAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        }

                        getAdvertiesment(jsonObject);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

        }
    }


    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    private void getTwitterList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            isFirstTime = true;
            if (isFirstTime) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.TwitterFeed, Param.getTwitterFeed(sessionManager.getEventId(), sessionManager.getEventType(), page_count, sessionManager.twitterHashTagName), 0, false, this);
                isFirstTime = false;
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.TwitterFeed, Param.getTwitterFeed(sessionManager.getEventId(), sessionManager.getEventType(), page_count, sessionManager.twitterHashTagName), 0, false, this);
            }

        } else {
            btn_load_more.setVisibility(View.GONE);
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                        Log.d("AITL    Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                rv_viewtweet.setVisibility(View.GONE);
                card_NoData.setVisibility(View.VISIBLE);
                btn_load_more.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }


    private void loadData(JSONObject jsonData) {
        try {
            tag_name = jsonData.getString("hashtag");
            total_pages = jsonData.getInt("total_page");
            if (jsonData.has("twitter_data")) {
                txt_tagName.setText("#" + tag_name);
                JSONArray jArraystatus = jsonData.getJSONArray("twitter_data");
                if (!isLoading) {
                    for (int i = 0; i < jArraystatus.length(); i++) {
                        media_url = "";
                        JSONObject index = jArraystatus.getJSONObject(i);
                        id = index.getString("id_str");
                        time = index.getString("created_at");
                        SimpleDateFormat inFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
                        Date dateParse = inFormat.parse(time);
//
                        SimpleDateFormat outFormat = new SimpleDateFormat("d MMMM ,yyyy HH:mm:ss");
                        String day = outFormat.format(dateParse);
                        feed_desc = index.getString("text");
                        JSONObject jsonUser = index.getJSONObject("user");
                        name = jsonUser.getString("name");
                        screen_name = "http://www.twitter.com/" + jsonUser.getString("screen_name");
                        profile_image = jsonUser.getString("profile_image_url");

                        if (index.has("extended_entities")) {

                            JSONObject jsonextendedEntity = index.getJSONObject("extended_entities");
                            JSONArray jArrayMedia = jsonextendedEntity.getJSONArray("media");
                            for (int j = 0; j < jArrayMedia.length(); j++) {
                                JSONObject jsonmedia = jArrayMedia.getJSONObject(j);
                                media_url = jsonmedia.getString("media_url");
                            }
                        }
                        feedArrayList.add(new twitterFeed(id, name, feed_desc, day, screen_name, profile_image, media_url));
                    }
                } else {
                    ArrayList<twitterFeed> tmp_twitterArrayList = new ArrayList<>();
                    for (int i = 0; i < jArraystatus.length(); i++) {
                        media_url = "";
                        JSONObject index = jArraystatus.getJSONObject(i);
                        id = index.getString("id_str");
                        time = index.getString("created_at");
                        SimpleDateFormat inFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
                        Date dateParse = inFormat.parse(time);
//
                        SimpleDateFormat outFormat = new SimpleDateFormat("d MMMM ,yyyy HH:mm:ss");
                        String day = outFormat.format(dateParse);

                        feed_desc = index.getString("text");
                        JSONObject jsonUser = index.getJSONObject("user");
                        name = jsonUser.getString("name");
                        screen_name = "http://www.twitter.com/" + jsonUser.getString("screen_name");
                        profile_image = jsonUser.getString("profile_image_url");

                        if (index.has("extended_entities")) {

                            JSONObject jsonextendedEntity = index.getJSONObject("extended_entities");
                            JSONArray jArrayMedia = jsonextendedEntity.getJSONArray("media");
                            for (int j = 0; j < jArrayMedia.length(); j++) {
                                JSONObject jsonmedia = jArrayMedia.getJSONObject(j);
                                media_url = jsonmedia.getString("media_url");
                            }
                        }
                        tmp_twitterArrayList.add(new twitterFeed(id, name, feed_desc, day, screen_name, profile_image, media_url));
                    }
                    feedArrayList.addAll(tmp_twitterArrayList);

                }
                set_recycler();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void set_recycler() {

        try {
            if (!isLoading) {

                if (feedArrayList.size() == 0) {

                    rv_viewtweet.setVisibility(View.GONE);
                    card_NoData.setVisibility(View.VISIBLE);
                } else {
                    rv_viewtweet.setVisibility(View.VISIBLE);
                    card_NoData.setVisibility(View.GONE);
                    feedAdapter = new TwitterFeedAdapter(feedArrayList, rv_viewtweet, linearLayoutManager, getActivity(), getActivity(), scrollView);
                    rv_viewtweet.setAdapter(feedAdapter);

                }
            } else {

                feedAdapter.notifyDataSetChanged();
                feedAdapter.setLoaded();
                isLoading = false;
            }

            if (feedArrayList.size() != 0)
                feedAdapter.setOnLoadMoreListener(() -> {

                    page_count++;

                    if (page_count <= total_pages) {

                        feedAdapter.addFooter();

                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {

                                feedAdapter.removeFooter();

                                isLoading = true;

                                try {

                                    if (GlobalData.isNetworkAvailable(getActivity()))
                                        btnLoadviewFeedApi();
                                    else
                                        Toast.makeText(getActivity(), getString(R.string.noInernet), Toast.LENGTH_SHORT).show();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }


                            }
                        }, 2000);
                    }

                });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
