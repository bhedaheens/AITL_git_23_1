package com.allintheloop.Util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.allintheloop.Bean.UidCommonKeyClass;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;

import org.json.JSONObject;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import io.fabric.sdk.android.Fabric;


public class AppController extends MultiDexApplication implements VolleyInterface {

    public static final String TAG = AppController.class.getSimpleName();
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static InetAddress serverAddr;
    public static Socket socket;
    private static AppController mInstance;
    SessionManager sessionManager;
    ArrayList<BeaconFoundDailog> arrayList_strId;
    BluetoothAdapter btAdapter;
    String scanID = "";
    Context context;
    TypeFactory mfontfacotry;
    UidCommonKeyClass uidCommonKeyClass;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Handler scanHandler;
    private int scan_interval_ms = 1000;
    private boolean isScanning = false;
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {


        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            if (GlobalData.isNetworkAvailable(mInstance)) {
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

//                Log.d("AITL ","SIZE :- "+arrayList_strId.size());
                    int pro = calculateProximity(getDistance(rssi, scanRecord[29]));
                    if (pro == 3 || pro == 0) {
                        if (checkexisting(beaconFoundDailogObj)) {
                            arrayList_strId.remove(removeObj(beaconFoundDailogObj));
                            Log.d("AITL REMOVE APP ", "SIZE :-" + arrayList_strId.size());

                        }
                    } else {
                        if (arrayList_strId.size() == 0) {
                            arrayList_strId.add(beaconFoundDailogObj);
                            sendNotification(uuid, String.valueOf(major), String.valueOf(minor), "", "");

                        }
                        if (!checkexisting(beaconFoundDailogObj)) {
                            arrayList_strId.add(beaconFoundDailogObj);
                            sendNotification(uuid, String.valueOf(major), String.valueOf(minor), "", "");
                        }
                    }

                }
            }
        }
    };
    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {

            if (GlobalData.isNetworkAvailable(mInstance)) {

                try {
                    if (isScanning) {
                        if (btAdapter != null && leScanCallback != null &&
                                btAdapter.getState() == BluetoothAdapter.STATE_ON) {
                            btAdapter.stopLeScan(leScanCallback);
                            scanHandler.removeCallbacks(scanRunnable);
                        }
                    } else {
                        if (btAdapter != null) {
                            btAdapter.startLeScan(leScanCallback);
                        }
                    }
                    isScanning = !isScanning;
                    scanHandler.postDelayed(this, scan_interval_ms);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    protected static int calculateProximity(double accuracy) {
        if (accuracy < 0) {
            return 0; // Unknown
            // accuracy is -1;
        } else if (accuracy < 0.5) {
            return 1;  // IMMEDIATE
        }
        // forums say 3.0 is the near/far threshold, but it looks to be based on experience that this is 4.0
        else if (accuracy <= 10.0) {
            return 2;  // NEAR
        }

        return 3;  // FAR

        // if it is > 4.0 meters, call it far
        // FAR

    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public Typeface getTypeFace(int type) {
        if (mfontfacotry == null) {
            mfontfacotry = new TypeFactory(this);
        }
        switch (type) {
            case Constans.bold:
                return mfontfacotry.getBold();
            case Constans.light:
                return mfontfacotry.getLight();
            case Constans.regular:
                return mfontfacotry.getRegular();
            default:
                return mfontfacotry.getLight();
        }
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Stetho.initializeWithDefaults(this);
        FacebookSdk.sdkInitialize(getApplicationContext());

        FontsOverride.setDefaultFont(this, "DEFAULT", "Lato-Light.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "Lato-Light.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "Lato-Light.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "Lato-Light.ttf");

        sessionManager = new SessionManager(this);
        arrayList_strId = new ArrayList<>();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
//        Mapbox.getInstance(getApplicationContext(), "pk.eyJ1IjoiYmhhdmRlZXAiLCJhIjoiY2o0ZHQ4ZGJ5MG9paDMybnRkN2l0cG00cCJ9.9Q6K5o3tPr8rxrQRttKP_g");

        mInstance = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public void startBeacon() {
        if (sessionManager.isLogin()) {
            if (GlobalData.checkForUIDVersion()) {
                uidCommonKeyClass = sessionManager.getUidCommonKey();
                if (uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                    arrayList_strId.clear();
                    if (btAdapter != null && !(btAdapter.isEnabled())) {
                        btAdapter.enable();
                    }
                    scanHandler = new Handler();
                    scanHandler.post(scanRunnable);
                }
            } else {
                if (sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied
                    arrayList_strId.clear();
                    if (btAdapter != null && !(btAdapter.isEnabled())) {
                        btAdapter.enable();
                    }
                    scanHandler = new Handler();
                    scanHandler.post(scanRunnable);
                }
            }

        }
    }

    double getDistance(int rssi, int txPower) {
        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }

    public void socketConnected() {
        try {
            serverAddr = InetAddress.getByName(sessionManager.getHostName());
            socket = new Socket(serverAddr, 6789);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void socketDisconnected() {
        try {
            if (socket.isConnected()) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBeacon() {
        if (sessionManager.isLogin()) {
            if (GlobalData.checkForUIDVersion()) {
                uidCommonKeyClass = sessionManager.getUidCommonKey();
                if (uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                    try {

                        if (btAdapter != null && btAdapter.isEnabled()) {
                            btAdapter.stopLeScan(leScanCallback);
                            scanHandler.removeCallbacks(scanRunnable);
                            btAdapter.disable();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (sessionManager.getRolId().equalsIgnoreCase("4")) {//changes applied
                    try {

                        if (btAdapter != null && btAdapter.isEnabled()) {
                            btAdapter.stopLeScan(leScanCallback);
                            scanHandler.removeCallbacks(scanRunnable);
                            btAdapter.disable();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void sendNotification(String uuid, String major, String minor, String instantid, String nameSpace) {

        if (GlobalData.isNetworkAvailable(this)) {
            new VolleyRequest(VolleyRequest.Method.POST, MyUrls.sendNotification, Param.sendNotification(sessionManager.getUserId(), sessionManager.getEventId(), uuid, major, minor, nameSpace, instantid), 0, false, this);
        } else {
//            ToastC.show(getApplicationContext(), getString(R.string.noInernet));
        }

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
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

    public interface Constans {
        int bold = 1, light = 2, regular = 3;
    }


}