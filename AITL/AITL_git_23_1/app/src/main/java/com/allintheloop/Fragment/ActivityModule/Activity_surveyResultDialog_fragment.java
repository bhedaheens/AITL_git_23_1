package com.allintheloop.Fragment.ActivityModule;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allintheloop.Bean.ActivityModule.ActivityCommonClass;
import com.allintheloop.Bean.ActivityModule.Activity_SurveyResult;
import com.allintheloop.R;
import com.allintheloop.Util.BoldTextView;
import com.allintheloop.Util.SessionManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_surveyResultDialog_fragment extends DialogFragment {


    SessionManager sessionManager;
    Dialog dialog;
    ArrayList ansKey = new ArrayList<>();
    ArrayList ansValue = new ArrayList<>();
    ArrayList colorVaLUE = new ArrayList<>();
    float totalAnsValue = 0f;
    String strRadioAns = "";
    ImageView img_close;
    PieChart pieChart;
    BoldTextView txt_boldText, txt_noResult;
    ActivityCommonClass activityCommonClass;
    Bundle bundle;
    String result_message = "";

    public Activity_surveyResultDialog_fragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_activity_survey_result_dialog_fragment, container, false);
        initView(rootView);
        setUpSurveyResult();
        return rootView;
    }

    private void initView(View rootView) {
        txt_boldText = rootView.findViewById(R.id.txt_boldText);
        txt_noResult = rootView.findViewById(R.id.txt_noResult);

        img_close = rootView.findViewById(R.id.img_close);
        pieChart = rootView.findViewById(R.id.piechart);
        sessionManager = new SessionManager(getActivity());
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        bundle = getArguments();
        if (bundle != null) {
            activityCommonClass = bundle.getParcelable("activitySurvey");
            txt_boldText.setText(activityCommonClass.getSurveyQuestion());
        }

    }

    private void setUpSurveyResult() {
        ansKey = new ArrayList();
        ansValue = new ArrayList();
        colorVaLUE = new ArrayList();
        totalAnsValue = 0f;
        if (activityCommonClass.getSurveyResult().size() != 0) {
            pieChart.setVisibility(View.VISIBLE);
            txt_noResult.setVisibility(View.GONE);
            for (int i = 0; i < activityCommonClass.getSurveyResult().size(); i++) {
                Activity_SurveyResult index = activityCommonClass.getSurveyResult().get(i);
                ansKey.add(index.getAnswerKey());
                ansValue.add(index.getAnswerValue());
                totalAnsValue = totalAnsValue + Float.parseFloat(index.getAnswerValue());
                colorVaLUE.add(Color.parseColor(index.getColor()));
            }
            setPiechart();
        } else {
            txt_noResult.setText(activityCommonClass.getResult_message());
            pieChart.setVisibility(View.GONE);
            txt_noResult.setVisibility(View.VISIBLE);
        }

    }

    private void setPiechart() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        float mult = 100;
        for (int i = 0; i < ansValue.size(); i++) {
            Float value = Float.parseFloat(ansValue.get(i).toString()) * mult / totalAnsValue;
            entries.add(new PieEntry(value, ansKey.get(i).toString()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colorVaLUE);
        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setValueTextSize(17f);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(1f);
        dataSet.setValueLinePart2Length(0.9f);
        dataSet.setSelectionShift(5f);
        dataSet.setValueTextColor(Color.parseColor("#ffffff"));

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setData(data);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawHoleEnabled(false);
        pieChart.getDescription().setEnabled(false);
    }

}
