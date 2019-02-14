package com.allintheloop.Fragment.cms;


import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.ToastC;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Webview_Fragment extends Fragment {

    WebView webView;
    Bundle bundle;
    ProgressDialog progressDialog;
    ImageView img_download;
    String filename;

    List<String> permissionsNeeded;
    List<String> permissionsList;

    public Webview_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).setTitle("");
        webView = (WebView) rootView.findViewById(R.id.webview);
        img_download = (ImageView) rootView.findViewById(R.id.img_download);
        progressDialog = new ProgressDialog(getActivity());


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        //  webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        bundle = getArguments();
        if (bundle.containsKey("Social_url")) {
            img_download.setVisibility(View.GONE);
//            progressDialog.setMessage("Please Wait..");
//            progressDialog.show();
            try {
                String[] file_split = bundle.getString("Social_url").split("\\.(?=[^\\.]+$)");
                String str_exten = file_split[1];
                if (str_exten.equalsIgnoreCase("pdf")) {
                    webView.setWebViewClient(new WebViewController());
                    webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + bundle.getString("Social_url"));

                } else {
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl(bundle.getString("Social_url"));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (bundle.containsKey("document_file")) {
            img_download.setVisibility(View.VISIBLE);
            Log.d("AITL", "BundleWebview" + "http://docs.google.com/gview?embedded=true&url=" + bundle.getString("document_file"));
            progressDialog.setMessage("Please Wait..");
            progressDialog.show();
            webView.setWebViewClient(new WebViewController());
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + bundle.getString("document_file"));

            img_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (isCameraPermissionGranted()) {
                            downloadFile();
                        } else {
                            requestPermission();
                        }
                    } else {
                        downloadFile();
                    }
                }
            });
        }
        return rootView;
    }


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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        }
    }

    public void downloadFile(String uRl, String dir, String filename) {
        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Downloading")
                .setDestinationInExternalPublicDir(dir, filename);
        mgr.enqueue(request);
    }


    private void downloadFile() {
        File storagePath = new File(Environment.getExternalStorageDirectory(), "AllInTheLoop");

        if (!storagePath.exists()) {
            storagePath.mkdirs();
        }
        getActivity().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        getActivity().registerReceiver(onNotificationClick, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));
        downloadFile(bundle.getString("document_file"), storagePath.getPath(), bundle.getString("file_name"));
    }


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
                Toast.makeText(ctxt, "Ummmm...hi!", Toast.LENGTH_LONG).show();
                getActivity().unregisterReceiver(onNotificationClick);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };


    private class WebViewController extends WebViewClient {
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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            Log.d("AITL", "" + url);

            if (url.equalsIgnoreCase("https://www.facebook.com/dialog/return/close?#_=_")) {
                GlobalData.CURRENT_FRAG = GlobalData.Fragment_Stack.pop();
//                        if(GlobalData.CURRENT_FRAG)
                Log.e("aiyaz", "POP up " + GlobalData.CURRENT_FRAG);
                //loadFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStackImmediate();
            }

            if (!progressDialog.isShowing()) {
                progressDialog.show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("AITL onPageFinished", "" + url);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d("AITL ReceivedError", "" + error);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(webView, (Object[]) null);

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


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

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

                    downloadFile();
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


