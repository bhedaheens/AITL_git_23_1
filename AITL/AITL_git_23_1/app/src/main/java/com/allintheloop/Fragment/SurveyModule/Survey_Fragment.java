package com.allintheloop.Fragment.SurveyModule;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Activity.LoginActivity;
import com.allintheloop.Activity.LoginMainScreen;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Survey_Fragment extends Fragment implements VolleyInterface {

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
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
    RelativeLayout MainLayout, relativeLayout_Data;
    LinearLayout linear_content;


    public Survey_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_survey, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        linear_start_page = rootView.findViewById(R.id.linear_start_page);
        linear_question_page = rootView.findViewById(R.id.linear_question_page);
        linear_end_page = rootView.findViewById(R.id.linear_end_page);
        linear_custom = rootView.findViewById(R.id.linear_custome);
        linear_nodata = rootView.findViewById(R.id.linear_nodata);
        linear_content = rootView.findViewById(R.id.linear_content);
        MainLayout = rootView.findViewById(R.id.MainLayout);
        relativeLayout_Data = rootView.findViewById(R.id.relativeLayout_Data);


        webView_start = rootView.findViewById(R.id.webView_start);
        img_survey = rootView.findViewById(R.id.img_survey);
        webView_start.getSettings().setDefaultTextEncodingName("utf-8");

        webView_thanku = rootView.findViewById(R.id.webView_thanku);
        webView_thanku.getSettings().setDefaultTextEncodingName("utf-8");

        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();


        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());

        webView_start.getSettings().setJavaScriptEnabled(true);
        webView_start.getSettings().setAllowContentAccess(true);
        webView_start.setVerticalScrollBarEnabled(true);
        webView_start.setHorizontalScrollBarEnabled(true);

        webView_thanku.getSettings().setJavaScriptEnabled(true);
        webView_thanku.getSettings().setAllowContentAccess(true);
        webView_thanku.setVerticalScrollBarEnabled(true);
        webView_thanku.setHorizontalScrollBarEnabled(true);

        listData = new HashMap<>();
        ansHashMap = new HashMap<>();

        jArrayInput = new JSONArray();
        arrayAddiChkList = new ArrayList<>();
        questionArrayList = new ArrayList<>();

        txt_question = rootView.findViewById(R.id.txt_question);

        btn_start = rootView.findViewById(R.id.btn_start);
        btn_back = rootView.findViewById(R.id.btn_back);
        btn_next = rootView.findViewById(R.id.btn_next);
        btn_finish = rootView.findViewById(R.id.btn_finish);
        btn_Qsfinish = rootView.findViewById(R.id.btn_Qsfinish);

        txt_hint = rootView.findViewById(R.id.txt_hint);
        txt_noData = rootView.findViewById(R.id.txt_noData);

        survey_progress = rootView.findViewById(R.id.survey_progress);
        sessionManager.strModuleId = sessionManager.getCategoryId();

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
        getSurveyDetail();
        text = "<html><body style=\"text-align:justify\"> %s </body></Html>";


        btn_Qsfinish.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("3")) {

                    EditText editText = (EditText) linear_custom.findViewWithTag("edt");
                    if (editText != null) {

                        questionArrayList.get(index).setAns(editText.getText().toString());
                        if (questionArrayList.get(index).getAns().length() != 0) {

                            try {
                                jsonInput.put("User_id", sessionManager.getUserId());
                                jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                                jsonInput.put("Answer", editText.getText().toString());
                                Log.d("Json", jsonInput.toString());
                                if (jsonInput.length() != 0) {

                                    ansHashMap.put(index + "", jsonInput);
                                    //jArrayInput.put(jsonInput);
                                    Log.d("JsonArray", ansHashMap.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("1")) {

                    if (questionArrayList.get(index).getAns().length() != 0) {

                        try {
                            jsonInput.put("User_id", sessionManager.getUserId());
                            jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                            jsonInput.put("Answer", questionArrayList.get(index).getAns());
                            if (jsonInput.length() != 0) {
                                ansHashMap.put(index + "", jsonInput);
                                //jArrayInput.put(jsonInput);
                                Log.d("JsonArray", ansHashMap.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("JsonObject", jsonInput.toString());

                } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("2")) {

                    if (questionArrayList.get(index).getAns().length() != 0) {

                        try {
                            jsonInput.put("User_id", sessionManager.getUserId());
                            jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                            jsonInput.put("Answer", arrayAddiChkList);
                            Log.d("JsonObject", jsonInput.toString());
                            if (jsonInput.length() != 0) {
                                ansHashMap.put(index + "", jsonInput);
                                //jArrayInput.put(jsonInput);
                                Log.d("JsonArray", ansHashMap.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("JsonObject", jsonInput.toString());

                }

                for (int i = 0; i < ansHashMap.size(); i++) {

                    jArrayInput.put(ansHashMap.get(i + ""));
                }

                Log.i("JsonArray", "FINAL JASON ARRAY " + jArrayInput.toString());

                //linear_end_page.setVisibility(View.GONE);
                if (sessionManager.isLogin()) {
                    saveSurvey();
                    if (str_thanku.equalsIgnoreCase("")) {
                        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                            GlobalData.CURRENT_FRAG = GlobalData.Fundrising_Home;
                            ((MainActivity) getActivity()).loadFragment();
                        } else {
                            GlobalData.CURRENT_FRAG = GlobalData.HOME_FRAGMENT;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    } else {
                        linear_question_page.setVisibility(View.GONE);
                        linear_end_page.setVisibility(View.VISIBLE);

                        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/fonts/Lato_Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
                        String pas = "</body></html>";
                        String myHtmlString = pish + str_thanku + pas;
                        webView_thanku.loadDataWithBaseURL("file:///android_asset", String.format(text, myHtmlString), "text/html; charset=utf-8", "utf-8", null);

                        //   webView_thanku.loadData(String.format(text, str_thanku),"text/html; charset=utf-8","utf-8");
                    }

                } else {
                    startActivity(new Intent(getActivity(), LoginMainScreen.class));
                }
            }
        });

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

        btn_back.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                Log.d("Left Button Clicked", "" + v.getId());

                if (index != 0) {
                    progress_amount = ((100 / questionArrayList.size()));
                    total_progres = total_progres - progress_amount;
                    survey_progress.setProgress(total_progres);
                }
                if (btn_Qsfinish.isShown()) {
                    btn_Qsfinish.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);
                }
                if (index == 1) {
                    index--;
                    btn_back.setVisibility(View.GONE);
                } else {
                    index--;
                }
                jsonInput = new JSONObject();
                Log.d("AITL Question_type", questionArrayList.get(index).getQ_type());

                if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("1")) {

                    linear_custom.removeAllViews();
                    txt_hint.setText("Please select one answer.");
                    RadioGroup radioGroup = new RadioGroup(getActivity());

                    try {
                        JSONArray jsonArray = listData.get(index + "");
                        for (int l = 0; l < jsonArray.length(); l++) {

                            RadioButton radioButton = new RadioButton(getActivity());
                            radioButton.setText(jsonArray.get(l).toString());

                            radioButton.setPadding(15, 15, 0, 15);
                            radioButton.setTextSize(15);
                            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                                radioButton.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                                radioButton.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                            } else {
                                radioButton.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                                radioButton.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                            }
                            GlobalData.customeRadioColorChange(radioButton, sessionManager);

                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params1.leftMargin = 50;
                            params1.rightMargin = 50;
                            params1.topMargin = 50;
                            radioGroup.addView(radioButton);
                            radioButton.setLayoutParams(params1);

                            Log.i("JsonArray", "BACK BUTTON BEAN" + questionArrayList.get(index).getAns());
                            Log.i("JsonArray", "BACK BUTTON RADIO" + radioButton.getText().toString());

                            if (radioButton.getText().toString().equalsIgnoreCase(questionArrayList.get(index).getAns())) {

                                radioButton.setChecked(true);
                            }
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {

                                    RadioButton rdButton = (RadioButton) group.findViewById(checkedId);

                                    StrRadio = rdButton.getText().toString();
                                    questionArrayList.get(index).setAns("");
                                    questionArrayList.get(index).setAns(StrRadio);
                                    Log.d("JsonArray", "Radio button" + StrRadio);

                                }
                            });
                        }
                        linear_custom.addView(radioGroup);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("2")) {


                    linear_custom.removeAllViews();
                    arrayAddiChkList.clear();
                    txt_hint.setText("Please answer in your own words");
                    try {

                        JSONArray jsonArray = listData.get(index + "");

                        for (int l = 0; l < jsonArray.length(); l++) {

                            final CheckBox chkBox = new CheckBox(getActivity());
                            chkBox.setText(jsonArray.get(l).toString());

                            chkBox.setPadding(10, 15, 0, 15);
                            chkBox.setTextSize(15);
                            chkBox.setId(l);
                            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                                chkBox.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                                chkBox.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                            } else {
                                chkBox.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                                chkBox.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                            }

                            GlobalData.customeCheckboxColorChange(chkBox, sessionManager);

                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params1.leftMargin = 50;
                            params1.topMargin = 50;
                            params1.rightMargin = 50;
                            params1.gravity = Gravity.CENTER_VERTICAL;
                            chkBox.setLayoutParams(params1);
                            linear_custom.addView(chkBox);

                            if (arrayAddiChkList.size() != 0) {

                                Log.d("AITL Array", questionArrayList.get(index).getAns().toString());
                                String strreplace = questionArrayList.get(index).getAns().replace("[", " ").replace("]", "");
                                Log.d("AITL replace", questionArrayList.get(index).getAns().replace("[", " ").replace("]", ""));
                                split_chk = strreplace.split(",");


                                for (int z = 0; z < split_chk.length; z++) {

                                    Log.d("AITL Array String", split_chk[z].toString());
                                    Log.d("AITL checkbox String", chkBox.getText().toString());

                                    if (chkBox.getText().toString().equalsIgnoreCase(split_chk[z].toString().trim())) {
                                        Log.d("AITL IF Array String", split_chk[z].toString());
                                        chkBox.setChecked(true);
                                    }
                                }

                            } else {
                            }


                            chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                    if (isChecked) {
                                        arrayAddiChkList.add(chkBox.getText().toString());
                                        questionArrayList.get(index).setAns("");
                                        questionArrayList.get(index).setAns(arrayAddiChkList.toString());
                                        //    chkboxArray.add(chkBox);
                                    } else {
                                        arrayAddiChkList.remove(chkBox.getText().toString());
                                        questionArrayList.get(index).setAns("");
                                        questionArrayList.get(index).setAns(arrayAddiChkList.toString());
//                                           chkboxArray.add(chkBox);
                                    }
                                }
                            });

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("3")) {

//            ToastC.show(getActivity(),"abc");

                    linear_custom.removeAllViews();
                    txt_hint.setText("Please type your answer below");
                    try {
                        JSONArray jsonArray = listData.get(index + "");
                        final EditText edt_ans = new EditText(getActivity());
                        edt_ans.setTag("edt");
                        for (int l = 0; l < jsonArray.length(); l++) {
                            edt_ans.setHint(jsonArray.get(l).toString());

                            edt_ans.setPadding(20, 25, 0, 25);
                            edt_ans.setTextSize(15);
                            edt_ans.setText(questionArrayList.get(index).getAns());
                            edt_ans.setMinLines(5);
                            edt_ans.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                            edt_ans.setSingleLine(false);
                            edt_ans.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                            edt_ans.setBackgroundResource(R.drawable.square_bg);
                            edt_ans.setId(l);
                            edt_ans.setGravity(Gravity.TOP);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                            params1.topMargin = 20;
                            edt_ans.setLayoutParams(params1);
                            linear_custom.addView(edt_ans);

//                    questionArrayList.get(index).setAns("");
//                    questionArrayList.get(index).setAns(edt_ans.getText().toString());
                        }


                        Log.d("JsonObject", jsonInput.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
//                   Log.d("Right Button Clicked", "" + quesList.get(index).toString());
//                   txt_question.setText(quesList.get(index).toString());
                Log.d("Right Button Clicked", "" + questionArrayList.get(index).getQuestion());
                txt_question.setText(questionArrayList.get(index).getQuestion());

                //     setSurveyData(index);
            }
        });


        btn_next.setOnClickListener(v -> {

            if (questionArrayList.isEmpty()) {
                ToastC.show(getActivity(), "No Data");

            } else {

                if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("3")) {

                    EditText editText = (EditText) linear_custom.findViewWithTag("edt");
                    if (editText != null) {
                        questionArrayList.get(index).setAns(editText.getText().toString());

                        if (questionArrayList.get(index).getAns().length() != 0) {

                            try {
                                jsonInput.put("User_id", sessionManager.getUserId());
                                jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                                jsonInput.put("Answer", editText.getText().toString());
                                Log.d("Json", jsonInput.toString());
                                if (jsonInput.length() != 0) {

                                    ansHashMap.put(index + "", jsonInput);
                                    //jArrayInput.put(jsonInput);
                                    Log.d("JsonArray", ansHashMap.toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("1")) {

                    if (questionArrayList.get(index).getAns().length() != 0) {


                        try {
                            jsonInput.put("User_id", sessionManager.getUserId());
                            jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                            jsonInput.put("Answer", questionArrayList.get(index).getAns());
                            if (jsonInput.length() != 0) {
                                ansHashMap.put(index + "", jsonInput);
                                //jArrayInput.put(jsonInput);
                                Log.d("JsonArray", ansHashMap.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("JsonObject", jsonInput.toString());

                } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("2")) {

                    if (questionArrayList.get(index).getAns().length() != 0) {

                        try {
                            jsonInput.put("User_id", sessionManager.getUserId());
                            jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                            jsonInput.put("Answer", arrayAddiChkList);
                            Log.d("JsonObject", jsonInput.toString());
                            if (jsonInput.length() != 0) {
                                ansHashMap.put(index + "", jsonInput);
                                //jArrayInput.put(jsonInput);
                                Log.d("JsonArray", ansHashMap.toString());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("JsonObject", jsonInput.toString());

                }

                if (questionArrayList.get(index).getAns().length() != 0) {
                    index++;
                    btn_back.setVisibility(View.VISIBLE);
                    if (index == (questionArrayList.size() - 1)) {
                        // index = 0;
                        btn_next.setVisibility(View.GONE);
                        btn_Qsfinish.setVisibility(View.VISIBLE);
                    }

                    if (index != 0) {
                        progress_amount = ((100 / questionArrayList.size()));
                        total_progres = total_progres + progress_amount;
                        survey_progress.setProgress(total_progres);
                    }
                    jsonInput = new JSONObject();
                    Log.d("AITL Question_type", questionArrayList.get(index).getQ_type());
                    if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("1")) {
                        linear_custom.removeAllViews();
                        txt_hint.setText("Please select one answer.");
                        RadioGroup radioGroup = new RadioGroup(getActivity());
                        try {
                            JSONArray jsonArray = listData.get(index + "");
                            for (int l = 0; l < jsonArray.length(); l++) {
                                RadioButton radioButton = new RadioButton(getActivity());
                                radioButton.setText(jsonArray.get(l).toString());

                                radioButton.setPadding(15, 15, 0, 15);
                                radioButton.setTextSize(15);
                                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                                    radioButton.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                                } else {
                                    radioButton.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                                }
                                GlobalData.customeRadioColorChange(radioButton, sessionManager);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 50;
                                params1.topMargin = 50;
                                params1.rightMargin = 50;
                                radioGroup.addView(radioButton);
                                radioButton.setLayoutParams(params1);

                                if (radioButton.getText().toString().equalsIgnoreCase(questionArrayList.get(index).getAns())) {

                                    radioButton.setChecked(true);
                                }

                                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                        StrRadio = radioButton.getText().toString();
                                        questionArrayList.get(index).setAns("");
                                        questionArrayList.get(index).setAns(StrRadio);
                                        Log.d("Radio button", StrRadio);

                                    }
                                });
                            }
                            linear_custom.addView(radioGroup);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("2")) {


                        linear_custom.removeAllViews();
                        txt_hint.setText("Please select Multiple answer.");
                        try {

                            JSONArray jsonArray = listData.get(index + "");
                            for (int l = 0; l < jsonArray.length(); l++) {

                                CheckBox chkBox = new CheckBox(getActivity());
                                chkBox.setText(jsonArray.get(l).toString());

                                chkBox.setPadding(10, 15, 0, 15);
                                chkBox.setTextSize(15);
                                chkBox.setId(l);
                                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                                    chkBox.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                                    chkBox.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                                } else {
                                    chkBox.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                                    chkBox.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                                }
                                GlobalData.customeCheckboxColorChange(chkBox, sessionManager);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 50;
                                params1.topMargin = 50;
                                params1.rightMargin = 50;
                                params1.gravity = Gravity.CENTER_VERTICAL;
                                chkBox.setLayoutParams(params1);
                                linear_custom.addView(chkBox);

                                if (arrayAddiChkList.size() != 0) {

                                    Log.d("AITL Array", questionArrayList.get(index).getAns().toString());
                                    String strreplace = questionArrayList.get(index).getAns().replace("[", " ").replace("]", "");
                                    Log.d("AITL replace", questionArrayList.get(index).getAns().replace("[", " ").replace("]", ""));
                                    split_chk = strreplace.split(",");


                                    for (int z = 0; z < split_chk.length; z++) {

                                        Log.d("AITL Array String", split_chk[z].toString());
                                        Log.d("AITL checkbox String", chkBox.getText().toString());

                                        if (chkBox.getText().toString().equalsIgnoreCase(split_chk[z].toString().trim())) {
                                            Log.d("AITL IF Array String", split_chk[z].toString());
                                            chkBox.setChecked(true);
                                        }
                                    }

                                } else {
                                    Log.d("AITL Array", "Null");
                                }

                                chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                        if (isChecked) {
                                            arrayAddiChkList.add(buttonView.getText().toString());
                                            questionArrayList.get(index).setAns("");
                                            questionArrayList.get(index).setAns(arrayAddiChkList.toString());
                                            Log.d("AITL", "BEAN CLASS DATA " + questionArrayList.get(index).getAns());
                                            //    chkboxArray.add(chkBox);
                                        } else {
                                            arrayAddiChkList.remove(buttonView.getText().toString());
                                            questionArrayList.get(index).setAns("");
                                            questionArrayList.get(index).setAns(arrayAddiChkList.toString());
                                            Log.d("AITL", "BEAN CLASS DATA " + questionArrayList.get(index).getAns());
//                                           chkboxArray.add(chkBox);
                                        }
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("3")) {

                        linear_custom.removeAllViews();
                        txt_hint.setText("Please type your answer below");
                        try {
                            JSONArray jsonArray = listData.get(index + "");
                            final EditText edt_ans = new EditText(getActivity());
                            edt_ans.setTag("edt");
                            for (int l = 0; l < jsonArray.length(); l++) {
                                edt_ans.setHint(jsonArray.get(l).toString());
                                edt_ans.setPadding(20, 25, 0, 25);
                                edt_ans.setTextSize(15);

                                edt_ans.setText(questionArrayList.get(index).getAns());
                                edt_ans.setMinLines(5);
                                edt_ans.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                edt_ans.setSingleLine(false);
                                edt_ans.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                                edt_ans.setBackgroundResource(R.drawable.square_bg);
                                edt_ans.setId(l);
                                edt_ans.setGravity(Gravity.TOP);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                                params1.topMargin = 20;
                                edt_ans.setLayoutParams(params1);
                                linear_custom.addView(edt_ans);
                            }
                            Log.d("JsonObject", jsonInput.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("Right Button Clicked", "" + questionArrayList.get(index).getQuestion());

                    }
                    txt_question.setText(questionArrayList.get(index).getQuestion());
                } else {
                    ToastC.show(getActivity(), "Please select an answer.");
                }

            }
        });


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

    @Override
    public void onResume() {
        super.onResume();

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
                        Log.d("AITL  Map Oflline", jsonObject.toString());
                        loadData(jsonObject);
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
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        Log.d("AITL  Map Oflline", jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }
        }
    }

    @SuppressLint("NewApi")
    private void loadData(JSONObject jsonData) {
        try {

            JSONArray jArraySurvey = jsonData.getJSONArray("survey");

            questionArrayList.clear();
            for (int j = 0; j < jArraySurvey.length(); j++) {
                JSONObject jObjectSurvey = jArraySurvey.getJSONObject(j);
                question_id = jObjectSurvey.getString("Question_id");
                question = jObjectSurvey.getString("Question");
                question_type = jObjectSurvey.getString("Question_type");
                JSONArray jArrayOption = jObjectSurvey.getJSONArray("Option");

                listData.put("" + j, jArrayOption);

                Log.d("AITL SURVEY OPTION", listData.toString());
                questionArrayList.add(new Question(question_id, question_type, question, ""));
            }
            JSONArray jArrayscreen = jsonData.getJSONArray("survey_screens");
            for (int i = 0; i < jArrayscreen.length(); i++) {
                JSONObject jObjectScr = jArrayscreen.getJSONObject(i);
                str_welcome = jObjectScr.getString("welcome_data");
                str_thanku = jObjectScr.getString("thanku_data");
            }


            if (jsonData.getString("is_blank").equalsIgnoreCase("0")) {
                if (jArraySurvey.length() != 0) {
                    linear_nodata.setVisibility(View.GONE);
                    if (jArrayscreen.length() == 0) {
                        linear_question_page.setVisibility(View.VISIBLE);
                    } else {

                        if (str_welcome.equalsIgnoreCase("")) {
                            linear_start_page.setVisibility(View.GONE);
                            linear_question_page.setVisibility(View.VISIBLE);
                        } else {
                            linear_question_page.setVisibility(View.GONE);
                            linear_start_page.setVisibility(View.VISIBLE);

                            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: left;}</style></head><body>";
                            String pas = "</body></html>";
                            String myHtmlString = pish + str_welcome + pas;
                            webView_start.loadDataWithBaseURL("file:///android_asset", String.format(text, myHtmlString), "text/html; charset=utf-8", "utf-8", null);

                            //    webView_start.loadData(String.format(text, str_welcome), "text/html; charset=utf-8","utf-8");
                        }
                    }

                    if (jArraySurvey.length() == 1) {
                        btn_next.setVisibility(View.GONE);
                        btn_Qsfinish.setVisibility(View.VISIBLE);
                    }

                    jsonInput = new JSONObject();
                    Log.d("AITL Question_type", questionArrayList.get(index).getQ_type());

                    total_progres = 0;
                    progress_amount = ((100 / questionArrayList.size()));
                    total_progres = total_progres + progress_amount;
                    survey_progress.setProgress(total_progres);

                    if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("1")) {
                        linear_custom.removeAllViews();
                        txt_hint.setText("Please select one answer.");
                        RadioGroup radioGroup = new RadioGroup(getActivity());
                        try {
                            JSONArray jsonArray = listData.get(index + "");
                            for (int l = 0; l < jsonArray.length(); l++) {
                                RadioButton radioButton = new RadioButton(getActivity());
                                radioButton.setText(jsonArray.get(l).toString());

                                radioButton.setPadding(15, 15, 0, 15);
                                radioButton.setTextSize(15);


                                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                                    radioButton.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                                } else {
                                    radioButton.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                                }
                                GlobalData.customeRadioColorChange(radioButton, sessionManager);

                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 50;
                                params1.topMargin = 50;
                                radioGroup.addView(radioButton);
                                radioButton.setLayoutParams(params1);
                                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                        StrRadio = radioButton.getText().toString();
                                        questionArrayList.get(index).setAns("");
                                        questionArrayList.get(index).setAns(StrRadio);
                                        Log.d("Radio button", StrRadio);

                                    }
                                });
                            }
                            linear_custom.addView(radioGroup);
                            jsonInput.put("User_id", sessionManager.getUserId());
                            jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                            jsonInput.put("Answer", questionArrayList.get(index).getAns());
                            Log.d("JsonObject", jsonInput.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("2")) {


                        linear_custom.removeAllViews();
                        txt_hint.setText("Please select Multiple answer.");
                        try {
                            JSONArray jsonArray = listData.get(index + "");
                            for (int l = 0; l < jsonArray.length(); l++) {
                                final CheckBox chkBox = new CheckBox(getActivity());
                                chkBox.setText(jsonArray.get(l).toString());

                                chkBox.setPadding(10, 15, 0, 15);
                                chkBox.setTextSize(15);
                                chkBox.setId(l);


                                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                                    chkBox.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                                    chkBox.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                                } else {
                                    chkBox.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                                    chkBox.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                                }
                                GlobalData.customeCheckboxColorChange(chkBox, sessionManager);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                params1.leftMargin = 50;
                                params1.topMargin = 50;
                                params1.gravity = Gravity.CENTER_VERTICAL;
                                chkBox.setLayoutParams(params1);
                                linear_custom.addView(chkBox);


                                if (arrayAddiChkList.size() != 0) {

                                    Log.d("AITL Array", questionArrayList.get(index).getAns().toString());
                                    String strreplace = questionArrayList.get(index).getAns().replace("[", " ").replace("]", "");
                                    Log.d("AITL replace", questionArrayList.get(index).getAns().replace("[", " ").replace("]", ""));
                                    split_chk = strreplace.split(",");


                                    for (int z = 0; z < split_chk.length; z++) {

                                        Log.d("AITL Array String", split_chk[z].toString());
                                        Log.d("AITL checkbox String", chkBox.getText().toString());

                                        if (chkBox.getText().toString().equalsIgnoreCase(split_chk[z].toString().trim())) {
                                            Log.d("AITL IF Array String", split_chk[z].toString());
                                            chkBox.setChecked(true);
                                        }
                                    }

                                } else {
                                    Log.d("AITL Array", "Null");
                                }
                                chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                        if (isChecked) {
                                            arrayAddiChkList.add(chkBox.getText().toString());
                                            questionArrayList.get(index).setAns("");
                                            questionArrayList.get(index).setAns(arrayAddiChkList.toString());
                                        } else {
                                            arrayAddiChkList.remove(chkBox.getText().toString());
                                            questionArrayList.get(index).setAns("");
                                            questionArrayList.get(index).setAns(arrayAddiChkList.toString());
                                        }
                                    }
                                });

                            }
                            jsonInput.put("User_id", sessionManager.getUserId());
                            jsonInput.put("Question_id", questionArrayList.get(index).getQ_id());
                            jsonInput.put("Answer", arrayAddiChkList);
                            Log.d("JsonObject", jsonInput.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (questionArrayList.get(index).getQ_type().equalsIgnoreCase("3")) {
                        linear_custom.removeAllViews();
                        txt_hint.setText("Please type your answer below");
                        try {
                            JSONArray jsonArray = listData.get(index + "");
                            final EditText edt_ans = new EditText(getActivity());
                            edt_ans.setTag("edt");
                            for (int l = 0; l < jsonArray.length(); l++) {
                                edt_ans.setHint(jsonArray.get(l).toString());
                                edt_ans.setPadding(20, 25, 0, 25);

                                edt_ans.setTextSize(15);
                                edt_ans.setText(questionArrayList.get(index).getAns());
                                edt_ans.setMinLines(5);
                                edt_ans.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                                edt_ans.setSingleLine(false);
                                edt_ans.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);

                                edt_ans.setBackgroundResource(R.drawable.square_bg);
                                edt_ans.setId(l);
                                edt_ans.setGravity(Gravity.TOP);
                                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                                params1.topMargin = 20;
                                edt_ans.setLayoutParams(params1);
                                linear_custom.addView(edt_ans);

                            }


                            Log.d("JsonObject", jsonInput.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.d("Right Button Clicked", "" + questionArrayList.get(index).getQuestion());
                    txt_question.setText(questionArrayList.get(index).getQuestion());
                } else {
                    linear_nodata.setVisibility(View.VISIBLE);
                    linear_start_page.setVisibility(View.GONE);
                    linear_question_page.setVisibility(View.GONE);
                    linear_end_page.setVisibility(View.GONE);
                }
            } else {
                linear_nodata.setVisibility(View.VISIBLE);
                txt_noData.setText(jsonData.getString("message"));
                linear_start_page.setVisibility(View.GONE);
                linear_question_page.setVisibility(View.GONE);
                linear_end_page.setVisibility(View.GONE);
            }

            /*else if (jArraySurvey.length() == 1){
                btn_next.setVisibility(View.GONE);
                btn_Qsfinish.setVisibility(View.VISIBLE);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
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
                        loadData(jsonData);
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

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    private void saveSurvey() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.Save_survey, Param.Save_survey(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getToken(), jArrayInput.toString(), sessionManager.getCategoryId()), 1, true, this);
    }


}