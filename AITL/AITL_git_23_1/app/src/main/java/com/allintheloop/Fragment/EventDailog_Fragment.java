package com.allintheloop.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Activity.LoginMainScreen;
import com.allintheloop.Activity.OnBoardScreenActivity;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventDailog_Fragment extends DialogFragment implements VolleyInterface {

    ImageView Imgclose, BackImgView;
    ImageView AppIcon;
    Button btnOpen;
    Bundle bundle;
    WebView webView;
    String WebStr = "", BackImgStr = "", LogoImgStr = "", AppName = "";
    TextView txtAppName;
    LinearLayout linear_transparent;
    View view;
    String Eventid, Token_id, event_type;
//    SharedPreferences preferences;

    SessionManager sessionManager;
    ProgressBar progressBar1;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor cursor;
    boolean isNetAvailable;

    String android_id;

    public EventDailog_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_event_dailog, container, false);
        bundle = getArguments();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        txtAppName = (TextView) rootView.findViewById(R.id.txtAppName);
        webView = (WebView) rootView.findViewById(R.id.webViewContent);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        Imgclose = (ImageView) rootView.findViewById(R.id.btnclose);
        AppIcon = (ImageView) rootView.findViewById(R.id.AppIcon);
        BackImgView = (ImageView) rootView.findViewById(R.id.BackImg);
        btnOpen = (Button) rootView.findViewById(R.id.btnOpen);
        linear_transparent = (LinearLayout) rootView.findViewById(R.id.linear_transparent);
        view = (View) rootView.findViewById(R.id.view);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        sessionManager = new SessionManager(getActivity());

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        // preferences=getActivity().getSharedPreferences(GlobalData.USER_PREFRENCE, Context.MODE_PRIVATE);

        Eventid = sessionManager.getEventId();
        Token_id = sessionManager.getToken();
        event_type = sessionManager.getEventType();

        Log.d("ListEventID", Eventid); // 71dd07494c5ee54992a27746d547e25dee01bd97
        //Toast.makeText(getActivity(), "Event ID :-" + Eventid, Toast.LENGTH_SHORT);


        android_id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        Log.d("AITL DEVICE ID", android_id);
        Imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2"))
