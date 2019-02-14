package com.allintheloop.Fragment.BeaconModule;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;

import com.allintheloop.Util.SessionManager;

import com.estimote.sdk.SystemRequirementsChecker;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeaconFinder_Fragment extends Fragment {

    LinearLayout linear_findDevice;
    Dialog dialog;
    SwitchCompat swch_button;
    BluetoothAdapter BTAdapter;
    Button btn_connectedDevice;
    boolean isBluetoothOn = true;
    String TAG = "AITL";
    SessionManager sessionManager;

    public BeaconFinder_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_beacon_finder, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        BTAdapter = BluetoothAdapter.getDefaultAdapter();
        linear_findDevice = (LinearLayout) rootView.findViewById(R.id.linear_findDevice);
        swch_button = (SwitchCompat) rootView.findViewById(R.id.swch_button);
        sessionManager = new SessionManager(getActivity());
        btn_connectedDevice = (Button) rootView.findViewById(R.id.btn_connectedDevice);
        sessionManager.strModuleId = "Beacon";

        if (BTAdapter.isEnabled()) {
            swch_button.setChecked(true);
            isBluetoothOn = false;
        } else {
            swch_button.setChecked(false);
            isBluetoothOn = true;
        }


        linear_findDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findBeacon();
            }
        });

        btn_connectedDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.getAllBeaconList;
                ((MainActivity) getActivity()).loadFragment();
            }
        });


//        btn_connectedDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                findBeacon();
//            }
//        });

        swch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                enabeldBuletooth();
            }
        });
        return rootView;
    }


    private void findBeacon() {

        SystemRequirementsChecker.checkWithDefaultDialogs(getActivity());
        swch_button.setChecked(true);
        isBluetoothOn = false;

        FragmentManager fm = getActivity().getSupportFragmentManager();
        BeaconFoundListing_DailogFragment fragment = new BeaconFoundListing_DailogFragment();
        fragment.show(fm, "BeaconFragmentDailog");

    }


    private void enabeldBuletooth() {
        try {
            if (isBluetoothOn) {
                if (!BTAdapter.isEnabled()) {
                    BTAdapter.enable();
                    swch_button.setChecked(true);
                    isBluetoothOn = false;
                }
            } else {
                BTAdapter.disable();
                isBluetoothOn = true;
                swch_button.setChecked(false);
//                unbindBeacon();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}


