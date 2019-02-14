package com.allintheloop.Fragment.ExhibitorFragment;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allintheloop.Adapter.Exhibitor.ExhibitorLead_SurveyPagerAdapter;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_MyLeadData_Offline;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_SurveyData;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_SurveyListData;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorPreviousRedirecId;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorSurveyQuestionAns;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.HomeCustomViewPager;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorLead_SurveyDailogFramgment extends DialogFragment implements VolleyInterface {


    SessionManager sessionManager;
    Button btn_back, btn_next, btn_Qsfinish, btn_save, btn_rest;
    String text;
    ExhibitorLead_SurveyPagerAdapter lead_surveyPagerAdapter;
    ExhibitorLead_SurveyListData lead_surveyListData;
    ArrayList<Object> objectArrayList;

    JSONArray jArrayInput;
    JSONObject jsonInput;
    String retrialId;
    int index = 0;
    int pos;
    Dialog dialog;
    HomeCustomViewPager viewPager;
    Bundle bundle;
    JSONObject jsonObject;
    int currentPage;
    JSONObject jsonObjectProfile;
    ArrayList<ExhibitorSurveyQuestionAns> surveyQuestionAns;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    ArrayList<ExhibitorPreviousRedirecId> redirecIdArrayList;
    ArrayList<Integer> qIdStrings;

    String scanData = "";
    ArrayList<ExhibitorLead_MyLeadData_Offline> data_offlineArrayList;
    LinearLayout linear_btn_extra;

    public ExhibitorLead_SurveyDailogFramgment() {
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
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor_lead_survey_dailog_framgment, container, false);
        viewPager = (HomeCustomViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setPagingEnabled(false);
        bundle = getArguments();
        surveyQuestionAns = new ArrayList<>();
        redirecIdArrayList = new ArrayList<>();
        btn_next = (Button) rootView.findViewById(R.id.btn_next);
        btn_Qsfinish = (Button) rootView.findViewById(R.id.btn_Qsfinish);
        btn_rest = (Button) rootView.findViewById(R.id.btn_rest);
        btn_save = (Button) rootView.findViewById(R.id.btn_save);
        btn_back = (Button) rootView.findViewById(R.id.btn_back);
        linear_btn_extra = (LinearLayout) rootView.findViewById(R.id.linear_btn_extra);
        jsonObjectProfile = new JSONObject();
        qIdStrings = new ArrayList<>();
        data_offlineArrayList = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        btn_back.setVisibility(View.INVISIBLE);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setPagingEnabled(true);

                getDataNextDetail(currentPage);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setPagingEnabled(true);
                getDataPreviousDetail(currentPage);

            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
            btn_rest.setVisibility(View.VISIBLE);
        } else {
            btn_rest.setVisibility(View.INVISIBLE);
        }


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                checkNextData(currentPage);
                fillupFinishData();
            }
        });

        btn_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetQueAns();
            }
        });


        btn_Qsfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setPagingEnabled(true);

                fillupFinishData();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    btn_back.setVisibility(View.INVISIBLE);
                    btn_Qsfinish.setVisibility(View.GONE);
                    btn_next.setVisibility(View.VISIBLE);
                } else {

                    btn_back.setVisibility(View.VISIBLE);
                    btn_next.setVisibility(View.VISIBLE);
                    if (btn_Qsfinish.getVisibility() == View.VISIBLE) {
                        btn_Qsfinish.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("Bhavdip CLICK", "" + position);
                viewPager.setPagingEnabled(false);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        sessionManager = new SessionManager(getActivity());
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

            btn_next.setBackground(drawable);
            btn_Qsfinish.setBackground(drawable);
            btn_back.setBackground(drawable);
            btn_save.setBackground(drawable);
            btn_rest.setBackground(drawable);

            btn_back.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_Qsfinish.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_save.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_rest.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

//            survey_progress.getProgressDrawable().setColorFilter(Color.parseColor(sessionManager.getTopBackColor()), PorterDuff.Mode.SRC_IN);


        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));

            btn_next.setBackground(drawable);
            btn_Qsfinish.setBackground(drawable);
            btn_back.setBackground(drawable);
            btn_save.setBackground(drawable);
            btn_rest.setBackground(drawable);
            btn_back.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_Qsfinish.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_next.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_save.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_rest.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

        }

        if (bundle.containsKey("isscanLead")) {
            linear_btn_extra.setVisibility(View.VISIBLE);
        }


        if (bundle.containsKey("isoffline")) {
            if (bundle.getString("isoffline").equalsIgnoreCase("1")) {
                bundleData();
                try {
                    scanData = bundle.getString("scan_data");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                bundleData();
            }
        }

        return rootView;
    }


    private void fillupFinishData() {
        jArrayInput = new JSONArray();
        data_offlineArrayList = new ArrayList<>();
        if (surveyQuestionAns.size() != 0) {
            for (int i = 0; i < surveyQuestionAns.size(); i++) {
                try {
                    jsonInput = new JSONObject();
                    jsonInput.put("exibitor_questionid", surveyQuestionAns.get(i).getQ_id());
                    jsonInput.put("Answer", surveyQuestionAns.get(i).getQ_ans());
                    jsonInput.put("answer_comment", surveyQuestionAns.get(i).getQ_comments());

                    jArrayInput.put(jsonInput);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (GlobalData.isNetworkAvailable(getActivity())) {
                UpdateLeadData();
            } else {
                if (sqLiteDatabaseHandler.isScanLeadDataExist(sessionManager.getEventId(), sessionManager.getUserId(), scanData)) {
                    try {
                        data_offlineArrayList = sqLiteDatabaseHandler.getMyLeadMyExiLeadDataBadgeNumberwise(sessionManager.getEventId(), sessionManager.getUserId(), scanData);
                        ExhibitorLead_MyLeadData_Offline data_offline = data_offlineArrayList.get(0);
                        String leadData = data_offline.getData();
                        JSONObject jsonObjectData = new JSONObject(leadData);
                        JSONObject jsonObjectLead = jsonObjectData.getJSONObject("leads");
                        JSONArray jsonArraySurvey = jsonObjectLead.getJSONArray("survey");


                        JSONObject c_jsonData = new JSONObject();
                        JSONObject c_jsonObjectLead = new JSONObject();
                        JSONArray c_jsonArraySurvey = new JSONArray();

                        c_jsonObjectLead.put("lead_user_id", jsonObjectLead.get("lead_user_id"));
                        c_jsonObjectLead.put("firstname", jsonObjectLead.get("firstname"));
                        c_jsonObjectLead.put("lastname", jsonObjectLead.get("lastname"));
                        c_jsonObjectLead.put("email", jsonObjectLead.get("email"));
                        c_jsonObjectLead.put("title", jsonObjectLead.get("title"));
                        c_jsonObjectLead.put("company_name", jsonObjectLead.get("company_name"));
                        c_jsonObjectLead.put("salutation", jsonObjectLead.get("salutation"));
                        c_jsonObjectLead.put("country", jsonObjectLead.get("country"));
                        c_jsonObjectLead.put("mobile", jsonObjectLead.get("mobile"));
                        c_jsonObjectLead.put("badgeNumber", jsonObjectLead.get("badgeNumber"));
                        c_jsonObjectLead.put("Unique_no", jsonObjectLead.get("Unique_no"));

                        if (jsonArraySurvey.length() != 0) {

                            for (int i = 0; i < jsonArraySurvey.length(); i++) {

                                JSONObject jsonObjectSurvey = new JSONObject();

                                JSONObject index = jsonArraySurvey.getJSONObject(i);
                                Log.d("Bhavdip Qid", index.getString("q_id"));

                                jsonObjectSurvey.put("q_id", index.getString("q_id"));
                                jsonObjectSurvey.put("event_id", index.getString("event_id"));
                                jsonObjectSurvey.put("exibitor_user_id", index.getString("exibitor_user_id"));
                                jsonObjectSurvey.put("Question", index.getString("Question"));
                                jsonObjectSurvey.put("Question_type", index.getString("Question_type"));
                                jsonObjectSurvey.put("Option", index.getJSONArray("Option"));
                                jsonObjectSurvey.put("redirectids", index.getJSONObject("redirectids"));
                                jsonObjectSurvey.put("a_redirectids", index.getJSONArray("a_redirectids"));
                                jsonObjectSurvey.put("show_commentbox", index.getString("show_commentbox"));
                                jsonObjectSurvey.put("commentbox_display_style", index.getString("commentbox_display_style"));
                                jsonObjectSurvey.put("commentbox_label_text", index.getString("commentbox_label_text"));
                                jsonObjectSurvey.put("show_commentbox", index.getString("show_commentbox"));
                                jsonObjectSurvey.put("sort_order", "");
                                jsonObjectSurvey.put("created_date", index.getString("created_date"));
                                jsonObjectSurvey.put("skip_logic", index.getString("skip_logic"));
                                jsonObjectSurvey.put("redirectid", index.getString("redirectid"));


                                JSONArray jsonArrayAns = new JSONArray();
                                for (int k = 0; k < surveyQuestionAns.size(); k++) {
                                    if (index.getString("q_id").equalsIgnoreCase(surveyQuestionAns.get(k).getQ_id())) {
                                        if (index.getString("Question_type").equalsIgnoreCase("2")) {
                                            if (surveyQuestionAns.get(k).getQ_ans().length() > 0) {
                                                String[] ary = surveyQuestionAns.get(k).getQ_ans().split(",");
                                                for (String strvalue : ary) {
                                                    jsonArrayAns.put(strvalue);
                                                }
                                            }
                                        } else {
                                            jsonArrayAns.put(surveyQuestionAns.get(k).getQ_ans());
                                        }
                                        surveyQuestionAns.remove(k);
                                    }
                                }
                                jsonObjectSurvey.put("survey_que_ans", jsonArrayAns);
                                c_jsonArraySurvey.put(i, jsonObjectSurvey);
                            }
                        }
                        c_jsonObjectLead.put("survey", c_jsonArraySurvey);
                        c_jsonObjectLead.put("custom_column_data", jsonObjectLead.getJSONArray("custom_column_data"));
                        c_jsonData.put("leads", c_jsonObjectLead);
                        c_jsonData.put("custom_column", jsonObjectData.getJSONArray("custom_column"));

                        sqLiteDatabaseHandler.updateMyExiLeadDataSurveyData(sessionManager.getEventId(), sessionManager.getUserId(), scanData, c_jsonData.toString());
                        GlobalData.exhibitorMyLeadTab(getActivity());

                        Log.d("Bhavdip LeadCreated", c_jsonData.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    Log.d("Bhavdip LeadFromDatabase",""+sqLiteDatabaseHandler.getMyLeadMyExiLeadDataBadgeNumberwise(sessionManager.getEventId(),sessionManager.getUserId(),scanData));
                }
                offlineData();
            }
        } else {
            ToastC.show(getActivity(), "Please Select at least one Answer");
        }


    }


    private void bundleData() {
        if (bundle.containsKey("jsonObj")) {
            try {
                jsonObject = new JSONObject(bundle.getString("jsonObj"));
                objectArrayList = new ArrayList<>();
                Gson gson = new Gson();
                lead_surveyListData = gson.fromJson(jsonObject.toString(), ExhibitorLead_SurveyListData.class);
                objectArrayList.addAll(lead_surveyListData.getExhibitorLead_myLeadDataArrayList());
                viewPager.setVisibility(View.VISIBLE);
                viewPager.setOffscreenPageLimit(objectArrayList.size());
                lead_surveyPagerAdapter = new ExhibitorLead_SurveyPagerAdapter(getActivity(), objectArrayList, viewPager, ExhibitorLead_SurveyDailogFramgment.this);
                viewPager.setAdapter(lead_surveyPagerAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (bundle.containsKey("retrialId")) {
            retrialId = bundle.getString("retrialId");
        }
    }

    private void offlineData() {
        if (sqLiteDatabaseHandler.isExhiSurveyUploadDataExist(sessionManager.getEventId(), sessionManager.getUserId(), scanData)) {
            sqLiteDatabaseHandler.UpdateExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), jArrayInput.toString(), scanData);
        }

        ToastC.show(getActivity(), "Lead Successfully Added");
        dialog.dismiss();

    }


    private void UpdateLeadData() {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.UpdateSurveyLeadData, Param.saveSurveyLeadData(sessionManager.getEventId(), sessionManager.getUserId(), retrialId, jArrayInput.toString()), 2, true, this);
    }

    private void resetQueAns() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.resetLeadData, Param.resetMyLeadQuestionData(sessionManager.getEventId(), retrialId, sessionManager.getUserId()), 1, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void getDataNextDetail(int position) {
        ExhibitorLead_SurveyData exhibitor_detailImageObj = (ExhibitorLead_SurveyData) objectArrayList.get(position);
        for (int i = 0; i < objectArrayList.size(); i++) {
            if (i == position) {
//                redirecIdArrayList.add(new ExhibitorPreviousRedirecId(exhibitor_detailImageObj.getQId(),i));
                if (exhibitor_detailImageObj.getSkipLogic().equalsIgnoreCase("0") && exhibitor_detailImageObj.getRedirectid().equalsIgnoreCase("")) {

                    if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("1")) {
                        if (surveyQuestionAns.size() != 0) {
                            if (exhibitor_detailImageObj.getSkipLogic().equalsIgnoreCase("0")) {
                                String isnotFound = "";
                                for (int j = 0; j < surveyQuestionAns.size(); j++) {
                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {
                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        btn_next.setVisibility(View.GONE);
                                        btn_Qsfinish.setVisibility(View.VISIBLE);
                                    }
                                }

                                if (isnotFound.equalsIgnoreCase("")) {
                                    ToastC.show(getActivity(), "Please Select Option");
                                }

                            } else {
                                String isnotFound = "";
                                for (int j = 0; j < surveyQuestionAns.size(); j++) {

                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {

                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        btn_next.setVisibility(View.GONE);
                                        btn_Qsfinish.setVisibility(View.VISIBLE);
                                    }
                                }

                                if (isnotFound.equalsIgnoreCase("")) {
                                    ToastC.show(getActivity(), "Please Select Option");
                                }
                            }
                        } else {
                            ToastC.show(getActivity(), "Please Select Option");
                        }

                    } else {

                        if (surveyQuestionAns.size() != 0) {

                            if (!(exhibitor_detailImageObj.getRedirectid().equalsIgnoreCase(""))) {
                                String isnotFound = "";
                                for (int j = 0; j < surveyQuestionAns.size(); j++) {
                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {
                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        btn_next.setVisibility(View.GONE);
                                        btn_Qsfinish.setVisibility(View.VISIBLE);
                                    }

                                }

                                if (isnotFound.equalsIgnoreCase("")) {
                                    if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("2")) {
                                        ToastC.show(getActivity(), "Please Select Multiple Option");
                                    } else if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("3")) {
                                        ToastC.show(getActivity(), "Please answer in your own words");
                                    }
                                }
                            } else {

                                String isnotFound = "";

                                for (int j = 0; j < surveyQuestionAns.size(); j++) {
                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {
                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        btn_next.setVisibility(View.GONE);
                                        btn_Qsfinish.setVisibility(View.VISIBLE);
                                    }
                                }


                                if (isnotFound.equalsIgnoreCase("")) {
                                    if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("2")) {
                                        ToastC.show(getActivity(), "Please Select Multiple Option");
                                    } else if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("3")) {
                                        ToastC.show(getActivity(), "Please answer in your own words");
                                    }
                                }
                            }
                        } else {
                            if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("2")) {
                                ToastC.show(getActivity(), "Please Select Multiple Option");
                            } else if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("3")) {
                                ToastC.show(getActivity(), "Please answer in your own words");
                            }
                        }

                    }
                } else {
                    btn_next.setVisibility(View.VISIBLE);
                    btn_Qsfinish.setVisibility(View.GONE);
                    if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("1")) {
                        if (surveyQuestionAns.size() != 0) {
                            if (exhibitor_detailImageObj.getSkipLogic().equalsIgnoreCase("0")) {
                                String isnotFound = "";
                                for (int j = 0; j < surveyQuestionAns.size(); j++) {
                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {
                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        getRedirectId(exhibitor_detailImageObj.getRedirectid());
                                        qIdStrings.add(position);
                                    }
                                }

                                if (isnotFound.equalsIgnoreCase("")) {
                                    ToastC.show(getActivity(), "Please Select Option");
                                }

                            } else {
                                String isnotFound = "";
                                for (int j = 0; j < surveyQuestionAns.size(); j++) {

                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {

                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        skipLogic(surveyQuestionAns.get(j).getQ_ans());
                                        qIdStrings.add(position);
                                    }
                                }

                                if (isnotFound.equalsIgnoreCase("")) {
                                    ToastC.show(getActivity(), "Please Select Option");
                                }
                            }
                        } else {
                            ToastC.show(getActivity(), "Please Select Option");
                        }
                    } else {
                        if (surveyQuestionAns.size() != 0) {

                            if (!(exhibitor_detailImageObj.getRedirectid().equalsIgnoreCase(""))) {
                                String isnotFound = "";
                                for (int j = 0; j < surveyQuestionAns.size(); j++) {
                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {
                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        getRedirectId(exhibitor_detailImageObj.getRedirectid());
                                        qIdStrings.add(position);
                                    }

                                }


                                if (isnotFound.equalsIgnoreCase("")) {
                                    if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("2")) {
                                        ToastC.show(getActivity(), "Please Select Multiple Option");
                                    } else if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("3")) {
                                        ToastC.show(getActivity(), "Please answer in your own words");
                                    }
                                }
                            } else {

                                String isnotFound = "";

                                for (int j = 0; j < surveyQuestionAns.size(); j++) {
                                    if (surveyQuestionAns.get(j).getQ_id().equalsIgnoreCase(exhibitor_detailImageObj.getQId())) {
                                        lead_surveyPagerAdapter.clearArrayList();
                                        lead_surveyPagerAdapter.ischeckClicked = false;
                                        isnotFound = "1";
                                        viewPager.setCurrentItem(currentPage + 1, true);
                                        qIdStrings.add(position);
                                    }
                                }


                                if (isnotFound.equalsIgnoreCase("")) {
                                    if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("2")) {
                                        ToastC.show(getActivity(), "Please Select Multiple Option");
                                    } else if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("3")) {
                                        ToastC.show(getActivity(), "Please answer in your own words");
                                    }
                                }
                            }
                        } else {
                            if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("2")) {
                                ToastC.show(getActivity(), "Please Select Multiple Option");
                            } else if (exhibitor_detailImageObj.getQuestionType().equalsIgnoreCase("3")) {
                                ToastC.show(getActivity(), "Please answer in your own words");
                            }
                        }
                    }
                }
            } else {
            }
        }
    }

    public void ansFillup(String q_id, String q_ans, int pos) {
        Log.d("Bhavdip Call API", q_id + "" + q_ans + "" + pos);

        if (surveyQuestionAns.size() != 0) {
            String isFound = "";
            for (int i = 0; i < surveyQuestionAns.size(); i++) {

                if (surveyQuestionAns.get(i).getQ_id().equalsIgnoreCase(q_id)) {
                    isFound = "1";
                    surveyQuestionAns.remove(i);
                    surveyQuestionAns.add(i, new ExhibitorSurveyQuestionAns(q_id, q_ans, ""));
                }
            }
            if (isFound.equalsIgnoreCase("")) {
                surveyQuestionAns.add(new ExhibitorSurveyQuestionAns(q_id, q_ans, ""));

            }
        } else {
            surveyQuestionAns.add(new ExhibitorSurveyQuestionAns(q_id, q_ans, ""));
        }
    }

    public void ansRemove(String q_id, int pos) {
        Log.d("Bhavdip Call API", q_id + "" + pos);

        if (surveyQuestionAns.size() != 0) {
            String isFound = "";
            for (int i = 0; i < surveyQuestionAns.size(); i++) {

                if (surveyQuestionAns.get(i).getQ_id().equalsIgnoreCase(q_id)) {
                    isFound = "1";
                    surveyQuestionAns.remove(i);
                }
            }

        }
    }

    private void skipLogic(String ans) {
        for (int i = 0; i < objectArrayList.size(); i++) {
            ExhibitorLead_SurveyData surveyData = (ExhibitorLead_SurveyData) objectArrayList.get(i);
            for (int j = 0; j < surveyData.getARedirectids().size(); j++) {
                if (surveyData.getARedirectids().get(j).getOption().equalsIgnoreCase(ans)) {
                    getRedirectId(surveyData.getARedirectids().get(j).getRedirectId());
                }

            }
        }
    }

    private void getDataPreviousDetail(int position) {
        if (qIdStrings.size() != 0) {
            try {
                Log.d("Bhavdip Before Postion", "" + qIdStrings.toString());

                viewPager.setCurrentItem(qIdStrings.get(qIdStrings.size() - 1), true);
//                viewPager.notify();
                qIdStrings.remove(qIdStrings.size() - 1);
                lead_surveyPagerAdapter.ischeckClicked = false;
                Log.d("Bhavdip after Postion", qIdStrings.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getRedirectId(String q_id) {

        for (int i = 0; i < objectArrayList.size(); i++) {
            ExhibitorLead_SurveyData surveyData = (ExhibitorLead_SurveyData) objectArrayList.get(i);
            if (surveyData.getQId().equalsIgnoreCase(q_id)) {
                viewPager.setCurrentItem(i, true);

            }
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        dialog.dismiss();
                        GlobalData.exhibitorMyLeadTab(getActivity());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        dialog.dismiss();
                        GlobalData.exhibitorMyLeadTab(getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        dialog.dismiss();
                        GlobalData.exhibitorMyLeadTab(getActivity());
//                        Exhibitor_Lead_Fragment.changeTab();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
