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

import com.allintheloop.Adapter.Agenda.AgendaListAdapterNew;
import com.allintheloop.Bean.AgendaData.Agenda;
import com.allintheloop.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgendaInstantFragmentNew extends Fragment {


    public AgendaInstantFragmentNew() {
        // Required empty public constructor
    }

    public static AgendaInstantFragmentNew newInstance(ArrayList<Agenda> agenda) {

        Bundle args = new Bundle();
        args.putSerializable("agenda", agenda);
        AgendaInstantFragmentNew fragment = new AgendaInstantFragmentNew();
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView rclView;
    ArrayList<Agenda> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dummy_agenda, container, false);

        Bundle b = getArguments();

        arrayList = (ArrayList<Agenda>) b.getSerializable("agenda");

        Log.d("array", "onCreateView: " + arrayList.toString());

        rclView = (RecyclerView) v.findViewById(R.id.recycler_agenda);
        rclView.setLayoutManager(new LinearLayoutManager(getContext()));
        rclView.setItemAnimator(new DefaultItemAnimator());

        AgendaListAdapterNew listAdapter = new AgendaListAdapterNew(getContext(), arrayList);
        rclView.setAdapter(listAdapter);

        return v;
    }

}
