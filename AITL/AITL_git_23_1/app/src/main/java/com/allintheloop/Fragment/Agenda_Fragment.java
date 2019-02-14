package com.allintheloop.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Adapter.Agenda.AgendaPagerAdapter;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Agenda_Fragment extends Fragment implements VolleyInterface {

    TabLayout tabLayout;
    ViewPager viewPager;
    AgendaPagerAdapter agendaPagerAdapter;
    //SharedPreferences preferences;
    SessionManager sessionManager;
    public Button view_pendingagenda;
    Button view_agneda;
    LinearLayout button_layout;

    Bundle bundle;
    String adverties_id = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout, relative_welcomeScreen;
    LinearLayout linear_content;
    FrameLayout frame_mainLayout;
    DefaultLanguage.DefaultLang defaultLanguage;


    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;

    BoldTextView txt_next;
    WebView agenda_webview;
    String topBackColor, topTextColor, funTopBackColor, funTopTextColor;

    public Agenda_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);
        sessionManager = new SessionManager(getActivity());

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        view_agneda = rootView.findViewById(R.id.view_agenda);
        view_pendingagenda = rootView.findViewById(R.id.view_pendingagenda);
        button_layout = rootView.findViewById(R.id.button_layout);
        linear_content = rootView.findViewById(R.id.linear_content);
        relativeLayout_Data = rootView.findViewById(R.id.relativeLayout_Data);
        MainLayout = rootView.findViewById(R.id.MainLayout);
        frame_mainLayout = rootView.findViewById(R.id.frame_mainLayout);
        relativeLayout_forceLogin = rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = rootView.findViewById(R.id.MainLayout);
        button_layout = rootView.findViewById(R.id.button_layout);
        txt_next = rootView.findViewById(R.id.txt_next);
        relative_welcomeScreen = rootView.findViewById(R.id.relative_welcomeScreen);
        agenda_webview = rootView.findViewById(R.id.agenda_webview);
        tabLayout = rootView.findViewById(R.id.Agenda_tab_layout);
        viewPager = rootView.findViewById(R.id.Agenda_view_pager);
        defaultLanguage = sessionManager.getMultiLangString();


        bundle = new Bundle();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        GlobalData.currentModuleForOnResume = GlobalData.agendaModuleid;
        view_pendingagenda.setText(defaultLanguage.get1PendingAgenda());
        view_agneda.setText(defaultLanguage.get1ViewMyAgenda());


        topBackColor = sessionManager.getTopBackColor();
        topTextColor = sessionManager.getTopTextColor();
        funTopBackColor = sessionManager.getFunTopBackColor();
        funTopTextColor = sessionManager.getFunTopTextColor();

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);

        if (sessionManager.getHeaderStatus().equalsIgnoreCase("1")) {
            Log.d("AITL", "Fundraising Color");
            tabLayout.setTabTextColors(Color.parseColor(funTopBackColor), Color.parseColor(funTopTextColor));
            tabLayout.setBackgroundColor(Color.parseColor(funTopBackColor));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(funTopTextColor));

            view_agneda.setBackgroundColor(Color.parseColor(funTopBackColor));
            view_agneda.setTextColor(Color.parseColor(funTopTextColor));

            view_pendingagenda.setBackgroundColor(Color.parseColor(funTopBackColor));
            view_pendingagenda.setTextColor(Color.parseColor(funTopTextColor));
            drawable.setColor(Color.parseColor(funTopBackColor));
            txt_next.setBackground(drawable);
            txt_next.setTextColor(Color.parseColor(funTopTextColor));


        } else {
            Log.d("AITL", "EvenApp Color");
            tabLayout.setTabTextColors(Color.parseColor(topTextColor), Color.parseColor(topTextColor));
            tabLayout.setBackgroundColor(Color.parseColor(topBackColor));
            tabLayout.setSelectedTabIndicatorColor(Color.parseColor(topTextColor));

            view_agneda.setBackgroundColor(Color.parseColor(topBackColor));
            view_agneda.setTextColor(Color.parseColor(topTextColor));

            view_pendingagenda.setBackgroundColor(Color.parseColor(topBackColor));
            view_pendingagenda.setTextColor(Color.parseColor(topTextColor));

            drawable.setColor(Color.parseColor(topBackColor));
            txt_next.setBackground(drawable);
            txt_next.setTextColor(Color.parseColor(topTextColor));
        }


        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout_Data.setVisibility(View.VISIBLE);
                relative_welcomeScreen.setVisibility(View.GONE);
            }
        });


        if (sessionManager.isLogin()) {
            button_layout.setVisibility(View.VISIBLE);
            relativeLayout_forceLogin.setVisibility(View.GONE);
            frame_mainLayout.setVisibility(View.VISIBLE);
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            button_layout.setVisibility(View.GONE);
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                frame_mainLayout.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                frame_mainLayout.setVisibility(View.VISIBLE);
            }

        }
        view_agneda.setOnClickListener(v -> {

            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.View_UserWise_Agenda;
            ((MainActivity) getActivity()).loadFragment();
        });

        if (sessionManager.getPendingAgendaButton().equalsIgnoreCase("1")) {
            view_pendingagenda.setVisibility(View.VISIBLE);
        } else {
            view_pendingagenda.setVisibility(View.GONE);
        }

        view_pendingagenda.setOnClickListener(v -> {
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.Pending_AgendaFragment;
            ((MainActivity) getActivity()).loadFragment();
        });

        getWelcomeScreenData();


        agendaPagerAdapter = new AgendaPagerAdapter(getChildFragmentManager(), sessionManager);

        viewPager.setAdapter(agendaPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (GlobalData.is_user_agenda) {
            viewPager.setCurrentItem(2);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("AITL TAB POSITION ", "" + tab.getPosition());
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        getAdvertiesment();
//        YoYo.with(Techniques.FadeIn)
//                .duration(700)
//                .playOn(tabLayout);

        return rootView;

    }

    private void getWelcomeScreenData() {
        String welcomeData = sqLiteDatabaseHandler.getAgendaWelcomeScreenData(sessionManager.getEventId(), sessionManager.getAgendaCategoryId());
        Log.d("Bhavdip WelcomeDta", welcomeData);

        if (welcomeData.isEmpty()) {
            relativeLayout_Data.setVisibility(View.VISIBLE);
            relative_welcomeScreen.setVisibility(View.GONE);
        } else {
            relativeLayout_Data.setVisibility(View.GONE);
            relative_welcomeScreen.setVisibility(View.VISIBLE);

            agenda_webview.getSettings().setDefaultTextEncodingName("utf-8");
            agenda_webview.getSettings().setAllowFileAccess(true);
            agenda_webview.getSettings().setSupportMultipleWindows(true);
            agenda_webview.setWebChromeClient(new MyWebChromeclient());
            agenda_webview.setHorizontalScrollBarEnabled(true);
            agenda_webview.setVerticalScrollBarEnabled(true);
            agenda_webview.getSettings().setJavaScriptEnabled(true);

            agenda_webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    clickEvent(url);
                    return true;
                }
            });


            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: left;}</style></head><body>";
            String pas = "</body></html>";
            String myHtmlString = pish + welcomeData + pas;


            agenda_webview.loadDataWithBaseURL("file:///asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);
        }


    }


    private boolean clickEvent(String url) {
        if (url.startsWith("tel:")) {
            Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            startActivity(tel);
            return true;
        } else if (url.startsWith("mailto:")) {
            Intent mail = new Intent(Intent.ACTION_SEND);
            mail.setType("application/octet-stream");
            mail.putExtra(Intent.EXTRA_EMAIL, new String[]{"email address"});
            mail.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            mail.putExtra(Intent.EXTRA_TEXT, "");
            startActivity(mail);
            return true;
        } else {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(url));
            startActivity(browserIntent);
            return true;
        }

    }


    private class MyWebChromeclient extends WebChromeClient {


        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {

            WebView newWebView = new WebView(getActivity());
            view.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    clickEvent(url);
                    return true;
                }
            });
            return true;
        }
    }


    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 0, false, this);

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
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Log.d("AITL AITL ", jsonObject.toString());

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
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.agendaModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.agendaModuleid);
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

            sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
