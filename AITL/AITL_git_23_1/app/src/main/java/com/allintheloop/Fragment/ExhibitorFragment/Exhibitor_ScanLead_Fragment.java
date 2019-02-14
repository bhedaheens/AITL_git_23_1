package com.allintheloop.Fragment.ExhibitorFragment;


import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLeadExtraColumnData;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLead_MyLeadData_Offline;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zbar.BarcodeFormat;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Exhibitor_ScanLead_Fragment extends Fragment implements ZBarScannerView.ResultHandler, VolleyInterface {

    public static final List<BarcodeFormat> ALL_FORMATS = new ArrayList();
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ZBarScannerView scannerView;
    EditText edt_enterCode;
    Button btn_submit;
    SessionManager sessionManager;
    GradientDrawable drawable;
    // UserInfo Dailog
    EditText edt_firstname, edt_surName, edt_email, edt_title, edt_company, edt_salutation, edt_mobile, edt_country;
    Button btn_next, btn_save;
    EditText editTextDynamic;
    TextView txtDynamic;
    LinearLayout linear_dynamicData;
    ArrayList<EditText> SinglelineArray;
    ArrayList<String> StrSinglelineArray;
    ArrayList<ExhibitorLeadExtraColumnData> extraColumnDataArrayList = new ArrayList<>();
    String str_firstname = "", str_lastName = "", str_email = "", str_cmpnyName = "", str_title = "", str_salutation = "", str_country = "", str_mobile = "";
    String retrialId = "";
    String str_jsonObj;
    Dialog userInfodialog;
    String offlineRetrivalId = "";
    JSONObject jsonObjectProfile;
    String scanId = "", scan_data = "";
    String email = "", isFirstTimeScan = "";
    SQLiteDatabaseHandler databaseHandler;
    TextView txt_close;
    List<String> permissionsNeeded;
    List<String> permissionsList;

    public Exhibitor_ScanLead_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_exhibitor__scan_lead, container, false);
        scannerView = (ZBarScannerView) rootView.findViewById(R.id.scannerView);

        ALL_FORMATS.add(BarcodeFormat.I25);
        ALL_FORMATS.add(BarcodeFormat.CODE39);
        ALL_FORMATS.add(BarcodeFormat.CODE128);
        ALL_FORMATS.add(BarcodeFormat.QRCODE);
        ALL_FORMATS.add(BarcodeFormat.CODE93);
        ALL_FORMATS.add(BarcodeFormat.CODABAR);
        ALL_FORMATS.add(BarcodeFormat.ISBN13);
        scannerView.setFormats(ALL_FORMATS);
        jsonObjectProfile = new JSONObject();
        databaseHandler = new SQLiteDatabaseHandler(getActivity());
        edt_enterCode = (EditText) rootView.findViewById(R.id.edt_enterCode);
        btn_submit = (Button) rootView.findViewById(R.id.btn_submit);
        sessionManager = new SessionManager(getActivity());
        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);


        if (Build.VERSION.SDK_INT >= 23) {
            if (isCameraPermissionGranted()) {

            } else {
                requestPermission();
            }
        } else {

        }


        SinglelineArray = new ArrayList<>();
        StrSinglelineArray = new ArrayList<>();
        userInfodialog = new Dialog(getActivity());
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {

            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_submit.setBackgroundDrawable(drawable);
            btn_submit.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        } else {
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_submit.setBackgroundDrawable(drawable);
            btn_submit.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }

        btn_submit.setOnClickListener(view -> {

            if (edt_enterCode.getText().length() != 0) {
                sessionManager.keyboradHidden(edt_enterCode);
                offlineRetrivalId = edt_enterCode.getText().toString().trim();
                if (GlobalData.isNetworkAvailable(getActivity())) {
                    enterCode(edt_enterCode.getText().toString(), "");
                } else {
                    scanId = "0";
                    scan_data = offlineRetrivalId;
                    str_email = scan_data;


                    showAlertDialoge();

//                    AlertDialog.Builder builder;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
//                    } else {
//                        builder = new AlertDialog.Builder(getActivity());
//                    }
//                    builder.setTitle(sessionManager.getEventName())
//                            .setCancelable(false)
//                            .setMessage("You have no connection, lead data will sync when you go online or enter manually.")
//                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                                GlobalData.hideSoftKeyboard(getActivity());
//                                offlineDataGenerate();
//                            })
//                            .show();
                }

            } else {
                ToastC.show(getActivity(), "Enter QR Code");
            }

        });


        return rootView;
    }

    @Override
    public void handleResult(me.dm7.barcodescanner.zbar.Result result) {
        scannerView.resumeCameraPreview(this);
        scanId = "1";
        scan_data = result.getContents();
        str_email = result.getContents();
        enterCode("", result.getContents());
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

    private void enterCode(String scanId, String scanData) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getDataFromScanLead, Param.getDataFromScanLead(sessionManager.getEventId(), sessionManager.getUserId(), scanId, scanData), 0, true, this);
            scannerView.stopCameraPreview();
        } else {
            scannerView.stopCameraPreview();
            showAlertDialoge();
        }
    }

    public void showAlertDialoge() {
        makeJsonObject(false);
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle(sessionManager.getEventName())
                .setCancelable(false)
                .setMessage("You have no connection, lead data will sync when you go online or enter manually.")
                .setPositiveButton(android.R.string.yes, (dialog, which) ->
                {
                    dialog.dismiss();
                    GlobalData.hideSoftKeyboard(getActivity());
                    offlineDataGenerate();
                })
                .show();
    }

    private void openUserDialog(JSONObject jsonData) {
        try {

            email = "";
            userInfodialog = new Dialog(getActivity());
            userInfodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            userInfodialog.setContentView(R.layout.exhibitor_lead_userinfo_dialog);
            userInfodialog.setCancelable(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = userInfodialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);


            edt_firstname = (EditText) userInfodialog.findViewById(R.id.edt_firstname);
            edt_country = (EditText) userInfodialog.findViewById(R.id.edt_country);
            edt_salutation = (EditText) userInfodialog.findViewById(R.id.edt_salutation);
            edt_mobile = (EditText) userInfodialog.findViewById(R.id.edt_mobile);
            edt_surName = (EditText) userInfodialog.findViewById(R.id.edt_surName);
            edt_email = (EditText) userInfodialog.findViewById(R.id.edt_email);
            edt_email.setEnabled(false);
            txt_close = (TextView) userInfodialog.findViewById(R.id.txt_close);
            edt_title = (EditText) userInfodialog.findViewById(R.id.edt_title);
            edt_company = (EditText) userInfodialog.findViewById(R.id.edt_company);
            btn_next = (Button) userInfodialog.findViewById(R.id.btn_next);
            btn_save = (Button) userInfodialog.findViewById(R.id.btn_save);
            linear_dynamicData = (LinearLayout) userInfodialog.findViewById(R.id.linear_dynamicData);

            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                btn_next.setBackgroundDrawable(drawable);
                btn_next.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

                btn_save.setBackgroundDrawable(drawable);
                btn_save.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            } else {
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                btn_next.setBackgroundDrawable(drawable);
                btn_next.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

                btn_save.setBackgroundDrawable(drawable);
                btn_save.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            }

            JSONObject jsonUserInfo = jsonData.getJSONObject("user_info");
            SinglelineArray = new ArrayList<>();
            StrSinglelineArray = new ArrayList<>();

            txt_close.setOnClickListener(view -> {
                edt_enterCode.setText("");
                edt_enterCode.setHint("Type the badge number or email");
                userInfodialog.dismiss();
                scannnerStart();
            });

            if (jsonUserInfo.length() != 0) {
                JSONArray jsonCustomeColumn = jsonData.getJSONArray("custom_column");
                retrialId = jsonUserInfo.getString("Id");
                edt_firstname.setText(jsonUserInfo.getString("Firstname"));
                edt_surName.setText(jsonUserInfo.getString("Lastname"));
                edt_email.setText(jsonUserInfo.getString("Email"));
                edt_title.setText(jsonUserInfo.getString("Title"));
                edt_company.setText(jsonUserInfo.getString("Company_name"));
                edt_salutation.setText(jsonUserInfo.getString("salutation"));
                edt_mobile.setText(jsonUserInfo.getString("mobile"));
                edt_country.setText(jsonUserInfo.getString("country"));


                if (jsonUserInfo.getString("Firstname").isEmpty()
                        && jsonUserInfo.getString("Lastname").isEmpty()
                        && jsonUserInfo.getString("Email").isEmpty()
                        && jsonUserInfo.getString("Title").isEmpty()
                        && jsonUserInfo.getString("Company_name").isEmpty()
                        && jsonUserInfo.getString("salutation").isEmpty()
                        && jsonUserInfo.getString("mobile").isEmpty()
                        && jsonUserInfo.getString("country").isEmpty()
                        && !jsonUserInfo.getString("Id").isEmpty()) {
                    isFirstTimeScan = "1";
                } else {
                    isFirstTimeScan = "0";
                }

                extraColumnDataArrayList = new ArrayList<>();
                JSONArray jsonArrayExtraColumn = jsonUserInfo.getJSONArray("extra_column");
                for (int j = 0; j < jsonArrayExtraColumn.length(); j++) {
                    JSONObject index = jsonArrayExtraColumn.getJSONObject(j);
                    extraColumnDataArrayList.add(new ExhibitorLeadExtraColumnData(index.getString("Que"), index.getString("Ans")));
                }

                for (int i = 0; i < jsonCustomeColumn.length(); i++) {
                    JSONObject index = jsonCustomeColumn.getJSONObject(i);
                    txtDynamic = new TextView(getActivity());
                    txtDynamic.setHint(index.getString("column_name"));
                    txtDynamic.setHintTextColor(getResources().getColor(R.color.black));
                    txtDynamic.setTextSize(13);
                    txtDynamic.setPadding(20, 25, 0, 25);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    txtDynamic.setLayoutParams(params);
                    linear_dynamicData.addView(txtDynamic);

                    editTextDynamic = new EditText(getActivity());

                    editTextDynamic.setHintTextColor(getResources().getColor(R.color.black));
                    editTextDynamic.setTextSize(13);
                    editTextDynamic.setPadding(20, 25, 0, 25);
                    editTextDynamic.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                    for (int j = 0; j < extraColumnDataArrayList.size(); j++) {
                        if (extraColumnDataArrayList.get(j).getQue().equalsIgnoreCase(index.getString("column_name"))) {
                            editTextDynamic.setText(extraColumnDataArrayList.get(j).getAns().toString());
                        }
                    }
                    editTextDynamic.setLayoutParams(params);
                    linear_dynamicData.addView(editTextDynamic);

                    SinglelineArray.add(editTextDynamic);
                    StrSinglelineArray.add(index.getString("column_name"));
                }
            }

            btn_next.setOnClickListener(view -> {
                str_firstname = edt_firstname.getText().toString();
                str_lastName = edt_surName.getText().toString();
                str_email = edt_email.getText().toString();
                str_cmpnyName = edt_company.getText().toString();
                str_title = edt_title.getText().toString();
                str_salutation = edt_salutation.getText().toString();
                str_mobile = edt_mobile.getText().toString();
                str_country = edt_country.getText().toString();

              /*  if (TextUtils.isEmpty(str_firstname)) {
                    edt_firstname.setError("Please enter first name");
                    return;
                }

                if (TextUtils.isEmpty(str_lastName)) {
                    edt_surName.setError("Please enter last name");
                    return;
                }

                if (TextUtils.isEmpty(str_title)) {
                    edt_title.setError("Please enter title");
                    return;
                }

                if (TextUtils.isEmpty(str_cmpnyName)) {
                    edt_company.setError("Please enter company name");
                    return;
                }

                if (TextUtils.isEmpty(str_email)) {
                    edt_email.setError("Please enter email");
                    return;
                }

                if (TextUtils.isEmpty(str_mobile)) {
                    edt_mobile.setError("Please enter mobile");
                    return;
                }

                if (TextUtils.isEmpty(str_country)) {
                    edt_country.setError("Please enter country");
                    return;
                }
*/
                try {
                    JSONObject jsonObject = new JSONObject();
                    for (int f = 0; f < SinglelineArray.size(); f++) {
                        jsonObject.put(StrSinglelineArray.get(f).toString(), SinglelineArray.get(f).getText().toString());
                    }

                    str_jsonObj = jsonObject.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                submitData();
            });

            btn_save.setOnClickListener(view -> {
                str_firstname = edt_firstname.getText().toString();
                str_lastName = edt_surName.getText().toString();
                str_email = edt_email.getText().toString();
                str_cmpnyName = edt_company.getText().toString();
                str_title = edt_title.getText().toString();
                str_salutation = edt_salutation.getText().toString();
                str_mobile = edt_mobile.getText().toString();
                str_country = edt_country.getText().toString();

               /* if (TextUtils.isEmpty(str_firstname)) {
                    edt_firstname.setError("Please enter first name");
                    return;
                }

                if (TextUtils.isEmpty(str_lastName)) {
                    edt_surName.setError("Please enter last name");
                    return;
                }

                if (TextUtils.isEmpty(str_title)) {
                    edt_title.setError("Please enter title");
                    return;
                }

                if (TextUtils.isEmpty(str_cmpnyName)) {
                    edt_company.setError("Please enter company name");
                    return;
                }

                if (TextUtils.isEmpty(str_email)) {
                    edt_email.setError("Please enter email");
                    return;
                }

                if (TextUtils.isEmpty(str_mobile)) {
                    edt_mobile.setError("Please enter mobile");
                    return;
                }

                if (TextUtils.isEmpty(str_country)) {
                    edt_country.setError("Please enter country");
                    return;
                }*/

                try {
                    JSONObject jsonObject = new JSONObject();
                    for (int f = 0; f < SinglelineArray.size(); f++) {
                        jsonObject.put(StrSinglelineArray.get(f).toString(), SinglelineArray.get(f).getText().toString());
                    }

                    str_jsonObj = jsonObject.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                saveDataWithoutQuestion();
            });

            if (userInfodialog.isShowing()) {
            } else {
                userInfodialog.show();
                scannerView.stopCameraPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void offlineDataGenerate() {
        try {
            userInfodialog = new Dialog(getActivity());
            userInfodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            userInfodialog.setContentView(R.layout.exhibitor_lead_userinfo_dialog);
            userInfodialog.setCancelable(false);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            Window window = userInfodialog.getWindow();
            lp.copyFrom(window.getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);

            edt_firstname = (EditText) userInfodialog.findViewById(R.id.edt_firstname);
            edt_country = (EditText) userInfodialog.findViewById(R.id.edt_country);
            edt_salutation = (EditText) userInfodialog.findViewById(R.id.edt_salutation);
            edt_mobile = (EditText) userInfodialog.findViewById(R.id.edt_mobile);
            edt_surName = (EditText) userInfodialog.findViewById(R.id.edt_surName);
            txt_close = (TextView) userInfodialog.findViewById(R.id.txt_close);
            edt_email = (EditText) userInfodialog.findViewById(R.id.edt_email);
            edt_email.setEnabled(false);
            edt_email.setText(scan_data);
            edt_title = (EditText) userInfodialog.findViewById(R.id.edt_title);
            edt_company = (EditText) userInfodialog.findViewById(R.id.edt_company);
            btn_next = (Button) userInfodialog.findViewById(R.id.btn_next);
            btn_save = (Button) userInfodialog.findViewById(R.id.btn_save);
            linear_dynamicData = (LinearLayout) userInfodialog.findViewById(R.id.linear_dynamicData);

            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                btn_next.setBackgroundDrawable(drawable);
                btn_next.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

                btn_save.setBackgroundDrawable(drawable);
                btn_save.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            } else {
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                btn_next.setBackgroundDrawable(drawable);
                btn_next.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

                btn_save.setBackgroundDrawable(drawable);
                btn_save.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            }

            if (!(sessionManager.getofflineCustomeColumnExhiLead().equalsIgnoreCase(""))) {
                SinglelineArray = new ArrayList<>();
                StrSinglelineArray = new ArrayList<>();
                JSONObject jsonData = new JSONObject(sessionManager.getofflineSurveyDataExhiLead());
                JSONArray jsonCustomeColumn = jsonData.getJSONArray("custom_column");
                for (int i = 0; i < jsonCustomeColumn.length(); i++) {
                    JSONObject index = jsonCustomeColumn.getJSONObject(i);
                    txtDynamic = new TextView(getActivity());
                    txtDynamic.setHint(index.getString("column_name"));
                    txtDynamic.setHintTextColor(getResources().getColor(R.color.black));
                    txtDynamic.setTextSize(13);
                    txtDynamic.setPadding(20, 25, 0, 25);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    txtDynamic.setLayoutParams(params);
                    linear_dynamicData.addView(txtDynamic);

                    editTextDynamic = new EditText(getActivity());

                    editTextDynamic.setHintTextColor(getResources().getColor(R.color.black));
                    editTextDynamic.setTextSize(13);
                    editTextDynamic.setPadding(20, 25, 0, 25);
                    editTextDynamic.setBackgroundResource(R.drawable.edittext_rounded_white_profile);
                    for (int j = 0; j < extraColumnDataArrayList.size(); j++) {
                        if (extraColumnDataArrayList.get(j).getQue().equalsIgnoreCase(index.getString("column_name"))) {
                            editTextDynamic.setText(extraColumnDataArrayList.get(j).getAns().toString());
                        }
                    }
                    editTextDynamic.setLayoutParams(params);
                    linear_dynamicData.addView(editTextDynamic);
                    SinglelineArray.add(editTextDynamic);
                    StrSinglelineArray.add(index.getString("column_name"));
                }
            }

            txt_close.setOnClickListener(view -> {
                //ToastC.show(getActivity(), "No Lead Saved");
                //makeJsonObject(false);
                edt_enterCode.setText("");
                edt_enterCode.setHint("Type the badge number or email");
                userInfodialog.dismiss();
                scannnerStart();
            });

            btn_next.setOnClickListener(view -> {
                jsonObjectProfile = new JSONObject();
                str_firstname = edt_firstname.getText().toString();
                str_lastName = edt_surName.getText().toString();
                str_email = edt_email.getText().toString();
                str_cmpnyName = edt_company.getText().toString();
                str_title = edt_title.getText().toString();

                str_salutation = edt_salutation.getText().toString();
                str_mobile = edt_mobile.getText().toString();
                str_country = edt_country.getText().toString();

               /* if (TextUtils.isEmpty(str_firstname)) {
                    edt_firstname.setError("Please enter first name");
                    return;
                }

                if (TextUtils.isEmpty(str_lastName)) {
                    edt_surName.setError("Please enter last name");
                    return;
                }

                if (TextUtils.isEmpty(str_title)) {
                    edt_title.setError("Please enter title");
                    return;
                }

                if (TextUtils.isEmpty(str_cmpnyName)) {
                    edt_company.setError("Please enter company name");
                    return;
                }

                if (TextUtils.isEmpty(str_email)) {
                    edt_email.setError("Please enter email");
                    return;
                }

                if (TextUtils.isEmpty(str_mobile)) {
                    edt_mobile.setError("Please enter mobile");
                    return;
                }

                if (TextUtils.isEmpty(str_country)) {
                    edt_country.setError("Please enter country");
                    return;
                }*/

                android.support.v7.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                }
                builder.setTitle(sessionManager.getEventName())
                        .setCancelable(false)
                        .setMessage("Full information on the lead will be available when you connect to the internet")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            GlobalData.hideSoftKeyboard(getActivity());
                            makeJsonObject(true);
                        })
                        .show();

            });

            btn_save.setOnClickListener(view -> {
                jsonObjectProfile = new JSONObject();
                str_firstname = edt_firstname.getText().toString();
                str_lastName = edt_surName.getText().toString();
                str_email = edt_email.getText().toString();
                str_cmpnyName = edt_company.getText().toString();
                str_title = edt_title.getText().toString();
                str_salutation = edt_salutation.getText().toString();
                str_mobile = edt_mobile.getText().toString();
                str_country = edt_country.getText().toString();

               /* if (TextUtils.isEmpty(str_firstname)) {
                    edt_firstname.setError("Please enter first name");
                    return;
                }

                if (TextUtils.isEmpty(str_lastName)) {
                    edt_surName.setError("Please enter last name");
                    return;
                }

                if (TextUtils.isEmpty(str_title)) {
                    edt_title.setError("Please enter title");
                    return;
                }

                if (TextUtils.isEmpty(str_cmpnyName)) {
                    edt_company.setError("Please enter company name");
                    return;
                }

                if (TextUtils.isEmpty(str_email)) {
                    edt_email.setError("Please enter email");
                    return;
                }

                if (TextUtils.isEmpty(str_mobile)) {
                    edt_mobile.setError("Please enter mobile");
                    return;
                }

                if (TextUtils.isEmpty(str_country)) {
                    edt_country.setError("Please enter country");
                    return;
                }*/

                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArrayQueAns = new JSONArray();
                try {

                    for (int f = 0; f < SinglelineArray.size(); f++) {
                        JSONObject quesionAns = new JSONObject();
                        jsonObject.put(StrSinglelineArray.get(f).toString(), SinglelineArray.get(f).getText().toString());
                        quesionAns.put("Que", StrSinglelineArray.get(f).toString());
                        quesionAns.put("Ans", SinglelineArray.get(f).getText().toString());
                        jsonArrayQueAns.put(f, quesionAns);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    jsonObjectProfile.put("firstname", str_firstname);
                    jsonObjectProfile.put("lastname", str_lastName);
                    jsonObjectProfile.put("email", str_email);
                    jsonObjectProfile.put("title", str_title);
                    jsonObjectProfile.put("company_name", str_cmpnyName);
                    jsonObjectProfile.put("salutation", str_salutation);
                    jsonObjectProfile.put("country", str_country);
                    jsonObjectProfile.put("mobile", str_mobile);
                    jsonObjectProfile.put("custom_column_data", jsonObject);

                    Log.d("Bhavdip bjectOffline", jsonObjectProfile.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                edt_enterCode.setText("");
                edt_enterCode.setHint("Type the badge number or email");
                userInfodialog.dismiss();
                scannnerStart();
                insertOfflineDatawithoutSurvey(jsonArrayQueAns);
            });

            if (userInfodialog.isShowing()) {

            } else {
                userInfodialog.show();
                scannerView.stopCameraPreview();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeJsonObject(boolean isDailogOpen) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArrayQueAns = new JSONArray();
        try {

            for (int f = 0; f < SinglelineArray.size(); f++) {
                JSONObject quesionAns = new JSONObject();
                jsonObject.put(StrSinglelineArray.get(f).toString(), SinglelineArray.get(f).getText().toString());
                quesionAns.put("Que", StrSinglelineArray.get(f).toString());
                quesionAns.put("Ans", SinglelineArray.get(f).getText().toString());
                jsonArrayQueAns.put(f, quesionAns);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (!isDailogOpen) {
                jsonObjectProfile.put("firstname", "");
                jsonObjectProfile.put("lastname", "");
                jsonObjectProfile.put("email", str_email);
                jsonObjectProfile.put("title", "");
                jsonObjectProfile.put("company_name", "");
                jsonObjectProfile.put("salutation", "");
                jsonObjectProfile.put("country", "");
                jsonObjectProfile.put("mobile", "");
                jsonObjectProfile.put("custom_column_data", jsonObject);
            } else {
                jsonObjectProfile.put("firstname", str_firstname);
                jsonObjectProfile.put("lastname", str_lastName);
                jsonObjectProfile.put("email", str_email);
                jsonObjectProfile.put("title", str_title);
                jsonObjectProfile.put("company_name", str_cmpnyName);
                jsonObjectProfile.put("salutation", str_salutation);
                jsonObjectProfile.put("country", str_country);
                jsonObjectProfile.put("mobile", str_mobile);
                jsonObjectProfile.put("custom_column_data", jsonObject);
            }

            Log.d("Bhavdip bjectOffline", jsonObjectProfile.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        edt_enterCode.setText("");
        edt_enterCode.setHint("Type the badge number or email");
        userInfodialog.dismiss();
        scannnerStart();
        inserofllineData(jsonArrayQueAns, isDailogOpen);
    }

    private void scannnerStart() {
        scannerView.startCamera();
        scannerView.setResultHandler(this);
        scannerView.setFocusableInTouchMode(true);
        scannerView.requestFocus();
    }

    private void inserofllineData(JSONArray jsonArray, boolean isDailogOpen) {
        JSONObject jsonObjectData = new JSONObject();
        if (databaseHandler.isExhiSurveyUploadDataExist(sessionManager.getEventId(), sessionManager.getUserId(), scan_data)) {
            databaseHandler.deleteExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scan_data);
            databaseHandler.insertExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scanId, scan_data, jsonObjectProfile.toString(), "", getDateTime());
        } else {
            databaseHandler.insertExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scanId, scan_data, jsonObjectProfile.toString(), "", getDateTime());
        }

        if (databaseHandler.isScanLeadDataExist(sessionManager.getEventId(), sessionManager.getUserId(), scan_data)) {
            databaseHandler.deleteScanLeadData(sessionManager.getEventId(), sessionManager.getUserId(), scan_data);
        }

        try {
            JSONObject jsonObjectLead = new JSONObject();

            JSONArray jsonArraycustome_column = new JSONArray();
            JSONArray jsonArraySurvey = new JSONArray();

            jsonObjectLead.put("lead_user_id", "");
            jsonObjectLead.put("firstname", str_firstname);
            jsonObjectLead.put("lastname", str_lastName);
            jsonObjectLead.put("email", str_email);
            jsonObjectLead.put("title", str_title);
            jsonObjectLead.put("company_name", str_cmpnyName);
            jsonObjectLead.put("salutation", str_salutation);
            jsonObjectLead.put("country", str_country);
            jsonObjectLead.put("mobile", str_mobile);
            jsonObjectLead.put("badgeNumber", scan_data);
            jsonObjectLead.put("Unique_no", scan_data);

            if (sessionManager.getofflineSurveyDataExhiLead().isEmpty()) {
                jsonObjectLead.put("survey", jsonArraySurvey);
            } else {
                JSONObject jsonObjectSurveyObj = new JSONObject(sessionManager.getofflineSurveyDataExhiLead());
                jsonArraySurvey = jsonObjectSurveyObj.getJSONArray("survey");
                jsonObjectLead.put("survey", jsonArraySurvey);
            }

            jsonObjectLead.put("custom_column_data", jsonArray);
            jsonObjectData.put("leads", jsonObjectLead);
            if (sessionManager.getofflineSurveyDataExhiLead().isEmpty()) {
                jsonObjectData.put("custom_column", jsonArraycustome_column);
            } else {
                JSONObject jsonObjectSurveyObj = new JSONObject(sessionManager.getofflineSurveyDataExhiLead());
                jsonArraycustome_column = jsonObjectSurveyObj.getJSONArray("custom_column");
                jsonObjectData.put("custom_column", jsonArraycustome_column);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ExhibitorLead_MyLeadData_Offline leadData_offline = new ExhibitorLead_MyLeadData_Offline();
        leadData_offline.setId("");
        leadData_offline.setRoleId(sessionManager.getRolId());
        leadData_offline.setOrganisorId(sessionManager.getOrganizer_id());
        leadData_offline.setFirstname(str_firstname);
        leadData_offline.setLastname(str_lastName);
        leadData_offline.setEmail(str_email);
        leadData_offline.setTitle(str_title);
        leadData_offline.setCompanyName(str_cmpnyName);
        leadData_offline.setData(jsonObjectData.toString());
        leadData_offline.setBadgeNumber(str_email);
        databaseHandler.insertMyExiLeadData(leadData_offline, sessionManager.getEventId(), sessionManager.getUserId());

        if (sessionManager.getofflineSurveyDataExhiLead().equalsIgnoreCase("")) {
//            ToastC.show(getActivity(), "No Survey Found");
        } else {
            Bundle bundle = new Bundle();
            if (isDailogOpen) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                ExhibitorLead_SurveyDailogFramgment fragment = new ExhibitorLead_SurveyDailogFramgment();
                bundle.putString("jsonObj", sessionManager.getofflineSurveyDataExhiLead());
                bundle.putString("retrialId", offlineRetrivalId);
                bundle.putString("profileObj", jsonObjectProfile.toString());
                bundle.putString("scan_data", scan_data);
                bundle.putString("scan_id", scanId);
                bundle.putString("isoffline", "1");
                bundle.putString("isscanLead", "1");
                fragment.setArguments(bundle);
                fragment.show(fm, "DialogFragment");
            }
        }

        GlobalData.exhibitorMyLeadTab(getActivity());
    }

    private void insertOfflineDatawithoutSurvey(JSONArray jsonArray) {
        JSONObject jsonObjectData = new JSONObject();
        if (databaseHandler.isExhiSurveyUploadDataExist(sessionManager.getEventId(), sessionManager.getUserId(), scan_data)) {
            databaseHandler.deleteExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scan_data);
            databaseHandler.insertExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scanId, scan_data, jsonObjectProfile.toString(), "", getDateTime());
        } else {
            databaseHandler.insertExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scanId, scan_data, jsonObjectProfile.toString(), "", getDateTime());
        }

        if (databaseHandler.isScanLeadDataExist(sessionManager.getEventId(), sessionManager.getUserId(), scan_data)) {
            databaseHandler.deleteScanLeadData(sessionManager.getEventId(), sessionManager.getUserId(), scan_data);
        }

        try {
            JSONObject jsonObjectLead = new JSONObject();

            JSONArray jsonArraycustome_column = new JSONArray();
            JSONArray jsonArraySurvey = new JSONArray();
            jsonObjectLead.put("lead_user_id", "");
            jsonObjectLead.put("firstname", str_firstname);
            jsonObjectLead.put("lastname", str_lastName);
            jsonObjectLead.put("email", str_email);
            jsonObjectLead.put("title", str_title);
            jsonObjectLead.put("company_name", str_cmpnyName);
            jsonObjectLead.put("salutation", str_salutation);
            jsonObjectLead.put("country", str_country);
            jsonObjectLead.put("mobile", str_mobile);
            jsonObjectLead.put("badgeNumber", scan_data);
            jsonObjectLead.put("Unique_no", scan_data);

//            if (sessionManager.getofflineSurveyDataExhiLead().isEmpty())
//            {
//                jsonObjectLead.put("survey", jsonArraySurvey);
//            } else {
//                JSONObject jsonObjectSurveyObj = new JSONObject(sessionManager.getofflineSurveyDataExhiLead());
//                jsonArraySurvey = jsonObjectSurveyObj.getJSONArray("survey");
//                jsonObjectLead.put("survey", jsonArraySurvey);
//            }
            jsonObjectLead.put("survey", jsonArraySurvey);
            jsonObjectLead.put("custom_column_data", jsonArray);
            jsonObjectData.put("leads", jsonObjectLead);
            if (sessionManager.getofflineSurveyDataExhiLead().isEmpty()) {
                jsonObjectData.put("custom_column", jsonArraycustome_column);
            } else {
                JSONObject jsonObjectSurveyObj = new JSONObject(sessionManager.getofflineSurveyDataExhiLead());
                jsonArraycustome_column = jsonObjectSurveyObj.getJSONArray("custom_column");
                jsonObjectData.put("custom_column", jsonArraycustome_column);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ExhibitorLead_MyLeadData_Offline leadData_offline = new ExhibitorLead_MyLeadData_Offline();
        leadData_offline.setId("");
        leadData_offline.setRoleId(sessionManager.getRolId());
        leadData_offline.setOrganisorId(sessionManager.getOrganizer_id());
        leadData_offline.setFirstname(str_firstname);
        leadData_offline.setLastname(str_lastName);
        leadData_offline.setEmail(str_email);
        leadData_offline.setTitle(str_title);
        leadData_offline.setCompanyName(str_cmpnyName);
        leadData_offline.setData(jsonObjectData.toString());
        leadData_offline.setBadgeNumber(str_email);
        databaseHandler.insertMyExiLeadData(leadData_offline, sessionManager.getEventId(), sessionManager.getUserId());

//        if (sessionManager.getofflineSurveyDataExhiLead().equalsIgnoreCase("")) {
////            ToastC.show(getActivity(), "No Survey Found");
//        } else {
//            Bundle bundle = new Bundle();
//            FragmentManager fm = getActivity().getSupportFragmentManager();
//            ExhibitorLead_SurveyDailogFramgment fragment = new ExhibitorLead_SurveyDailogFramgment();
//            bundle.putString("jsonObj", sessionManager.getofflineSurveyDataExhiLead());
//            bundle.putString("retrialId", offlineRetrivalId);
//            bundle.putString("profileObj", jsonObjectProfile.toString());
//            bundle.putString("scan_data", scan_data);
//            bundle.putString("scan_id", scanId);
//            bundle.putString("isoffline", "1");
//            bundle.putString("isscanLead", "1");
//            fragment.setArguments(bundle);
//            fragment.show(fm, "DialogFragment");
//        }

        GlobalData.exhibitorMyLeadTab(getActivity());
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        final JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONObject jsonuserInfo = jsonData.getJSONObject("user_info");
                        GlobalData.exhibitorMyLeadTab(getActivity());
                        if (jsonuserInfo.length() != 0) {
                            if (userInfodialog.isShowing()) {
                            } else {
                                try {
                                    if (jsonuserInfo.getString("Firstname").isEmpty()
                                            && jsonuserInfo.getString("Lastname").isEmpty()
                                            && jsonuserInfo.getString("Title").isEmpty()
                                            && jsonuserInfo.getString("Company_name").isEmpty()
                                            && jsonuserInfo.getString("mobile").isEmpty()
                                            && jsonuserInfo.getString("country").isEmpty()
                                            && jsonuserInfo.getString("Email").isEmpty()) {
                                        AlertDialog.Builder builder;
                                        builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle(sessionManager.getEventName())
                                                .setCancelable(false)
                                                .setMessage("No data currently availabe,please enter manually")
                                                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                                    GlobalData.exhibitorMyLeadTab(getActivity());
                                                    openUserDialog(jsonData);
                                                })
                                                .show();
                                    } else {
                                        GlobalData.exhibitorMyLeadTab(getActivity());
                                        openUserDialog(jsonData);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            scannnerStart();
                            ToastC.show(getActivity(), jsonData.getString("message"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        edt_enterCode.setText("");
                        edt_enterCode.setHint("Type the badge number or email");
                        userInfodialog.dismiss();
                        scannnerStart();
                        GlobalData.exhibitorMyLeadTab(getActivity());
                        JSONArray jsonArray = jsonObject.getJSONArray("survey");
                        if (jsonArray.length() != 0) {
                            Bundle bundle = new Bundle();
                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            ExhibitorLead_SurveyDailogFramgment fragment = new ExhibitorLead_SurveyDailogFramgment();
                            bundle.putString("jsonObj", jsonObject.toString());
                            bundle.putString("retrialId", retrialId);
                            bundle.putString("isoffline", "0");
                            bundle.putString("isscanLead", "1");
                            fragment.setArguments(bundle);
                            fragment.show(fm, "DialogFragment");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        edt_enterCode.setText("");
                        edt_enterCode.setHint("Type the badge number or email");
                        userInfodialog.dismiss();
                        scannnerStart();
                        GlobalData.exhibitorMyLeadTab(getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void submitData() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.saveScanLeadData, Param.saveScanLeadData(sessionManager.getEventId(), sessionManager.getUserId(), retrialId, str_firstname, str_lastName, str_email, str_cmpnyName, str_title, str_jsonObj, str_mobile, str_country, str_salutation, "", isFirstTimeScan), 1, true, this);
        }
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

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GlobalData.exhibitorMyLeadTabLoad(getActivity());
                        }
                    }, 2000);

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

    private void saveDataWithoutQuestion() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.saveScanLeadData, Param.saveScanLeadData(sessionManager.getEventId(), sessionManager.getUserId(), retrialId, str_firstname, str_lastName, str_email, str_cmpnyName, str_title, str_jsonObj, str_mobile, str_country, str_salutation, "", isFirstTimeScan), 2, true, this);
        }
    }
}