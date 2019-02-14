package com.allintheloop.Fragment.PresantationModule;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Presantation_Detail_Adapter;
import com.allintheloop.Adapter.PresentationImageAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.AppController;
import com.allintheloop.Util.Client;
import com.allintheloop.Util.CustomViewPager;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.allintheloop.Util.GlobalData.PresantationSendMessage;
import static com.allintheloop.Util.GlobalData.SocketIoException;
import static com.allintheloop.Util.GlobalData.Update_presantation;

/**
 * A simple {@link Fragment} subclass.
 */
public class Presantation_Detail_Fragment extends Fragment implements VolleyInterface {


    public static String presantation_id;
    public static Timer timer, swipeTimer;
    public static TextView txt_lock;
    public static ImageView img_unlock;
    public static LinearLayout linear_unlock, linear_ViewResult, linear_ViewbarChart, linear_push, linear_mode;
    public static TextView txt_mode;
    public static String str_type = "", strLock_image, strPush_image, strPush_result;
    public static String image_name = "";
    public static Client myClient;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public CustomViewPager viewPager;
    public ArrayList<Exhibitor_DetailImage> viewpager_arrayList;
    public Presantation_Detail_Adapter imgdetail_adapter;
    public TextView txt_push, txt_question, txt_viewResult, txt_next, txt_previous, txt_help;
    public LinearLayout linear_Topaction, linear_bottomAction, linear_question, linear_help, linear_previous, linear_next;
    public String is_presenter = "false";
    public String current_type = "";
    public String type_data = "";
    RecyclerView rv_imageview;
    PresentationImageAdapter imgadapter;
    SessionManager sessionManager;
    Bundle bundle;
    String currentDate, currentTime;
    String id, end_date, end_time, status, thumb_status, auto_status;
    TextView textViewNoDATA;
    ArrayList<String> arrayListImgLock;
    ArrayList<String> arrayListImg;
    int cnt = 0;
    Dialog help_dialog;
    ImageView dailog_help_close;
    String isLockResult;
    DisplayMetrics displaymetrics;
    LinearLayout linear_data;
    CardView card_nopresan;
    String android_id;
    int isLast = 0, totalImage = 0, imageCount = 0;
    SQLiteDatabaseHandler databaseHandler;
    ArrayList<String> imageArraySize;
    ProgressDialog progressDialog;
    boolean txt_isLock = true;


    List<String> permissionsNeeded;
    List<String> permissionsList;
    private int selectedItem = 0;
    private BroadcastReceiver presanationSendMessage = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d("AITL PRESANTATION", "SendMessage Called");
            // TODO Auto-generated method stub
//            sendMessage();
            PresantatorSocket();
        }
    };
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d("AITL PRESANTATION", "Reciver Called : ");
            // TODO Auto-generated method stub
            refreshPresantation();
        }
    };
    private BroadcastReceiver socketConnectionUpdate = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d("AITL PRESANTATION", "SOCKET Called : ");
            // TODO Auto-generated method stub
            socketConnected();
        }
    };

    public Presantation_Detail_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_presantation__detail, container, false);
        viewpager_arrayList = new ArrayList<>();
        arrayListImgLock = new ArrayList<>();
        arrayListImg = new ArrayList<>();
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        viewPager = (CustomViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);
        imageArraySize = new ArrayList<>();
        databaseHandler = new SQLiteDatabaseHandler(getActivity());

        progressDialog = new ProgressDialog(getActivity());
        rv_imageview = (RecyclerView) rootView.findViewById(R.id.rv_imageview);
