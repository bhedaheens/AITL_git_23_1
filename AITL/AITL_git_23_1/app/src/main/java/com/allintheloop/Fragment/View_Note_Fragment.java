package com.allintheloop.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.ViewNoteAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.ViewNote;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.allintheloop.Util.GlobalData.UpdateNoteListing;

/**
 * A simple {@link Fragment} subclass.
 */
public class View_Note_Fragment extends Fragment implements VolleyInterface {


    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public ViewNoteAdapter viewNoteAdapter;
    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    RecyclerView recyclerView;
    Button btn_add;
    SessionManager sessionManager;
    EditText search;
    String note_id, heading, desc, created_at, organizor_id, title, Menu_id, Module_id, is_custom_title;
    ArrayList<ViewNote> noteArrayList;
    FragmentManager fm;
    TextView txt_nodata;
    Bundle bundle;
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String adverties_id = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    LinearLayout linear_askNotes, linear_main, linearSearch;
    DefaultLanguage.DefaultLang defaultLang;
    Button btn_downloadNotes;
    List<String> permissionsNeeded;
    List<String> permissionsList;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @SuppressLint("LongLogTag")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("AITL NoteListingCalled", "Reciver Called : ");
            // TODO Auto-generated method stub
            getNoteList();
        }
    };

    public View_Note_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_view_note, container, false);

