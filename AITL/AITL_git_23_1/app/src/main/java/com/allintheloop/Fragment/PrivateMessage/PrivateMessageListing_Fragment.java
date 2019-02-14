package com.allintheloop.Fragment.PrivateMessage;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Adapter.UnreadPrivateListingAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.UnreadPrivateMessageList;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.OnLoadMoreListener;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateMessageListing_Fragment extends Fragment implements VolleyInterface {


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    TextView txt_topbar, txt_startNewConv, textwholeNoDATA;
    RecyclerView message_recycler_view;
    UnreadPrivateListingAdapter privateListingAdapter;
    ArrayList<UnreadPrivateMessageList> unreadPrivateMessageListArrayList;
    SessionManager sessionManager;
    int page_count = 1, total_pages;
    boolean isLoading;
    Handler handler;
    WrapContentLinearLayoutManager linearLayoutManager;
    CardView card_noconver;
    LinearLayout linear_data, linear_content;
    SwipeRefreshLayout mSwipeRefreshLayout;
    boolean isFirstTime;
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    DefaultLanguage.DefaultLang defaultLang;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;

    public PrivateMessageListing_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_private_message_listing, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        txt_topbar = (TextView) rootView.findViewById(R.id.txt_topbar);
        txt_startNewConv = (TextView) rootView.findViewById(R.id.txt_startNewConv);
        textwholeNoDATA = (TextView) rootView.findViewById(R.id.textwholeNoDATA);
        linear_data = (LinearLayout) rootView.findViewById(R.id.linear_data);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        message_recycler_view = (RecyclerView) rootView.findViewById(R.id.message_recycler_view);
        card_noconver = (CardView) rootView.findViewById(R.id.card_noconver);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        unreadPrivateMessageListArrayList = new ArrayList<>();
        linearLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        message_recycler_view.setLayoutManager(linearLayoutManager);
        handler = new Handler();
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        txt_startNewConv.setText(defaultLang.get12StartANewConversation());
        txt_topbar.setText(defaultLang.get12YourConversations());
        txt_topbar.setText(defaultLang.get12YourConversations());


        txt_startNewConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.getAllCommonData;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        message_recycler_view.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                UnreadPrivateMessageList msgObj = unreadPrivateMessageListArrayList.get(position);
                sessionManager.private_senderId = msgObj.getSender_id();
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.PrivateMessageSenderWiseDetail;
                ((MainActivity) getActivity()).loadFragment();
            }
        }));


        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                unreadPrivateMessageListArrayList.clear();

                page_count = 1;
                isFirstTime = false;
                getPrivateMessageListing();
            } else {
                ToastC.show(getActivity(), "No Internet Connection");
            }
        });


        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            txt_topbar.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
        } else {
            txt_topbar.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
        }

        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getPrivateMessageListing();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                card_noconver.setVisibility(View.VISIBLE);
                textwholeNoDATA.setText("Login or Sign Up to proceed. To sign up or login tap the Sign Up button on the top right of the screen.");
                linear_data.setVisibility(View.GONE);
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

    private void getPrivateMessageListing() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            isFirstTime = true;
            if (isFirstTime) {
                if (GlobalData.checkForUIDVersion())
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageListUid, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, false, this);
                else
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageList, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, false, this);
                isFirstTime = false;
            } else {
                if (GlobalData.checkForUIDVersion())
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageListUid, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, false, this);
                else
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageList, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 0, false, this);
            }

        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void buttonPrivateListing() {
        if (GlobalData.checkForUIDVersion())
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageListUid, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 1, false, this);
        else
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPrivateUnreadMessageList, Param.getAllprivateMessage(sessionManager.getEventId(), sessionManager.getUserId(), page_count, sessionManager.getToken()), 1, false, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        if (sessionManager.isLogin())


                            mSwipeRefreshLayout.setRefreshing(false);
                        total_pages = jsonObject.getInt("total_page");
                        unreadPrivateMessageListArrayList = new ArrayList<>();
                        JSONObject jsonData = jsonObject.getJSONObject("data");
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

    private void loadData(JSONObject jsonData) {
        try {

            JSONArray jArrayMessage = jsonData.getJSONArray("messages");
            if (!isLoading) {
                for (int i = 0; i < jArrayMessage.length(); i++) {
                    JSONObject index = jArrayMessage.getJSONObject(i);

                    unreadPrivateMessageListArrayList.add(new UnreadPrivateMessageList(index.getString("Sendername")
                            , index.getString("Sender_id")
                            , index.getString("Senderlogo")
                            , index.getString("unread_count")));
                }

            } else {
                ArrayList<UnreadPrivateMessageList> tmp_unreadPrivateMessageLists = new ArrayList<>();
                for (int i = 0; i < jArrayMessage.length(); i++) {
                    JSONObject index = jArrayMessage.getJSONObject(i);

                    tmp_unreadPrivateMessageLists.add(new UnreadPrivateMessageList(index.getString("Sendername")
                            , index.getString("Sender_id")
                            , index.getString("Senderlogo")
                            , index.getString("unread_count")));
                }
                unreadPrivateMessageListArrayList.addAll(tmp_unreadPrivateMessageLists);
            }

            set_recycler();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 5, false, this);
        }
    }

    private void set_recycler() {

        if (!isLoading) {
            Log.i("AAKASH", "IF LOAD " + isLoading + "");
            Log.d("AITL Message OUT IF", "SIZE" + unreadPrivateMessageListArrayList.size());
            if (unreadPrivateMessageListArrayList.size() != 0) {

                Log.d("AITL Message IF", "SIZE" + unreadPrivateMessageListArrayList.size());
                linear_data.setVisibility(View.VISIBLE);
                card_noconver.setVisibility(View.GONE);
                privateListingAdapter = new UnreadPrivateListingAdapter(message_recycler_view, linearLayoutManager, unreadPrivateMessageListArrayList, getActivity());
                message_recycler_view.setAdapter(privateListingAdapter);
                message_recycler_view.post(new Runnable() {
                    @Override
                    public void run() {

                        message_recycler_view.smoothScrollToPosition(0);
                    }
                });
            } else {
                Log.d("AITL Message ELSE", "SIZE" + unreadPrivateMessageListArrayList.size());
                card_noconver.setVisibility(View.VISIBLE);
                message_recycler_view.setVisibility(View.GONE);
                textwholeNoDATA.setText("Start new Conversations");
            }

        } else {
            Log.i("AAKASH", "ELSE LOAD " + isLoading + "");
            privateListingAdapter.notifyDataSetChanged();
            privateListingAdapter.setLoaded();
            isLoading = false;
        }

        if (unreadPrivateMessageListArrayList.size() != 0)
            privateListingAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {

                @Override
                public void onLoadMore() {

                    page_count++;

                    if (page_count <= total_pages) {

                        privateListingAdapter.addFooter();

                        handler.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                //   remove progress item
                                Log.i("AAKASH", "BEFORE LOAD" + isLoading + "");

                                privateListingAdapter.removeFooter();
                                //add items one by one

                                isLoading = true;
                                Log.i("AAKASH", "AFTER LOAD" + isLoading + "");

                                try {
                                    if (GlobalData.isNetworkAvailable(getActivity()))
                                        buttonPrivateListing();
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
    }


}
