package com.allintheloop.Fragment.PresantationModule;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
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
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PresantationDetail_ViewResultDialog extends DialogFragment implements VolleyInterface {

    Dialog dialog;
    Bundle bundle;
    TextView txt_label;
    ImageView dailog_close;
    String poll_slideId = "", isBarchart = "";
    Button btn_pushRemove;
    WebView webview_chart;
    boolean is_push;
    SessionManager sessionManager;
    BarChart barChart;
    PieChart pieChart;
    String str_question = "";

    ArrayList ansKey = new ArrayList<>();
    ArrayList ansValue = new ArrayList<>();
    ArrayList colorVaLUE = new ArrayList<>();
    float totalAnsValue = 0f;

    public PresantationDetail_ViewResultDialog() {
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
        View rootView = inflater.inflate(R.layout.fragment_presantationdetail__viewresult, container, false);
        txt_label = rootView.findViewById(R.id.txt_label);
        dailog_close = rootView.findViewById(R.id.dailog_close);
        barChart = rootView.findViewById(R.id.barChart);
        pieChart = rootView.findViewById(R.id.piechart);
        btn_pushRemove = rootView.findViewById(R.id.btn_pushRemove);
        webview_chart = rootView.findViewById(R.id.webview_chart);
        webview_chart.getSettings().setJavaScriptEnabled(true);
        webview_chart.getSettings().setAllowContentAccess(true);
        webview_chart.setVerticalScrollBarEnabled(true);
        webview_chart.setHorizontalScrollBarEnabled(true);
        sessionManager = new SessionManager(getActivity());
        webview_chart.getSettings().setDefaultTextEncodingName("utf-8");
        bundle = getArguments();


        if (bundle.containsKey("image")) {

            String image_name = bundle.getString("image");
            if (!(image_name.equalsIgnoreCase(""))) {
                String extension = MimeTypeMap.getFileExtensionFromUrl(image_name);
                String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                if (type != null) {
                    if (type.equalsIgnoreCase("image/jpeg")) {
                        btn_pushRemove.setVisibility(View.GONE);
                        webview_chart.setVisibility(View.GONE);
                        barChart.setVisibility(View.GONE);
                    } else {
                        poll_slideId = bundle.getString("image");
                    }
                } else {
                    poll_slideId = bundle.getString("image");
                }
            }
        }
        if (bundle.containsKey("isbarChart")) {
            isBarchart = bundle.getString("isbarChart");

        }

        if (!(poll_slideId.equalsIgnoreCase(""))) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.presantationDetailViewResultUid, Param.get_Presantation_DetailViewResult(sessionManager.getEventId(), poll_slideId, Presantation_Detail_Fragment.presantation_id, isBarchart), 0, false, this);
            else
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.presantationDetailViewResult, Param.get_Presantation_DetailViewResult(sessionManager.getEventId(), poll_slideId, Presantation_Detail_Fragment.presantation_id, isBarchart), 0, false, this);
        } else {
            txt_label.setVisibility(View.VISIBLE);
            btn_pushRemove.setVisibility(View.GONE);
            webview_chart.setVisibility(View.GONE);
            barChart.setVisibility(View.GONE);
        }

        dailog_close.setOnClickListener(view -> getDialog().dismiss());

        btn_pushRemove.setOnClickListener(view -> {
            if (!is_push) {
                pushViewResult();
            } else {
                RemoveViewResult();
            }
        });

        return rootView;
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
        barDataSet1.setColors(colorVaLUE);
        barDataSet1.setValueTextColor(Color.parseColor("#000000"));
        barDataSet1.setValueTextSize(17f);

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);

        return dataSets;
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
    }


    private void pushViewResult() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.presantationDetailPushViewResult, Param.getPresantationDetailPushResult(Presantation_Detail_Fragment.presantation_id, poll_slideId, isBarchart), 1, true, this);
    }

    private void RemoveViewResult() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.presantationDetailRemoveResult, Param.get_Presantation_DetailRemoveResult(Presantation_Detail_Fragment.presantation_id, isBarchart), 2, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        pieChart.setVisibility(View.GONE);
                        barChart.setVisibility(View.GONE);
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        str_question = jsonObjectData.getString("question");
                        ansKey.clear();
                        ansValue.clear();
                        colorVaLUE.clear();
                        totalAnsValue = 0f;
                        JSONArray jsonArray = jsonObjectData.getJSONArray("answer");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject index = jsonArray.getJSONObject(i);
                            ansKey.add(index.getString("answer_key"));
                            ansValue.add(index.getString("answer_value"));
                            totalAnsValue = totalAnsValue + Float.parseFloat(index.getString("answer_value"));
                            colorVaLUE.add(Color.parseColor(index.getString("color")));
                        }
                        if (isBarchart.equalsIgnoreCase("1")) {
                            barChart.setVisibility(View.VISIBLE);
                            pieChart.setVisibility(View.GONE);
                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1);
                            xAxis.mLabelWidth = 50;
                            xAxis.setLabelRotationAngle(20);
                            xAxis.setCenterAxisLabels(false);
                            xAxis.setAvoidFirstLastClipping(true);
                            xAxis.setTextSize(7);
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
                            barChart.getAxisRight().setEnabled(false);
                            leftAxis.setSpaceTop(35f);
                            leftAxis.setSpaceBottom(10f);
                            leftAxis.setDrawLimitLinesBehindData(true);

                            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
                            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
                            BarData data = new BarData(getDataSet());
                            barChart.setData(data);
                            barChart.getDescription().setEnabled(false);
                            barChart.getLegend().setWordWrapEnabled(true);
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


                        GlobalData.presantationSendMessage(getActivity());
                        txt_label.setVisibility(View.GONE);
                        barChart.setVisibility(View.VISIBLE);
                        webview_chart.setVisibility(View.GONE);
                        String url = jsonObject.getString("data");
                        webview_chart.loadUrl("http://" + url);
                        btn_pushRemove.setVisibility(View.VISIBLE);
                        String show_remove_button = jsonObject.getString("show_remove_button");
                        if (show_remove_button.equalsIgnoreCase("1")) {
                            btn_pushRemove.setText("Remove Push");
                            is_push = true;
                        } else {
                            btn_pushRemove.setClickable(true);
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
                        GlobalData.presantationSendMessage(getActivity());
                        btn_pushRemove.setText("Remove Push");
                        is_push = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.presantationSendMessage(getActivity());
                        btn_pushRemove.setText("Push Result");
                        is_push = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }
}

