package com.allintheloop.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.allintheloop.Bean.AgendaData.AgendaData;
import com.allintheloop.Bean.CmsGroupData.CmsListandDetailList;
import com.allintheloop.Bean.EventList;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorOfflineData;
import com.allintheloop.Bean.GroupingData.GrouppingOfflineList;
import com.allintheloop.Bean.Map.MapListData;
import com.allintheloop.Bean.Speaker.SpeakerListMainClass;
import com.allintheloop.Bean.SponsorClass.SponsorMainListClasss;
import com.allintheloop.Fragment.VersionCodeUpdateDailog;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SQLiteDatabaseHandler;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Splash_Activity extends AppCompatActivity implements VolleyInterface {

    private static final int PERMISSION_REQUEST_CODE = 1;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    SessionManager sessionManager;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    String ImgStr, ImgUrl, id, eName, Fb_status, SecretKey, eventType, fundrising_enabled, linkedin_login_enabled, defultLang, show_login_screen = "";
    EventList EvntObj;
    String versionName = "";
    int versionCode = -1;
    String android_id;
    SQLiteDatabaseHandler databaseHandler;
    String show_once = "";
    String exhibitorUpdateDate = "", groupUpdateDate = "", MapUpdateDate = "", SponsorUpdateDate = "", AgendaUpdateDate = "", CmsUpdateDate = "", speakerUpdateDate = "";
    String exhibitorMenuId = "3", MapMenuid = "10", groupMenuid = "100", SponsorMenuId = "43", AgendaMenuId = "1", CmsMenuId = "21", speakerMenuId = "7";
    boolean isExhibitorData = false, isAgendaData = false, isSponsorData = false, isGroupData = false, isMapData = false, isCMsData = false, isFirtOnBoard = false, isSpeakerData = false;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.setAndroidId(android_id);
        /// this for call onupgrade method and logout user when app update from playstore
        databaseHandler = new SQLiteDatabaseHandler(Splash_Activity.this);
        databaseHandler.insertTmpValueData("1", "Name");
        updateVersionCode();
//        handlerMethod();
    }

    public boolean isPermissionGranted() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


//    @Override
//    protected void onNewIntent(Intent intent)
//    {
//        super.onNewIntent(intent);
//        String appLinkAction = intent.getAction();
//        Uri uri = intent.getData();
//        if (Intent.ACTION_VIEW.equals(appLinkAction) && uri != null)
//        {
//            if (sessionManager.isLogin())
//            {
//                GlobalData.deeplinkBroadCast(getApplicationContext(),intent);
//            }
//        }
//    }

    private void requestPermission() {
        permissionsNeeded = new ArrayList<String>();
        permissionsNeeded.clear();
        permissionsList = new ArrayList<String>();
        permissionsList.clear();

        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
//        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
//            permissionsNeeded.add("Write External Storage");

        Log.e("AAKASH", "PSERMISSION LIST ARRAY SIZE :: " + permissionsList.size());
        Log.e("AAKASH", "PSERMISSION NEEDED ARRAY SIZE :: " + permissionsNeeded.size());

        if (permissionsList.size() > 0) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            }

            return;
        }

    }

    private void updateVersionCode() {
        if (sessionManager.isLoginNoti) {
            sessionManager.logout();
            sessionManager.isLoginNoti = false;
            startActivity(new Intent(Splash_Activity.this, SearchApp_Activity.class));
            finish();
        } else {
            getVersionInfo();
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {

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

    private void handlerMethod() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (Build.VERSION.SDK_INT >= 23) {
                    if (isPermissionGranted()) {
                        nextStep();
                    } else {
                        requestPermission();
                    }
                } else {
                    nextStep();
                }

            }
        }, 2000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
//                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++) {
                    perms.put(permissions[i], grantResults[i]);
                    Log.i("niral", permissions[i] + " :" + perms.get(permissions[i]));
                }
                // Check for ACCESS_FINE_LOCATION
// perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                ) {
                    // All Permissions Granted
                    nextStep();

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

    private void nextStep() {
//        sessionManager.setIsFirstTimeOnBoard(true);
        if (sessionManager.isLogin()) {
            if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
                if (!isFinishing()) {
                    try {
                        mProgressDialog = new ProgressDialog(Splash_Activity.this);
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                checkUpdateData();
            } else {
                startActivity(new Intent(Splash_Activity.this, MainActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(Splash_Activity.this, SearchApp_Activity.class));
            finish();
        }
    }

    private void checkUpdateData() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.checkUpdateData,
                    Param.checkUpdate(sessionManager.getEventId()),
                    4, false, this);
        } else {
            gotoHome();
        }
    }

    private void getExhibitorsData() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            if (GlobalData.checkForUIDVersion()) {
                new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_ExhibitorDataUid,
                        Param.getExhibitorList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", 1, "", sessionManager.getExhibitorParentCategoryId(), sessionManager.getIsLastCategoryName()),
                        8, false, this);
            } else {
                new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_ExhibitorData,
                        Param.getExhibitorList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", 1, "", sessionManager.getExhibitorParentCategoryId(), sessionManager.getIsLastCategoryName()),
                        8, false, this);
            }
        }
    }

    private void getGroupModuleData() {
        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
                new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.getGroupModuleData,
                        Param.getGroupingData(sessionManager.getEventId()),
                        2, false, this);
            }
        }
    }

    private void getMapListData() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_MapList, Param.getMapList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 5, false, this);
        }
    }

    private void getSponsorListData() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SponsorList_offlineUid, Param.getSponsorOfflineList(sessionManager.getEventId()), 6, false, this);
            else
                new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SponsorList_offline, Param.getSponsorOfflineList(sessionManager.getEventId()), 6, false, this);
        }
    }

    private void getAgendaListData() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_AgendaList_offline, Param.getSponsorOfflineList(sessionManager.getEventId()), 7, false, this);
        }
    }

    private void getCmsListData() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.getCMSofflineData, Param.getMapList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 10, false, this);
        }
    }

    private void getSpeakerListData() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SpeakerList_offlineUid, Param.getSpeakerList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 12, false, this);
            else
                new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SpeakerList_offline, Param.getSpeakerList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 12, false, this);
        }
    }

    private void gotoHome() {
        if (!isFinishing()) {

            try {
                if (mProgressDialog != null) {
                    mProgressDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        gotoHomeAfterImage();
    }

    private void gotoHomeAfterImage() {
        startActivity(new Intent(Splash_Activity.this, MainActivity.class));
        finish();
    }


    private void getVersionInfo() {

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;

            Log.d("AITL VERSIONNAME", versionName);
            Log.d("AITL VERSION CODE", "" + versionCode);

            if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
                getVersionCode();
            } else {
                handlerMethod();
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {

            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

//                        loadGroupData(jsonObject);
                        new updateGroupDatabase(jsonObject).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
//                        storeAndCheckUpdateAPI(jsonObject);
                        new storeAndCheckUpdateAPIAsync(jsonObject).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip MapListFragment", jsonObject.toString());
//                        loadMapListData(jsonObject);

                        new updateMapDatabase(jsonObject).execute();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 6:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip SponsorList", jsonObject.toString());
//                        loadMapListData(jsonObject);

//                        loadSponsorData(jsonObject);

                        new updateSponsorDatabase(jsonObject).execute();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip AgendaList", jsonObject.toString());

//                        loadAgendaData(jsonObject);

                        new updateAgendaDatabase(jsonObject).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
//                        loadExhibitorData(jsonObject);

                        new updateExhibitorDatabase(jsonObject).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 10:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip AgendaList", jsonObject.toString());

//                        loadAgendaData(jsonObject);
                        new updateCmsListDatabase(jsonObject).execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 11:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase("true")) {

                        sessionManager.setVersionCodeId(jsonObject.optString("version_id"));

                        if (!jsonObject.getString("code").isEmpty()) {
                            if (versionName.equalsIgnoreCase(jsonObject.getString("code"))) {
                                handlerMethod();
                            } else {
                                FragmentManager fm = getSupportFragmentManager();
                                VersionCodeUpdateDailog fragment = new VersionCodeUpdateDailog();
                                fragment.show(fm, "DialogFragment");
                            }
                        } else {
                            handlerMethod();
                        }
//                        new updateCmsListDatabase(jsonObject).execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 12:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        Log.d("Bahvdip AgendaList", jsonObject.toString());

//                        loadAgendaData(jsonObject);
                        new updateSpeakerListDatabase(jsonObject).execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void storeAndCheckUpdateAPI(JSONObject jsonObject) {

        try {
            exhibitorUpdateDate = jsonObject.getString("exhibitor");
            groupUpdateDate = jsonObject.getString("group");
            MapUpdateDate = jsonObject.getString("map");
            SponsorUpdateDate = jsonObject.getString("sponsor");
            AgendaUpdateDate = jsonObject.getString("agenda");
            CmsUpdateDate = jsonObject.getString("cms");
            speakerUpdateDate = jsonObject.getString("speaker");

            Cursor exhibitorcursor = databaseHandler.getUpdateModuleData(sessionManager.getEventId(), exhibitorMenuId);
            Cursor groupcursor = databaseHandler.getUpdateModuleData(sessionManager.getEventId(), groupMenuid);
            Cursor mapcursor = databaseHandler.getUpdateModuleData(sessionManager.getEventId(), MapMenuid);
            Cursor Sponsorcursor = databaseHandler.getUpdateModuleData(sessionManager.getEventId(), SponsorMenuId);
            Cursor AgendaCursor = databaseHandler.getUpdateModuleData(sessionManager.getEventId(), AgendaMenuId);
            Cursor CMsCursor = databaseHandler.getUpdateModuleData(sessionManager.getEventId(), CmsMenuId);
            Cursor SpeakerCursor = databaseHandler.getUpdateModuleData(sessionManager.getEventId(), speakerMenuId);
            if (exhibitorcursor.getCount() > 0) {
                String previoisDate = "";
                if (exhibitorcursor.moveToFirst()) {
                    previoisDate = exhibitorcursor.getString(exhibitorcursor.getColumnIndex(databaseHandler.Update_DATE));
                }


                if (!(previoisDate.equalsIgnoreCase(exhibitorUpdateDate))) {
                    getExhibitorsData();
                } else {
                    isExhibitorData = true;
                }
            } else {
                getExhibitorsData();
            }

            if (SpeakerCursor.getCount() > 0) {
                String previoisDate = "";
                if (SpeakerCursor.moveToFirst()) {
                    previoisDate = SpeakerCursor.getString(SpeakerCursor.getColumnIndex(databaseHandler.Update_DATE));
                }

                if (!(previoisDate.equalsIgnoreCase(speakerUpdateDate))) {
                    getSpeakerListData();
                } else {
                    isSpeakerData = true;
                }
            } else {
                getSpeakerListData();
            }

            if (groupcursor.getCount() > 0) {
                String previoisDate = "";
                if (groupcursor.moveToFirst()) {
                    previoisDate = groupcursor.getString(groupcursor.getColumnIndex(databaseHandler.Update_DATE));
                }

                if (!(previoisDate.equalsIgnoreCase(groupUpdateDate))) {
                    getGroupModuleData();
                } else {
                    isGroupData = true;
                }
            } else {
                getGroupModuleData();
            }

            if (mapcursor.getCount() > 0) {
                String previoisDate = "";
                if (mapcursor.moveToFirst()) {
                    previoisDate = mapcursor.getString(mapcursor.getColumnIndex(databaseHandler.Update_DATE));
                }
                if (!(previoisDate.equalsIgnoreCase(MapUpdateDate))) {
                    getMapListData();
                } else {
                    isMapData = true;
                }
            } else {
                getMapListData();
            }

            if (Sponsorcursor.getCount() > 0) {
                String previoisDate = "";
                if (Sponsorcursor.moveToFirst()) {
                    previoisDate = Sponsorcursor.getString(Sponsorcursor.getColumnIndex(databaseHandler.Update_DATE));
                }

                if (!(previoisDate.equalsIgnoreCase(SponsorUpdateDate))) {
                    getSponsorListData();
                } else {
                    isSponsorData = true;
                }
            } else {
                getSponsorListData();
            }


            if (AgendaCursor.getCount() > 0) {
                String previoisDate = "";
                if (AgendaCursor.moveToFirst()) {
                    previoisDate = AgendaCursor.getString(AgendaCursor.getColumnIndex(databaseHandler.Update_DATE));
                }
                if (!(previoisDate.equalsIgnoreCase(AgendaUpdateDate))) {
                    getAgendaListData();
                } else {
                    isAgendaData = true;
                }
            } else {
                getAgendaListData();
            }


            if (CMsCursor.getCount() > 0) {
                String previoisDate = "";
                if (CMsCursor.moveToFirst()) {
                    previoisDate = CMsCursor.getString(CMsCursor.getColumnIndex(databaseHandler.Update_DATE));
                }
                if (!(previoisDate.equalsIgnoreCase(CmsUpdateDate))) {
                    getCmsListData();
                } else {
                    isCMsData = true;
                }
            } else {
                getCmsListData();
            }


            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCMSData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (databaseHandler.isUpdateDataExist(sessionManager.getEventId(), CmsMenuId)) {
                    databaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), CmsMenuId);
                    databaseHandler.insertUpdateModuleData(CmsMenuId, "CMSDATA", CmsUpdateDate, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateModuleData(CmsMenuId, "CMSDATA", CmsUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                CmsListandDetailList cmsListandDetailList = gson.fromJson(jsonObject.get("data").toString(), CmsListandDetailList.class);
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                if (databaseHandler.isCmsPageExistFromSplah(sessionManager.getEventId())) {
                    databaseHandler.deleteCmsPageDataFromSplash(sessionManager.getEventId());
                    databaseHandler.insertCmsPageFromSplash(sessionManager.getEventId(), jsonObjectData);
                } else {
                    databaseHandler.insertCmsPageFromSplash(sessionManager.getEventId(), jsonObjectData);
                }

                if (databaseHandler.isCMSLISTDataExist(sessionManager.getEventId())) {
                    databaseHandler.deleteCMSLISTData(sessionManager.getEventId());
                    databaseHandler.insertCMSLISTData(cmsListandDetailList.getCmsListDataArrayList(), sessionManager.getEventId());
                } else {
                    databaseHandler.insertCMSLISTData(cmsListandDetailList.getCmsListDataArrayList(), sessionManager.getEventId());
                }
                isCMsData = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadMapListData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (databaseHandler.isUpdateDataExist(sessionManager.getEventId(), MapMenuid)) {
                    databaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), MapMenuid);
                    databaseHandler.insertUpdateModuleData(MapMenuid, "map", MapUpdateDate, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateModuleData(MapMenuid, "map", MapUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                MapListData offlineData = gson.fromJson(jsonObject.get("data").toString(), MapListData.class);

                //addToDatabase
                if (databaseHandler.isMapListExist(sessionManager.getEventId())) {
                    databaseHandler.deleteMapListExistData(sessionManager.getEventId());
                    databaseHandler.insertUpdateAllMapListData(offlineData.getMapNewDataArrayList(), sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateAllMapListData(offlineData.getMapNewDataArrayList(), sessionManager.getEventId());
                }
                isMapData = true;

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExhibitorData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {
                if (databaseHandler.isUpdateDataExist(sessionManager.getEventId(), exhibitorMenuId)) {
                    databaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), exhibitorMenuId);
                    databaseHandler.insertUpdateModuleData(exhibitorMenuId, "exhibitor", exhibitorUpdateDate, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateModuleData(exhibitorMenuId, "exhibitor", exhibitorUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                ExhibitorOfflineData offlineData = gson.fromJson(jsonObject.get("data").toString(), ExhibitorOfflineData.class);
                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                if (databaseHandler.isExhibitorDataExist(sessionManager.getEventId())) {

                    databaseHandler.deleteExhibitorListData(sessionManager.getEventId());
                    databaseHandler.insertUpdateAllParentCategory(offlineData.getPcategories(), sessionManager.getEventId());
                    databaseHandler.insertUpdateAllSubCategory(offlineData.getCategories(), sessionManager.getEventId());
                    databaseHandler.insertUpdateExhibitorListdata(offlineData.getExhibitor_list(), sessionManager.getEventId(), sessionManager.getExhibitorParentCategoryId(), sessionManager.getUserId(), sessionManager.getEventType());
                    databaseHandler.insertExhibitorParentGroupData(offlineData.getExhibitorParentCatGroups(), sessionManager.getEventId());
                    databaseHandler.insertUpdateAllCountries(offlineData.getCountries(), sessionManager.getEventId());
                    databaseHandler.insertExhibitorDetailFromSplash(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData);
                } else {
                    databaseHandler.insertUpdateAllParentCategory(offlineData.getPcategories(), sessionManager.getEventId());
                    databaseHandler.insertUpdateAllSubCategory(offlineData.getCategories(), sessionManager.getEventId());
                    databaseHandler.insertUpdateExhibitorListdata(offlineData.getExhibitor_list(), sessionManager.getEventId(), sessionManager.getExhibitorParentCategoryId(), sessionManager.getUserId(), sessionManager.getEventType());
                    databaseHandler.insertUpdateAllCountries(offlineData.getCountries(), sessionManager.getEventId());
                    databaseHandler.insertExhibitorParentGroupData(offlineData.getExhibitorParentCatGroups(), sessionManager.getEventId());
                    databaseHandler.insertExhibitorDetailFromSplash(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getEventType(), jsonObjectData);
                }
                isExhibitorData = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGroupData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (databaseHandler.isUpdateDataExist(sessionManager.getEventId(), groupMenuid)) {
                    databaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), groupMenuid);
                    databaseHandler.insertUpdateModuleData(groupMenuid, "group", groupUpdateDate, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateModuleData(groupMenuid, "group", groupUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                GrouppingOfflineList offlineData = gson.fromJson(jsonObject.get("data").toString(), GrouppingOfflineList.class);

                // Simple All GroupDataInsert
                if (databaseHandler.isGroupDataExist(sessionManager.getEventId())) {
                    databaseHandler.deleteGroupExistData(sessionManager.getEventId());
                    databaseHandler.insertUpdateAllGroupModuleData(offlineData.getGroupModuleData());
                } else {
                    databaseHandler.insertUpdateAllGroupModuleData(offlineData.getGroupModuleData());
                }

                // SuperGroupDataInsert
                if (databaseHandler.isSuperGroupDataExist(sessionManager.getEventId())) {
                    databaseHandler.deleteSuperGroupExistData(sessionManager.getEventId());
                    databaseHandler.insertSuperGroupModuleData(offlineData.getSuperGroupDataArrayList());
                } else {
                    databaseHandler.insertSuperGroupModuleData(offlineData.getSuperGroupDataArrayList());
                }

                // insert SuperGroupRelationData

                if (databaseHandler.isSuperGroupRelationDataExist(sessionManager.getEventId())) {
                    databaseHandler.deleteSuperRelationGroupExistData(sessionManager.getEventId());
                    databaseHandler.insertSuperGroupRelationModuleData(offlineData.getSuperGroupRelationDataArrayList(), sessionManager.getEventId());
                } else {
                    databaseHandler.insertSuperGroupRelationModuleData(offlineData.getSuperGroupRelationDataArrayList(), sessionManager.getEventId());
                }

                // GroupRelationDataInsert
                if (databaseHandler.isGroupRelationDataExist(sessionManager.getEventId())) {
                    databaseHandler.deleteGroupRelationExistData(sessionManager.getEventId());
                    databaseHandler.insertUpdateAllGroupModuleRelationData(offlineData.getGroupRelationModuleDataArrayList(), sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateAllGroupModuleRelationData(offlineData.getGroupRelationModuleDataArrayList(), sessionManager.getEventId());
                }
                isGroupData = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAgendaData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {
                if (databaseHandler.isUpdateDataExist(sessionManager.getEventId(), AgendaMenuId)) {
                    databaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), AgendaMenuId);
                    databaseHandler.insertUpdateModuleData(AgendaMenuId, "Agenda", AgendaUpdateDate, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateModuleData(AgendaMenuId, "Agenda", AgendaUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                AgendaData agendaData = gson.fromJson(jsonObject.get("data").toString(), AgendaData.class);
                if (databaseHandler.isAgendaDataExist(sessionManager.getEventId())) {
                    databaseHandler.deleteAgendaCatData(sessionManager.getEventId());
                    databaseHandler.deleteAgendaCatRelationData(sessionManager.getEventId());
                    databaseHandler.deleteAgendaListData(sessionManager.getEventId());
                    databaseHandler.deleteAgendaTypeData(sessionManager.getEventId());
                    databaseHandler.insertUpdateAgendadata(agendaData, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateAgendadata(agendaData, sessionManager.getEventId());
                }
                isAgendaData = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSponsorData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {
                if (databaseHandler.isUpdateDataExist(sessionManager.getEventId(), SponsorMenuId)) {
                    databaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), SponsorMenuId);
                    databaseHandler.insertUpdateModuleData(SponsorMenuId, "sponsor", SponsorUpdateDate, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateModuleData(SponsorMenuId, "sponsor", SponsorUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                SponsorMainListClasss offlineSponsorData = gson.fromJson(jsonObject.get("data").toString(), SponsorMainListClasss.class);
                if (databaseHandler.isSponsorDataExist(sessionManager.getEventId())) {
                    databaseHandler.deleteSponsorListData(sessionManager.getEventId());
                    databaseHandler.deleteSponsorTypeData(sessionManager.getEventId());
                    databaseHandler.insertUpdateSponsordata(offlineSponsorData.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                } else {
                    databaseHandler.insertUpdateSponsordata(offlineSponsorData.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                }
                isSponsorData = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSpeakerData(JSONObject jsonObject) {
        try {
            if (jsonObject.has("data")) {

                if (databaseHandler.isUpdateDataExist(sessionManager.getEventId(), speakerMenuId)) {
                    databaseHandler.deleteUpdateModuleData(sessionManager.getEventId(), speakerMenuId);
                    databaseHandler.insertUpdateModuleData(speakerMenuId, "SpeakerData", speakerUpdateDate, sessionManager.getEventId());
                } else {
                    databaseHandler.insertUpdateModuleData(speakerMenuId, "SpeakerData", speakerUpdateDate, sessionManager.getEventId());
                }

                Gson gson = new Gson();
                SpeakerListMainClass speakerListClass = gson.fromJson(jsonObject.get("data").toString(), SpeakerListMainClass.class);
                if (databaseHandler.isSpeakerListExist(sessionManager.getEventId())) {
                    databaseHandler.deleteSpeakerListExistData(sessionManager.getEventId());
                    databaseHandler.insertUpdateSpeakerdata(speakerListClass.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                } else {
                    databaseHandler.insertUpdateSpeakerdata(speakerListClass.getSponsorListNewDataArrayList(), sessionManager.getEventId(), sessionManager.getUserId());
                }
                isSpeakerData = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        nextStep();
    }

    private void getVersionCode() {
        if (GlobalData.isNetworkAvailable(Splash_Activity.this)) {
            new VolleyRequest(Splash_Activity.this, VolleyRequest.Method.POST, MyUrls.updateVersionCode, Param.updateVerioncode("Android"), 11, false, this);
        }
    }

    public class updateSponsorDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        public updateSponsorDatabase(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadSponsorData(jsonObject);
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class updateExhibitorDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        public updateExhibitorDatabase(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadExhibitorData(jsonObject);
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class updateGroupDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        public updateGroupDatabase(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadGroupData(jsonObject);
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class updateMapDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        public updateMapDatabase(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadMapListData(jsonObject);
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class updateAgendaDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        public updateAgendaDatabase(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadAgendaData(jsonObject);
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class updateCmsListDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        public updateCmsListDatabase(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadCMSData(jsonObject);
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class updateSpeakerListDatabase extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        public updateSpeakerListDatabase(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (jsonObject != null)
                loadSpeakerData(jsonObject);
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
            super.onPostExecute(aBoolean);
        }
    }

    public class storeAndCheckUpdateAPIAsync extends AsyncTask<Void, Void, Boolean> {

        JSONObject jsonObject;

        storeAndCheckUpdateAPIAsync(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            storeAndCheckUpdateAPI(jsonObject);
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (isAgendaData == true && isSponsorData == true && isExhibitorData == true
                    && isMapData == true && isGroupData == true && isCMsData == true && isSpeakerData == true) {
                gotoHome();
            }
        }
    }

//    private class GetVersionCode extends AsyncTask<Void, String, String> {
//        @Override
//        protected String doInBackground(Void... voids) {
//            String newVersion = null;
//            try {
//                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + Splash_Activity.this.getPackageName() + "&hl=it")
//                        .timeout(30000)
//                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                        .referrer("http://www.google.com")
//                        .get()
//                        .select("div[itemprop=softwareVersion]")
//                        .first()
//                        .ownText();
//                return newVersion;
//
//            } catch (Exception e) {
//                return newVersion;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String onlineVersion) {
//            super.onPostExecute(onlineVersion);
////            Log.d("AITL NewVersion","Version"+onlineVersion);
//
//            if (onlineVersion != null && !onlineVersion.isEmpty()) {
//                if (versionName.equalsIgnoreCase(onlineVersion)) {
//                    Log.d("AITL", "Current version " + versionName + "playstore version " + onlineVersion);        //show dialog
//                    handlerMethod();
//                } else {
//                    FragmentManager fm = getSupportFragmentManager();
//                    VersionCodeUpdateDailog fragment = new VersionCodeUpdateDailog();
//                    fragment.show(fm, "DialogFragment");
//                }
//            } else {
//                handlerMethod();
//            }
//        }
//
//    }

}
