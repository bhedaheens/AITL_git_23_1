package com.allintheloop.Fragment.MapModule;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class New_MapDetail_Fragment extends Fragment {


    public New_MapDetail_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_new__map_detail_, container, false);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();


    }
}