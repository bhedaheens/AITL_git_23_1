package com.allintheloop.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.allintheloop.Adapter.ExhibitorListWithSection.AdapterGroupSectionParentCategoryListing;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.Bean.SectionHeaderParentGroup;
import com.allintheloop.R;
import com.allintheloop.Util.FastScrollRecyclerViewItemDecoration;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorParentCatListActivity extends AppCompatActivity {


    public ExhibitorParentCatListActivity() {
        // Required empty public constructor
    }

    AdapterGroupSectionParentCategoryListing groupSectionParentCategoryListing;

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
//    VerticalRecyclerViewFastScroller fastScroller;

    //    RecyclerView FastScrollRecyclerView;
    ArrayList<SectionHeaderParentGroup> myDataset;
    ArrayList<SectionHeaderParentGroup> myDatasetOriginal = new ArrayList<>();
    ArrayList<String> selected = new ArrayList<>();
    ArrayList<String> countryId = new ArrayList<>();
    ArrayList<String> parentCatId = new ArrayList<>();
    ArrayList<String> CategoryId = new ArrayList<>();
    HashMap<String, Integer> mapIndex;
    Button btnDone;
    EditText edt_search;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    SessionManager sessionManager;
    String isGroupData = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_country_list);

        btnDone = (Button) findViewById(R.id.btnDone);
        mRecyclerView = (RecyclerView) findViewById(R.id.rclCountry);
        edt_search = (EditText) findViewById(R.id.edt_search);
//        fastScroller= (VerticalRecyclerViewFastScroller)findViewById(R.id.fast_scroller);
//        fastScroller.setRecyclerView(mRecyclerView);
//        mRecyclerView.setOnScrollListener(fastScroller.getOnScrollListener());
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(this);
        sessionManager = new SessionManager(this);
        myDatasetOriginal = new ArrayList<>();
        myDataset = (ArrayList<SectionHeaderParentGroup>) getIntent().getExtras().getSerializable("categories");

        myDatasetOriginal.addAll(myDataset);
        isGroupData = getIntent().getStringExtra("isGroupData");

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

        if (myDataset != null) {
            Collections.sort(myDataset, new SortComparator());
            mapIndex = calculateIndexesForName(myDataset);
            int scrollPosition = 0;
//            if (mRecyclerView.getLayoutManager() != null) {
//                scrollPosition =
//                        ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
//            }
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExhibitorParentCatListActivity.this);
            mRecyclerView.setLayoutManager(linearLayoutManager);
//            mRecyclerView.scrollToPosition(scrollPosition);
            groupSectionParentCategoryListing = new AdapterGroupSectionParentCategoryListing(getApplicationContext(), myDataset, mapIndex);
            mRecyclerView.setAdapter(groupSectionParentCategoryListing);
            FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
            mRecyclerView.addItemDecoration(decoration);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected.clear();
                countryId.clear();
                parentCatId.clear();
                CategoryId.clear();
                sessionManager.keyboradHidden(edt_search);
                ArrayList<SectionHeaderParentGroup> selectedList = groupSectionParentCategoryListing.getSelectedList();
                for (int i = 0; i < selectedList.size(); i++) {
                    for (ExhibitorCategoryListing categorylist : selectedList.get(i).getExhibitorCategoryListings()) {
                        if (categorylist.isCheck()) {
                            selected.add(categorylist.getSector());
                            countryId.add(categorylist.getShortDesc());
                            parentCatId.add(categorylist.getId());
                        }
                    }
                }
                Intent intent = new Intent();
                intent.putExtra("selected", countryId);
                intent.putExtra("myData", myDatasetOriginal);
                intent.putExtra("selectedName", selected);
                intent.putExtra("categoryId", parentCatId);
                setResult(RESULT_OK, intent);
                finish();
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

                if (s.toString().trim().length() > 0) {
                    groupSectionParentCategoryListing.filter(s.toString());
                } else if (s.toString().trim().length() == 0) {
                    groupSectionParentCategoryListing.filter(s.toString());
                }
            }
        });
    }

    private HashMap<String, Integer> calculateIndexesForName(ArrayList<SectionHeaderParentGroup> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();

        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getSectionText();
            if (!name.isEmpty()) {
                String index = name.substring(0, 1);
                index = index.toUpperCase();
                if (!mapIndex.containsKey(index)) {
                    mapIndex.put(index, i);
                }
            }
        }
        return mapIndex;
    }

    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String time1 = "", time2 = "";
            Date date1, date2;
            if (o1 instanceof SectionHeaderParentGroup) {
                time1 = ((SectionHeaderParentGroup) o1).getSectionText();
            }
            if (o2 instanceof SectionHeaderParentGroup) {
                time2 = ((SectionHeaderParentGroup) o2).getSectionText();
            }
            return time1.compareTo(time2);
        }
    }
}