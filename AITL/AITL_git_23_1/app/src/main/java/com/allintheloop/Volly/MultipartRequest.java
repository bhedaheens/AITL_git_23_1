package com.allintheloop.Volly;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.allintheloop.Util.GlobalData;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private final Map<String, String> mStringPart;
    private final Map<String, File> mFilePart;
    private MultipartEntityBuilder mBuilder;

    public MultipartRequest(String url, Map<String, File> mFilePart, Map<String, String> mStringPart, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mBuilder = MultipartEntityBuilder.create();
        mListener = listener;
        this.mFilePart = mFilePart;
        this.mStringPart = mStringPart;
        buildMultipartEntity();
        setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        setShouldCache(false);
    }

    private void buildMultipartEntity() {
        try {
            for (Map.Entry<String, File> entry : mFilePart.entrySet()) {
                Log.e(entry.getKey(), entry.getValue().toString());
                //entity.addPart(entry.getKey(), new FileBody(entry.getValue()));
                if (!GlobalData.isNullString(entry.getValue().toString())) {
                    mBuilder.addBinaryBody(entry.getKey(), entry.getValue(), ContentType.create("image"), entry.getValue().getName());
                }
            }
            for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
                Log.e(entry.getKey(), entry.getValue());
                mBuilder.addTextBody(entry.getKey(), entry.getValue());
            }

            mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            mBuilder.setLaxMode().setBoundary("xx").setCharset(Charset.forName("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (headers == null || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
        }
        headers.put("Accept", "application/json");
        return headers;
    }

    @Override
    public String getBodyContentType() {
        String contentTypeHeader = mBuilder.build().getContentType().getValue();
        return contentTypeHeader;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mBuilder.build().writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(new String(response.data, "UTF-8"), getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // fuck it, it should never happen though
            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
