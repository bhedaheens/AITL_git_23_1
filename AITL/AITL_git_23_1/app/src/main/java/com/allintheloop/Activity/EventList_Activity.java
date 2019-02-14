package com.allintheloop.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.EventListAdapter;
import com.allintheloop.Adapter.RecyclerItemClickListener;
import com.allintheloop.Bean.AgendaData.AgendaData;
import com.allintheloop.Bean.CmsGroupData.CmsListandDetailList;
import com.allintheloop.Bean.EventList;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorOfflineData;
import com.allintheloop.Bean.GroupingData.GrouppingOfflineList;
import com.allintheloop.Bean.Map.MapListData;
import com.allintheloop.Bean.Speaker.SpeakerListMainClass;
import com.allintheloop.Bean.SponsorClass.SponsorMainListClasss;
import com.allintheloop.Fragment.EventDailog_Fragment;
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
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class EventList_Activity extends SuperActivity implements VolleyInterface {

    ArrayList<EventList> arrayEventList;
    String ImgStr, ImgUrl, id, eName, Fb_status, SecretKey, eventType, fundrising_enabled, linkedin_login_enabled, show_login_screen, defultLang;
    RecyclerView recyclerView;
    EventListAdapter adapter;
    Toolbar toolbar;
    SearchView searchView;
    SearchManager searchManager;
    SessionManager sessionManager;
    SQLiteDatabaseHandler databaseHandler;
    Cursor cursor;
    EventList EvntObj;
    TextView txtNoDataFoud;
    String android_id;
    String gcm_id;
    String show_once = "";
    String exhibitorUpdateDate = "", groupUpdateDate = "", MapUpdateDate = "", SponsorUpdateDate = "", AgendaUpdateDate = "", CmsUpdateDate = "", speakerUpdateDate = "";
    String exhibitorMenuId = "3", MapMenuid = "10", groupMenuid = "100", SponsorMenuId = "43", AgendaMenuId = "1", CmsMenuId = "21", speakerMenuId = "7";
    boolean isExhibitorData = false, isAgendaData = false, isSponsorData = false, isGroupData = false, isMapData = false, isCMsData = false, isFirtOnBoard = false, isSpeakerData = false;
    private LoginManager loginManager;
    private ProgressDialog mProgressDialog;

    //    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_list);
        arrayEventList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.EventListtoolbar);

        setSupportActionBar(toolbar);
        databaseHandler = new SQLiteDatabaseHandler(this);
        sessionManager = new SessionManager(this);
