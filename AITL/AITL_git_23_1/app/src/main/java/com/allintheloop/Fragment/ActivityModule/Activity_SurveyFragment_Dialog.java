package com.allintheloop.Fragment.ActivityModule;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.allintheloop.Bean.ActivityModule.ActivityCommonClass;
import com.allintheloop.Bean.ActivityModule.Activity_SurveyResult;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_SurveyFragment_Dialog extends DialogFragment implements VolleyInterface {


    SessionManager sessionManager;
    Dialog dialog;
    BoldTextView txt_boldText;
    Button btn_submit;
    LinearLayout linear_option;
    Bundle bundle;
    ActivityCommonClass activityCommonClass;
    List<String> optionlist;
    ImageView img_close;
    PieChart pieChart;
    LinearLayout linear_question_page;
    ArrayList ansKey = new ArrayList<>();
    ArrayList ansValue = new ArrayList<>();
    ArrayList colorVaLUE = new ArrayList<>();
    float totalAnsValue = 0f;
    String strRadioAns = "";

    public Activity_SurveyFragment_Dialog() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_activity__survey_fragment__dialog, container, false);
        initView(rootView);
        setUpViewcolor();
        initSurveyView();
        return rootView;
    }

    private void initSurveyView() {
        txt_boldText.setText(activityCommonClass.getSurveyQuestion());
        linear_question_page.setVisibility(View.VISIBLE);
        pieChart.setVisibility(View.GONE);
        setUpSurveyOption();
    }

    private void setUpSurveyOption() {
        optionlist.addAll(activityCommonClass.getSurveyOptions());
        if (linear_option.getChildCount() != 0) {
            linear_option.removeAllViews();
        }
        try {
            RadioGroup radioGroup = new RadioGroup(getActivity());
            for (int l = 0; l < optionlist.size(); l++) {
                RadioButton radioButton = new RadioButton(getActivity());
                radioButton.setText(optionlist.get(l).toString());

                radioButton.setPadding(15, 15, 0, 15);
                radioButton.setTextSize(15);
                if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                    radioButton.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                } else {
                    radioButton.setTextColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                    radioButton.setBackgroundColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    GlobalData.customeRadioColorChange(radioButton, sessionManager);
                }
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params1.leftMargin = 50;
                params1.topMargin = 50;
                params1.rightMargin = 50;
                radioGroup.addView(radioButton);
                radioButton.setLayoutParams(params1);


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                        strRadioAns = radioButton.getText().toString();
                        Log.d("Radio button", strRadioAns);
                    }
                });

            }
            linear_option.addView(radioGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpViewcolor() {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(80.0f);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {


            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_submit.setBackground(drawable);
            btn_submit.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        } else {

            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_submit.setBackground(drawable);
            btn_submit.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }
    }

    private void initView(View rootView) {
        btn_submit = rootView.findViewById(R.id.btn_submit);
        txt_boldText = rootView.findViewById(R.id.txt_boldText);
        linear_option = rootView.findViewById(R.id.linear_option);
        img_close = rootView.findViewById(R.id.img_close);
        pieChart = rootView.findViewById(R.id.piechart);
        linear_question_page = rootView.findViewById(R.id.linear_question_page);
        sessionManager = new SessionManager(getActivity());
        optionlist = new ArrayList<>();
        bundle = getArguments();
        activityCommonClass = bundle.getParcelable("activitySurvey");
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    if (strRadioAns.isEmpty()) {
                        ToastC.show(getActivity(), "Please Select Option.");
                    } else {
                        submitSurvey();
                    }
                }
            }
        });
    }


    private void submitSurvey() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.ansActivityFeedSurvey, Param.submitActivitySurvey(sessionManager.getEventId(), sessionManager.getUserId(), activityCommonClass.getId(), strRadioAns), 0, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObjectData.getJSONArray("survey_result");
                        if (jsonArray.length() != 0) {
                            dialog.cancel();
                        } else {
                            dialog.cancel();
                            ToastC.show(getActivity(), jsonObject.getString("message"));
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
