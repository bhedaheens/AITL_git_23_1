package com.allintheloop.Fragment.AgendaModule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.allintheloop.Adapter.Agenda.AgendaBookStatusAdapter;
import com.allintheloop.Bean.AgendaData.AgendaBookStatus;
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
public class Agenda_Bookstatus_Fragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewStatus;
    Button btn_gotoBack;
    ArrayList<AgendaBookStatus> statusArrayList;
    SessionManager sessionManager;
    AgendaBookStatusAdapter bookStatusAdapter;
    TextView txt_nodata;

    public Agenda_Bookstatus_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_agenda__bookstatus, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);


        rv_viewStatus = (RecyclerView) rootView.findViewById(R.id.rv_viewStatus);
        rv_viewStatus.setNestedScrollingEnabled(false);
        btn_gotoBack = (Button) rootView.findViewById(R.id.btn_gotoBack);
        txt_nodata = (TextView) rootView.findViewById(R.id.txt_nodata);
        statusArrayList = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        submitAddAgenda();


        btn_gotoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalData.CURRENT_FRAG = GlobalData.AGENDA_FRAGMENT;
                ((MainActivity) getActivity()).loadFragment();
            }
        });

        return rootView;
    }


    private void submitAddAgenda() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (sessionManager.isLogin()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.savePendingAgenda, Param.savePendingAgenda(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getUserId()), 0, true, this);
            } else {
                ToastC.show(getActivity(), "Please Login First");
            }

        }

    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.agendaRefresh(getActivity());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsondata = jsonArray.getJSONObject(i);
                            statusArrayList.add(new AgendaBookStatus(jsondata.getString("Heading"), jsondata.getString("Start_time"), jsondata.getString("book"), jsondata.getString("resion")));
                        }
                        bookStatusAdapter = new AgendaBookStatusAdapter(statusArrayList, getActivity());
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        if (statusArrayList.size() == 0) {
                            rv_viewStatus.setVisibility(View.GONE);
                            btn_gotoBack.setVisibility(View.GONE);
                            txt_nodata.setVisibility(View.VISIBLE);
                        } else {
                            txt_nodata.setVisibility(View.GONE);
                            rv_viewStatus.setVisibility(View.VISIBLE);
                            btn_gotoBack.setVisibility(View.VISIBLE);
                            rv_viewStatus.setLayoutManager(mLayoutManager);
                            rv_viewStatus.setItemAnimator(new DefaultItemAnimator());
                            rv_viewStatus.setAdapter(bookStatusAdapter);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
}
