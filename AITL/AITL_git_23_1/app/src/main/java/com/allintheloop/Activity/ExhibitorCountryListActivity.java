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

import com.allintheloop.Adapter.CountryCardAdapter;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCountry;
import com.allintheloop.R;
import com.allintheloop.Util.FastScrollRecyclerViewItemDecoration;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorCountryListActivity extends AppCompatActivity {


    public ExhibitorCountryListActivity() {
        // Required empty public constructor
    }

    CountryCardAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mRecyclerView;
    ArrayList<ExhibitorCountry> myDataset;
    ArrayList<ExhibitorCountry> myDatasetOriginal = new ArrayList<>();
    ArrayList<String> selectedParentCat;
    ArrayList<String> selected = new ArrayList<>();
    ArrayList<String> countryId = new ArrayList<>();
    HashMap<String, Integer> mapIndex;
    Button btnDone;
    EditText edt_search;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    SessionManager sessionManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_country_list);

        btnDone = (Button) findViewById(R.id.btnDone);
        mRecyclerView = (RecyclerView) findViewById(R.id.rclCountry);
        edt_search = (EditText) findViewById(R.id.edt_search);

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(this);
        sessionManager = new SessionManager(this);
//        myDataset = sqLiteDatabaseHandler.getExhibitorCountries(sessionManager.getEventId());

        // specify an adapter (see also next example)
        myDataset = (ArrayList<ExhibitorCountry>) getIntent().getExtras().getSerializable("countries");
        selectedParentCat = (ArrayList<String>) getIntent().getExtras().getSerializable("selected");
        myDatasetOriginal.addAll(myDataset);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getBaseContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


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
            mAdapter = new CountryCardAdapter(myDataset, mapIndex);
            mRecyclerView.setAdapter(mAdapter);

            FastScrollRecyclerViewItemDecoration decoration = new FastScrollRecyclerViewItemDecoration(this);
            mRecyclerView.addItemDecoration(decoration);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selected.clear();
                countryId.clear();
                sessionManager.keyboradHidden(edt_search);
                ArrayList<ExhibitorCountry> selectedList = mAdapter.getSelectedList();
                for (int i = 0; i < selectedList.size(); i++) {
                    selected.add(selectedList.get(i).getCountry_name());
                    countryId.add(selectedList.get(i).getId());
                }
                Intent intent = new Intent();
                intent.putExtra("selected", countryId);
                intent.putExtra("myData", myDatasetOriginal);
                intent.putExtra("selectedName", selected);
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
                    mAdapter.getFilter().filter(s.toString());
                } else if (s.toString().trim().length() == 0) {
//                        sessionManager.keyboradHidden(edt_search);
                    mAdapter.getFilter().filter(s.toString());
                }
            }
        });
    }

    private HashMap<String, Integer> calculateIndexesForName(ArrayList<ExhibitorCountry> items) {
        HashMap<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < items.size(); i++) {
            String name = items.get(i).getCountry_name();
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
            if (o1 instanceof ExhibitorCountry) {
                time1 = ((ExhibitorCountry) o1).getCountry_name();
            }
            if (o2 instanceof ExhibitorCountry) {
                time2 = ((ExhibitorCountry) o2).getCountry_name();
            }
            return time1.compareTo(time2);
        }
    }


}