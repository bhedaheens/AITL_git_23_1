package com.allintheloop.Fragment.AgendaModule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Adapter.Agenda.UserWiseAgendaListAdapter;
import com.allintheloop.Bean.AgendaData.Agenda_Time;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserWiseAgendaInstantFragment extends Fragment {


    RecyclerView rclView;
    ArrayList<Agenda_Time> arrayList;

    public UserWiseAgendaInstantFragment() {
        // Required empty public constructor
    }

    public static UserWiseAgendaInstantFragment newInstance(ArrayList<Agenda_Time> agenda) {

        Bundle args = new Bundle();
        args.putSerializable("agenda", agenda);
        UserWiseAgendaInstantFragment fragment = new UserWiseAgendaInstantFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_wise_agenda_instant, container, false);

        Bundle b = getArguments();

        arrayList = (ArrayList<Agenda_Time>) b.getSerializable("agenda");

        Log.d("array", "onCreateView: " + arrayList.toString());

        rclView = (RecyclerView) rootView.findViewById(R.id.recycler_agenda);
        rclView.setLayoutManager(new LinearLayoutManager(getContext()));
        rclView.setItemAnimator(new DefaultItemAnimator());

        UserWiseAgendaListAdapter listAdapter = new UserWiseAgendaListAdapter(getContext(), arrayList);
        rclView.setAdapter(listAdapter);
        return rootView;
    }

}
