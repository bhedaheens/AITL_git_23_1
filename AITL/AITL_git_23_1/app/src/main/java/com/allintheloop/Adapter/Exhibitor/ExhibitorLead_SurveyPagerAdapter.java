package com.allintheloop.Adapter.Exhibitor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_SurveyData;
import com.allintheloop.Fragment.ExhibitorFragment.ExhibitorLead_SurveyDailogFramgment;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.HomeCustomViewPager;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nteam on 11/9/17.
 */

public class ExhibitorLead_SurveyPagerAdapter extends PagerAdapter implements VolleyInterface {

    Context context;
    ArrayList<Object> arrayList;
    ArrayList<String> arrayAddiChkList;
    SessionManager sessionManager;
    HomeCustomViewPager customViewPager;
    TextView txt_question, txt_hint;
    LinearLayout linear_custome;
    String StrRadio;
    ExhibitorLead_SurveyDailogFramgment lead_surveyDailogFramgment;
    public Boolean ischeckClicked = false;

    public ExhibitorLead_SurveyPagerAdapter(Context context, ArrayList<Object> arrayList, HomeCustomViewPager viewPager, ExhibitorLead_SurveyDailogFramgment lead_surveyDailogFramgment) {
        this.context = context;
        this.arrayList = arrayList;
        sessionManager = new SessionManager(context);
        this.customViewPager = viewPager;
        arrayAddiChkList = new ArrayList<>();
        this.lead_surveyDailogFramgment = lead_surveyDailogFramgment;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View viewItem = inflater.inflate(R.layout.adapter_exhibitorlead_surveyquestion, container, false);
        txt_question = (TextView) viewItem.findViewById(R.id.txt_question);
        txt_hint = (TextView) viewItem.findViewById(R.id.txt_hint);
        linear_custome = (LinearLayout) viewItem.findViewById(R.id.linear_custome);
        final ExhibitorLead_SurveyData dataObj = (ExhibitorLead_SurveyData) arrayList.get(position);

        Log.d("AITL Ans", "Q_Id :- " + dataObj.getQId() + "Ans :- " + dataObj.getSurvey_que_ans().toString());


        if (dataObj.getQuestionType().equalsIgnoreCase("1")) {
            if (linear_custome.getChildCount() != 0) {
                linear_custome.removeAllViews();
            }
            txt_hint.setText("Please select one answer.");

            try {
                RadioGroup radioGroup = new RadioGroup(context);
                for (int l = 0; l < dataObj.getOption().size(); l++) {
                    RadioButton radioButton = new RadioButton(context);
                    radioButton.setText(dataObj.getOption().get(l).toString());

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

                    if (dataObj.getSurvey_que_ans().size() != 0) {
                        if (dataObj.getSurvey_que_ans().get(0).toString().equalsIgnoreCase(dataObj.getOption().get(l).toString())) {
                            radioButton.setChecked(true);
                            StrRadio = radioButton.getText().toString();
                            sessionManager.skipLogicAns = StrRadio;
                            lead_surveyDailogFramgment.ansFillup(dataObj.getQId(), StrRadio, position);
                        }
                    }

                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group, int checkedId) {
                            RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                            StrRadio = radioButton.getText().toString();
                            sessionManager.skipLogicAns = StrRadio;
                            lead_surveyDailogFramgment.ansFillup(dataObj.getQId(), StrRadio, position);
//                            questionArrayList.get(index).setAns("");
//                            questionArrayList.get(index).setAns(StrRadio);
                            Log.d("Radio button", StrRadio);
                        }
                    });

                }
                linear_custome.addView(radioGroup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (dataObj.getQuestionType().equalsIgnoreCase("2")) {
            if (linear_custome.getChildCount() != 0)
                linear_custome.removeAllViews();
            txt_hint.setText("Please select Multiple answer.");
            try {
                for (int l = 0; l < dataObj.getOption().size(); l++) {
                    final CheckBox chkBox = new CheckBox(context);
                    chkBox.setText(dataObj.getOption().get(l).toString());

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
                    params1.bottomMargin = 50;
                    params1.gravity = Gravity.CENTER_VERTICAL;
                    chkBox.setLayoutParams(params1);
                    linear_custome.addView(chkBox);

                    if (dataObj.getSurvey_que_ans() != null) {
                        if (dataObj.getSurvey_que_ans().size() != 0) {
                            boolean isfound = false;
                            /*for (int i=0;i<=dataObj.getSurvey_que_ans().size();i++)
                            {
                                if (dataObj.getOption().get(l).toString().equalsIgnoreCase(dataObj.getSurvey_que_ans().get(i).toString()))
                                {
                                    isfound="1";
                                    arrayAddiChkList.add(dataObj.getSurvey_que_ans().get(i).toString());
                                    chkBox.setChecked(true);
                                }
                            }*/

                            if (dataObj.getSurvey_que_ans().contains(dataObj.getOption().get(l).toString())) {
                                isfound = true;
                                arrayAddiChkList.add(dataObj.getOption().get(l).toString());
                                chkBox.setChecked(true);
                            }


                            if (isfound) {
                                String patternString = arrayAddiChkList.toString().replaceAll("[\\[\\](){}]", "");
                                patternString = patternString.replaceAll(", ", ",");
                                Log.d("TEST", patternString);
                                lead_surveyDailogFramgment.ansFillup(dataObj.getQId(), patternString.toString(), position);
                            }
//                            arrayAddiChkList.clear();
                        }
                    }
                    chkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (arrayAddiChkList.size() == 0 && !ischeckClicked) {
                                for (int l = 0; l < dataObj.getOption().size(); l++) {
                                    ischeckClicked = true;
                                    if (dataObj.getSurvey_que_ans().contains(dataObj.getOption().get(l).toString())) {
                                        arrayAddiChkList.add(dataObj.getOption().get(l).toString());
                                    }
                                }
                            }

                            if (isChecked) {
                                Log.d("AITL CheckBox Ans", chkBox.getText().toString());
                                if (!arrayAddiChkList.contains(chkBox.getText().toString())) {
                                    arrayAddiChkList.add(chkBox.getText().toString());
                                    dataObj.getSurvey_que_ans().add(chkBox.getText().toString());
                                }
                            } else {
                                if (arrayAddiChkList.contains(chkBox.getText().toString())) {
                                    arrayAddiChkList.remove(chkBox.getText().toString());
                                    dataObj.getSurvey_que_ans().remove(chkBox.getText().toString());
                                }
                                Log.d("AITL CheckBox Ans", chkBox.getText().toString());
                            }

                            String patternString = arrayAddiChkList.toString().replaceAll("[\\[\\](){}]", "");
                            patternString = patternString.replaceAll(", ", ",");
                            Log.d("TEST", patternString);
                            if (arrayAddiChkList.size() > 0) {
                                lead_surveyDailogFramgment.ansFillup(dataObj.getQId(), patternString.toString(), position);
                            } else {
                                lead_surveyDailogFramgment.ansRemove(dataObj.getQId(), position);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (dataObj.getQuestionType().equalsIgnoreCase("3")) {
            linear_custome.removeAllViews();
            txt_hint.setText("Please type your answer below");
            try {
                final EditText edt_ans = new EditText(context);
                edt_ans.setTag("edt");

                if (dataObj.getOption().size() != 0) {
                    edt_ans.setHint(dataObj.getOption().get(0).toString());
                } else {
                    edt_ans.setHint("");
                }
                edt_ans.setPadding(20, 25, 0, 25);

                edt_ans.setTextSize(15);
                edt_ans.setMinLines(5);
                edt_ans.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                edt_ans.setSingleLine(false);
                edt_ans.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                edt_ans.setBackgroundResource(R.drawable.square_bg);
                edt_ans.setId(0);
                edt_ans.setGravity(Gravity.TOP);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
                params1.topMargin = 20;
                edt_ans.setLayoutParams(params1);
                linear_custome.addView(edt_ans);

                if (dataObj.getSurvey_que_ans() != null) {
                    if (dataObj.getSurvey_que_ans().size() != 0) {
                        edt_ans.setText(dataObj.getSurvey_que_ans().get(0).toString());
                        lead_surveyDailogFramgment.ansFillup(dataObj.getQId(), edt_ans.getText().toString(), position);
                    }
                }
                edt_ans.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO Auto-generated method stub
                        if (s.toString().length() == 0) {
                            lead_surveyDailogFramgment.ansRemove(dataObj.getQId(), position);
                        } else {
                            lead_surveyDailogFramgment.ansFillup(dataObj.getQId(), s.toString(), position);
                        }

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                });
//                Log.d("JsonObject", jsonInput.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        txt_question.setText(dataObj.getQuestion());
        Log.d("Bhadvip QuestionId", dataObj.getQId());
        viewItem.setTag(dataObj);
        container.addView(viewItem, 0);
        return viewItem;
    }

    public void clearArrayList() {
        arrayAddiChkList = new ArrayList<>();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
