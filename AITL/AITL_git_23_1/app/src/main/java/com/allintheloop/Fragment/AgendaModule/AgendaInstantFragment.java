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

import com.allintheloop.Adapter.Agenda.AgendaListAdapter;
import com.allintheloop.Bean.AgendaData.Agenda_Time;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaInstantFragment extends Fragment {


    public AgendaInstantFragment() {
        // Required empty public constructor
    }

    public static AgendaInstantFragment newInstance(ArrayList<Agenda_Time> agenda) {

        Bundle args = new Bundle();
        args.putSerializable("agenda", agenda);
        AgendaInstantFragment fragment = new AgendaInstantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView rclView;
    ArrayList<Agenda_Time> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dummy_agenda, container, false);

        Bundle b = getArguments();

        arrayList = (ArrayList<Agenda_Time>) b.getSerializable("agenda");

        Log.d("array", "onCreateView: " + arrayList.toString());

        rclView = (RecyclerView) v.findViewById(R.id.recycler_agenda);
        rclView.setLayoutManager(new LinearLayoutManager(getContext()));
        rclView.setItemAnimator(new DefaultItemAnimator());

        AgendaListAdapter listAdapter = new AgendaListAdapter(getContext(), arrayList);
        rclView.setAdapter(listAdapter);

        return v;
    }

}
