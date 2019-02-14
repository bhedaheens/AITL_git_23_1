package com.allintheloop.Fragment.cms;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.sax.Element;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.Document;
import com.allintheloop.Bean.HomeData.DashboardItemList;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class Test1_Fragment extends Fragment implements VolleyInterface {

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    ArrayList<DashboardItemList> ArrayItem;
    WebView webView;
    String WebStr = "", Module = "", ImgStr = "", ImgUrl = "", type = "";
    RelativeLayout layout;
    ImageView imageView;
    String myHtmlString, str_url;
    SessionManager sessionManager;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    RelativeLayout relativeLayout_Data, MainLayout;
    DefaultLanguage.DefaultLang defaultLang;
    LinearLayout linear_content;

    public Test1_Fragment() {
        // Required empty public constructor
    }


    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cms, container, false);
        sessionManager = new SessionManager(getActivity());

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);
        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        GlobalData.currentModuleForOnResume = GlobalData.cmsModuleid;
        webView = (WebView) rootView.findViewById(R.id.webViewContent);
        webView.getSettings().setDefaultTextEncodingName("utf-8");
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                clickEvent(url);
                return true;
            }
        });


//        webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "Android");
        webView.setWebChromeClient(new MyWebChromeclient());
        sessionManager.isNoteCms = "1";
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        imageView = (ImageView) rootView.findViewById(R.id.Banner);

        if (sessionManager.isLogin()) {
            pagewiseClick();
        }
        getAdvertiesment();
        return rootView;

    }

    private void deepLinkdata(String url) {

        Uri uri = Uri.parse(url);
        String menuId = GlobalData.getDataFromBaseEncrypt(uri.getQueryParameter("node_main"));
        String moduleId = GlobalData.getDataFromBaseEncrypt(uri.getQueryParameter("node_sub"));
        String neventId = GlobalData.getDataFromBaseEncrypt(uri.getQueryParameter("nevent"));
        if (sessionManager.getEventId().equalsIgnoreCase(neventId)) {
            ((MainActivity) getActivity()).deepLinkRedirectionMethod(menuId, moduleId, false);
        }
    }


    private void getAdvertiesment(JSONObject jsonObject) {

        try {
            JSONObject jsonObjectAdavertiesment = jsonObject.getJSONObject("data");
            JSONArray jArrayHeader = jsonObjectAdavertiesment.getJSONArray("header");
            JSONArray jArrayFooter = jsonObjectAdavertiesment.getJSONArray("footer");

            topAdverViewArrayList = new ArrayList<>();
            bottomAdverViewArrayList = new ArrayList<>();
            for (int i = 0; i < jArrayHeader.length(); i++) {
                JSONObject index = jArrayHeader.getJSONObject(i);
                topAdverViewArrayList.add(new AdvertiesmentTopView(index.getString("image"), index.getString("url"), index.getString("id")));
            }

            for (int i = 0; i < jArrayFooter.length(); i++) {
                JSONObject index = jArrayFooter.getJSONObject(i);
                bottomAdverViewArrayList.add(new AdvertiesMentbottomView(index.getString("image"), index.getString("url"), index.getString("id")));
            }
            if (jsonObjectAdavertiesment.getString("show_sticky").equalsIgnoreCase("1")) {
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            }

//            sessionManager.footerView(getContext(),"1",MainLayout,relativeLayout_Data,linear_content,bottomAdverViewArrayList,getActivity());
//            sessionManager.HeaderView(getContext(),"1",MainLayout,relativeLayout_Data,linear_content,topAdverViewArrayList,getActivity());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertismentForCms(sessionManager.getEventId(), sessionManager.get_cmsId(), "1"), 1, false, this);

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.adverties_Data)));
                        Log.d("AITL  Map Oflline", jsonObject.toString());
                        getAdvertiesment(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCmsPage();
        if (GlobalData.currentModuleForOnResume.equalsIgnoreCase(GlobalData.cmsModuleid)) {
            ((MainActivity) getActivity()).getUpdatedDataFromParticularmodule(GlobalData.cmsModuleid);
        }

    }

    private void loadData(JSONObject jsonData) {

        try {

            Module = jsonData.getString("fecture_module");
            WebStr = jsonData.getString("Description");
            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///asset/Lato-Light.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
            String pas = "</body></html>";

            myHtmlString = pish + WebStr + pas;
            if (jsonData.has("url")) {
                str_url = jsonData.getString("url");
                if (!(str_url.equalsIgnoreCase(""))) {
                    String extension = MimeTypeMap.getFileExtensionFromUrl(str_url);

                    if (extension != null) {

                        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        if (type != null) {
                            if (type.equalsIgnoreCase("application/pdf")) {
                                webView.getSettings().setBuiltInZoomControls(true);
                                webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800));
                                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + str_url);
                            } else {
                                loadUrl();
                            }
                        } else {
                            setUrl();
                        }
                    } else {
                        loadUrl();
                    }
                } else {
                    loadUrl();
                }
            } else {
                loadUrl();
            }
            ImgStr = jsonData.getString("Images");
            ImgUrl = MyUrls.Imgurl + ImgStr;
            Log.d("AITL", ImgUrl);

            Glide.with(getActivity())
                    .load(ImgUrl)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            imageView.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            imageView.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).into(imageView);


            Log.d("AITL Module", Module);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUrl() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        webView.setWebViewClient(new SSLTolerentWebViewClient());
        webView.loadUrl(str_url);
    }

    private void loadUrl() {
        LinearLayout.LayoutParams params;
        if (myHtmlString.contains("iframe")) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        webView.setLayoutParams(params);
        webView.loadDataWithBaseURL("file:///asset", myHtmlString, "text/html; charset=utf-8", "utf-8", null);
    }


    private void getCmsPage() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            ArrayItem = new ArrayList<>();
            Cursor cursor = sqLiteDatabaseHandler.getCmsPageData(sessionManager.getEventId(), sessionManager.get_cmsId());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.CmsData)));
                        Log.d("AITL ", "CMSDATA Oflline " + jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getCms_Page, Param.get_CmsPage(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.get_cmsId(), sessionManager.getToken()), 0, false, this);
            }
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getCms_Page, Param.get_CmsPage(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.get_cmsId(), sessionManager.getToken()), 0, false, this);
        } else {
            ArrayItem = new ArrayList<>();
            Cursor cursor = sqLiteDatabaseHandler.getCmsPageData(sessionManager.getEventId(), sessionManager.get_cmsId());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.CmsData)));
                        Log.d("AITL ", "CMSDATA Oflline " + jsonObject.toString());
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                ToastC.show(getActivity(), getString(R.string.noInernet));
            }
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {

        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject MainJson = new JSONObject(volleyResponse.output);
                    if (MainJson.getString("success").equalsIgnoreCase("true")) {
                        ArrayItem = new ArrayList<>();
                        JSONObject jsonData = MainJson.getJSONObject("data");
                        JSONArray jsonArray = jsonData.getJSONArray("cms_details");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject index = jsonArray.getJSONObject(i);
                            if (sqLiteDatabaseHandler.isCmsPageExist(sessionManager.getEventId(), sessionManager.get_cmsId())) {
                                sqLiteDatabaseHandler.deleteCmsPageData(sessionManager.getEventId(), sessionManager.get_cmsId());
                                sqLiteDatabaseHandler.insertCmsPage(sessionManager.getEventId(), sessionManager.get_cmsId(), index.toString());
                            } else {
                                sqLiteDatabaseHandler.insertCmsPage(sessionManager.getEventId(), sessionManager.get_cmsId(), index.toString());
                            }
                            loadData(index);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
//                    pagewiseClick();
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Log.d("AITL Advertiesment", jsonObject.toString());

                        if (sqLiteDatabaseHandler.isAdvertiesMentExist(sessionManager.getEventId(), sessionManager.getMenuid())) {
                            sqLiteDatabaseHandler.deleteAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        } else {
                            sqLiteDatabaseHandler.insertAdvertiesmentData(sessionManager.getEventId(), sessionManager.getMenuid(), jsonObject.toString());
                        }

                        getAdvertiesment(jsonObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(webView, (Object[]) null);
            webView.loadUrl("about:blank");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
        } catch (InvocationTargetException ite) {
            ite.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        }
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.OtherPageWiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "CMS", "", "", sessionManager.get_cmsId(), ""), 5, false, this);
        }
    }

    private boolean clickEvent(String url) {
        if (Uri.parse(url).getScheme().equalsIgnoreCase(GlobalData.deeplinkHostName)) {
            deepLinkdata(url);
            return true;
        } else {
            if (url.startsWith("tel:")) {
                Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                startActivity(tel);
                return true;
            } else if (url.startsWith("mailto:")) {
                Intent mail = new Intent(Intent.ACTION_SEND);
                mail.setType("application/octet-stream");
                mail.putExtra(Intent.EXTRA_EMAIL, new String[]{"email address"});
                mail.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                mail.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(mail);
                return true;
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(url));
                startActivity(browserIntent);
                return true;
            }
        }

    }

    private class MyWebChromeclient extends WebChromeClient {


        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {

            WebView newWebView = new WebView(getActivity());
            view.addView(newWebView);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
            newWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    clickEvent(url);
                    return true;
                }
            });
            return true;
        }
    }

//    private void pagewiseClick() {
//        if (GlobalData.isNetworkAvailable(getActivity()))
//        {
//            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.OtherPageWiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "CMS", "","",sessionManager.get_cmsId(),""), 5, false, this);
//        }
//    }

    private class SSLTolerentWebViewClient extends WebViewClient {
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}