package com.allintheloop.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.bumptech.glide.Glide;
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

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Social_Fragment extends Fragment implements VolleyInterface {

    TextView txt_facebook, txt_twitter, txt_instagram, txt_linkedin, txt_pintrest, txt_youtube;
    String facebook_link, twitter_link, insta_link, linkedin_link, pintrest_link, youtube_link;
    TextView textViewNoDATA;
    SessionManager sessionManager;


    Bundle bundle;
    String tag = "Social";
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String adverties_id = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    LinearLayout linear_content;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;

    public Social_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_social, container, false);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);


        txt_facebook = (TextView) rootView.findViewById(R.id.facebook_name);
        txt_twitter = (TextView) rootView.findViewById(R.id.twitter_name);
        txt_instagram = (TextView) rootView.findViewById(R.id.instagram_name);
        txt_linkedin = (TextView) rootView.findViewById(R.id.linkedin_name);
        txt_pintrest = (TextView) rootView.findViewById(R.id.pinterest_name);
        txt_youtube = (TextView) rootView.findViewById(R.id.youtube_name);


        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        sessionManager = new SessionManager(getActivity());
        bundle = new Bundle();

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();


        bundle = new Bundle();


        txt_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sessionManager.strModuleId = facebook_link;
                bundle.putString("Social_url", facebook_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                Log.d("AITL", "Push" + GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });

        txt_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.strModuleId = twitter_link;
                bundle.putString("Social_url", twitter_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });
        txt_instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.strModuleId = insta_link;
                bundle.putString("Social_url", insta_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });
        txt_linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.strModuleId = linkedin_link;
                bundle.putString("Social_url", linkedin_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });
        txt_pintrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.strModuleId = pintrest_link;
                bundle.putString("Social_url", pintrest_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });
        txt_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.strModuleId = youtube_link;
                bundle.putString("Social_url", youtube_link);
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Webview_fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });


        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getSocialList();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getSocialList();
            }

        }
        getAdvertiesment();
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("AITL TAG", tag);

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

    private void getSocialList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL  SOCIAL Oflline", jsonArray.toString());
                        loadData(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SocialList, Param.getSocialList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_SocialList, Param.getSocialList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 0, false, this);
            }

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONArray jsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL  SOCIAL Oflline", jsonArray.toString());
                        loadData(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                textViewNoDATA.setVisibility(View.VISIBLE);
                txt_facebook.setVisibility(View.GONE);
                txt_twitter.setVisibility(View.GONE);
                txt_instagram.setVisibility(View.GONE);
                txt_linkedin.setVisibility(View.GONE);
                txt_pintrest.setVisibility(View.GONE);
                txt_youtube.setVisibility(View.GONE);
            }
        }
    }

    private void loadData(JSONArray jsonArray) {
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonsocialList = jsonArray.getJSONObject(i);
                facebook_link = jsonsocialList.getString("facebook_url");
                twitter_link = jsonsocialList.getString("twitter_url");
                insta_link = jsonsocialList.getString("instagram_url");
                linkedin_link = jsonsocialList.getString("linkedin_url");
                pintrest_link = jsonsocialList.getString("pinterest_url");
                youtube_link = jsonsocialList.getString("youtube_url");

            }


            if (jsonArray.length() == 0) {
                textViewNoDATA.setVisibility(View.VISIBLE);
                txt_facebook.setVisibility(View.GONE);
                txt_twitter.setVisibility(View.GONE);
                txt_instagram.setVisibility(View.GONE);
                txt_linkedin.setVisibility(View.GONE);
                txt_pintrest.setVisibility(View.GONE);
                txt_youtube.setVisibility(View.GONE);
            } else {
                textViewNoDATA.setVisibility(View.GONE);

                if (facebook_link.equalsIgnoreCase("")) {
                    txt_facebook.setVisibility(View.GONE);
                } else {
                    txt_facebook.setVisibility(View.VISIBLE);
                }


                if (twitter_link.equalsIgnoreCase("")) {
                    txt_twitter.setVisibility(View.GONE);
                } else {
                    txt_twitter.setVisibility(View.VISIBLE);
                }

                if (insta_link.equalsIgnoreCase("")) {
                    txt_instagram.setVisibility(View.GONE);
                } else {
                    txt_instagram.setVisibility(View.VISIBLE);
                }


                if (linkedin_link.equalsIgnoreCase("")) {
                    txt_linkedin.setVisibility(View.GONE);
                } else {
                    txt_linkedin.setVisibility(View.VISIBLE);
                }


                if (pintrest_link.equalsIgnoreCase("")) {
                    txt_pintrest.setVisibility(View.GONE);
                } else {
                    txt_pintrest.setVisibility(View.VISIBLE);
                }


                if (youtube_link.equalsIgnoreCase("")) {
                    txt_youtube.setVisibility(View.GONE);
                } else {
                    txt_youtube.setVisibility(View.VISIBLE);
                }
            }

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
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        pagewiseClick();
                        if (jsonObject.has("data")) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("social_list");

                            if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                                sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                                sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString(), tag);
                            } else {
                                sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString(), tag);
                            }
                            loadData(jsonArray);
                        }
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
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