//        action_button = (Button) rootView.findViewById(R.id.action_button);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        txt_lock = (TextView) rootView.findViewById(R.id.txt_lock);
        img_unlock = (ImageView) rootView.findViewById(R.id.img_unlock);
        txt_push = (TextView) rootView.findViewById(R.id.txt_push);
        txt_mode = (TextView) rootView.findViewById(R.id.txt_mode);
        txt_question = (TextView) rootView.findViewById(R.id.txt_question);
        txt_viewResult = (TextView) rootView.findViewById(R.id.txt_viewResult);
        txt_previous = (TextView) rootView.findViewById(R.id.txt_previous);
        txt_next = (TextView) rootView.findViewById(R.id.txt_next);
        txt_help = (TextView) rootView.findViewById(R.id.txt_help);
        linear_Topaction = (LinearLayout) rootView.findViewById(R.id.linear_Topaction);
        linear_bottomAction = (LinearLayout) rootView.findViewById(R.id.linear_bottomAction);
        linear_unlock = (LinearLayout) rootView.findViewById(R.id.linear_unlock);
        linear_question = (LinearLayout) rootView.findViewById(R.id.linear_question);
        linear_ViewResult = (LinearLayout) rootView.findViewById(R.id.linear_ViewResult);
        linear_ViewbarChart = (LinearLayout) rootView.findViewById(R.id.linear_ViewbarChart);
        linear_help = (LinearLayout) rootView.findViewById(R.id.linear_help);
        linear_previous = (LinearLayout) rootView.findViewById(R.id.linear_previous);
        linear_push = (LinearLayout) rootView.findViewById(R.id.linear_push);
        linear_next = (LinearLayout) rootView.findViewById(R.id.linear_next);
        linear_mode = (LinearLayout) rootView.findViewById(R.id.linear_mode);
        linear_data = (LinearLayout) rootView.findViewById(R.id.linear_data);
        card_nopresan = (CardView) rootView.findViewById(R.id.card_nopresan);
        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        sessionManager = new SessionManager(getActivity());
        bundle = getArguments();
        if (bundle.containsKey("presantation_id")) {
            presantation_id = bundle.getString("presantation_id");
            sessionManager.strModuleId = presantation_id;
//            sessionManager.setMenuid("9");
            sessionManager.strMenuId = "9";
        }


        sessionManager.setAndroidId(android_id);
        sessionManager.setPresantationId(presantation_id);

        if (!(databaseHandler.isPresantationDetailIdExist(sessionManager.getEventId(), presantation_id))) {
            databaseHandler.insertPresantationDetailId(presantation_id, sessionManager.getEventId());
            sessionManager.setIsFirstTimeLoadPresantation("0");
        } else {
            sessionManager.setIsFirstTimeLoadPresantation("1");
        }

        displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        rv_imageview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                viewPager.setCurrentItem(position, true);
                selectImage(position);
                imgdetail_adapter.changeColor(currentPage);
            }
        }));


        viewPager.setVisibility(View.VISIBLE);
        viewPager.setOffscreenPageLimit(viewpager_arrayList.size());
        imgadapter = new PresentationImageAdapter(getActivity(), viewpager_arrayList, viewPager);
        viewPager.setAdapter(imgadapter);

        imgdetail_adapter = new Presantation_Detail_Adapter(viewpager_arrayList, getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_imageview.setLayoutManager(layoutManager);
        rv_imageview.setItemAnimator(new DefaultItemAnimator());
        rv_imageview.setAdapter(imgdetail_adapter);


        linear_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                help_dialog = new Dialog(getActivity());
                help_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                help_dialog.setContentView(R.layout.presantation_help_dialog);
                dailog_help_close = (ImageView) help_dialog.findViewById(R.id.dailog_help_close);
                dailog_help_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        help_dialog.dismiss();
                    }
                });
                help_dialog.show();
            }
        });

        getHeightAndWidth();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                selectImage(position);
                imgdetail_adapter.changeColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        linear_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(currentPage + 1, true);
                selectImage(currentPage);
                imgdetail_adapter.changeColor(currentPage);
            }
        });

        linear_previous.setOnClickListener(view -> {
            if (currentPage != 0) {
                viewPager.setCurrentItem(currentPage - 1, true);
                selectImage(currentPage);
                imgdetail_adapter.changeColor(currentPage);
            }
        });


        linear_unlock.setOnClickListener(view -> {
            if (GlobalData.isNetworkAvailable(getActivity())) {
                lockUnlock();
            } else {
                ToastC.show(getActivity(), "No Internet Connection");
            }
        });

