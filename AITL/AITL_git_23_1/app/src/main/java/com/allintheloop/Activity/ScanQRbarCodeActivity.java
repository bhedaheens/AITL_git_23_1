package com.allintheloop.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class ScanQRbarCodeActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {


    ZBarScannerView scannerView;
    SessionManager sessionManager;

    List<String> permissionsNeeded;
    List<String> permissionsList;

    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrbar_code);


        scannerView = (ZBarScannerView) findViewById(R.id.scannerView);
        sessionManager = new SessionManager(ScanQRbarCodeActivity.this);
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
    }


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        scannnerStart();
    }

    private void scannnerStart() {
        scannerView.startCamera();
        scannerView.setResultHandler(this);
        scannerView.setFocusableInTouchMode(true);
        scannerView.requestFocus();
    }


    public boolean camerAaddPermission(List<String> permissionsList, String permission) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
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
    public void handleResult(Result result) {
        scannerView.resumeCameraPreview(this);
        Log.d("AITL", "" + result.getContents());

        Intent intent = new Intent();
        intent.putExtra("result", result.getContents());
        setResult(RESULT_OK, intent);
        finish();

//        enterCode(result.getContents());
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}