//        mAdView = (AdView)rootView.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_viewNote);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_downloadNotes = (Button) rootView.findViewById(R.id.btn_downloadNotes);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);


        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();


        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        linear_askNotes = (LinearLayout) rootView.findViewById(R.id.linear_askNotes);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_main = (LinearLayout) rootView.findViewById(R.id.linear_main);
        linearSearch = (LinearLayout) rootView.findViewById(R.id.linearSearch);

        search = (EditText) rootView.findViewById(R.id.search);
        txt_nodata = (TextView) rootView.findViewById(R.id.txt_nodata);
        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();

        btn_add.setText(defaultLang.get6AddNotes());

        noteArrayList = new ArrayList<>();
        fm = getFragmentManager();

        setViewButton();


        btn_downloadNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(getActivity())) {


                    if (Build.VERSION.SDK_INT >= 23) {
                        if (isCameraPermissionGranted()) {
                            downloadPDF();
                        } else {
                            requestPermission();
                        }
                    } else {
                        downloadPDF();
                    }
                } else {
                    ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
                }
            }
        });

        if (sessionManager.isLogin()) {
            btn_add.setVisibility(View.VISIBLE);
            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            linear_askNotes.setVisibility(View.VISIBLE);
            search.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            btn_add.setVisibility(View.VISIBLE);
            txt_nodata.setVisibility(View.GONE);
            getNoteList();
        } else {
            btn_add.setVisibility(View.GONE);
            Log.d("AITL IS FORCELOGIN ", sessionManager.getIsForceLogin());
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
                linear_askNotes.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                linear_askNotes.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                txt_nodata.setVisibility(View.VISIBLE);
                txt_nodata.setText("Login or Sign Up to proceed. To sign up or login tap the Sign Up button on the top right of the screen.");
            }

        }


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("edit", "0");
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.NotesDetail_Fragment;
                ((MainActivity) getActivity()).loadFragment(bundle);

            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (noteArrayList.size() > 0) {
                    viewNoteAdapter.getFilter().filter(s);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (noteArrayList.size() > 0) {
                    viewNoteAdapter.getFilter().filter(s);
                }

            }
        });

        getAdvertiesment();

        return rootView;
    }

    private void setViewButton() {
        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

            btn_add.setBackgroundDrawable(drawable);
            btn_downloadNotes.setBackgroundDrawable(drawable);
            btn_add.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
            btn_downloadNotes.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));

            btn_add.setBackgroundDrawable(drawable);
            btn_downloadNotes.setBackgroundDrawable(drawable);
            btn_add.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
            btn_downloadNotes.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }
    }

    private void downloadPDF() {
        if (GlobalData.checkForUIDVersion())
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.downloadNotesUid, Param.getExhibitorLeadDataOffline(sessionManager.getEventId(), sessionManager.getUserId()), 2, true, this);
        else
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.downloadNotes, Param.getExhibitorLeadDataOffline(sessionManager.getEventId(), sessionManager.getUserId()), 2, true, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(UpdateNoteListing));
        setViewButton();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 5, false, this);
        }
    }

    private void getNoteList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            noteArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getNotesListingData(sessionManager.getEventId(), sessionManager.getUserId());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONArray jsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.NotesData)));
                        Log.d("AITL  Oflline", jsonArray.toString());
                        loadData(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.View_Notes, Param.View_Note(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
            } else {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.View_Notes, Param.View_Note(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getUserId()), 0, false, this);
            }

        } else {
            noteArrayList.clear();
            Cursor cursor = sqLiteDatabaseHandler.getNotesListingData(sessionManager.getEventId(), sessionManager.getUserId());
            Log.d("AITL Cursor Size", "" + cursor.getCount());
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONArray jsonArray = new JSONArray(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.NotesData)));
                        Log.d("AITL  Oflline", jsonArray.toString());
                        loadData(jsonArray);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                search.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.VISIBLE);

                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL", "ViewNoteResponse" + jsonObject.toString());

                    if (jsonObject.getString("success").equalsIgnoreCase(Param.SUCCESS_CODE)) {
                        if (jsonObject.has("data")) {
                            JSONObject jsonData = jsonObject.getJSONObject("data");
                            JSONArray jsonArray = jsonData.getJSONArray("note_list");

                            noteArrayList = new ArrayList<>();
                            Log.d("AITL DATA", "" + sqLiteDatabaseHandler.getEventListData());

                            if (sqLiteDatabaseHandler.isNotesListExist(sessionManager.getEventId(), sessionManager.getUserId())) {
                                sqLiteDatabaseHandler.deleteNotesListingData(sessionManager.getEventId(), sessionManager.getUserId());
                                sqLiteDatabaseHandler.insertNotesListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString());
                            } else {
                                sqLiteDatabaseHandler.insertNotesListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString());
                            }

                            loadData(jsonArray);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    pagewiseClick();
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
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {
                        Log.d("AITL PDFFILE", jsonObject.toString());
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1.getString("pdf_name").isEmpty()) {
                            ToastC.show(getActivity(), "No Notes Available");
                        } else {


                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(jsonObject1.getString("pdf_name")));
                            request.setTitle(sessionManager.getFirstName() + " " + sessionManager.getLastName());
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                request.allowScanningByMediaScanner();
                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            }
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, sessionManager.getEventName() + "_Notes_" + sessionManager.getUserId() + ".pdf");
                            DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                            manager.enqueue(request);

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 1, false, this);

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
                sessionManager.NotefooterView(getContext(), "1", MainLayout, relativeLayout_Data, linear_askNotes, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_main, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_Data, linear_askNotes, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_main, topAdverViewArrayList, getActivity());
            }

//            sessionManager.footerView(getContext(),"1",MainLayout,relativeLayout_Data,linear_askNotes,bottomAdverViewArrayList,getActivity());
//            sessionManager.HeaderView(getContext(),"1",MainLayout,relativeLayout_Data,linear_main,topAdverViewArrayList,getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                note_id = object.getString("Id");
                heading = object.getString("Heading");
                desc = object.getString("Description");
                created_at = object.getString("Created_at");
                organizor_id = object.getString("Organisor_id");
                title = object.getString("title");
                Menu_id = object.getString("Menu_id");
                Module_id = object.getString("Module_id");
                is_custom_title = object.getString("is_custom_title");
                String is_cms = object.getString("is_cms");
                noteArrayList.add(new ViewNote(note_id, heading, desc, created_at, organizor_id, title, Menu_id, Module_id, is_custom_title, is_cms));
            }
            viewNoteAdapter = new ViewNoteAdapter(noteArrayList, getActivity(), fm);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            if (noteArrayList.size() == 0) {
                btn_downloadNotes.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

            } else {
                btn_downloadNotes.setVisibility(View.VISIBLE);
                btn_add.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(viewNoteAdapter);
            }
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

                    downloadPDF();
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