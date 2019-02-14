package com.allintheloop.Fragment.ExhibitorFragment;


import android.Manifest;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.ExhibitorLead_MyLeadAdapterOffline;
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

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.allintheloop.Util.GlobalData.exhibitorMyLeadLoad;

/**
 * A simple {@link Fragment} subclass.
 */
public class Exhibitor_MyLead_Fragment extends Fragment implements VolleyInterface {

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    ProgressBar progressBar1;
    SessionManager sessionManager;
    Button btn_export;
    EditText edt_search;
    RecyclerView rv_leadListing;
    ArrayList<ExhibitorLead_MyLeadData_Offline> dataOfflines = new ArrayList<>();
    ExhibitorLead_MyLeadAdapterOffline adapterOffline;
    TextView txt_noDataFound;
    GradientDrawable drawable;
    String url = "";
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
    String scanId = "0", scan_data = "";
    String str_retriveEmail;
    String badgeNumber = "";
    SQLiteDatabaseHandler databaseHandler;
    TextView txt_close;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    private String isFirstTimeScan = "";

    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            try {
                ToastC.show(getActivity(), "Download Completed");
                getActivity().unregisterReceiver(onComplete);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    BroadcastReceiver onNotificationClick = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            try {
                getActivity().unregisterReceiver(onNotificationClick);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    private BroadcastReceiver exhibitorListingData = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getLeadData();
        }
    };

