package com.allintheloop.Fragment.BeaconModule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.Adapter.BeaconListDataAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.BeaconListData;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.BeaconFoundDailog;
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
public class Beacon_FindDeviceListing_Fragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewBeaconList;
    ArrayList<BeaconListData> beaconListDatas;
    BeaconListDataAdapter beaconListDataAdapter;
    TextView txt_label;
    SessionManager sessionManager;
    CardView card_nodata;

    public Beacon_FindDeviceListing_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_beacon__find_device_listing, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        txt_label = (TextView) rootView.findViewById(R.id.txt_label);
        rv_viewBeaconList = (RecyclerView) rootView.findViewById(R.id.rv_viewBeaconList);
        card_nodata = (CardView) rootView.findViewById(R.id.card_nodata);
        beaconListDatas = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());

        sessionManager.strModuleId = "Beacon";


        return rootView;
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject index = jsonArray.getJSONObject(i);
                            beaconListDatas.add(new BeaconListData(index.getString("Id")
                                    , index.getString("UDID")
                                    , index.getString("beacon_name")));
                        }

                        if (beaconListDatas.size() != 0) {
                            card_nodata.setVisibility(View.GONE);
                            rv_viewBeaconList.setVisibility(View.VISIBLE);
                            txt_label.setVisibility(View.VISIBLE);
                            beaconListDataAdapter = new BeaconListDataAdapter(beaconListDatas, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            rv_viewBeaconList.setVisibility(View.VISIBLE);
                            rv_viewBeaconList.setLayoutManager(mLayoutManager);
                            rv_viewBeaconList.setItemAnimator(new DefaultItemAnimator());
                            rv_viewBeaconList.setAdapter(beaconListDataAdapter);
                        } else {
                            card_nodata.setVisibility(View.VISIBLE);
                            rv_viewBeaconList.setVisibility(View.GONE);
                            txt_label.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getBeaconListData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getALlBeaconIdList, Param.getBeaconListData(sessionManager.getUserId(), sessionManager.getEventId()), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getBeaconListData();

    }
}
