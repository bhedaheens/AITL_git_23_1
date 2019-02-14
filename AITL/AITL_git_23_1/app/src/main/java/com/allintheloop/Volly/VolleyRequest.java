package com.allintheloop.Volly;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.allintheloop.Util.AppController;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyCustomProgressDialog;
import com.allintheloop.Util.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 2/25/2015.
 */
public class VolleyRequest implements Response.Listener<String>, Response.ErrorListener {

    private static final String TAG = "VolleyRequest";
    private String mUrl = null;
    // private ProgressDialog mProgressDialog;
    private MyCustomProgressDialog mProgressDialog;
    SessionManager sessionManager;
    private Activity activity;
    private Context context;
    private Map<String, String> mParams = new HashMap<String, String>();
    private Map<String, File> mParamFile = new HashMap<String, File>();
    private boolean isShowProgressDialog;
    private int type;
    private VolleyInterface objInterface;

    public VolleyRequest(Activity activity, Method requestType, String url, Map<String, String> params, int type, boolean isShowDialog, VolleyInterface objVolley) {
        this.activity = activity;
        sessionManager = new SessionManager(activity);
        params.put("lang_id", sessionManager.getLangId());
        this.mParams = getHTMLEntityData(params);
        Log.e("Volly", "" + mParams);
        this.isShowProgressDialog = isShowDialog;
        this.type = type;
        this.objInterface = objVolley;
        mUrl = url;
        switch (requestType) {
            case GET:
                if (params != null) {
                    mUrl = GlobalData.getURLFromParams(mUrl, mParams);
                }
                Log.e(TAG, "URL==>" + mUrl);
                callGetWebApiRequest();
                break;
            case POST:
                Log.e(TAG, "URL==>" + mUrl);
                callPostWebApiRequest();
                break;
            case DELETE:
                Log.e(TAG, "URL==>" + mUrl);
                callDeleteWebApiRequest();
                break;
            default:
                break;
        }
    }

    public VolleyRequest(Context context, Method requestType, String url, Map<String, String> params, int type, boolean isShowDialog, VolleyInterface objVolley) {
        this.context = context;
        sessionManager = new SessionManager(context);
        params.put("lang_id", sessionManager.getLangId());
        this.mParams = getHTMLEntityData(params);
//        Log.d("HTML ENTITYDATA",""+getHTMLEntityData(params));
        Log.d("AITL Volly", "" + getHTMLEntityData(params));
        this.isShowProgressDialog = isShowDialog;
        this.type = type;
        this.objInterface = objVolley;
        mUrl = url;
        switch (requestType) {
            case GET:
                if (params != null) {
                    mUrl = GlobalData.getURLFromParams(mUrl, params);
                }
                Log.e(TAG, "URL==>" + mUrl);
                callGetWebApiRequest();
                break;
            case POST:
                Log.e(TAG, "URL==>" + mUrl);
                callPostWebApiRequest();
                break;
            case DELETE:
                Log.e(TAG, "URL==>" + mUrl);
                callDeleteWebApiRequest();
                break;
            default:
                break;
        }
    }


    public VolleyRequest(Method requestType, String url, Map<String, String> params, int type, boolean isShowDialog, VolleyInterface objVolley) {
        this.mParams = getHTMLEntityData(params);
        this.isShowProgressDialog = isShowDialog;
        this.type = type;
        this.objInterface = objVolley;
        mUrl = url;

        switch (requestType) {
            case GET:
                if (params != null) {
                    mUrl = GlobalData.getURLFromParams(mUrl, mParams);
                }
                Log.e(TAG, "URL==>" + mUrl);
                callGetWebApiRequest();
                break;
            case POST:
                Log.e(TAG, "URL==>" + mUrl);
                callPostWebApiRequest();
                break;
            case DELETE:
                Log.e(TAG, "URL==>" + mUrl);
                callDeleteWebApiRequest();
                break;
            default:
                break;
        }
    }


    public VolleyRequest(Activity activity, String url, Map<String, File> fileParam, Map<String, String> params, int type, boolean isShowDialog, VolleyInterface objVolley) {
        sessionManager = new SessionManager(activity);
        params.put("lang_id", sessionManager.getLangId());
        this.activity = activity;
        this.mParams = getHTMLEntityData(params);
        Log.d("HTML ENTITYDATA", "" + mParams);
        this.isShowProgressDialog = isShowDialog;
        this.type = type;
        mUrl = url;
        this.objInterface = objVolley;
        showProgress();
        Log.e(TAG, "URL==>" + mUrl);
        MultipartRequest multipartRequest = new MultipartRequest(url, fileParam, mParams, VolleyRequest.this, VolleyRequest.this);
        AppController.getInstance().addToRequestQueue(multipartRequest, mUrl);
    }


    public VolleyRequest(String url, Map<String, File> fileParam, Map<String, String> params, int type, boolean isShowDialog, VolleyInterface objVolley) {
        this.mParams = getHTMLEntityData(params);
//        Log.d("HTML ENTITYDATA",""+getHTMLEntityData(params));
        this.isShowProgressDialog = isShowDialog;
        this.type = type;
        mUrl = url;
        this.objInterface = objVolley;
        showProgress();
        Log.e(TAG, "URL==>" + mUrl);
        MultipartRequest multipartRequest = new MultipartRequest(url, fileParam, mParams, VolleyRequest.this, VolleyRequest.this);
        AppController.getInstance().addToRequestQueue(multipartRequest, mUrl);
    }


