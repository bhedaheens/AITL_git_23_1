package com.allintheloop.Fragment.BeaconModule;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Adapter_BeaconFoundDailog;
import com.allintheloop.Adapter.RecyclerItemClickListener;
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


import org.json.JSONObject;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class BeaconFoundListing_DailogFragment extends DialogFragment implements VolleyInterface {

    Dialog dialog;
    Adapter_BeaconFoundDailog adapter_beaconFoundDailog;
    RecyclerView recycle_beaconView;
    ImageView dailog_noti_close;
    //    private BeaconManager beaconManager;
//    private Region region;
    ArrayList<BeaconFoundDailog> arrayList_strId;
    SessionManager sessionManager;
    String scanID;
    RelativeLayout relativeLayout_forceLogin;
    TextView txt_nologin;

    private BluetoothAdapter btAdapter;
    private Handler scanHandler = new Handler();
    private int scan_interval_ms = 1000;
    private boolean isScanning = false;


    public BeaconFoundListing_DailogFragment() {
        // Required empty public constructor
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_beacon_found_listing__dailog, container, false);
        recycle_beaconView = (RecyclerView) rootView.findViewById(R.id.recycle_beaconView);
        dailog_noti_close = (ImageView) rootView.findViewById(R.id.dailog_noti_close);
        txt_nologin = (TextView) rootView.findViewById(R.id.txt_nologin);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        arrayList_strId = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        scanHandler.post(scanRunnable);
        dailog_noti_close.setOnClickListener(view -> dialog.dismiss());


        if (arrayList_strId.size() != 0) {
            recycle_beaconView.setVisibility(View.VISIBLE);
            relativeLayout_forceLogin.setVisibility(View.GONE);
        } else {
            recycle_beaconView.setVisibility(View.GONE);
            relativeLayout_forceLogin.setVisibility(View.VISIBLE);
            txt_nologin.setText("Scanning.....");
        }

        recycle_beaconView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                BeaconFoundDailog beaconObj = arrayList_strId.get(position);
                if (beaconObj.getTag().equalsIgnoreCase("iBeacon")) {
                    sendUuid(beaconObj.getUuid(), beaconObj.getMajor().toString(), beaconObj.getMinor().toString(), "", "");
                } else {
                    sendUuid("", "", "", beaconObj.getEdystoneNameSpace(), beaconObj.getEdystoneInstance());
                }

                //startActivity(new Intent(getActivity(), Map_Detail_Activity.class).putExtra("map_id",mapObj.getId()));
            }
        }));


        return rootView;
    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {

            if (isScanning) {
                if (btAdapter != null) {
                    btAdapter.stopLeScan(leScanCallback);
                }
            } else {
                if (btAdapter != null) {
                    btAdapter.startLeScan(leScanCallback);
                }
            }

            isScanning = !isScanning;

            scanHandler.postDelayed(this, scan_interval_ms);
        }
    };


    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {


        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            int startByte = 2;
            boolean patternFound = false;
            while (startByte <= 5) {
                if (((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                        ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                    patternFound = true;
                    break;
                }
                startByte++;
            }

            if (patternFound) {
                //Convert to hex String
                byte[] uuidBytes = new byte[16];
                System.arraycopy(scanRecord, startByte + 4, uuidBytes, 0, 16);
                String hexString = bytesToHex(uuidBytes);

                //UUID detection
                String uuid = hexString.substring(0, 8) + "-" +
                        hexString.substring(8, 12) + "-" +
                        hexString.substring(12, 16) + "-" +
                        hexString.substring(16, 20) + "-" +
                        hexString.substring(20, 32);

                // major
                final int major = (scanRecord[startByte + 20] & 0xff) * 0x100 + (scanRecord[startByte + 21] & 0xff);

                // minor
                final int minor = (scanRecord[startByte + 22] & 0xff) * 0x100 + (scanRecord[startByte + 23] & 0xff);


                BeaconFoundDailog beaconFoundDailogObj = new BeaconFoundDailog(uuid, String.valueOf(major), String.valueOf(minor), "IBeacon");
                int pro = calculateProximity(getDistance(rssi, scanRecord[29]));
                if (pro == 3 || pro == 0) {
                    if (checkexisting(beaconFoundDailogObj)) {
                        arrayList_strId.remove(removeObj(beaconFoundDailogObj));
                        methoLoad();
                        adapter_beaconFoundDailog.notifyDataSetChanged();
                    }
                } else {
                    if (arrayList_strId.size() == 0) {
                        arrayList_strId.add(beaconFoundDailogObj);
                        methoLoad();
                    }
                    if (!checkexisting(beaconFoundDailogObj)) {
                        arrayList_strId.add(beaconFoundDailogObj);
                        methoLoad();
                    }
                }

            }
        }
    };


    private void methoLoad()
    {
        if (arrayList_strId.size() > 0) {
            try {
                recycle_beaconView.setVisibility(View.VISIBLE);
                relativeLayout_forceLogin.setVisibility(View.GONE);
                adapter_beaconFoundDailog = new Adapter_BeaconFoundDailog(arrayList_strId, getActivity());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recycle_beaconView.setVisibility(View.VISIBLE);
                recycle_beaconView.setLayoutManager(mLayoutManager);
                recycle_beaconView.setItemAnimator(new DefaultItemAnimator());
                recycle_beaconView.setAdapter(adapter_beaconFoundDailog);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            try {
                recycle_beaconView.setVisibility(View.GONE);
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                txt_nologin.setText(getResources().getString(R.string.txt_noBeaconFound));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private boolean checkexisting(BeaconFoundDailog beaconFoundDailogObj) {
        boolean isFound = false;
        if (beaconFoundDailogObj != null) {
            for (int i = 0; i < arrayList_strId.size(); i++) {

                if (arrayList_strId.get(i).getUuid().equals(beaconFoundDailogObj.getUuid())
                        && arrayList_strId.get(i).getMajor().equalsIgnoreCase(beaconFoundDailogObj.getMajor())
                        && arrayList_strId.get(i).getMinor().equalsIgnoreCase(beaconFoundDailogObj.getMinor())) {
                    isFound = true;
                    break;
                }

            }
        }
        return isFound;
    }


    private int removeObj(BeaconFoundDailog beaconFoundDailogObj) {
        int pos = 0;

        for (int i = 0; i < arrayList_strId.size(); i++) {

            if (arrayList_strId.get(i).getUuid().equals(beaconFoundDailogObj.getUuid())
                    && arrayList_strId.get(i).getMajor().equalsIgnoreCase(beaconFoundDailogObj.getMajor())
                    && arrayList_strId.get(i).getMinor().equalsIgnoreCase(beaconFoundDailogObj.getMinor())) {
                pos = i;
                break;
            }

        }
        return pos;
    }


    private void sendUuid(String uuid, String major, String minor, String nameSpaceId, String InstanceId) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.sendBeaconId, Param.sendBeaconID(sessionManager.getUserId(), uuid, sessionManager.getEventId(), major, minor, nameSpaceId, InstanceId), 0, true, this);
        } else {
            ToastC.show(getApplicationContext(), getString(R.string.noInernet));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (btAdapter != null) {
            btAdapter.stopLeScan(leScanCallback);

        }
        scanHandler.removeCallbacks(scanRunnable);
    }


    //    @Override
//    public void onResume() {
//        super.onResume();
//        beaconManager.startRanging(region);
//        beaconManager.startMonitoring(region);
//        beaconManager.startEddystoneScanning();
//    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL UUID", jsonObject.toString());
//                        sendNotification();

                        ToastC.show(getActivity(), jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL NOTIFICATION", jsonObject.toString());
                        //sendNotification();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    protected static int calculateProximity(double accuracy) {
        if (accuracy < 0) {
            return 0; // Unknown

            // accuracy is -1;
        } else if (accuracy < 0.5)
        {
            return 1;  // IMMEDIATE
        }
        // forums say 3.0 is the near/far threshold, but it looks to be based on experience that this is 4.0
        else if (accuracy <= 10.0)
        {
            return 2;  // NEAR
        }

        return 3;  // FAR

        // if it is > 4.0 meters, call it far
        // FAR

    }

    double getDistance(int rssi, int txPower) {
        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }


    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}