    public Exhibitor_MyLead_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor__my_lead_, container, false);
        btn_export = (Button) rootView.findViewById(R.id.btn_export);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        txt_noDataFound = (TextView) rootView.findViewById(R.id.txt_noDataFound);
        rv_leadListing = (RecyclerView) rootView.findViewById(R.id.rv_leadListing);
        sessionManager = new SessionManager(getActivity());
        databaseHandler = new SQLiteDatabaseHandler(getActivity());
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    try {
                        if (dataOfflines.size() > 0 && adapterOffline.getFilter() != null) {
                            if (edt_search.getText().length() > 0) {
                                adapterOffline.getFilter().filter(edt_search.getText().toString());
                                sessionManager.keyboradHidden(edt_search);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                return false;
            }

        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {
                    try {
                        if (adapterOffline.getFilter() != null) {
                            adapterOffline.getFilter().filter(edt_search.getText().toString());
                            sessionManager.keyboradHidden(edt_search);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(13.0f);
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {

            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
            btn_export.setBackgroundDrawable(drawable);
            btn_export.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        } else {
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
            btn_export.setBackgroundDrawable(drawable);
            btn_export.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }

        getLeadData();

        btn_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.exhibitor_lead_export_popup,
                        popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.email:

                                // Or Some other code you want to put
                                // here.. This is just an example.
                                emailData("1");

                                break;
                            case R.id.download:


                                if (Build.VERSION.SDK_INT >= 23) {
                                    if (isCameraPermissionGranted()) {
                                        emailData("0");
                                    } else {
                                        requestPermission();
                                    }
                                } else {
                                    emailData("0");
                                }
                                break;

                            default:
                                break;
                        }

                        return true;
                    }
                });
            }
        });
        return rootView;
    }

    public void openUserDailog(final JSONObject jsonData, final ExhibitorLead_MyLeadData_Offline myLeadDataOffline) {
        try {
            userInfodialog = new Dialog(getActivity());
            userInfodialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            userInfodialog.setContentView(R.layout.exhibitor_lead_userinfo_dialog);
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
            btn_save.setVisibility(View.GONE);
            sessionManager.keyboradHidden(edt_firstname);
            linear_dynamicData = (LinearLayout) userInfodialog.findViewById(R.id.linear_dynamicData);
            btn_next.setText("Update");
            if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
                drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));
                btn_next.setBackgroundDrawable(drawable);
                btn_next.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            } else {
                drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));
                btn_next.setBackgroundDrawable(drawable);
                btn_next.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            }

            final JSONObject jsonUserInfo = jsonData.getJSONObject("leads");
            JSONArray jsonCustomeColumn = jsonData.getJSONArray("custom_column");
            retrialId = jsonUserInfo.getString("lead_user_id");
            edt_firstname.setText(jsonUserInfo.getString("firstname"));
            edt_surName.setText(jsonUserInfo.getString("lastname"));
            edt_mobile.setText(jsonUserInfo.getString("mobile"));
            edt_salutation.setText(jsonUserInfo.getString("salutation"));
            edt_country.setText(jsonUserInfo.getString("country"));
            scan_data = jsonUserInfo.getString("email");
            edt_email.setText(jsonUserInfo.getString("email"));
            edt_title.setText(jsonUserInfo.getString("title"));
            edt_company.setText(jsonUserInfo.getString("company_name"));
            extraColumnDataArrayList = new ArrayList<>();
            JSONArray jsonArrayExtraColumn = jsonUserInfo.getJSONArray("custom_column_data");
            for (int j = 0; j < jsonArrayExtraColumn.length(); j++) {
                JSONObject index = jsonArrayExtraColumn.getJSONObject(j);
                extraColumnDataArrayList.add(new ExhibitorLeadExtraColumnData(index.getString("Que"), index.getString("Ans")));
            }

            SinglelineArray = new ArrayList<>();
            StrSinglelineArray = new ArrayList<>();

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


            txt_close.setOnClickListener(view -> {
                userInfodialog.dismiss();
            });

            btn_next.setOnClickListener(view ->
            {

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

                try {
                    if (jsonUserInfo.getString("firstname").isEmpty()
                            && jsonUserInfo.getString("lastname").isEmpty()
                            && jsonUserInfo.getString("mobile").isEmpty()
                            && jsonUserInfo.getString("salutation").isEmpty()
                            && jsonUserInfo.getString("country").isEmpty()
                            && jsonUserInfo.getString("email").isEmpty()
                            && jsonUserInfo.getString("title").isEmpty()
                            && jsonUserInfo.getString("company_name").isEmpty()
                            && !jsonUserInfo.getString("lead_user_id").isEmpty()) {
                        isFirstTimeScan = "1";
                    } else {
                        isFirstTimeScan = "0";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArrayQueAns = new JSONArray();
                try {

                    for (int f = 0; f < SinglelineArray.size(); f++) {

                        JSONObject quesionAns = new JSONObject();
                        jsonObject.put(StrSinglelineArray.get(f).toString(), SinglelineArray.get(f).getText().toString());
                        quesionAns.put("Que", StrSinglelineArray.get(f).toString());
                        quesionAns.put("Ans", SinglelineArray.get(f).getText().toString());
                        jsonArrayQueAns.put(f, quesionAns);
//                            jsonArrayQueAns.put(f,quesionAns);
                    }

                    str_jsonObj = jsonObject.toString();
                    Log.d("Bhavdip JsonQues", jsonArrayQueAns.toString());
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

                } catch (Exception e) {
                    e.printStackTrace();
                }

                myLeadDataOffline.setFirstname(edt_firstname.getText().toString());
                myLeadDataOffline.setLastname(edt_surName.getText().toString());
                myLeadDataOffline.setTitle(edt_title.getText().toString());
                myLeadDataOffline.setCompanyName(edt_company.getText().toString());
                myLeadDataOffline.setEmail(edt_email.getText().toString());
                myLeadDataOffline.setBadgeNumber(edt_email.getText().toString());
//                    prepareOffline(leadDataOffline);
                submitData(prepareOffline(myLeadDataOffline, jsonArrayQueAns));

//                    submitData(myLeadDataOffline);

                try {
                    JSONArray jsonArray = jsonUserInfo.getJSONArray("survey");
                    if (jsonArray.length() != 0) {
                        openSurveyDialog(jsonUserInfo);
                    } else {
                        sessionManager.keyboradHidden(edt_firstname);
                        userInfodialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if (!userInfodialog.isShowing()) {
                userInfodialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openSurveyDialog(JSONObject jsonArray) {
        if (userInfodialog.isShowing())
            userInfodialog.dismiss();
        Bundle bundle = new Bundle();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ExhibitorLead_SurveyDailogFramgment fragment = new ExhibitorLead_SurveyDailogFramgment();
        bundle.putString("jsonObj", jsonArray.toString());
        bundle.putString("retrialId", retrialId);
        bundle.putString("scan_data", scan_data);
        bundle.putString("isoffline", "1");
        bundle.putString("isscanLead", "0");
        fragment.setArguments(bundle);
        fragment.show(fm, "DialogFragment");
    }

    private void emailData(String isEmail) {

        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.exportedLead, Param.exportedLead(sessionManager.getEventId(), sessionManager.getUserId(), isEmail), 2, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void getLeadData() {
        dataOfflines = new ArrayList<>();
        progressBar1.setVisibility(View.GONE);
        dataOfflines = databaseHandler.getMyLeadMyExiLeadData(sessionManager.getEventId(), sessionManager.getUserId());

        if (dataOfflines.size() > 0) {
            rv_leadListing.setVisibility(View.VISIBLE);
            txt_noDataFound.setVisibility(View.GONE);
            adapterOffline = new ExhibitorLead_MyLeadAdapterOffline(dataOfflines, getActivity(), Exhibitor_MyLead_Fragment.this);
            rv_leadListing.setItemAnimator(new DefaultItemAnimator());
            rv_leadListing.setLayoutManager(new LinearLayoutManager(getContext()));
            rv_leadListing.setAdapter(adapterOffline);
        } else {
            txt_noDataFound.setVisibility(View.VISIBLE);
            rv_leadListing.setVisibility(View.GONE);
        }
        GlobalData.exhibitorMyLeadCountUpdate(getActivity(), dataOfflines.size());
    }

    private void submitData(ExhibitorLead_MyLeadData_Offline myLeadDataOffline) {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.MyleadUpdateData, Param.saveScanLeadData(sessionManager.getEventId(), sessionManager.getUserId(), retrialId, str_firstname, str_lastName, str_email, str_cmpnyName, str_title, str_jsonObj, str_mobile, str_country, str_salutation, "", isFirstTimeScan), 6, false, this);
        } else {

            if (databaseHandler.isExhiSurveyUploadDataExist(sessionManager.getEventId(), sessionManager.getUserId(), scan_data)) {
                databaseHandler.deleteExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scan_data);
                databaseHandler.insertExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scanId, scan_data, jsonObjectProfile.toString(), "", getDateTime());
            } else {
                databaseHandler.insertExhiSurveyUploadData(sessionManager.getEventId(), sessionManager.getUserId(), scanId, scan_data, jsonObjectProfile.toString(), "", getDateTime());
            }


            databaseHandler.updateMyExiLeadData(myLeadDataOffline, sessionManager.getEventId(), sessionManager.getUserId());
            GlobalData.exhibitorMyLeadTab(getActivity());
        }
    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public ExhibitorLead_MyLeadData_Offline prepareOffline(final ExhibitorLead_MyLeadData_Offline leadDataOffline, JSONArray jsonArrayCustome) {
        try {

            JSONObject jsonData = new JSONObject(leadDataOffline.getData());

            final JSONObject jsonUserInfo = jsonData.getJSONObject("leads");
            JSONArray jsonCustomeColumn = jsonData.getJSONArray("custom_column");
            jsonUserInfo.put("firstname", edt_firstname.getText().toString());
            jsonUserInfo.put("lastname", edt_surName.getText().toString());
            jsonUserInfo.put("mobile", edt_mobile.getText().toString());
            jsonUserInfo.put("salutation", edt_salutation.getText().toString());
            jsonUserInfo.put("country", edt_country.getText().toString());
            jsonUserInfo.put("email", edt_email.getText().toString());
            jsonUserInfo.put("title", edt_title.getText().toString());
            jsonUserInfo.put("company_name", edt_company.getText().toString());
            jsonUserInfo.put("custom_column_data", jsonArrayCustome);

            jsonData.put("leads", jsonUserInfo);
            leadDataOffline.setData(jsonData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return leadDataOffline;
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {

            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        if (jsonData.has("url")) {
                            url = jsonData.getString("url");
                            ContextWrapper wrapper = new ContextWrapper(getContext());
                            String rootPath = "AllInTheLoop/" + sessionManager.getEventName() + "/" + "ExhibitorMyLeadDocument";
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), rootPath);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            getActivity().registerReceiver(onComplete,
                                    new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                            getActivity().registerReceiver(onNotificationClick,
                                    new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
                            downloadFile(url, rootPath, "LeadDocument.csv");

                        }
                        ToastC.show(getActivity(), jsonData.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.exhibitorMyLeadTab(getActivity());

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(exhibitorListingData, new IntentFilter(exhibitorMyLeadLoad));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(exhibitorListingData);
    }

    public void downloadFile(String uRl, String dir, String filename) {
        try {
            DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(uRl);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir(dir, filename);
            mgr.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((MainActivity) getActivity()).checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

        if (!camerAaddPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write External Storage");

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
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    emailData("0");
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

}