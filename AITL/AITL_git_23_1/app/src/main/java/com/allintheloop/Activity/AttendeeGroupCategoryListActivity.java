package com.allintheloop.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.allintheloop.Adapter.Attendee.AttendeeGroupCategoeyAdapter;
import com.allintheloop.Bean.Attendee.AttendeeCategoryList;
import com.allintheloop.R;
import com.allintheloop.Util.FastScrollRecyclerViewItemDecoration;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class AttendeeGroupCategoryListActivity extends AppCompatActivity implements VolleyInterface {

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    Button btnDone;
    EditText edt_search;
    ArrayList<AttendeeCategoryList.AttendeeCategory> attendeeCategoryArrayList;
    ArrayList<AttendeeCategoryList.AttendeeCategory> myDatasetOriginal = new ArrayList<>();
    HashMap<String, Integer> mapIndex;
    AttendeeGroupCategoeyAdapter attendeeGroupCategoeyAdapter;
    SessionManager sessionManager;
    ArrayList<String> selected = new ArrayList<>();
    ArrayList<String> categoryKeyword = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_category_list);
        btnDone = findViewById(R.id.btnDone);
        mRecyclerView = findViewById(R.id.rclCountry);
        edt_search = findViewById(R.id.edt_search);

        sessionManager = new SessionManager(AttendeeGroupCategoryListActivity.this);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));


            btnDone.setBackgroundDrawable(drawable);
            btnDone.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));


            btnDone.setBackgroundDrawable(drawable);
            btnDone.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().length() > 0) {
                    attendeeGroupCategoeyAdapter.getFilter().filter(s.toString());
                } else if (s.toString().trim().length() == 0) {
//                        sessionManager.keyboradHidden(edt_search);
                    attendeeGroupCategoeyAdapter.getFilter().filter(s.toString());
                }
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected.clear();
                categoryKeyword.clear();
                sessionManager.keyboradHidden(edt_search);
                ArrayList<AttendeeCategoryList.AttendeeCategory> selectedList = attendeeGroupCategoeyAdapter.getSelectedList();
                for (int i = 0; i < selectedList.size(); i++) {
                    selected.add(selectedList.get(i).getCategory());
                    categoryKeyword.add(selectedList.get(i).getCategorieKeywords());
                }
                Intent intent = new Intent();
                intent.putExtra("selected", categoryKeyword);
                intent.putExtra("myData", myDatasetOriginal);
                intent.putExtra("selectedName", selected);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        if (attendeeCategoryArrayList != null) {
            Collections.sort(attendeeCategoryArrayList, new SortComparator());
            mapIndex = calculateIndexesForName(attendeeCategoryArrayList);
            attendeeGroupCategoeyAdapter = new AttendeeGroupCategoeyAdapter(attendeeCategoryArrayList, mapIndex);
            mLayoutManager = new LinearLayoutManager(getBaseContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(attendeeGroupCategoeyAdapter);
            FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
            mRecyclerView.addItemDecoration(decoration);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }

//        getGroupCategories();
    }

    private HashMap<String, Integer> calculateIndexesForName(ArrayList<AttendeeCategoryList.AttendeeCategory> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getCategory();
            String index = name.substring(0, 1);
            index = index.toUpperCase();
            if (!mapIndex.containsKey(index)) {
                mapIndex.put(index, i);
            }
        }
        return mapIndex;
    }


    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String time1 = "", time2 = "";
            if (o1 instanceof AttendeeCategoryList.AttendeeCategory) {
                time1 = ((AttendeeCategoryList.AttendeeCategory) o1).getCategory();
            }
            if (o2 instanceof AttendeeCategoryList.AttendeeCategory) {
                time2 = ((AttendeeCategoryList.AttendeeCategory) o2).getCategory();
            }
            return time1.compareTo(time2);
        }
    }


//    private void getGroupCategories() {
//
//        if (GlobalData.isNetworkAvailable(AttendeeGroupCategoryListActivity.this)) {
//
//            new VolleyRequest(AttendeeGroupCategoryListActivity.this, VolleyRequest.Method.POST, MyUrls.get_AttendanceList, Param.getAttendanceList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getUserId(), "", 1, ""), 4, false, this);
//        }
//    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
