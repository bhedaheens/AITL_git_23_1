package com.allintheloop.Fragment.ExhibitorFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allintheloop.R;
import com.allintheloop.Util.ToastC;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorScanLeadNew extends Fragment implements ZBarScannerView.ResultHandler {


    ZBarScannerView scannerView;
    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList();

    public ExhibitorScanLeadNew() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor_scan_lead_new, container, false);
        scannerView = (ZBarScannerView) rootView.findViewById(R.id.scannerView);
        ALL_FORMATS.add(BarcodeFormat.I25);
        ALL_FORMATS.add(BarcodeFormat.CODE39);
        ALL_FORMATS.add(BarcodeFormat.CODE128);
        ALL_FORMATS.add(BarcodeFormat.QRCODE);
        ALL_FORMATS.add(BarcodeFormat.CODE93);
        ALL_FORMATS.add(BarcodeFormat.CODABAR);
        ALL_FORMATS.add(BarcodeFormat.ISBN13);
        scannerView.setFormats(ALL_FORMATS);
        return rootView;
    }

    @Override
    public void handleResult(Result result) {
        scannerView.resumeCameraPreview(this);
        Log.d("AITL ScanLoad", result.toString());
        ToastC.show(getActivity(), "" + result.getContents());
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.startCamera();
        scannerView.setResultHandler(this);
        scannerView.setFocusableInTouchMode(true);
        scannerView.requestFocus();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }
}
