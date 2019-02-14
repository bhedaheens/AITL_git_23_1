package com.allintheloop.Fragment.ExhibitorFragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.ExhibitorLeadRepresentatives_Adapter;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLinRepresenetativesArrayList;
import com.allintheloop.R;
import com.allintheloop.Util.EndlessScrollListener_temp;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.allintheloop.Util.GlobalData.getexhibitorLeadSettingUpdate;

/**
 * A simple {@link Fragment} subclass.
 */
public class Exhibitor_SettingLead_Fragment extends Fragment implements VolleyInterface {


    Button btn_addnewRep;
    SessionManager sessionManager;
    GradientDrawable drawable;
    ProgressBar progressBar1;
    int total_pages, current_page = 1;

    ArrayList<Object> objectArrayList = new ArrayList<>();
    ExhibitorLinRepresenetativesArrayList linRepresenetativesArrayList;
    ExhibitorLeadRepresentatives_Adapter leadRepresentatives_adapter;
    WrapContentLinearLayoutManager mLayoutManager;
    RecyclerView rv_repListing;
    TextView txt_noDataFound;
    EndlessScrollListener_temp endlessScrollListener;
    NestedScrollView scrollView;

    public Exhibitor_SettingLead_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor__settinglead, container, false);
        btn_addnewRep = (Button) rootView.findViewById(R.id.btn_addnewRep);
        sessionManager = new SessionManager(getActivity());
        rv_repListing = (RecyclerView) rootView.findViewById(R.id.rv_repListing);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        txt_noDataFound = (TextView) rootView.findViewById(R.id.txt_noDataFound);

        leadRepresentatives_adapter = new ExhibitorLeadRepresentatives_Adapter(objectArrayList, getActivity());
        rv_repListing.setAdapter(leadRepresentatives_adapter);
        rv_repListing.setNestedScrollingEnabled(false);
        mLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rv_repListing.setLayoutManager(mLayoutManager);
        rv_repListing.setItemAnimator(new DefaultItemAnimator());

        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {

            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_addnewRep.setBackgroundDrawable(drawable);
            btn_addnewRep.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        } else {
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_addnewRep.setBackgroundDrawable(drawable);
            btn_addnewRep.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }


        btn_addnewRep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ExhibitorLead_AttendeeSelectList fragment = new ExhibitorLead_AttendeeSelectList();
                fragment.show(fm, "DialogFragment");
            }
        });


        endlessScrollListener = new EndlessScrollListener_temp(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                if (total_pages > page) {
                    current_page = page + 1;
                    getLeadMoreData();
                }
            }
        };
        scrollView.setOnScrollChangeListener(endlessScrollListener);
        getRepresentativesData();
        return rootView;
    }

    private void getRepresentativesData() {
        objectArrayList = new ArrayList<>();
        progressBar1.setVisibility(View.GONE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getExhibitorRepresentativesLead, Param.getExhibitorLeadRepresentativedData(sessionManager.getEventId(), sessionManager.getUserId(), current_page), 0, false, this);
        }
    }

    private void refreshData() {
        objectArrayList = new ArrayList<>();
        progressBar1.setVisibility(View.GONE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getExhibitorRepresentativesLead, Param.getExhibitorLeadRepresentativedData(sessionManager.getEventId(), sessionManager.getUserId(), current_page), 1, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    private void getLeadMoreData() {
        progressBar1.setVisibility(View.VISIBLE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getExhibitorRepresentativesLead, Param.getExhibitorLeadRepresentativedData(sessionManager.getEventId(), sessionManager.getUserId(), current_page), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private class setListview extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            leadRepresentatives_adapter.updateList(objectArrayList);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        progressBar1.setVisibility(View.GONE);
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        total_pages = jsonObjectData.getInt("total_page");
                        Gson gson = new Gson();
                        linRepresenetativesArrayList = gson.fromJson(jsonObjectData.toString(), ExhibitorLinRepresenetativesArrayList.class);
                        objectArrayList.addAll(linRepresenetativesArrayList.getExhibitorLead_myLeadDataArrayList());

                        if (objectArrayList.size() != 0) {
                            rv_repListing.setVisibility(View.VISIBLE);
                            txt_noDataFound.setVisibility(View.GONE);
                        } else {
                            rv_repListing.setVisibility(View.GONE);
                            txt_noDataFound.setVisibility(View.VISIBLE);
                        }

                        new setListview().execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        progressBar1.setVisibility(View.GONE);
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        total_pages = jsonObjectData.getInt("total_page");
                        Gson gson = new Gson();
                        linRepresenetativesArrayList = gson.fromJson(jsonObjectData.toString(), ExhibitorLinRepresenetativesArrayList.class);
                        objectArrayList.addAll(linRepresenetativesArrayList.getExhibitorLead_myLeadDataArrayList());

                        if (objectArrayList.size() != 0) {
                            rv_repListing.setVisibility(View.VISIBLE);
                            txt_noDataFound.setVisibility(View.GONE);
                        } else {
                            rv_repListing.setVisibility(View.GONE);
                            txt_noDataFound.setVisibility(View.VISIBLE);
                        }

                        leadRepresentatives_adapter = new ExhibitorLeadRepresentatives_Adapter(objectArrayList, getActivity());
                        rv_repListing.setAdapter(leadRepresentatives_adapter);
                        rv_repListing.setNestedScrollingEnabled(false);
                        mLayoutManager = new WrapContentLinearLayoutManager(getActivity());
                        rv_repListing.setLayoutManager(mLayoutManager);
                        rv_repListing.setItemAnimator(new DefaultItemAnimator());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private BroadcastReceiver exhibitorListingData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d("niral", "Reciver Called : " + sessionManager.getUserProfile());
            // TODO Auto-generated method stub
            current_page = 1;
            refreshData();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(exhibitorListingData, new IntentFilter(getexhibitorLeadSettingUpdate));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(exhibitorListingData);
    }
}
