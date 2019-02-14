package com.allintheloop.Util;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nteam on 20/6/18.
 */

public class NetworkChangeReceiverNougat {

    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    Cursor sendPendingRequestCursor, sendPendingMessageRequestCursor, sendPendingMessageRequestCursorTemp;
    String agenda_id, event_id, user_id, token, str_url;
    String message_eventId, message_userId, message_token, message_url, message_Imgurl, str_message, str_message_id, res_messageId;
    SessionManager sessionManager;
    Context context;
    int isLast = 0;

    public NetworkChangeReceiverNougat(Context context) {
        this.context = context;
    }

    public void updateData() {
        boolean status = GlobalData.isNetworkAvailable(context);
        sessionManager = new SessionManager(context);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(context);
        Log.d("AITL IF OUt Status", "Net " + status);

        if (status) {

            if (!GlobalData.isAppIsInBackground(context)) {
                GlobalData.updatePresantation(context);
                AppController.getInstance().startBeacon();
                try {
                    sendPendingRequestCursor = sqLiteDatabaseHandler.getPendingAgendaRequestListing("0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (sendPendingRequestCursor != null && sendPendingRequestCursor.getCount() > 0) {
                    sendPendingRequestCursor.moveToFirst();
                    while (sendPendingRequestCursor.isAfterLast() == false) {
                        Log.d("AITL Inactive DaTa URL", sendPendingRequestCursor.getString(sendPendingRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequesturl)));
                        agenda_id = sendPendingRequestCursor.getString(sendPendingRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestAgendaId));
                        event_id = sendPendingRequestCursor.getString(sendPendingRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestEventId));
                        user_id = sendPendingRequestCursor.getString(sendPendingRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestUserId));
                        token = sendPendingRequestCursor.getString(sendPendingRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestToken));
                        str_url = sendPendingRequestCursor.getString(sendPendingRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequesturl));
                        serviceCall();
                        sendPendingRequestCursor.moveToNext();
                    }
                    sendPendingRequestCursor.close();
                }

                try {
                    sendPendingMessageRequestCursor = sqLiteDatabaseHandler.getPendingMessageRequest(sessionManager.getEventId(), sessionManager.getUserId());
                    sendPendingMessageRequestCursorTemp = sqLiteDatabaseHandler.getPendingMessageRequest(sessionManager.getEventId(), sessionManager.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (sendPendingMessageRequestCursor != null && sendPendingMessageRequestCursor.getCount() > 0) {

                    sendPendingMessageRequestCursor.moveToFirst();

                    while (sendPendingMessageRequestCursor.isAfterLast() == false) {
                        res_messageId = sendPendingMessageRequestCursor.getString(sendPendingMessageRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessageId));
                        Log.d("AITL DATA MESSAGEID", "ID" + res_messageId);
                        str_message = sendPendingMessageRequestCursor.getString(sendPendingMessageRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessage));
                        message_eventId = sendPendingMessageRequestCursor.getString(sendPendingMessageRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessageEventId));
                        message_userId = sendPendingMessageRequestCursor.getString(sendPendingMessageRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessageUserId));
                        message_token = sendPendingMessageRequestCursor.getString(sendPendingMessageRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessageToken));
                        message_url = sendPendingMessageRequestCursor.getString(sendPendingMessageRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessageurl));
                        message_Imgurl = sendPendingMessageRequestCursor.getString(sendPendingMessageRequestCursor.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessageImageurl));
                        HashMap<String, String> apiParams = new HashMap<>();

                        apiParams.put("event_id", message_eventId);
                        apiParams.put("message", str_message);
                        apiParams.put("user_id", message_userId);
                        apiParams.put("token", message_token);


                        new callService(sendPendingMessageRequestCursor.getPosition(), apiParams).execute(message_url);
                        sendPendingMessageRequestCursor.moveToNext();
                    }
                    sendPendingMessageRequestCursor.close();
                }
            }
        } else {
            AppController.getInstance().stopBeacon();
        }
    }

    private void UploadePhotoAPI(String multipleImg, final String message_id, final int size) {


        Log.d("AITL MultipleImageAPI", multipleImg);

        new VolleyRequest(message_Imgurl, Param.message_img(new File(multipleImg)), Param.addActivityImageRequest(str_message_id), 0, false, new VolleyInterface() {
            @Override
            public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
                switch (volleyResponse.type) {
                    case 0:
                        try {
                            JSONObject jsonObject = new JSONObject(volleyResponse.output);
                            if (isLast == size) {
                                Log.d("AITL  SuccessFully", jsonObject.toString());
                                Log.d("AITL IMAGE", "ARRAYLIST GET CLEARED");
//                            img_arrayList.clear();
                                sqLiteDatabaseHandler.DeletePendigMessageRequest(message_id);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });
    }


    private void serviceCall() {

        new VolleyRequest(VolleyRequest.Method.POST, str_url, Param.Update_Agenda(event_id, token, agenda_id, user_id), 0, false, new VolleyInterface() {
            @Override
            public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
                switch (volleyResponse.type) {
                    case 0:
                        try {
                            JSONObject jsonObject = new JSONObject(volleyResponse.output);
                            if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                                if (jsonObject.has("agenda_id")) {
                                    String resAgenda_id = jsonObject.getString("agenda_id");
                                    Log.d("AITL Agenda_id", resAgenda_id);
                                    sqLiteDatabaseHandler.DeletePendigRequest(resAgenda_id, event_id, user_id);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }
        });

    }

    private String getQuery(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }

    public class callService extends AsyncTask<String, Void, JSONObject> {

        int position;
        HashMap<String, String> apiParams;

        public callService(int position, HashMap<String, String> apiParams) {
            this.position = position;
            this.apiParams = apiParams;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                String getParams = getQuery(apiParams);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getParams);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();

                int status = conn.getResponseCode();

                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        System.out.println(sb.toString());
                        return new JSONObject(sb.toString());
                }


                if (conn != null) {
                    try {
                        conn.disconnect();
                    } catch (Exception ex) {
                    }
                }
                return null;
            } catch (IOException ie) {

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            try {
                if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                    str_message_id = jsonObject.getString("message_id");

                    sendPendingMessageRequestCursorTemp.moveToPosition(position);
                    String str_imgArray = sendPendingMessageRequestCursorTemp.getString(sendPendingMessageRequestCursorTemp.getColumnIndex(sqLiteDatabaseHandler.PendingRequestImageArray));
                    ;
                    String res_messageId = sendPendingMessageRequestCursorTemp.getString(sendPendingMessageRequestCursorTemp.getColumnIndex(sqLiteDatabaseHandler.PendingRequestMessageId));
                    ;
                    String regex = "\\[|\\]";
                    str_imgArray = str_imgArray.replaceAll(regex, "");

                    Log.d("AITL CONVERT String", str_imgArray);
                    ArrayList<String> img_arrayList = new ArrayList<>();
                    if (str_imgArray.trim().length() != 0) {
                        String[] parts = str_imgArray.split(",");
                        for (int i = 0; i < parts.length; i++) {
                            parts[i] = parts[i].replaceAll("\\s", "");
                            Log.d("AITL SplidString", "" + parts[i]);

                            img_arrayList.add(parts[i]);
                        }
                    }


                    if (img_arrayList.size() > 0) {
                        for (int j = 0; j < img_arrayList.size(); j++) {
                            isLast = j + 1;
                            UploadePhotoAPI(img_arrayList.get(j), res_messageId, img_arrayList.size());
                            Log.d("AITL IF Upload", img_arrayList.get(j));
                            Log.d("AITL IF ImageSize", "" + img_arrayList.size());
                        }
                    } else {
                        sqLiteDatabaseHandler.DeletePendigMessageRequest(res_messageId);
                        Log.d("AITL MessageId ELSE ", jsonObject.getString("message_id"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
