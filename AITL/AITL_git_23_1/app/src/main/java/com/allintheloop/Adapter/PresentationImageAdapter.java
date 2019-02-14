package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Fragment.PresantationModule.Presantation_Detail_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.CustomViewPager;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.TouchImageView;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by nteam on 4/8/16.
 */
public class PresentationImageAdapter extends PagerAdapter implements VolleyInterface {

    Context context;
    ArrayList<Exhibitor_DetailImage> arrayList;
    TouchImageView array_img;
    ProgressBar progressBar1;
    RelativeLayout relative_image;
    LinearLayout linear_question, linear_option;
    SessionManager sessionManager;
    String question_id, question, question_type, question_ans;
    TextView txt_question, txt_question_chart;
    WebView webview_chart;
    LinearLayout linear_webview;
    CustomViewPager viewPager;
    ArrayList<RadioButton> radioButtons = new ArrayList<>();


    PieChart pieChart;
    BarChart barChart;

    ArrayList ansKey = new ArrayList<>();
    ArrayList ansValue = new ArrayList<>();
    ArrayList colorValue = new ArrayList<>();
    String str_question = "";
    float totalAnsValue = 0f;


    SQLiteDatabaseHandler databaseHandler;

    public PresentationImageAdapter(Context context, ArrayList<Exhibitor_DetailImage> arrayList, CustomViewPager viewPager) {
        this.context = context;
        this.arrayList = arrayList;
        sessionManager = new SessionManager(context);
        this.viewPager = viewPager;
        databaseHandler = new SQLiteDatabaseHandler(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.adapter_detailpresantation_viewpager, container, false);

        Exhibitor_DetailImage imgObj = arrayList.get(position);

        array_img = (TouchImageView) viewItem.findViewById(R.id.array_img);
        progressBar1 = (ProgressBar) viewItem.findViewById(R.id.progressBar1);
        relative_image = (RelativeLayout) viewItem.findViewById(R.id.relative_image);
        linear_question = (LinearLayout) viewItem.findViewById(R.id.linear_question);
        linear_option = (LinearLayout) viewItem.findViewById(R.id.linear_option);
        linear_webview = (LinearLayout) viewItem.findViewById(R.id.linear_webview);
        webview_chart = (WebView) viewItem.findViewById(R.id.webview_chart);
        txt_question = (TextView) viewItem.findViewById(R.id.txt_question);
        txt_question_chart = (TextView) viewItem.findViewById(R.id.txt_question_chart);

        viewPager.setVisibility(View.VISIBLE);
        relative_image.setVisibility(View.VISIBLE);
        progressBar1.setVisibility(View.GONE);
        pieChart = (PieChart) viewItem.findViewById(R.id.piechart);
        barChart = (BarChart) viewItem.findViewById(R.id.barChart);


        final GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);


