package com.allintheloop.Fragment.SurveyModule;


import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.Question;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import static com.allintheloop.Util.GlobalData.sessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyNewDesign_Fragment extends Fragment implements VolleyInterface {

    LinearLayout linear_start_page, linear_question_page, linear_end_page, linear_custom, linear_nodata;
    WebView webView_start, webView_thanku;
    SessionManager sessionManager;
    String str_welcome = "", str_thanku = "";
    Button btn_start, btn_finish;
    Button btn_back, btn_next, btn_Qsfinish;
    TextView txt_hint, txt_noData;
    String text;
    String question_id, question, question_type;
    TextView txt_question;
    ProgressBar survey_progress;
    String StrRadio, stredt;

    int progress_amount;

    HashMap<String, JSONArray> listData;

    ArrayList<Question> questionArrayList;
    ArrayList<String> arrayAddiChkList;

    JSONArray jArrayInput;
    JSONObject jsonInput;
    int index = 0;
    int total_progres = 0;
    int pos;
    HashMap<String, JSONObject> ansHashMap;
    String split_chk[];

    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String tag = "SurveyFragment";
    ImageView img_survey;
    DefaultLanguage.DefaultLang defaultLang;


    public SurveyNewDesign_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_survey_new_design, container, false);

        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        linear_start_page = (LinearLayout) rootView.findViewById(R.id.linear_start_page);
        linear_question_page = (LinearLayout) rootView.findViewById(R.id.linear_question_page);
        linear_end_page = (LinearLayout) rootView.findViewById(R.id.linear_end_page);
        linear_custom = (LinearLayout) rootView.findViewById(R.id.linear_custome);
        linear_nodata = (LinearLayout) rootView.findViewById(R.id.linear_nodata);


        webView_start = (WebView) rootView.findViewById(R.id.webView_start);
        img_survey = (ImageView) rootView.findViewById(R.id.img_survey);
        webView_start.getSettings().setDefaultTextEncodingName("utf-8");

        webView_thanku = (WebView) rootView.findViewById(R.id.webView_thanku);
        webView_thanku.getSettings().setDefaultTextEncodingName("utf-8");

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());

        webView_start.getSettings().setJavaScriptEnabled(true);
        webView_start.getSettings().setAllowContentAccess(true);
        webView_start.setVerticalScrollBarEnabled(true);
        webView_start.setHorizontalScrollBarEnabled(true);

        webView_thanku.getSettings().setJavaScriptEnabled(true);
        webView_thanku.getSettings().setAllowContentAccess(true);
        webView_thanku.setVerticalScrollBarEnabled(true);
        webView_thanku.setHorizontalScrollBarEnabled(true);


        txt_question = (TextView) rootView.findViewById(R.id.txt_question);

        btn_start = (Button) rootView.findViewById(R.id.btn_start);
        btn_back = (Button) rootView.findViewById(R.id.btn_back);
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        btn_finish = (Button) rootView.findViewById(R.id.btn_finish);
        btn_Qsfinish = (Button) rootView.findViewById(R.id.btn_Qsfinish);

        txt_hint = (TextView) rootView.findViewById(R.id.txt_hint);
        txt_noData = (TextView) rootView.findViewById(R.id.txt_noData);

        survey_progress = (ProgressBar) rootView.findViewById(R.id.survey_progress);
        sessionManager.strModuleId = sessionManager.getCategoryId();
//        sessionManager.setMenuid("15");
        sessionManager.strMenuId = "15";

        btn_start.setText(defaultLang.get15Start());
        btn_finish.setText(defaultLang.get15End());

        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

            btn_start.setBackgroundDrawable(drawable);
            btn_next.setBackground(drawable);
            btn_Qsfinish.setBackground(drawable);
            btn_back.setBackground(drawable);
            btn_finish.setBackground(drawable);

            btn_finish.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_back.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_Qsfinish.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_start.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                survey_progress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(sessionManager.getTopBackColor())));
            } else {
                survey_progress.getIndeterminateDrawable().setColorFilter(Color.parseColor(sessionManager.getTopBackColor()), PorterDuff.Mode.SRC_IN);
            }
//            survey_progress.getProgressDrawable().setColorFilter(Color.parseColor(sessionManager.getTopBackColor()), PorterDuff.Mode.SRC_IN);


        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_start.setBackgroundDrawable(drawable);
            btn_next.setBackground(drawable);
            btn_Qsfinish.setBackground(drawable);
            btn_back.setBackground(drawable);
            btn_finish.setBackground(drawable);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                survey_progress.setProgressTintList(ColorStateList.valueOf(Color.parseColor(sessionManager.getFunTopBackColor())));
            } else {
                survey_progress.getIndeterminateDrawable().setColorFilter(Color.parseColor(sessionManager.getFunTopBackColor()), PorterDuff.Mode.SRC_IN);
            }
            btn_finish.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_back.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_Qsfinish.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_start.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

        }
        text = "<html><body style=\"text-align:justify\"> %s </body></Html>";


        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_start_page.setVisibility(View.GONE);
                linear_question_page.setVisibility(View.VISIBLE);
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.isNetworkAvailable(getActivity())) {

                    if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                        GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                        ((MainActivity) getActivity()).loadFragment();
                    } else {
                        GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } else {
                    ToastC.show(getActivity(), getString(R.string.noInernet));
                }
            }
        });
        btn_back.setVisibility(View.GONE);

        return rootView;
    }


    private void saveSurvey() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Save_survey, Param.Save_survey(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), jArrayInput.toString(), sessionManager.getCategoryId()), 1, true, this);
    }


    private void getSurveyDetail() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

//            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(),sessionManager.getCategoryId(), tag);
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.getCategoryId(), tag);
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.AtteendeeSpeakerDetail_Data)));
//                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (sessionManager.isLogin()) {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_survey, Param.getSurvey(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.getToken(), sessionManager.getCategoryId()), 0, false, this);
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_survey, Param.getSurvey(sessionManager.getEventId(), sessionManager.getEventType(), "", sessionManager.getToken(), sessionManager.getCategoryId()), 0, false, this);
                }
            } else {
                if (sessionManager.isLogin()) {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),5, false, this);
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_survey, Param.getSurvey(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), sessionManager.getToken(), sessionManager.getCategoryId()), 0, false, this);
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_survey, Param.getSurvey(sessionManager.getEventId(), sessionManager.getEventType(), "", sessionManager.getToken(), sessionManager.getCategoryId()), 0, false, this);
                }
            }
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.getCategoryId(), tag);

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
//                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }
        }
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject mainObj = new JSONObject(volleyResponse.output);
                    if (mainObj.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = mainObj.getJSONObject("data");

                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.getCategoryId(), tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.getCategoryId(), tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonData.toString(), sessionManager.getCategoryId(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonData.toString(), sessionManager.getCategoryId(), tag);
                        }
//                        loadData(jsonData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject mainObj = new JSONObject(volleyResponse.output);
                    if (mainObj.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = mainObj.getJSONObject("data");

                        if (sqLiteDatabaseHandler.isAttdeeSpeaker_DetailExist(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.getCategoryId(), tag)) {
                            sqLiteDatabaseHandler.deleteAttdeeSpeaker_DetailData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), sessionManager.getCategoryId(), tag);
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonData.toString(), sessionManager.getCategoryId(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttdeeSpeaker_Detail(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonData.toString(), sessionManager.getCategoryId(), tag);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
