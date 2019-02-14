package com.allintheloop.Fragment.RequestMeetingModule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allintheloop.Adapter.ListSugesstedDateTimeListAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.RequestMeeting.ViewRequestMettigDateTime;
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
public class ViewRequestedMettingDateTimeList_Fragment extends Fragment implements VolleyInterface {


    RecyclerView rv_viewtimeList;
    TextView txt_noData, txt_label;
    ListSugesstedDateTimeListAdapter listAdapter;
    ArrayList<ViewRequestMettigDateTime> dateTimeArrayList;
    SessionManager sessionManager;
    String date = "", sugessId = "";

    public ViewRequestedMettingDateTimeList_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_requested_metting_date_time_list_, container, false);
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        rv_viewtimeList = (RecyclerView) rootView.findViewById(R.id.rv_viewtimeList);
        txt_noData = (TextView) rootView.findViewById(R.id.txt_noData);
        txt_label = (TextView) rootView.findViewById(R.id.txt_label);
        sessionManager = new SessionManager(getActivity());
        sessionManager.strModuleId = "Suggested Meetings";
        dateTimeArrayList = new ArrayList<>();
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getSuggestTime, Param.getNewSuggestedTime(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.mettingId), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }

        return rootView;
    }

    private void bookTime() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.bookSuggestedTime, Param.booksuggestedTime(sessionManager.getEventId(), sessionManager.getUserId(), sugessId, date), 1, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);

                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        JSONArray jsonArrayData = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject index = jsonArrayData.getJSONObject(i);
                            dateTimeArrayList.add(new ViewRequestMettigDateTime(index.getString("Id"), index.getString("Heading"), index.getString("date_time"), index.getString("api_date_time")));
                        }

                        if (dateTimeArrayList.size() != 0) {

                            listAdapter = new ListSugesstedDateTimeListAdapter(dateTimeArrayList, getActivity());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

                            txt_noData.setVisibility(View.GONE);
                            txt_label.setVisibility(View.VISIBLE);
                            rv_viewtimeList.setVisibility(View.VISIBLE);
                            rv_viewtimeList.setLayoutManager(mLayoutManager);
                            rv_viewtimeList.setItemAnimator(new DefaultItemAnimator());
                            rv_viewtimeList.setAdapter(listAdapter);

                            rv_viewtimeList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    ViewRequestMettigDateTime mapObj = dateTimeArrayList.get(position);

                                    date = mapObj.getApi_date_time();
                                    sugessId = mapObj.getId();
                                    bookTime();
                                    Log.d("AITL ID", mapObj.getId());
                                    //startActivity(new Intent(getActivity(), Map_Detail_Activity.class).putExtra("map_id",mapObj.getId()));
                                }
                            }));
                        } else {
                            txt_noData.setText("No Data Found");
                            txt_noData.setVisibility(View.VISIBLE);
                            txt_label.setVisibility(View.GONE);
                            rv_viewtimeList.setVisibility(View.GONE);
                        }
                        Log.d("AITL DATA", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(getActivity(), jsonObject.getString("message"));
                        GlobalData.CURRENT_FRAG = GlobalData.View_UserWise_Agenda;
                        ((MainActivity) getActivity()).loadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

        }
    }
}