//        getPresantationDetail();

        return rootView;
    }

    private void lockUnlock() {
        if (txt_isLock) {
            if (!(image_name.equalsIgnoreCase(""))) {
                img_unlock.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_open_lock));
                txt_lock.setText("UnLocked");

                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.presantationDetailLockUnlock, Param.get_Presantation_DetailLockUnlock(sessionManager.getEventId(), "0", presantation_id), 5, true, this);


                txt_isLock = false;
            } else {
                ToastC.show(getActivity(), "Please Select Image Slide");
            }
        } else {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.presantationDetailLockUnlock, Param.get_Presantation_DetailLockUnlock(sessionManager.getEventId(), image_name, presantation_id), 5, true, this);
            img_unlock.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lock));
            txt_lock.setText("locked");
            txt_isLock = true;
        }
    }

    private void isFirstTimeLock() {
        if ((!strLock_image.equalsIgnoreCase(""))) {

            img_unlock.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_lock));
            txt_lock.setText("Locked");
            txt_isLock = true;
        } else {
            img_unlock.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_open_lock));
            txt_lock.setText("Unlocked");
            txt_isLock = false;
        }

    }

    private void socketConnected() {
        if (is_presenter.equalsIgnoreCase("false")) {
            Client myClient = new Client(getActivity());
            myClient.execute();
        }
    }

    private void getPresantationDetail() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Presantation_DetailUid, Param.get_Presantation_Detail(sessionManager.getEventId(), sessionManager.getUserId(), presantation_id, sessionManager.getToken()), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Presantation_Detail, Param.get_Presantation_Detail(sessionManager.getEventId(), sessionManager.getUserId(), presantation_id, sessionManager.getToken()), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Activity a = getActivity();
            if (a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }


    private void getHeightAndWidth() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                width,
                height - (((MainActivity) getActivity()).getSupportActionBar().getHeight() + result));

        viewPager.setLayoutParams(layoutParams);
    }

    private void getHeight() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                width,
                height);
        viewPager.setLayoutParams(layoutParams);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

            getHeight();
            if (is_presenter.equalsIgnoreCase("true")) {
                linear_Topaction.setVisibility(View.VISIBLE);
                linear_bottomAction.setVisibility(View.VISIBLE);
                rv_imageview.setVisibility(View.VISIBLE);
                linear_mode.setVisibility(View.VISIBLE);
            } else {
                linear_Topaction.setVisibility(View.GONE);
                linear_bottomAction.setVisibility(View.GONE);
                rv_imageview.setVisibility(View.GONE);
                linear_mode.setVisibility(View.GONE);
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
            getHeightAndWidth();
            if (is_presenter.equalsIgnoreCase("true")) {
                linear_Topaction.setVisibility(View.VISIBLE);
                linear_bottomAction.setVisibility(View.VISIBLE);
                rv_imageview.setVisibility(View.VISIBLE);
                linear_mode.setVisibility(View.VISIBLE);
            } else {
                linear_Topaction.setVisibility(View.GONE);
                linear_bottomAction.setVisibility(View.GONE);
                rv_imageview.setVisibility(View.GONE);
                linear_mode.setVisibility(View.GONE);
            }

        }

    }

    public void selectImage(int position) {

        try {
            Exhibitor_DetailImage exhibitor_detailImageObj = viewpager_arrayList.get(position);

            for (int i = 0; i < viewpager_arrayList.size(); i++) {

                if (i == position) {
                    if (exhibitor_detailImageObj.getType().equalsIgnoreCase("image")) {
                        image_name = exhibitor_detailImageObj.getImage_link();
                        Log.d("AITL ImageName", image_name);
                    } else if (exhibitor_detailImageObj.getType().equalsIgnoreCase("survay")) {
                        try {
                            JSONObject jsonObject = new JSONObject(exhibitor_detailImageObj.getImage_link());
                            image_name = jsonObject.getString("Id");
                            Log.d("AITL SurvetId", image_name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    viewpager_arrayList.get(i).setIsChanged(true);
                } else
                    viewpager_arrayList.get(i).setIsChanged(false);
            }
            imgdetail_adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONArray jArrayPresan = jsonObject.getJSONArray("presentation");
                        is_presenter = jsonObject.getString("is_presenter");
                        sessionManager.setIsPresantor(jsonObject.getString("is_presenter"));
                        if (is_presenter.equalsIgnoreCase("false")) {
                            socketConnected();
                        }
                        if (jArrayPresan.length() == 0) {
                            linear_data.setVisibility(View.GONE);
                            card_nopresan.setVisibility(View.VISIBLE);
                        } else {
                            linear_data.setVisibility(View.VISIBLE);
                            card_nopresan.setVisibility(View.GONE);


                            for (int i = 0; i < jArrayPresan.length(); i++) {
                                JSONObject index = jArrayPresan.getJSONObject(i);
                                id = index.getString("Id");
                                end_date = index.getString("End_date");
                                end_time = index.getString("End_time");
                                status = index.getString("Status");
                                thumb_status = index.getString("Thumbnail_status");
                                auto_status = index.getString("Auto_slide_status");
                                str_type = index.getString("type");
                                strLock_image = index.getString("lock_image");
                                strPush_image = index.getString("push_image");
                                strPush_result = index.getString("push_result");
                                isLockResult = index.getString("is_locked_result");
                                isFirstTimeLock();
                                JSONArray jsonimg_lock = index.getJSONArray("Image_lock");
                                for (int j = 0; j < jsonimg_lock.length(); j++) {
                                    arrayListImgLock.add(jsonimg_lock.getString(j).toString());
                                }

                                JSONArray jArrayImg = index.getJSONArray("Images");
                                for (int k = 0; k < jArrayImg.length(); k++) {
//                                arrayListImg.add(jArrayImg.get(k).toString());
                                    if (arrayListImgLock.get(k).equals("1")) {
                                        JSONObject jsonImgObject = jArrayImg.getJSONObject(k);
                                        viewpager_arrayList.add(new Exhibitor_DetailImage(jsonImgObject.getString("temp_value"), "Presantation", false, jsonImgObject.getString("type"), jsonImgObject.getString("chart_type")));
                                    }
                                }
                            }
                            if (status.equalsIgnoreCase("1")) {
                                try {
                                    if (viewpager_arrayList.size() == 0) {
                                        viewPager.setVisibility(View.GONE);
                                    } else {
                                        viewPager.setVisibility(View.VISIBLE);
                                        if (sessionManager.getIsFirstTimeLoadPresantation().equalsIgnoreCase("0")) {

                                            getPresentationImage(jArrayPresan);

                                        } else {
                                            imgadapter.notifyDataSetChanged();
                                            imgdetail_adapter.notifyDataSetChanged();
                                            selectImage(currentPage);
                                            imgdetail_adapter.changeColor(currentPage);

                                            if (is_presenter.equalsIgnoreCase("false")) {
                                                setDataOtherUserdata();
                                            }

                                        }

                                        if (is_presenter.equalsIgnoreCase("true")) {
                                            linear_Topaction.setVisibility(View.VISIBLE);
                                            linear_bottomAction.setVisibility(View.VISIBLE);
                                            rv_imageview.setVisibility(View.VISIBLE);
                                            linear_mode.setVisibility(View.VISIBLE);
                                        } else {

                                            linear_Topaction.setVisibility(View.GONE);
                                            linear_bottomAction.setVisibility(View.GONE);
                                            rv_imageview.setVisibility(View.GONE);
                                            linear_mode.setVisibility(View.GONE);
                                        }


//                                            Log.d("AITL CURRENT PAGE FIRST CALL", "" + currentPage);
                                        if (auto_status.equalsIgnoreCase("1")) {
                                            try {
                                                NUM_PAGES = viewpager_arrayList.size();
//                                                Log.d("AITL Handler Size", "" + NUM_PAGES);
                                                final Handler handler = new Handler();

                                                final Runnable Update = new Runnable() {
                                                    public void run() {
                                                        if (currentPage == NUM_PAGES) {
                                                            currentPage = 0;
                                                        }
                                                        viewPager.setCurrentItem(currentPage++, true);
                                                        selectImage(currentPage);
                                                        imgdetail_adapter.changeColor(currentPage);
                                                    }
                                                };
                                                swipeTimer = new Timer();
                                                swipeTimer.schedule(new TimerTask() {
                                                    @Override
                                                    public void run() {
                                                        handler.post(Update);
                                                    }
                                                }, 5000, 3000);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                textViewNoDATA.setVisibility(View.VISIBLE);
                                rv_imageview.setVisibility(View.GONE);
//                            action_button.setVisibility(View.GONE);
                                viewPager.setVisibility(View.GONE);
                                if (is_presenter.equalsIgnoreCase("false")) {
                                    setDataOtherUserdata();
                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        ArrayList<Exhibitor_DetailImage> temp_viewpager_arrayList = new ArrayList<>();
                        ArrayList<String> temp_arrayListImgLock = new ArrayList<>();

                        JSONArray jArrayPresan = jsonObject.getJSONArray("presentation");
                        is_presenter = jsonObject.getString("is_presenter");
                        if (is_presenter.equalsIgnoreCase("false")) {
                            socketConnected();
                        }
                        for (int i = 0; i < jArrayPresan.length(); i++) {

                            JSONObject index = jArrayPresan.getJSONObject(i);

                            id = index.getString("Id");
                            end_date = index.getString("End_date");
                            end_time = index.getString("End_time");
                            status = index.getString("Status");
                            thumb_status = index.getString("Thumbnail_status");
                            auto_status = index.getString("Auto_slide_status");

                            str_type = index.getString("type");
                            strLock_image = index.getString("lock_image");
                            strPush_image = index.getString("push_image");
                            strPush_result = index.getString("push_result");
                            isLockResult = index.getString("is_locked_result");
                            isFirstTimeLock();

                            JSONArray jsonimg_lock = index.getJSONArray("Image_lock");

                            for (int j = 0; j < jsonimg_lock.length(); j++) {

                                temp_arrayListImgLock.add(jsonimg_lock.getString(j).toString());
                            }

                            JSONArray jArrayImg = index.getJSONArray("Images");

                            for (int k = 0; k < jArrayImg.length(); k++) {

                                if (temp_arrayListImgLock.get(k).equals("1")) {

                                    JSONObject jsonImgObject = jArrayImg.getJSONObject(k);
                                    //imgadapter.addItem(new Exhibitor_DetailImage(jsonImgObject.getString("value"), "Presantation", false, jsonImgObject.getString("type")));
                                    temp_viewpager_arrayList.add(new Exhibitor_DetailImage(jsonImgObject.getString("temp_value"), "Presantation", false, jsonImgObject.getString("type"), jsonImgObject.getString("chart_type")));
                                }
                            }
                        }
                        viewpager_arrayList = new ArrayList<>();
                        if (arrayListImgLock.size() != temp_arrayListImgLock.size()) {
                            imgadapter.removeAll();
                            arrayListImgLock.clear();
                            arrayListImgLock.addAll(temp_arrayListImgLock);
                            imgadapter.addItem(temp_viewpager_arrayList);
                            viewpager_arrayList.addAll(temp_viewpager_arrayList);
                            NUM_PAGES = viewpager_arrayList.size();

                            if (is_presenter.equalsIgnoreCase("false")) {
                                imgadapter.notifyDataSetChanged();
                                setDataOtherUserdata();
                                linear_mode.setVisibility(View.GONE);
                            } else {
                                selectImage(currentPage);
                                imgdetail_adapter.changeColor(currentPage);
                                imgdetail_adapter.notifyDataSetChanged();
                                linear_mode.setVisibility(View.VISIBLE);
                            }
                        } else {
                            imgadapter.removeAll();
                            imgadapter.addItem(temp_viewpager_arrayList);
                            viewpager_arrayList.addAll(temp_viewpager_arrayList);
                            NUM_PAGES = viewpager_arrayList.size();
                            if (is_presenter.equalsIgnoreCase("false")) {
                                imgadapter.notifyDataSetChanged();
                                setDataOtherUserdata();
                                linear_mode.setVisibility(View.GONE);
                            } else {
                                selectImage(currentPage);
                                imgdetail_adapter.changeColor(currentPage);
                                imgdetail_adapter.notifyDataSetChanged();
                                linear_mode.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        PresantatorSocket();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }

    private void setDataOtherUserdata() {

        try {
            if (viewpager_arrayList.size() > 0) {

                for (int i = 0; i < viewpager_arrayList.size(); i++) {

                    if (str_type.equalsIgnoreCase("image")) {
                        current_type = str_type;

                        if (!(strLock_image.equalsIgnoreCase(""))) {
                            if (viewpager_arrayList.get(i).getImage_link().toString().equalsIgnoreCase(strLock_image)) {

                                type_data = strLock_image;

                                viewPager.setCurrentItem(i);

                                if (isLockResult.equalsIgnoreCase("0")) {
                                    viewPager.setPagingEnabled(true);
                                } else {
                                    viewPager.setPagingEnabled(false);
                                }
                            }
                        } else {
                            if (viewpager_arrayList.get(i).getImage_link().toString().equalsIgnoreCase(strPush_image)) {

                                type_data = strPush_image;

                                viewPager.setCurrentItem(i);
                                if (isLockResult.equalsIgnoreCase("0")) {
                                    viewPager.setPagingEnabled(true);
                                } else {
                                    viewPager.setPagingEnabled(false);
                                }
                            }
                        }
                    } else if (str_type.equalsIgnoreCase("survay")) {
                        current_type = str_type;

                        if (!(strLock_image.equalsIgnoreCase(""))) {
                            if (viewpager_arrayList.get(i).getType().toString().equalsIgnoreCase("survay")) {
//                                Log.d("AITL TYPE", viewpager_arrayList.get(i).getType().toString());
                                try {
//                                    Log.d("AITL IMAGELINK", viewpager_arrayList.get(i).getImage_link().toString());
                                    JSONObject jsonObject = new JSONObject(viewpager_arrayList.get(i).getImage_link().toString());
                                    String question_id = jsonObject.getString("Id");
                                    type_data = question_id;
                                    if (question_id.equalsIgnoreCase(strLock_image)) {
//                                        Log.d("AITL COUNT", "" + i);
//                                        Log.d("AITL QUESTIONID", question_id);
                                        viewPager.setCurrentItem(i);

                                        if (isLockResult.equalsIgnoreCase("0")) {
                                            viewPager.setPagingEnabled(true);
                                        } else {
                                            viewPager.setPagingEnabled(false);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            if (viewpager_arrayList.get(i).getType().toString().equalsIgnoreCase("survay")) {
//                                Log.d("AITL TYPE", viewpager_arrayList.get(i).getType().toString());
                                try {
//                                    Log.d("AITL IMAGELINK", viewpager_arrayList.get(i).getImage_link().toString());
                                    JSONObject jsonObject = new JSONObject(viewpager_arrayList.get(i).getImage_link().toString());
                                    String question_id = jsonObject.getString("Id");
                                    type_data = question_id;
                                    if (question_id.equalsIgnoreCase(strPush_image)) {
//                                        Log.d("AITL COUNT", "" + i);
//                                        Log.d("AITL QUESTIONID", question_id);
                                        viewPager.setCurrentItem(i);

                                        if (isLockResult.equalsIgnoreCase("0")) {
                                            viewPager.setPagingEnabled(true);
                                        } else {
                                            viewPager.setPagingEnabled(false);
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else if (str_type.equalsIgnoreCase("result")) {
                        current_type = str_type;

                        if (viewpager_arrayList.get(i).getType().toString().equalsIgnoreCase("result")) {
//                            Log.d("AITL ARRAY OF IMAGE", "" + viewpager_arrayList.size());
//
//                            Log.d("AITL TYPE", viewpager_arrayList.get(i).getType().toString());
                            try {
//                                Log.d("AITL IMAGELINK", viewpager_arrayList.get(i).getImage_link().toString());
                                String id = viewpager_arrayList.get(i).getImage_link().toString();
//                                Log.d("AITL ID CHECK", id);
//                                Log.d("AITL ID PUSHRESULT", strPush_result);
                                type_data = id;

//                                Log.d("AITL COUNT", "" + i);
//                                Log.d("AITL QUESTIONID", id);
                                viewPager.setCurrentItem(0);
                                if (isLockResult.equalsIgnoreCase("0")) {
                                    viewPager.setPagingEnabled(true);
                                } else {
                                    viewPager.setPagingEnabled(false);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void PresantatorSocket() {
        Client myClient = new Client(getActivity());
        myClient.execute();
    }

    @Override
    public void onPause() {

//        Log.d("AITL CALL PAUSE", "PAUSE");
        if (timer != null) {
            timer.cancel();
        }
        if (swipeTimer != null) {
            swipeTimer.cancel();
        }
        super.onPause();
    }

    private void refreshPresantation() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPresantationImageRefresh_DetailUid, Param.get_PresantationRefresh_Detail(sessionManager.getEventId(), presantation_id, sessionManager.getUserId()), 1, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPresantationImageRefresh_Detail, Param.get_PresantationRefresh_Detail(sessionManager.getEventId(), presantation_id, sessionManager.getUserId()), 1, false, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(Update_presantation));
        getActivity().registerReceiver(socketConnectionUpdate, new IntentFilter(SocketIoException));
        getActivity().registerReceiver(presanationSendMessage, new IntentFilter(PresantationSendMessage));
        currentPage = 0;
//        NUM_PAGES=0;


        if (Build.VERSION.SDK_INT >= 23) {
            if (isCameraPermissionGranted()) {
                getPresantationDetail();
            } else {
                requestPermission();
            }
        } else {
            getPresantationDetail();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(socketConnectionUpdate);
        getActivity().unregisterReceiver(presanationSendMessage);
//        Log.d("AITL CALL STOP", "STOP");
        if (timer != null) {
            timer.cancel();
        }
        if (AppController.socket != null) {
            AppController.getInstance().socketDisconnected();
        }
//        myClient.stopsocket();
    }

    private void getPresentationImage(JSONArray jArrayPresan) {

        if (!(progressDialog.isShowing())) {
            progressDialog.show();
            progressDialog.setMessage("Downloading.....");
            progressDialog.setCancelable(false);
        }
        try {
            for (int i = 0; i < jArrayPresan.length(); i++) {
                JSONArray imageArray = jArrayPresan.getJSONObject(i).getJSONArray("Images");
                for (int j = 0; j < imageArray.length(); j++) {


                    JSONObject imageObject = imageArray.getJSONObject(j);
                    String type = imageObject.getString("type");
                    if (type.equalsIgnoreCase("image")) {
                        imageArraySize.add(imageObject.getString("value"));
                        String imageName = imageObject.getString("value");
                        Log.d("AITL ", "getPresentationImage: " + imageName);
                        String imageUrl = MyUrls.Imgurl + imageName;
                        downloadImage(imageUrl, imageName);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void downloadImage(String url, final String image_name) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        ImageRequest imageRequest = new ImageRequest(
                url, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        saveImageToInternalStorage(response, image_name);
                    }
                },
                0, // Image width
                0, // Image height
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        requestQueue.add(imageRequest);
    }

    protected Uri saveImageToInternalStorage(Bitmap bitmap, String image_name) {
        // Initialize ContextWrapper
        totalImage = totalImage + 1;
        ContextWrapper wrapper = new ContextWrapper(getContext());
        String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AllInTheLoop/" + sessionManager.getEventName() + "/" + presantation_id;
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdirs();
        }

        File imgFile = new File(file, image_name);
        try {

            OutputStream stream = null;
            stream = new FileOutputStream(imgFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            stream.flush();
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(imgFile.getAbsolutePath());
        databaseHandler.insertPresantationDetailImage(presantation_id, "Time", sessionManager.getEventId(), "", savedImageURI.toString(), image_name);
        if (totalImage == imageArraySize.size()) {
            progressDialog.dismiss();
            sessionManager.setIsFirstTimeLoadPresantation("1");
            Log.d("AITL LastPosition", "" + totalImage);
            imgadapter.notifyDataSetChanged();
            imgdetail_adapter.notifyDataSetChanged();

            if (is_presenter.equalsIgnoreCase("false")) {
                setDataOtherUserdata();
            }
        }
        return savedImageURI;
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean camerAaddPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (((MainActivity) getActivity()).checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    public void requestPermission() {
        permissionsNeeded = new ArrayList<String>();
        permissionsNeeded.clear();
        permissionsList = new ArrayList<String>();
        permissionsList.clear();

        if (!camerAaddPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");

        if (permissionsList.size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    getPresantationDetail();
//                    GlobalData.cameraPermissionbroadCast(MainActivity.this);
                    Log.d("Bhavdip", "MainActivty PermissionGranted");

                } else {
                    // Permission Denied
                    requestPermission();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}


