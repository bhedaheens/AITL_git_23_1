package com.allintheloop.Fragment;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.Bean.UidCommonKeyClass;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class QrCodeScanner_ShareContact_Fragment extends Fragment implements ZBarScannerView.ResultHandler, VolleyInterface {


    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList();
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ZBarScannerView scannerView;
    SessionManager sessionManager;
    UidCommonKeyClass uidCommonKeyClass;
    List<String> permissionsNeeded;
    List<String> permissionsList;


    public QrCodeScanner_ShareContact_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_qr_code_scanner__share_contact, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        scannerView = (ZBarScannerView) rootView.findViewById(R.id.scannerView);
        sessionManager = new SessionManager(getActivity());
        if (GlobalData.checkForUIDVersion())
            uidCommonKeyClass = sessionManager.getUidCommonKey();
        ALL_FORMATS.add(BarcodeFormat.I25);
        ALL_FORMATS.add(BarcodeFormat.CODE39);
        ALL_FORMATS.add(BarcodeFormat.CODE128);
        ALL_FORMATS.add(BarcodeFormat.QRCODE);
        ALL_FORMATS.add(BarcodeFormat.CODE93);
        ALL_FORMATS.add(BarcodeFormat.CODABAR);
        ALL_FORMATS.add(BarcodeFormat.ISBN13);
        scannerView.setFormats(ALL_FORMATS);


        if (Build.VERSION.SDK_INT >= 23) {
            if (isCameraPermissionGranted()) {

            } else {
                requestPermission();
            }
        } else {

        }


        return rootView;
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean camerAaddPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (((MainActivity) getActivity()).checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    //  get Result of  QR Scan Result
    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        scannerView.resumeCameraPreview(this);
        Log.d("AITL", "" + result.getContents());
        enterCode(result.getContents());
    }


    @Override
    public void onResume() {
        super.onResume();
        scannnerStart();
    }


    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    private void scannnerStart() {
        scannerView.startCamera();
        scannerView.setResultHandler(this);
        scannerView.setFocusableInTouchMode(true);
        scannerView.requestFocus();
    }

    private void enterCode(String scanId) {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.scanQrForSurveyandAttendeeUid,
                        Param.scanBadgeForQRcodeUid(sessionManager.getEventId(), sessionManager.getUserId(),
                                scanId, uidCommonKeyClass.getIsOnlyAttendeeUser()), 0, true, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.scanQrForSurveyandAttendee,
                        Param.scanBadgeForQRcode(sessionManager.getEventId(), sessionManager.getUserId(),
                                scanId, sessionManager.getRolId()), 0, true, this);  //changes applied

            }
            scannerView.stopCameraPreview();
        }
    }

    public void requestPermission() {
        permissionsNeeded = new ArrayList<String>();
        permissionsNeeded.clear();
        permissionsList = new ArrayList<String>();
        permissionsList.clear();

        if (!camerAaddPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");

        if (permissionsList.size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

//                    GlobalData.cameraPermissionbroadCast(MainActivity.this);
                    Log.d("Bhavdip", "MainActivty PermissionGranted");

                } else {
                    // Permission Denied
                    requestPermission();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        if (jsonObject.getString("code_type").equalsIgnoreCase("1")) {
                            if (GlobalData.checkForUIDVersion()) {
                                if (uidCommonKeyClass.getIsOnlyAttendeeUser().equalsIgnoreCase("1")) {
                                    ToastC.show(getActivity(), jsonObject.getString("message").toString());
                                    scannnerStart();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                                    ((MainActivity) getActivity()).loadFragment();
                                } else {
                                    displayAlertDailog(jsonObject);
                                }
                            } else {
                                if (sessionManager.getRolId().equalsIgnoreCase("4")) {  //changes applied
                                    ToastC.show(getActivity(), jsonObject.getString("message").toString());
                                    scannnerStart();
                                    GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                    GlobalData.CURRENT_FRAG = GlobalData.attndeeShareContactFragmentTag;
                                    ((MainActivity) getActivity()).loadFragment();
                                } else {
                                    displayAlertDailog(jsonObject);
                                }
                            }
                        } else if (jsonObject.getString("code_type").equalsIgnoreCase("0")) {
                            if (jsonObject.getString("is_survey_avilable").equalsIgnoreCase("1")) {
                                scannnerStart();
                                sessionManager.setCategoryId(jsonObject.getString("survey_id"));
                                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                                GlobalData.CURRENT_FRAG = GlobalData.SurveyCategoryWiseFragment;
                                ((MainActivity) getActivity()).loadFragment();
                            } else {
                                displayAlertDailog(jsonObject);
                            }
                        }
                    } else {
                        displayAlertDailog(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private void displayAlertDailog(JSONObject jsonObject) {
        try {
            new AlertDialog.Builder(getActivity())
                    .setMessage(jsonObject.getString("message").toString())
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    scannnerStart();
                                }
                            }
                    ).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
