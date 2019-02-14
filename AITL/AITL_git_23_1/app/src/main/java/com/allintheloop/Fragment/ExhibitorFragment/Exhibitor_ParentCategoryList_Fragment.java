package com.allintheloop.Fragment.ExhibitorFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.ExhibitorParentCategory_AdapterListing;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorParentCategoryData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class Exhibitor_ParentCategoryList_Fragment extends Fragment implements VolleyInterface {


    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data;
    ProgressBar progressBar;
    DefaultLanguage.DefaultLang defaultLang;

    WrapContentLinearLayoutManager linearLayoutManager;
    RecyclerView rv_viewCategoryListing;
    NestedScrollView scrollView;
    TextView textViewNoDATA;

    SQLiteDatabaseHandler databaseHandler;
    SessionManager sessionManager;
    Bundle bundle;
    EditText edt_search;
    String adverties_id = "", keyword = "";
    ArrayList<ExhibitorParentCategoryData> exhibitorParentCategoryData;
    ExhibitorParentCategory_AdapterListing parentCategoryAdapterListing;

    public Exhibitor_ParentCategoryList_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor__parent_category_list, container, false);


        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        GlobalData.currentModuleForOnResume = GlobalData.exhibitorModuleid;
        databaseHandler = new SQLiteDatabaseHandler(getActivity());
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        rv_viewCategoryListing = (RecyclerView) rootView.findViewById(R.id.rv_viewCategoryListing);
        bundle = new Bundle();

//        if(sessionManager.getShowSlider().equalsIgnoreCase("0"))
//        {
//            sessionManager.setExhibitorParentCategoryId("");
//            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
//            GlobalData.CURRENT_FRAG=GlobalData.ExhibitorCategoryWiseData;
//            ((MainActivity)getActivity()).loadFragment();
//        }

        edt_search.setHint(defaultLang.get3Search());

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (exhibitorParentCategoryData.size() != 0) {
                    parentCategoryAdapterListing.getFilter().filter(charSequence.toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (exhibitorParentCategoryData.size() != 0) {
                    parentCategoryAdapterListing.getFilter().filter(editable.toString().trim());
                }
            }
        });

        exhibitorParentCategoryData = new ArrayList<>();


        if (sessionManager.isLogin()) {
            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);

            getCategoryListData();
        } else {
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);

                getCategoryListData();
            }
        }


        return rootView;
    }


    private void getCategoryListData() {
        exhibitorParentCategoryData.clear();
        exhibitorParentCategoryData = databaseHandler.getParentCategoryList1(sessionManager.getEventId());
//        Collections.sort(exhibitorParentCategoryData, new SortingCategory());
        if (exhibitorParentCategoryData.size() != 0) {
            parentCategoryAdapterListing = new ExhibitorParentCategory_AdapterListing(exhibitorParentCategoryData, getActivity());
            rv_viewCategoryListing.setItemAnimator(new DefaultItemAnimator());
            rv_viewCategoryListing.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_viewCategoryListing.setAdapter(parentCategoryAdapterListing);
        } else {
            sessionManager.setExhibitorParentCategoryId("");
            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
            GlobalData.CURRENT_FRAG = GlobalData.ExhibitorCategoryWiseData;
            ((MainActivity) getActivity()).loadFragment();
        }
//        }
    }

    private void advertiesClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", adverties_id, "AD", ""), 5, false, this);
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 5, false, this);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                Log.e("DAtabase", "GetParentDAta: " + databaseHandler.getParentCategoryList1(sessionManager.getEventId()).size());
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        exhibitorParentCategoryData.clear();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArrayCategory = jsonObject1.getJSONArray("categories");
                        for (int i = 0; i < jsonArrayCategory.length(); i++) {
                            JSONObject index = jsonArrayCategory.getJSONObject(i);
                            exhibitorParentCategoryData.add(new ExhibitorParentCategoryData(
                                    index.getString("c_id")
                                    , index.getString("category")
                                    , index.getString("categorie_icon")));
                        }

                        if (exhibitorParentCategoryData.size() != 0) {
                            databaseHandler.insertUpdateAllParentCategory(exhibitorParentCategoryData, sessionManager.getEventId());
                            parentCategoryAdapterListing = new ExhibitorParentCategory_AdapterListing(exhibitorParentCategoryData, getActivity());
                            rv_viewCategoryListing.setItemAnimator(new DefaultItemAnimator());
                            rv_viewCategoryListing.setLayoutManager(new LinearLayoutManager(getContext()));
                            rv_viewCategoryListing.setAdapter(parentCategoryAdapterListing);
                        } else {
                            sessionManager.setExhibitorParentCategoryId("");
                            GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                            GlobalData.CURRENT_FRAG = GlobalData.ExhibitorCategoryWiseData;
                            ((MainActivity) getActivity()).loadFragment();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.exhibitorModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.exhibitorModuleid);
        }
    }

    private class SortingCategory implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {
            String ob1 = "", ob2 = "";
            Date date1, date2;
            if (o1 instanceof ExhibitorParentCategoryData) {
                ob1 = ((ExhibitorParentCategoryData) o1).getCategory();
            }
            if (o2 instanceof ExhibitorParentCategoryData) {
                ob2 = ((ExhibitorParentCategoryData) o2).getCategory();
            }
            return ob1.compareTo(ob2);
        }
    }
}
