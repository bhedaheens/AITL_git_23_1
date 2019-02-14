package com.allintheloop.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import com.allintheloop.Bean.ExhibitorListClass.ExhibitorSurveyofflineUploadData;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nteam on 15/9/17.
 */

public class ExhibitorOfflineUploadData extends BroadcastReceiver {
    SessionManager sessionManager;
    SQLiteDatabaseHandler databaseHandler;
    ArrayList<ExhibitorSurveyofflineUploadData> exhibitorSurveyofflineUploadData;
    int isLast = 0;
    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        sessionManager = new SessionManager(context);
        this.context = context;
        databaseHandler = new SQLiteDatabaseHandler(context);
        exhibitorSurveyofflineUploadData = new ArrayList<>();
        boolean status = GlobalData.isNetworkAvailable(context);
        isLast = 0;
        if (status) {
            if (sessionManager.isLogin()) {
                Cursor exhiSurveyData = databaseHandler.getExhiSurveyUploadData();
                Log.d("Bhavdip  Count", "" + exhiSurveyData.getCount());
                if (exhiSurveyData.getCount() > 0) {
                    exhiSurveyData.moveToFirst();
                    while (exhiSurveyData.isAfterLast() == false) {

                        String event_id, user_id, scan_id, scan_data, upload_lead, survey_data, created_date;

                        event_id = exhiSurveyData.getString(exhiSurveyData.getColumnIndex(databaseHandler.ExiLead_EventId));
                        user_id = exhiSurveyData.getString(exhiSurveyData.getColumnIndex(databaseHandler.ExiLead_userId));
                        scan_id = exhiSurveyData.getString(exhiSurveyData.getColumnIndex(databaseHandler.ExiLead_scanId));
                        scan_data = exhiSurveyData.getString(exhiSurveyData.getColumnIndex(databaseHandler.ExiLead_scanData));
                        upload_lead = exhiSurveyData.getString(exhiSurveyData.getColumnIndex(databaseHandler.ExiLead_uploadData));
                        survey_data = exhiSurveyData.getString(exhiSurveyData.getColumnIndex(databaseHandler.ExiLead_LeadSurveyData));
                        created_date = exhiSurveyData.getString(exhiSurveyData.getColumnIndex(databaseHandler.ExiLead_TimeData));
                        exhibitorSurveyofflineUploadData.add(new ExhibitorSurveyofflineUploadData(event_id, user_id, scan_id, scan_data, upload_lead, survey_data, created_date));
                        exhiSurveyData.moveToNext();
                    }
                    exhiSurveyData.close();
                    Log.d("Bhavdip ArrayList Size", "" + exhibitorSurveyofflineUploadData.size());
                    for (int i = 0; i < exhibitorSurveyofflineUploadData.size(); i++) {
                        isLast = i + 1;
                        serviceCall(exhibitorSurveyofflineUploadData.get(i).getEvent_id(),
                                exhibitorSurveyofflineUploadData.get(i).getUser_id(),
                                exhibitorSurveyofflineUploadData.get(i).getScan_id(),
                                exhibitorSurveyofflineUploadData.get(i).getScan_data(),
                                exhibitorSurveyofflineUploadData.get(i).getUpload_lead(),
                                exhibitorSurveyofflineUploadData.get(i).getSurvey_data(),
                                exhibitorSurveyofflineUploadData.get(i).getExiLead_TimeData());
                    }
                }
            }
        }
    }

    private void serviceCall(String event_id, String user_id, String scan_id, final String scan_data, String upload_lead, String survey_data, String ExiLead_TimeData) {
        Log.d("Bhavdip hs01", "eventId : " + event_id + " UserId :" + user_id + " ScanData :" + scan_data);
        new VolleyRequest(VolleyRequest.Method.POST, MyUrls.saveSurveyOfflineData, Param.saveSurveyOfflineData(event_id, user_id, scan_id, scan_data, upload_lead, survey_data, ExiLead_TimeData), 0, false, new VolleyInterface() {
            @Override
            public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
                switch (volleyResponse.type) {
                    case 0:
                        try {
                            JSONObject jsonObject = new JSONObject(volleyResponse.output);
                            if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                                Log.d("Bhavdip ScanData", scan_data);
                                try {
                                    if (isLast == exhibitorSurveyofflineUploadData.size()) {
                                        databaseHandler.deleteAllExhiSurveyUploadData();
                                        exhibitorSurveyofflineUploadData.clear();
                                        GlobalData.exhibitorMyLeadTab(context);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });

    }

}