//        gcm_id= FirebaseInstanceId.getInstance().getToken();
//        sessionManager.setGcm_id(gcm_id);
//        dialog=new ProgressDialog(this);

        txtNoDataFoud = (TextView) findViewById(R.id.txtNoDataFoud);
        recyclerView = (RecyclerView) findViewById(R.id.rv_viewEvent);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        getEventList();
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EvntObj = adapter.getItem(position);
                Log.d("EventId", EvntObj.getiD());
                if (!(sessionManager.getEventId().equalsIgnoreCase(EvntObj.getiD()))) {
                    sessionManager.languageClear();
                }
                if (sessionManager.isLogin()) {
                    Log.d("AITL LOGIN", "" + sessionManager.isLogin());
                    Log.d("AITL  NOW EVENTID", EvntObj.getiD());
                    if (!(sessionManager.getEventId().equalsIgnoreCase(EvntObj.getiD()))) {
                        Log.d("AITL LOGIN", sessionManager.getEventId());
                        Log.d("AITL LOGIN", "" + sessionManager.isLogin());

                        MaterialDialog dialog = new MaterialDialog.Builder(EventList_Activity.this)
                                .title("To open this app you must log out of" + " " + sessionManager.getEventName())
                                .items("Would you like to log out and open" + " " + EvntObj.geteName() + " ?")
                                .positiveColor(getResources().getColor(R.color.colorAccent))
                                .positiveText(getResources().getString(R.string.txtYes))
                                .negativeText(getResources().getString(R.string.txtNo))
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        logoutUser();

                                    }
                                })
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(MaterialDialog dialog, DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .cancelable(false)
                                .build();
                        dialog.show();
                    } else {
                        openDialog();
                    }
                } else {
                    openDialog();
                }
            }
        }));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EventList_Activity.this, SearchApp_Activity.class));
        finish();
    }

    private void logoutUser()  // call Api for Logout
    {

        if (!sessionManager.getFacebookId().equals("")) {
            loginManager.getInstance().logOut();
        }
        sessionManager.logout();
        sessionManager.languageClear();
        openDialog();

    }

    private void openDialog() {
        sessionManager.eventdata(EvntObj);

        sessionManager.onBoradData = "";
        sessionManager.showOnce = "";

        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            checkUpdateData();
        } else {
            if (sessionManager.isLogin())
                gotoHomeData();
        }
    }

    private void onBoardCall() {
        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.getOnBoardScreen, Param.GetFormData(sessionManager.getEventId()), 1, false, this);
        }
    }

    private void checkUpdateData() {

        mProgressDialog = new ProgressDialog(EventList_Activity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
//            mProgressDialog.setIndeterminate(true);
        mProgressDialog.show();


        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.checkUpdateData,
                    Param.checkUpdate(sessionManager.getEventId()),
                    4, false, this);
        }
    }

    private void getEventList() {
        try {
            if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {

                if (sessionManager.getPrivatePublicStatus().equalsIgnoreCase("1")) {
                    new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.EVENT_LIST, Param.EventList(android_id), 0, false, this);
                } else if (sessionManager.getPrivatePublicStatus().equalsIgnoreCase("0")) {

                    SecretKey = sessionManager.getSecretKey();
                    Log.d("SecrentKey", "" + SecretKey);
                    new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.SECURE_EVENT, Param.FindSecyrEvent(SecretKey), 0, false, this);
                }
            } else {
                recyclerView.setVisibility(View.GONE);
                txtNoDataFoud.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void searchEvent(String eventName) {
        new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.searchEvent, Param.searchEvent(eventName), 0, false, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (sessionManager.getPrivatePublicStatus().equalsIgnoreCase("1")) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_search, menu);

            searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setMaxWidth(Integer.MAX_VALUE);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (GlobalData.isNetworkAvailable(getApplicationContext())) {
                        if (query.trim().length() > 0) {
                            searchEvent(query);
                        } else {
                            getEventList();
                        }
                    } else {
                        if (arrayEventList.size() > 0) {
                            adapter.getFilter().filter(query);
                        }
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (GlobalData.isNetworkAvailable(getApplicationContext())) {
                        if (newText.trim().length() > 0) {
                            searchEvent(newText);
                        } else {
                            getEventList();
                        }
                    } else {
                        if (arrayEventList.size() > 0) {
                            adapter.getFilter().filter(newText);
                        }
                    }
                    return true;
                }
            });
            super.onCreateOptionsMenu(menu);
        }

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        loadEventListData(jsonObject);
                    } else {
                        recyclerView.setVisibility(View.GONE);
                        txtNoDataFoud.setVisibility(View.VISIBLE);
                        txtNoDataFoud.setText(jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {

                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("AITL DataOnBoard", jsonObject.toString());
                        onBoradScreenLoad(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

                        new updateGroupDatabase(jsonObject).execute();

//                        loadGroupData(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 3:
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
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        storeAndCheckUpdateAPI(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
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
//                        loadMapListData(jsonObject);


                        new updateSponsorDatabase(jsonObject).execute();
//                        loadSponsorData(jsonObject);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {

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

//                        loadAgendaData(jsonObject);
                        new updateCmsListDatabase(jsonObject).execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 12:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.optString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
//                        loadAgendaData(jsonObject);
                        new updateSpeakerListDatabase(jsonObject).execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

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
                    databaseHandler.insertUpdateAllCountries(offlineData.getCountries(), sessionManager.getEventId());
                    databaseHandler.insertExhibitorParentGroupData(offlineData.getExhibitorParentCatGroups(), sessionManager.getEventId());
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

    private void getExhibitorsData() {
        if (GlobalData.checkForUIDVersion()) {
            if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
                new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_ExhibitorDataUid,
                        Param.getExhibitorList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", 1, "", sessionManager.getExhibitorParentCategoryId(), sessionManager.getIsLastCategoryName()),
                        3, false, this);
            }
        } else {

            new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_ExhibitorData,
                    Param.getExhibitorList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", 1, "", sessionManager.getExhibitorParentCategoryId(), sessionManager.getIsLastCategoryName()),
                    3, false, this);

        }
    }

    private void loadEventListData(JSONObject jsonObject) {
        try {
            arrayEventList = new ArrayList<>();
            if (jsonObject.has("data")) {
                JSONArray jsonArray = jsonObject.optJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject index = (JSONObject) jsonArray.get(i);
                    id = index.getString("event_id");
                    eName = index.getString("Event_name");
                    Fb_status = index.getString("facebook_login");
                    eventType = index.optString("Event_type");
                    ImgStr = index.getString("Logo_images");
                    fundrising_enabled = index.getString("fundraising_enbled");
                    linkedin_login_enabled = index.getString("linkedin_login_enabled");
                    show_login_screen = index.getString("show_login_screen");
                    String default_lang = index.getJSONObject("default_lang").toString();
                    arrayEventList.add(new EventList(id, eName, MyUrls.Imgurl + ImgStr, Fb_status, eventType, fundrising_enabled, linkedin_login_enabled, default_lang, show_login_screen));
                }
                recyclerView.setVisibility(View.VISIBLE);
                txtNoDataFoud.setVisibility(View.GONE);
                adapter = new EventListAdapter(arrayEventList, getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            } else {
                recyclerView.setVisibility(View.GONE);
                txtNoDataFoud.setVisibility(View.VISIBLE);
                txtNoDataFoud.setText(jsonObject.getString("message"));
//                            ToastC.show(getApplicationContext(),jsonObject.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeAndCheckUpdateAPI(JSONObject jsonObject) {
//        dialog.setMessage("Downloading...");
//        dialog.show();

//        mProgressDialog=GlobalData.getProgressDialog(Splash_Activity.this);


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
                gotoHomeData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSponsorListData() {
        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SponsorList_offlineUid, Param.getSponsorOfflineList(sessionManager.getEventId()), 6, false, this);
            else
                new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SponsorList_offline, Param.getSponsorOfflineList(sessionManager.getEventId()), 6, false, this);
        }
    }

    private void getAgendaListData() {
        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_AgendaList_offline, Param.getSponsorOfflineList(sessionManager.getEventId()), 7, false, this);
        }
    }

    private void getMapListData() {
        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_MapList, Param.getMapList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 5, false, this);
        }
    }

    private void getCmsListData() {
        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.getCMSofflineData, Param.getMapList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 8, false, this);
        }
    }

    private void getSpeakerListData() {
        if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
            if (GlobalData.checkForUIDVersion())
                new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SpeakerList_offlineUid, Param.getSpeakerList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 12, false, this);
            else
                new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.get_SpeakerList_offline, Param.getSpeakerList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType()), 12, false, this);

        }

    }

    private void onBoradScreenLoad(JSONObject jsonObject) {
        try {
            isFirtOnBoard = true;
            JSONObject jsonCode = jsonObject.getJSONObject("code");
            JSONArray jsonArrayOScreen = jsonCode.getJSONArray("o_screen");
            if (jsonArrayOScreen.length() > 0) {
                sessionManager.onBoradData = jsonCode.toString();
                sessionManager.showOnce = jsonCode.getString("show_once");
                show_once = jsonCode.getString("show_once");

                if (sessionManager.getIsFirstTimeOnBoard()) {

                    gotoHomeData();
                } else {

                    if (sessionManager.getEventType().equalsIgnoreCase("3")) {
                        FragmentManager fm = getSupportFragmentManager();
                        EventDailog_Fragment fragment = new EventDailog_Fragment();
                        fragment.show(fm, "DialogFragment");
                    } else {

                        if (show_once.equalsIgnoreCase("0")) {
                            sessionManager.setIsFirstTimeOnBoard(false);
                        } else if (show_once.equalsIgnoreCase("1")) {
                            sessionManager.setIsFirstTimeOnBoard(true);
                        }
                        startActivity(new Intent(EventList_Activity.this, OnBoardScreenActivity.class));
                        finish();
                    }
                }


            } else {
                gotoHomeData();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getGroupModuleData() {
        if (GlobalData.isNetworkAvailable(getApplicationContext())) {
            if (GlobalData.isNetworkAvailable(EventList_Activity.this)) {
                new VolleyRequest(EventList_Activity.this, VolleyRequest.Method.POST, MyUrls.getGroupModuleData,
                        Param.getGroupingData(sessionManager.getEventId()),
                        2, false, this);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void eventType4Data() {
        if (sessionManager.isLogin()) {
            startActivity(new Intent(EventList_Activity.this, MainActivity.class));
            finish();
        } else {
            if (sessionManager.get_show_login_screen().equalsIgnoreCase("1")) {
                if (GlobalData.isNetworkAvailable(getApplicationContext())) {
                    startActivity(new Intent(EventList_Activity.this, LoginMainScreen.class));
                    finish();
                } else {
                    ToastC.show(getApplicationContext(), getString(R.string.noInernet));
                }
            } else {
                startActivity(new Intent(EventList_Activity.this, MainActivity.class));
                finish();
            }
        }
    }

    private void eventTypeOneTWoData() {
        if (sessionManager.isLogin()) {
            startActivity(new Intent(EventList_Activity.this, MainActivity.class));
            finish();
        } else {
            if (GlobalData.isNetworkAvailable(getApplicationContext())) {
                startActivity(new Intent(EventList_Activity.this, LoginMainScreen.class));
                finish();
            } else {
                ToastC.show(getApplicationContext(), getString(R.string.noInernet));
            }
        }
    }

    private void gotoHomeData() {
        if (sessionManager.getIsFirstTimeOnBoard()) {
            if (sessionManager.getEventType().equalsIgnoreCase("3")) {
                FragmentManager fm = getSupportFragmentManager();
                EventDailog_Fragment fragment = new EventDailog_Fragment();
                fragment.show(fm, "DialogFragment");
            } else if (sessionManager.getEventType().equalsIgnoreCase("4")) {

                eventType4Data();
            } else {
                eventTypeOneTWoData();
            }
        } else {
            if (GlobalData.isNetworkAvailable(this)) {
                if (!isFirtOnBoard) {
                    onBoardCall();
                } else {
                    if (sessionManager.getEventType().equalsIgnoreCase("3")) {
                        FragmentManager fm = getSupportFragmentManager();
                        EventDailog_Fragment fragment = new EventDailog_Fragment();
                        fragment.show(fm, "DialogFragment");
                    } else if (sessionManager.getEventType().equalsIgnoreCase("4")) {

                        eventType4Data();
                    } else {
                        eventTypeOneTWoData();
                    }
                }
            } else {
                if (sessionManager.getEventType().equalsIgnoreCase("3")) {
                    FragmentManager fm = getSupportFragmentManager();
                    EventDailog_Fragment fragment = new EventDailog_Fragment();
                    fragment.show(fm, "DialogFragment");
                } else if (sessionManager.getEventType().equalsIgnoreCase("4")) {

                    eventType4Data();
                } else {
                    eventTypeOneTWoData();
                }
            }

        }
        if (!isFinishing()) {
            if (mProgressDialog != null) {
//            GlobalData.dismissDialog(this,mProgressDialog);
                mProgressDialog.dismiss();
            }
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
                gotoHomeData();
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
                gotoHomeData();
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
                gotoHomeData();
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
                gotoHomeData();
            }
            super.onPostExecute(aBoolean);
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
                gotoHomeData();
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
                gotoHomeData();
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
                gotoHomeData();
            }
            super.onPostExecute(aBoolean);
        }
    }

}