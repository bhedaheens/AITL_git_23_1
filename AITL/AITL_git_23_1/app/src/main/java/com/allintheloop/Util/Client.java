package com.allintheloop.Util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.allintheloop.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Aiyaz on 24/5/17.
 */

public class Client extends AsyncTask<Object, Object, String> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView textResponse;
    Context context;

    SessionManager sessionManager;
    public static final int BUFFER_SIZE = 2048;

    public Client(Context context) {

        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    protected String doInBackground(Object... arg0) {
        try {
            AppController.getInstance().socketConnected();
            OutputStream os = AppController.socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("operation", "1");
                if (sessionManager.getIsPresantor().equalsIgnoreCase("false")) {
                    jsonObject.put("user_type", "U");

                } else {
                    jsonObject.put("user_type", "P");
                }
                jsonObject.put("p_id", sessionManager.getPresantationId());
                jsonObject.put("gcm_id", sessionManager.getAndroidId());
                jsonObject.put("isandroid", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bw.write(jsonObject.toString());
            Log.d("AITL BW", bw.toString());
            bw.flush();

            Log.d("AITL DataJSon", jsonObject.toString());
            InputStream is = AppController.socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            System.out.println("Message received from the server : " + message);
            response = message;
            return response;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
            if (sessionManager.getIsPresantor().equalsIgnoreCase("false")) {
                GlobalData.SocketUpdate(context);
            }
            ////
        } catch (IOException e) {
            e.printStackTrace();
            response = "IOException: " + e.toString();
            if (sessionManager.getIsPresantor().equalsIgnoreCase("false")) {
                GlobalData.SocketUpdate(context);
            }
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("AITL RESULT", "" + result);
        if (result != null) {
            if (result.equalsIgnoreCase("200")) {
                GlobalData.updatePresantation(context);
            }
        } else {
            GlobalData.SocketUpdate(context);
        }
        super.onPostExecute(result);
    }
}