    /**
     * Making callGetWebApiRequest request
     */
    private synchronized void callGetWebApiRequest() {
        showProgress();
        StringRequest strReq = new StringRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "GET==>" + response);
                dismissProgress();
                try {
                    if (!GlobalData.isNullString(response)) {
                        objInterface.getVolleyRequestResponse(new VolleyRequestResponse(response.trim(), type));
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                volleyErrorHandler(error);
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(5000, 4, 2));
        strReq.setShouldCache(false);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, mUrl);
    }

    /**
     * Making callPostWebApiRequest request
     */
    private synchronized void callPostWebApiRequest() {
        showProgress();
        StringRequest strReq = new StringRequest(Request.Method.POST, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e(TAG, "POST==>" + response.trim());
                dismissProgress();
                try {
                    if (!GlobalData.isNullString(response)) {
                        objInterface.getVolleyRequestResponse(new VolleyRequestResponse(response.trim(), type));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Post Exception==>" + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                volleyErrorHandler(error);
            }
        }) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                return mParams;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(5000, 4, 2));
        strReq.setShouldCache(false);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, mUrl);
    }

    /**
     * Making callDeleteWebApiRequest request
     */
    private synchronized void callDeleteWebApiRequest() {
        showProgress();
        StringRequest strReq = new StringRequest(Request.Method.DELETE, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "DELETE==>" + response);
                dismissProgress();
                try {
                    if (!GlobalData.isNullString(response)) {
                        objInterface.getVolleyRequestResponse(new VolleyRequestResponse(response.trim(), type));
                    }
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgress();
                volleyErrorHandler(error);
            }
        });

        strReq.setRetryPolicy(new DefaultRetryPolicy(5000, 4, 2));
        strReq.setShouldCache(false);
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, mUrl);
    }

    //Multipart Response
    @Override
    public void onResponse(String response) {
        Log.e(TAG, "Multipart POST==>" + response.trim());
        dismissProgress();
        try {
            if (!GlobalData.isNullString(response)) {
                objInterface.getVolleyRequestResponse(new VolleyRequestResponse(response, type));
            }
        } catch (Exception e) {
        }
    }

    //Multipart Error
    @Override
    public void onErrorResponse(VolleyError error) {
        dismissProgress();
        volleyErrorHandler(error);
    }

    private void showProgress() {
        if (isShowProgressDialog) {
            if (!(activity.isFinishing()))
                mProgressDialog = GlobalData.getProgressDialog(activity);

//            GlobalData.getProgressDialog(activity);
        }
    }

    private void dismissProgress() {
        if (isShowProgressDialog) {
            GlobalData.dismissDialog(mProgressDialog);
        }
    }


    private void volleyErrorHandler(VolleyError error) {
        try {
//            if (error instanceof TimeoutError)
//            {
//                ToastC.show(activity, activity.getResources().getString(R.string.toast_time_out));
//            } else if (error instanceof AuthFailureError) {
//                //TODO
//                ToastC.show(activity, activity.getResources().getString(R.string.toast_something_wrong));
//            } else if (error instanceof ServerError) {
//                //TODO
//                ToastC.show(activity, activity.getResources().getString(R.string.toast_try_after_sometimes));
//            }else if (error instanceof ParseError) {
//                //TODO
//                ToastC.show(activity, activity.getResources().getString(R.string.toast_something_wrong));
//            } else if (error instanceof NoConnectionError) {
//                //TODO
//                ToastC.show(activity, activity.getResources().getString(R.string.toast_something_wrong));
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public enum Method {
        POST, GET, DELETE
    }


    public Map<String, String> getHTMLEntityData(Map<String, String> params) {

        Map<String, String> temp = new HashMap<String, String>();
        Iterator entries = params.entrySet().iterator();
        int i = 0;
        while (entries.hasNext()) {
            try {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
//            temp.put(key, TextUtils.htmlEncode(value));
                temp.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return temp;
    }


    public String extractText(String html) {
        html = html.replaceAll("<(.*?)\\>", "");
        html = html.replaceAll("<(.*?)\\\n", "");
        html = html.replaceFirst("(.*?)\\>", "");
        html = html.replaceAll(" ", "");
        html = html.replaceAll("&", "");
        html = html.replaceAll("alert()", "");
        html = html.replaceAll("alert(\\/)", "");
        html = html.replaceAll("<marquee>", "");
        html = html.replaceAll("marquee", "");
        html = html.replaceAll("</marquee>", "");
        html = html.replaceAll("onfocus", "");
        html = html.replaceAll("onclick", "");
        html = html.replaceAll("onblur", "");
        html = html.replaceAll("autofocus", "");
        html = html.replaceAll("javascript", "");
        html = html.replaceAll("onerror", "");
        html = html.replaceAll("onscroll", "");
        html = html.replaceAll("onload\\/", "");
        html = html.replaceAll("oninput", "");
        html = html.replaceAll("onpageshow", "");
        html = html.replaceAll("window.location", "");
        html = html.replaceAll("onwebkittransitionend", "");
        return html.trim();
    }

}
