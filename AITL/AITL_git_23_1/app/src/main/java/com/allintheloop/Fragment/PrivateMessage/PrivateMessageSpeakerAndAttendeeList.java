package com.allintheloop.Fragment.PrivateMessage;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.allintheloop.Adapter.PrivateMessage_Sppr_Adapter;
import com.allintheloop.Adapter.PrivateSpeakerExpandtableList;
import com.allintheloop.Bean.PrivateMessage_Sppiner;
import com.allintheloop.Bean.PrivateSpeakerList;
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
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrivateMessageSpeakerAndAttendeeList extends DialogFragment implements VolleyInterface {


    ExpandableListView lvExp;
    PrivateSpeakerExpandtableList listAdapter;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<PrivateSpeakerList>> listDataChild;
    ArrayList<PrivateSpeakerList> child;
    SessionManager sessionManager;

    public PrivateMessageSpeakerAndAttendeeList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_private_message_speaker_and_attendee_list, container, false);
        lvExp = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        sessionManager = new SessionManager(getActivity());

        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAllPrivateSpeaker, Param.getAllprivateSpeaker(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken()), 0, false, this);
        }

        return rootView;
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jObjectlist = new JSONObject(volleyResponse.output);
                    if (jObjectlist.getString("success").equalsIgnoreCase("true")) {


                        listDataHeader = new ArrayList<String>();
                        listDataChild = new HashMap<String, ArrayList<PrivateSpeakerList>>();

                        listDataHeader.add("Speaker");
                        listDataHeader.add("attendees");
                        JSONArray jArraySpeaker = jObjectlist.getJSONArray("speakers");

                        child = new ArrayList<>();
                        for (int sp = 0; sp < jArraySpeaker.length(); sp++) {

                            JSONObject jObjectspeaker = jArraySpeaker.getJSONObject(sp);
                            child.add(new PrivateSpeakerList(jObjectspeaker.getString("Firstname") + " " + jObjectspeaker.getString("Lastname"), jObjectspeaker.getString("Id")));
                        }

                        listDataChild.put(listDataHeader.get(0).toString(), child);

                        JSONArray jArrayattndee = jObjectlist.getJSONArray("attendees");
                        child = new ArrayList<>();
                        for (int at = 0; at < jArrayattndee.length(); at++) {

                            JSONObject jObjectattendee = jArrayattndee.getJSONObject(at);
                            child.add(new PrivateSpeakerList(jObjectattendee.getString("Firstname") + " " + jObjectattendee.getString("Lastname"), jObjectattendee.getString("Id")));
                        }

                        Log.d("ListDataHeader", listDataHeader.toString());

                        Log.d("ListDataChild", listDataChild.toString());
                        listDataChild.put(listDataHeader.get(1).toString(), child);
                        listAdapter = new PrivateSpeakerExpandtableList(getActivity(), listDataHeader, listDataChild);
                        lvExp.setAdapter(listAdapter);

                        lvExp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                                PrivateSpeakerList privateObj = (PrivateSpeakerList) listAdapter.getChild(groupPosition, childPosition);
                                Log.d("AITL Data", privateObj.getName() + " Name" + privateObj.getId());

                                Private_Message_Fragment.spn_user.setText(privateObj.getName());
                                Private_Message_Fragment.sppinerArrayList.add(new PrivateMessage_Sppiner(privateObj.getId(), privateObj.getName()));
                                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                                Private_Message_Fragment.spprAdapter = new PrivateMessage_Sppr_Adapter(Private_Message_Fragment.sppinerArrayList, getActivity());
                                Private_Message_Fragment.rv_viewUser.setLayoutManager(layoutManager);
                                Private_Message_Fragment.rv_viewUser.setItemAnimator(new DefaultItemAnimator());
                                Private_Message_Fragment.rv_viewUser.setAdapter(Private_Message_Fragment.spprAdapter);
                                Private_Message_Fragment.arrayReqid.put(privateObj.getId());
                                getDialog().dismiss();
                                return false;
                            }
                        });


                    } else {
                        ToastC.show(getActivity(), jObjectlist.getString("message"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }
}
