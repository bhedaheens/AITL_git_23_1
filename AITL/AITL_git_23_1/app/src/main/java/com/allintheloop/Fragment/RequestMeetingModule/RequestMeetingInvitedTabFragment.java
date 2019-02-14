package com.allintheloop.Fragment.RequestMeetingModule;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Adapter.RequestMetting.Adapter_RequestMeetingInvitedMoreAtteendeList;
import com.allintheloop.Bean.Attendee.AttendeeInviteMoreList;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.allintheloop.Util.GlobalData.updateRequestMeetingInvitedList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestMeetingInvitedTabFragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewinviteMore;
    NestedScrollView nestedScrollView;
    ProgressBar progressBar;
    EditText edt_search;
    ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> objectArrayList;
    AttendeeInviteMoreList attendeeInviteMoreList;
    Adapter_RequestMeetingInvitedMoreAtteendeList adapterInviteMore;
    SessionManager sessionManager;
    TextView txt_noData;

    public RequestMeetingInvitedTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_request_meeting_invited_tab, container, false);
        rv_viewinviteMore = (RecyclerView) rootView.findViewById(R.id.rv_viewinviteMore);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);
        objectArrayList = new ArrayList<>();
        txt_noData = (TextView) rootView.findViewById(R.id.txt_noData);
        sessionManager = new SessionManager(getActivity());


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (objectArrayList.size() > 0) {
                    adapterInviteMore.getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        getData();
        return rootView;
    }


    private void getData() {
        objectArrayList.clear();
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getinvitedAttedeeMoreData, Param.getInviteEdData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getRequestMeetingId()), 0, false, this);
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(exhibitorListingData, new IntentFilter(updateRequestMeetingInvitedList));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(exhibitorListingData);
    }

    private BroadcastReceiver exhibitorListingData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getData();
        }
    };

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        attendeeInviteMoreList = gson.fromJson(jsonObject1.toString(), AttendeeInviteMoreList.class);
                        objectArrayList.addAll(attendeeInviteMoreList.getAttendeeinviteMoreDataArrayList());
                        if (objectArrayList.size() != 0) {
                            txt_noData.setVisibility(View.GONE);
                            rv_viewinviteMore.setVisibility(View.VISIBLE);
                            adapterInviteMore = new Adapter_RequestMeetingInvitedMoreAtteendeList(objectArrayList, getContext());
                            rv_viewinviteMore.setAdapter(adapterInviteMore);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rv_viewinviteMore.setLayoutManager(mLayoutManager);
                            rv_viewinviteMore.setItemAnimator(new DefaultItemAnimator());
                        } else {
                            txt_noData.setVisibility(View.VISIBLE);
                            rv_viewinviteMore.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
