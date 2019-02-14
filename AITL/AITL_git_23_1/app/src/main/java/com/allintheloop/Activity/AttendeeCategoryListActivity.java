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

import com.allintheloop.Adapter.Attendee.AttendeeCategoryAdapter;
import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Bean.Attendee.AttendeeKeywordData;
import com.allintheloop.R;
import com.allintheloop.Util.FastScrollRecyclerViewItemDecoration;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class AttendeeCategoryListActivity extends AppCompatActivity {

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    Button btnDone;
    EditText edt_search;
    List<AttendeeFilterList.Data> dataArrayList;
    ArrayList<AttendeeFilterList.Data> mydataArrayList = new ArrayList<>();
    HashMap<String, Integer> mapIndex;
    AttendeeCategoryAdapter attendeeCategoeyAdapter;
    SessionManager sessionManager;

    List<AttendeeKeywordData> keyword = new ArrayList<>();
    ArrayList<String> selected = new ArrayList<>();
    ArrayList<String> categoryKeyword = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_category_list);
        btnDone = (Button) findViewById(R.id.btnDone);
        mRecyclerView = (RecyclerView) findViewById(R.id.rclCountry);
        edt_search = (EditText) findViewById(R.id.edt_search);
        sessionManager = new SessionManager(AttendeeCategoryListActivity.this);
        sessionManager.keyboradHidden(edt_search);
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
                    attendeeCategoeyAdapter.getFilter().filter(s.toString());
                } else if (s.toString().trim().length() == 0) {
//                        sessionManager.keyboradHidden(edt_search);
                    attendeeCategoeyAdapter.getFilter().filter(s.toString());
                }
            }
        });


        try {
            AttendeeFilterList.Data data = (AttendeeFilterList.Data) getIntent().getExtras().getSerializable("Attendeecategory");
            categoryKeyword = (ArrayList<String>) getIntent().getExtras().getSerializable("selectedData");
            dataArrayList = new ArrayList<>();
            dataArrayList.add(data);

            for (String value : dataArrayList.get(0).getKeywords()) {
                keyword.add(new AttendeeKeywordData(value, false));
            }
            mydataArrayList.addAll(dataArrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected.clear();
                sessionManager.keyboradHidden(edt_search);
                ArrayList<AttendeeKeywordData> selectedList = attendeeCategoeyAdapter.getSelectedList();
                for (int i = 0; i < selectedList.size(); i++) {
                    selected.add(selectedList.get(i).getKeyword());
                }

                Intent intent = new Intent();
                intent.putExtra("myData", mydataArrayList);
                intent.putExtra("selectedName", selected);
                intent.putExtra("categoryid", dataArrayList.get(0).getId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        if (keyword != null) {
            Collections.sort(keyword, new SortComparator());
            mapIndex = calculateIndexesForName(keyword);
            attendeeCategoeyAdapter = new AttendeeCategoryAdapter(keyword, mapIndex);
            mLayoutManager = new LinearLayoutManager(getBaseContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(attendeeCategoeyAdapter);
            FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
            mRecyclerView.addItemDecoration(decoration);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }


    private HashMap<String, Integer> calculateIndexesForName(List<AttendeeKeywordData> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getKeyword();
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
            if (o1 instanceof AttendeeKeywordData) {
                time1 = ((AttendeeKeywordData) o1).getKeyword();
            }
            if (o2 instanceof AttendeeKeywordData) {
                time2 = ((AttendeeKeywordData) o2).getKeyword();
            }
            return time1.compareTo(time2);
        }
    }

}
