package com.allintheloop.Fragment.ExhibitorFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.ExhibitorRequestMettingAdapter;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorRequestListing;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorReuqestMettingList extends Fragment implements VolleyInterface {


    TextView textViewNoDATA;
    EditText edt_search;
    ArrayList<ExhibitorRequestListing> listingArray;
    RecyclerView rv_viewMettingList;
    ExhibitorRequestMettingAdapter requestMettingAdapter;
    SessionManager sessionManager;
    String str_tag = "";
    SwipeRefreshLayout mSwipeRefreshLayout;

    public ExhibitorReuqestMettingList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor_reuqest_metting_list, container, false);
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        str_tag = getTag();
        Log.d("AITL TAG", "TAG" + str_tag);    // 0 Exhibitor
        // 1 Attendee
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        rv_viewMettingList = (RecyclerView) rootView.findViewById(R.id.rv_viewMettingList);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        sessionManager = new SessionManager(getActivity());

        sessionManager.strModuleId = "Meetings";

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (listingArray.size() != 0) {
                    if (charSequence.length() > 0) {
                        requestMettingAdapter.getFilter().filter(charSequence);
                    } else {
                        requestMettingAdapter = new ExhibitorRequestMettingAdapter(getActivity(), listingArray);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rv_viewMettingList.setLayoutManager(mLayoutManager);
                        rv_viewMettingList.setItemAnimator(new DefaultItemAnimator());
                        rv_viewMettingList.setAdapter(requestMettingAdapter);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (listingArray.size() != 0) {
                    if (editable.length() > 0) {
                        requestMettingAdapter.getFilter().filter(editable);
                    } else {
                        requestMettingAdapter = new ExhibitorRequestMettingAdapter(getActivity(), listingArray);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        rv_viewMettingList.setLayoutManager(mLayoutManager);
                        rv_viewMettingList.setItemAnimator(new DefaultItemAnimator());
                        rv_viewMettingList.setAdapter(requestMettingAdapter);

                    }
                }
            }
        });
//


        getListData();


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListData();
            }
        });


        return rootView;
    }


    private void getListData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (str_tag.equalsIgnoreCase("0")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exhibitorgetAllRequestMeeting, Param.getAllRequestMettingList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
            } else if (str_tag.equalsIgnoreCase("1")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.attendeegetAllRequestMeeting, Param.getAllRequestMettingList(sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
            }
        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        mSwipeRefreshLayout.setRefreshing(false);
                        listingArray = new ArrayList<>();
                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject index = jsonArray.getJSONObject(i);

                            listingArray.add(new ExhibitorRequestListing(
                                    index.getString("Firstname")
                                    , index.getString("Lastname")
                                    , index.getString("date")
                                    , index.getString("time")
                                    , index.getString("status")
                                    , index.getString("request_id")
                                    , index.getString("exhibiotor_id")
                                    , index.getString("Logo")
                                    , str_tag
                                    , index.getString("location")
                                    , index.getString("Company_name")
                                    , index.getString("Title")));
                        }

                        if (listingArray.size() != 0) {
                            edt_search.setVisibility(View.VISIBLE);
                            textViewNoDATA.setVisibility(View.GONE);
                            rv_viewMettingList.setVisibility(View.VISIBLE);
                            requestMettingAdapter = new ExhibitorRequestMettingAdapter(getActivity(), listingArray);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rv_viewMettingList.setLayoutManager(mLayoutManager);
                            rv_viewMettingList.setItemAnimator(new DefaultItemAnimator());
                            rv_viewMettingList.setAdapter(requestMettingAdapter);
                        } else {
                            edt_search.setVisibility(View.VISIBLE);
                            textViewNoDATA.setVisibility(View.VISIBLE);
                            rv_viewMettingList.setVisibility(View.GONE);
                        }
                    } else {
                        edt_search.setVisibility(View.VISIBLE);
                        textViewNoDATA.setVisibility(View.VISIBLE);
                        rv_viewMettingList.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