//                {
//                    if (sessionManager.isLogin()) {
//                        getDialog().dismiss();
//                        startActivity(new Intent(getActivity(), MainActivity.class));
//                        getActivity().finish();
//                    } else {
//                        getDialog().dismiss();
//                        startActivity(new Intent(getActivity(), LoginActivity.class));
//                        getActivity().finish();
//                    }
//                } else {
//                    getDialog().dismiss();
//                    startActivity(new Intent(getActivity(), MainActivity.class));
//                    getActivity().finish();
//                }

                if (!(sessionManager.showOnce.equalsIgnoreCase(""))) {
                    sessionManager.showOnce = "";
                    if (sessionManager.getIsFirstTimeOnBoard()) {
                        if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
                            if (sessionManager.isLogin()) {
                                getDialog().dismiss();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            } else {
                                getDialog().dismiss();
                                startActivity(new Intent(getActivity(), LoginMainScreen.class));
                                getActivity().finish();
                            }
                        } else {
                            getDialog().dismiss();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                    } else {

                        if (sessionManager.showOnce.equalsIgnoreCase("0")) {
                            sessionManager.setIsFirstTimeOnBoard(false);
                        } else if (sessionManager.showOnce.equalsIgnoreCase("1")) {
                            sessionManager.setIsFirstTimeOnBoard(true);
                        }
                        getDialog().dismiss();
                        startActivity(new Intent(getActivity(), OnBoardScreenActivity.class));
                        getActivity().finish();
                    }

                    saveDeviceId();
                } else {
                    if (sessionManager.getEventType().equalsIgnoreCase("1") || sessionManager.getEventType().equalsIgnoreCase("2")) {
                        if (sessionManager.isLogin()) {
                            getDialog().dismiss();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        } else {
                            getDialog().dismiss();
                            startActivity(new Intent(getActivity(), LoginMainScreen.class));
                            getActivity().finish();
                        }
                    } else {
                        getDialog().dismiss();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                }
            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {

//            if (sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()), 5, false, this);
//            }
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.home_pageIndexUid, Param.EventDialog(Token_id, Eventid, event_type), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.home_pageIndex, Param.EventDialog(Token_id, Eventid, event_type), 0, false, this);
        } else {
            cursor = sqLiteDatabaseHandler.getEventHomeData(Eventid);
            if (cursor.moveToFirst()) {

                try {
                    JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.Home_EventData)));
                    JSONArray jArrayevent = jsonObject.getJSONArray("events");
                    loadData(jArrayevent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return rootView;
    }

    private void saveDeviceId() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.OpenApp, Param.saveDeviceId(sessionManager.getEventId(), android_id), 5, false, this);
        }
    }

    @Override
    public void getVolleyRequestResponse(final VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        isNetAvailable = true;
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        //JSONObject jsonEvent = jsonData.getJSONObject("events");
                        JSONArray jArrayevent = jsonData.getJSONArray("events");
                        Log.d("AITL JSonObj", jsonData.toString());

                        loadData(jArrayevent);
                        if (sqLiteDatabaseHandler.isEventDataExist(sessionManager.getEventId()))  // Use To store EventHomeData
                        {
                            sqLiteDatabaseHandler.UpdateEventHomeData(sessionManager.getEventId(), jsonData.toString());
                        } else {
//                            if (isNetAvailable)
//                            {
//                                sqLiteDatabaseHandler.deleteHomeData();
                            sqLiteDatabaseHandler.insertEventHomeData(sessionManager.getEventId(), jsonData.toString());
                            //           isNetAvailable = false;
                            //         }
                        }
                    } else {
                        ToastC.show(getContext(), jsonObject.optString(Param.KEY_MESSAGE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void loadData(JSONArray jArrayevent) {
        try {
            for (int e = 0; e < jArrayevent.length(); e++) {
                JSONObject jObjectevent = (JSONObject) jArrayevent.get(e);
                WebStr = jObjectevent.getString("description1");
                BackImgStr = jObjectevent.getString("Background_img1");
                LogoImgStr = jObjectevent.getString("Logo_images");
                AppName = jObjectevent.getString("Event_name");
            }
            Glide.with(getActivity()).load(MyUrls.Imgurl + LogoImgStr).centerCrop().fitCenter().placeholder(R.drawable.noimage).into(AppIcon);
            txtAppName.setText(AppName);

            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Lato_Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
            String pas = "</body></html>";
            String myHtmlString = pish + WebStr + pas;
            webView.loadDataWithBaseURL("file:///android_asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);

//            webView.loadData(WebStr, "text/html; charset=utf-8", "utf-8");
            if (LogoImgStr.equalsIgnoreCase("")) {
                progressBar1.setVisibility(View.GONE);
                AppIcon.setVisibility(View.VISIBLE);
                Glide.with(getActivity()).load(MyUrls.Imgurl + LogoImgStr).centerCrop().fitCenter().placeholder(R.drawable.noimage).into(AppIcon);
            } else {
                Glide.with(getActivity())
                        .load(MyUrls.Imgurl + LogoImgStr)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                AppIcon.setVisibility(View.GONE);
                                Glide.with(getActivity()).load(MyUrls.Imgurl + LogoImgStr).centerCrop().fitCenter().placeholder(R.drawable.noimage).into(AppIcon);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                AppIcon.setVisibility(View.VISIBLE);
                                return false;
                            }
                        })
                        .into(AppIcon);
            }

            Glide.with(getActivity())
                    .load(MyUrls.Imgurl + BackImgStr)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            view.setVisibility(View.GONE);
                            linear_transparent.setVisibility(View.GONE);
                            BackImgView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            view.setVisibility(View.VISIBLE);
                            linear_transparent.setVisibility(View.GONE);
                            BackImgView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .into(BackImgView);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("DAILOG", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("DAILOG", "onResume");

    }
}
