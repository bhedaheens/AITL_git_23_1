package com.allintheloop.Fragment.EditProfileModule;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allintheloop.Adapter.EditProfile.ProfileAboutYouAdapter;
import com.allintheloop.Adapter.ExhibitorListWithSection.AdapterSectionRecyclerNew;
import com.allintheloop.Bean.Attendee.Attendance;
import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Bean.Attendee.AttendeeKeywordData;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorType;
import com.allintheloop.Bean.ExhibitorListClass.SectionHeader;
import com.allintheloop.Bean.SectionHeaderParentGroup;
import com.allintheloop.Bean.editProfileAbout.EditProfileAboutYouGeneralClass;
import com.allintheloop.Bean.editProfileAbout.EditProfileKeywordUpdateData;
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
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileEditAboutYou_activity extends AppCompatActivity implements VolleyInterface {


    ImageView img_back;
    RecyclerView recyclerView;
    List<AttendeeFilterList.Data> mainKeywordList;
    List<AttendeeFilterList.Data> selectedKeywordList;
    ArrayList<String> onlykeywordList;
    List<String> onlykIDList;
    ProfileAboutYouAdapter aboutYouAdapter;
    ArrayList<EditProfileAboutYouGeneralClass> generalClassArrayList;
    BoldTextView txt_count, txt_save;
    SessionManager sessionManager;
    EditText edt_search;
    HashMap<String, List<String>> keywordValue;
    boolean isKeywordCountUpaate = true;
    JSONArray jsonMainArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_about_you_activity);
        recyclerView = findViewById(R.id.recyclerview_categoeyListing);
        img_back = findViewById(R.id.img_back);
        txt_count = findViewById(R.id.txt_count);
        txt_save = findViewById(R.id.txt_save);
        edt_search = findViewById(R.id.edt_search);
        onlykeywordList = new ArrayList<>();
        onlykIDList = new ArrayList<>();
        keywordValue = new HashMap<String, List<String>>();
        mainKeywordList = (List<AttendeeFilterList.Data>) getIntent().getSerializableExtra("keywordData");
        selectedKeywordList = (List<AttendeeFilterList.Data>) getIntent().getSerializableExtra("selectedKeywordData");
        generalClassArrayList = new ArrayList<>();
        sessionManager = new SessionManager(this);
        sessionManager.keyboradHidden(edt_search);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.keyboradHidden(edt_search);
                onBackPressed();
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "About";

            @Override
            public void onClick(View v) {
                if (keywordValue.size() != 0) {
                    JSONArray jsonKeywordData;
                    jsonMainArray = new JSONArray();
                    for (Map.Entry<String, List<String>> entry : keywordValue.entrySet()) {
                        String key = entry.getKey();
                        List<String> values = entry.getValue();
                        jsonKeywordData = new JSONArray();
                        for (String keyword : values) {
                            jsonKeywordData.put(keyword);
                        }
                        try {
                            JSONObject jsonkeywordObject = new JSONObject();
                            jsonkeywordObject.put("keywords", jsonKeywordData);
                            jsonkeywordObject.put("k", key);
                            jsonMainArray.put(jsonkeywordObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    submitData();
                } else {
                    ToastC.show(getApplicationContext(), "Please Fillup Keyword");
                }

            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    isKeywordCountUpaate = false;
                    filter(v.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.toString().length() == 0)
//                    filter(s.toString().trim());

            }
        });
        new getKeywordAsynTask().execute();
    }

    public class getKeywordAsynTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            ArrayList<AttendeeKeywordData> child;
            for (int i = 0; i < mainKeywordList.size(); i++) {
                child = new ArrayList<>();
                for (String keyword : mainKeywordList.get(i).getKeywords()) {
                    if (!keyword.trim().isEmpty()) {
                        child.add(new AttendeeKeywordData(keyword.trim(), false));
                    }
                }
                mainKeywordList.get(i).setKeywordData(child);
            }

            for (int i = 0; i < mainKeywordList.size(); i++) {
                for (int j = 0; j < selectedKeywordList.size(); j++) {
                    if (mainKeywordList.get(i).getId().equals(selectedKeywordList.get(j).getId())) {
                        for (int k = 0; k < mainKeywordList.get(i).getKeywordData().size(); k++) {
                            for (int l = 0; l < selectedKeywordList.get(j).getKeywords().size(); l++) {
                                for (int m = 0; m < mainKeywordList.get(i).getKeywordData().size(); m++) {
                                    if (selectedKeywordList.get(j).getKeywords().get(l).equals(mainKeywordList.get(i).getKeywordData().get(k).getKeyword())) {
                                        mainKeywordList.get(i).getKeywordData().get(k).setCheck(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            aboutYouAdapter = new ProfileAboutYouAdapter(getApplicationContext(), mainKeywordList, false, ProfileEditAboutYou_activity.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(aboutYouAdapter);
//            buttonSet();
        }
    }

    private void submitData() {
        if (GlobalData.isNetworkAvailable(this)) {
            new VolleyRequest(this, VolleyRequest.Method.POST, MyUrls.updateAttendeeKeywordData, Param.submitAboutYouDat(sessionManager.getEventId(), sessionManager.getUserId(), jsonMainArray.toString()), 1, true, this);
        }
    }


    public void switchCountEditorNot(boolean isedit) {
        isKeywordCountUpaate = isedit;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public boolean isCheckedLeft() {
        if (onlykeywordList.size() < 10)
            return true;
        else
            return false;
    }

    public void setLimitExceedMessage() {
        txt_count.setTextColor(getResources().getColor(R.color.red));
        txt_count.setText("You do not have any selections left. Please uncheck another selection to check this selection");
    }

    private void updateListData(String getCategoryName, String getKeyword, String getCategoryId, String k, boolean isCheck) {

        if (isKeywordCountUpaate) {
            if (onlykeywordList.contains(getKeyword)) {
                for (int i = 0; i < onlykeywordList.size(); i++) {
                    if (onlykeywordList.get(i).equalsIgnoreCase(getKeyword)) {
                        try {
                            onlykeywordList.remove(i);
                            generalClassArrayList.remove(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                generalClassArrayList.add(new EditProfileAboutYouGeneralClass(getKeyword, getCategoryId, getCategoryName, k));
                onlykeywordList.add(getKeyword);
            }
            if (onlykeywordList.size() <= 10) {
                txt_count.setTextColor(getResources().getColor(R.color.black));
                txt_save.setEnabled(true);
                if (onlykeywordList.size() == 0) {
                    txt_count.setText("10 Selections Left");
                } else {
                    txt_count.setText(String.valueOf(10 - onlykeywordList.size() + " Selections Left"));
                }
            } else {
                txt_count.setTextColor(getResources().getColor(R.color.red));
                txt_count.setText("You do not have any selections left. Please uncheck another selection to check this selection");
//            ToastC.show(this, "You do not have any selections left. Please uncheck another selection to check this selection");
            }

            if (isCheck) {
                ArrayList<String> temp_keyword = new ArrayList<>();
                if (keywordValue.containsKey(k)) {
                    keywordValue.get(k).add(getKeyword);
                } else {
                    temp_keyword.add(getKeyword);
                    keywordValue.put(k, temp_keyword);
                }
            } else {
                if (keywordValue.containsKey(k)) {
                    for (int i = 0; i < keywordValue.get(k).size(); i++) {
                        if (keywordValue.get(k).get(i).equalsIgnoreCase(getKeyword)) {
                            keywordValue.get(k).remove(i);
                            if (keywordValue.get(k).size() <= 0) {
                                keywordValue.remove(k);
                            }
                        }
                    }
                }
            }
        }
    }


    private void filter(String text) {
        List<AttendeeFilterList.Data> filteredList = new ArrayList<>();
        for (AttendeeFilterList.Data d : mainKeywordList) {
            ArrayList<String> tempChild = new ArrayList();
            ArrayList<AttendeeKeywordData> keywordData = new ArrayList();
            for (AttendeeKeywordData keyword : d.getKeywordData()) {
                if (keyword.getKeyword().toLowerCase().contains(text.toLowerCase())) {
                    tempChild.add(keyword.getKeyword());
                    keywordData.add(new AttendeeKeywordData(keyword.getKeyword(), keyword.isCheck()));

                }
            }
            if (keywordData.size() > 0) {
                AttendeeFilterList.Data dataobj = new AttendeeFilterList().new Data();
                dataobj.setKeywords(tempChild);
                dataobj.setKeywordData(keywordData);
                dataobj.setColumnName(d.getColumnName());
                dataobj.setCategory(d.getCategory());
                dataobj.setK(d.getK());
                dataobj.setId(d.getId());
                filteredList.add(dataobj);
            }
        }
        aboutYouAdapter.filterList(filteredList, true);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditProfileKeywordUpdateData(EditProfileKeywordUpdateData data) {
        updateListData(data.getCategoryName(), data.getKeyword(), data.getCategoryId(), data.getK(), data.isCheck());


    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        sessionManager.keyboradHidden(edt_search);
                        setResult(RESULT_OK, new Intent().putExtra("aboutYouData", (Serializable) generalClassArrayList));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