        if (imgObj.getImage_link().equalsIgnoreCase("")) {
            progressBar1.setVisibility(View.GONE);
            array_img.setVisibility(View.VISIBLE);
        } else {

            if (imgObj.getTag().equalsIgnoreCase("Presantation")) {

                if (imgObj.getType().equalsIgnoreCase("image")) {
                    linear_question.setVisibility(View.GONE);
                    linear_webview.setVisibility(View.GONE);
                    Cursor cursor = databaseHandler.getPresantationDetailImage(Presantation_Detail_Fragment.presantation_id, "Time", sessionManager.getEventId(), "", imgObj.getImage_link());
                    String jsonObject;
                    if (cursor.getCount() > 0) {
                        if (cursor.moveToFirst()) {
                            try {

                                jsonObject = cursor.getString(cursor.getColumnIndex(databaseHandler.PresantationImagePath));
                                array_img.setVisibility(View.VISIBLE);
                                Glide.with(context)
                                        .load(new File(jsonObject))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(array_img);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else if (imgObj.getType().equalsIgnoreCase("survay")) {
                    linear_question.setVisibility(View.VISIBLE);
                    relative_image.setVisibility(View.GONE);
                    linear_webview.setVisibility(View.GONE);
                    try {

                        LinearLayout.LayoutParams TextViewParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);


                        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);

                        final TextView tv = new TextView(context);
                        tv.setId(position);
                        tv.setTextColor(Color.parseColor("#000000"));
                        tv.setPadding(15, 15, 0, 15);
                        tv.setGravity(Gravity.CENTER);
                        tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                        tv.setTextSize(17);
                        final Button myButton = new Button(context);
                        myButton.setText("Submit");
                        myButton.setId(position);
                        myButton.setGravity(Gravity.CENTER);
                        buttonParams.topMargin = 10;
                        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
                            GradientDrawable drawable1 = new GradientDrawable();
                            drawable1.setShape(GradientDrawable.RECTANGLE);
                            drawable1.setCornerRadius(13.0f);
                            myButton.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

                            drawable1.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                            myButton.setBackground(drawable1);
                            txt_question.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                            txt_question.setBackgroundColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                        } else {
                            GradientDrawable drawable1 = new GradientDrawable();
                            drawable1.setShape(GradientDrawable.RECTANGLE);
                            drawable1.setCornerRadius(13.0f);
                            myButton.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                            drawable1.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                            myButton.setBackground(drawable1);

                            txt_question.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                            txt_question.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
                        }

                        myButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {
                                if (GlobalData.isNetworkAvailable(context)) {
                                    myButton.setVisibility(View.GONE);
                                    tv.setText("Thank you for your answer");
                                    questionSubmit();
                                } else {
                                    ToastC.show(context, "No Internet Connection");
                                }
                            }
                        });

                        JSONObject jsonObject = new JSONObject(imgObj.getImage_link());
                        question_id = jsonObject.getString("Id");
                        question = jsonObject.getString("Question");
                        question_type = jsonObject.getString("Question_type");
                        question_ans = jsonObject.getString("Answer");
                        if (!(question_ans.equalsIgnoreCase(""))) {
                            tv.setText("Thank you for your answer");
                            myButton.setVisibility(View.GONE);
                        } else {
                            tv.setText("Please Select One answer");
                            myButton.setVisibility(View.VISIBLE);
                        }
                        final RadioGroup radioGroup = new RadioGroup(context);
                        JSONArray jsonArray = jsonObject.getJSONArray("Option");
                        txt_question.setText(question);
                        for (int i = 0; i < jsonArray.length(); i++) {

                            RadioButton radioButton = new RadioButton(context);
                            radioButton.setText(jsonArray.get(i).toString());

                            radioButton.setPadding(15, 15, 0, 15);
                            radioButton.setTextSize(15);
                            GlobalData.customeRadioColorChange(radioButton, sessionManager);
                            if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {

                                radioButton.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
                                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                                radioButton.setBackground(drawable);

                            } else {
                                radioButton.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
                                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                                radioButton.setBackground(drawable);
                            }


                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            params1.leftMargin = 50;
                            params1.rightMargin = 50;
                            params1.topMargin = 50;
                            radioGroup.addView(radioButton);
                            radioButton.setLayoutParams(params1);
                            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                    RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                                    question_ans = radioButton.getText().toString();
//
                                }
                            });
                            if (jsonArray.get(i).toString().equalsIgnoreCase(question_ans)) {
                                radioButton.setChecked(true);


                            }
                            radioButtons.add(radioButton);
                        }
                        linear_option.addView(tv, TextViewParams);
                        linear_option.addView(radioGroup);
                        linear_option.addView(myButton, buttonParams);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (imgObj.getType().equalsIgnoreCase("result")) {

                    linear_question.setVisibility(View.GONE);
                    relative_image.setVisibility(View.GONE);
                    linear_webview.setVisibility(View.VISIBLE);
                    webview_chart.getSettings().setJavaScriptEnabled(true);
                    webview_chart.getSettings().setAllowContentAccess(true);
                    webview_chart.setVerticalScrollBarEnabled(true);
                    webview_chart.setHorizontalScrollBarEnabled(true);
                    webview_chart.getSettings().setDefaultTextEncodingName("utf-8");
                    String url = imgObj.getImage_link();
                    viewChart(url, imgObj.getChart_type());
//                    webview_chart.loadUrl("http://" + url);
                }
            }

            viewItem.setTag(imgObj);
            container.addView(viewItem, 0);
        }

        return viewItem;
    }

    private void questionSubmit() {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.presantationDetailSaveSurvey, Param.get_Presantation_DetailSurvey(Presantation_Detail_Fragment.image_name, sessionManager.getUserId(), question_ans), 0, true, this);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void addItem(ArrayList<Exhibitor_DetailImage> detailImageObj) {

        arrayList.addAll(detailImageObj);

        notifyDataSetChanged();
    }

    public void removeAll() {
        arrayList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
//                        btn_submit.setVisibility(View.GONE);
//                        txt_hint.setText("Thank you for your answer");
                        GlobalData.updatePresantation(context);
//                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        dataSet.setColors(colorValue);
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
        pieChart.setData(data);
        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawHoleEnabled(false);
    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;


        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        BarDataSet barDataSet1;
        for (int i = 0; i < ansValue.size(); i++) {
            BarEntry v1e1 = new BarEntry(i, Float.parseFloat(ansValue.get(i).toString()), ansKey.get(i).toString());
            valueSet1.add(v1e1);

        }
        barDataSet1 = new BarDataSet(valueSet1, "");
        barDataSet1.setColors(colorValue);
        barDataSet1.setValueTextColor(Color.parseColor("#000000"));
        barDataSet1.setValueTextSize(17f);
        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);

        return dataSets;
    }

    private void viewChart(String questiondata, String isBarchart) {

        try {

            JSONObject jsonObjectData = new JSONObject(questiondata);

            str_question = jsonObjectData.getString("question");
            txt_question_chart.setText(str_question);
            JSONArray jsonArray = jsonObjectData.getJSONArray("answer");
            ansKey.clear();
            ansValue.clear();
            colorValue.clear();
            totalAnsValue = 0f;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject index = jsonArray.getJSONObject(i);
                ansKey.add(index.getString("answer_key"));
                ansValue.add(index.getString("answer_value"));
                totalAnsValue = totalAnsValue + Float.parseFloat(index.getString("answer_value"));
                colorValue.add(Color.parseColor(index.getString("color")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isBarchart.equalsIgnoreCase("1")) {
            barChart.setVisibility(View.VISIBLE);

            pieChart.setVisibility(View.GONE);
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setGranularity(1);
            xAxis.setAvoidFirstLastClipping(true);
            xAxis.setLabelRotationAngle(20);
            xAxis.setTextSize(7);
//            Log.d("Bhavdip",""+xAxis.getTextSize());
            xAxis.disableGridDashedLine();
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {

                    // Log.d("axis", "getFormattedValue: " + value);
                    if (((int) value < ansKey.size()))
                        if ((int) value >= 0)
                            return ansKey.get((int) value).toString();
                        else
                            return "";
                    else
                        return "";
                }
            });

            YAxis leftAxis = barChart.getAxisLeft();
            barChart.getDescription().setEnabled(false);
            barChart.getAxisRight().setEnabled(false);
            leftAxis.setSpaceTop(35f);
            leftAxis.setSpaceBottom(10f);
            leftAxis.setDrawLimitLinesBehindData(true);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
            BarData data = new BarData(getDataSet());
            barChart.setData(data);
            barChart.animateXY(2000, 2000);
            barChart.invalidate();
            barChart.setTouchEnabled(false);


        } else {
            barChart.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
            pieChart.getDescription().setEnabled(false);
            pieChart.setTouchEnabled(false);
            setPiechart();
        }
    }

}
