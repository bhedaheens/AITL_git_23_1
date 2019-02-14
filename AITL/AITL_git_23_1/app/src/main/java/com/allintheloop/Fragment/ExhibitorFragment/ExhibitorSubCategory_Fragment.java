package com.allintheloop.Fragment.ExhibitorFragment;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.ExhibitorSubCategoryListAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorCategoryListing;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorSubCategory_Fragment extends Fragment implements VolleyInterface {


    EditText edt_search;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    RecyclerView rv_viewCategoryListing;
    TextView textViewNoDATA;
    ArrayList<ExhibitorCategoryListing> categoryListings;
    ExhibitorSubCategoryListAdapter categoryListAdapter;
    Context context;
    SessionManager sessionManager;
    DefaultLanguage.DefaultLang defaultLang;

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    RelativeLayout MainLayout, relativeLayout_Data;
    LinearLayout Container_Attendance;

    public ExhibitorSubCategory_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor_sub_category, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        GlobalData.currentModuleForOnResume = GlobalData.exhibitorModuleid;

        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        rv_viewCategoryListing = (RecyclerView) rootView.findViewById(R.id.rv_viewCategoryListing);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        Container_Attendance = (LinearLayout) rootView.findViewById(R.id.Container_Attendance);

        context = getActivity();
        categoryListings = new ArrayList<>();
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        sessionManager = new SessionManager(context);
        defaultLang = sessionManager.getMultiLangString();
        edt_search.setHint(defaultLang.get3Search());


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (categoryListings.size() != 0) {
                    categoryListAdapter.getFilter().filter(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (categoryListings.size() != 0) {
                    categoryListAdapter.getFilter().filter(editable.toString().trim());
                }
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    sessionManager.keyboradHidden(edt_search);
                    return true;
                }

                return false;
            }

        });
        getCategoryListing();
        getAdvertiesment();
        return rootView;

    }

    private void getCategoryListing() {
        categoryListings = sqLiteDatabaseHandler.getSubCategoryList1(sessionManager.getExhibitorParentCategoryId(), sessionManager.getEventId());
        if (categoryListings.size() > 0) {
            Collections.sort(categoryListings, new SortComparator());
            rv_viewCategoryListing.setVisibility(View.VISIBLE);
            textViewNoDATA.setVisibility(View.GONE);
            categoryListAdapter = new ExhibitorSubCategoryListAdapter(categoryListings, context);
            rv_viewCategoryListing.setItemAnimator(new DefaultItemAnimator());
            rv_viewCategoryListing.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_viewCategoryListing.setAdapter(categoryListAdapter);
        } else {
            rv_viewCategoryListing.setVisibility(View.GONE);
            textViewNoDATA.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Log.d("AITL Advertiesment", jsonObject.toString());

                        if (sqLiteDatabaseHandler.isAdvertiesMentExist(sessionManager.getEventId(), sessionManager.getMenuid())) {
                            sqLiteDatabaseHandler.deleteAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        }

                        getAdvertiesment(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class SortComparator implements Comparator<Object> {
        @Override
        public int compare(Object o1, Object o2) {
            String first = "", second = "";
            if (o1 instanceof ExhibitorCategoryListing) {
                first = ((ExhibitorCategoryListing) o1).getSector();
            }
            if (o2 instanceof ExhibitorCategoryListing) {
                second = ((ExhibitorCategoryListing) o2).getSector();
            }
            return first.compareTo(second);
        }
    }

    private void getAdvertiesment(JSONObject jsonObject) {

        try {
            JSONObject jsonObjectAdavertiesment = jsonObject.getJSONObject("data");
            JSONArray jArrayHeader = jsonObjectAdavertiesment.getJSONArray("header");
            JSONArray jArrayFooter = jsonObjectAdavertiesment.getJSONArray("footer");

            topAdverViewArrayList = new ArrayList<>();
            bottomAdverViewArrayList = new ArrayList<>();
            for (int i = 0; i < jArrayHeader.length(); i++) {
                JSONObject index = jArrayHeader.getJSONObject(i);
                topAdverViewArrayList.add(new AdvertiesmentTopView(index.getString("image"), index.getString("url"), index.getString("id")));
            }

            for (int i = 0; i < jArrayFooter.length(); i++) {
                JSONObject index = jArrayFooter.getJSONObject(i);
                bottomAdverViewArrayList.add(new AdvertiesMentbottomView(index.getString("image"), index.getString("url"), index.getString("id")));
            }

            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1")) {
                sessionManager.footerView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "0", MainLayout, relativeLayout_Data, Container_Attendance, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(context, "1", MainLayout, relativeLayout_Data, Container_Attendance, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(context, "1", MainLayout, relativeLayout_Data, Container_Attendance, topAdverViewArrayList, getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 2, false, this);

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.adverties_Data)));
                        Log.d("AITL  Map Oflline", jsonObject.toString());
                        getAdvertiesment(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.exhibitorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.exhibitorModuleid);
        }
    }
}

