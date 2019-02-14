package com.allintheloop.Fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.DocumentAdapter;
import com.allintheloop.Bean.AdvertiesMentbottomView;
import com.allintheloop.Bean.AdvertiesmentTopView;
import com.allintheloop.Bean.DefaultLanguage;
import com.allintheloop.Bean.Document;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Document_Fragment extends Fragment implements VolleyInterface {

    public ArrayList<AdvertiesmentTopView> topAdverViewArrayList;
    public ArrayList<AdvertiesMentbottomView> bottomAdverViewArrayList;
    SessionManager sessionManager;
    RecyclerView rv_viewDocument;
    ArrayList<Document> docArrayList;
    DocumentAdapter documentAdapter;
    EditText edt_search;
    Bundle bundle;
    TextView textViewNoDATA;
    String tag = "DocumentList";
    SQLiteDatabaseHandler sqLiteDatabaseHandler;
    String adverties_id = "";
    RelativeLayout relativeLayout_forceLogin, relativeLayout_Data, MainLayout;
    DefaultLanguage.DefaultLang defaultLang;
    TextView txt_doc;
    LinearLayout linear_content;

    public Document_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_document, container, false);
        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        sessionManager = new SessionManager(getActivity());
        defaultLang = sessionManager.getMultiLangString();
        rv_viewDocument = (RecyclerView) rootView.findViewById(R.id.rv_viewDocument);
        rv_viewDocument.setNestedScrollingEnabled(false);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);

        docArrayList = new ArrayList<>();
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        txt_doc = (TextView) rootView.findViewById(R.id.txt_doc);


        topAdverViewArrayList = new ArrayList<>();
        bottomAdverViewArrayList = new ArrayList<>();

        sqLiteDatabaseHandler = new SQLiteDatabaseHandler(getActivity());
        bundle = new Bundle();

        relativeLayout_Data = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_Data);
        MainLayout = (RelativeLayout) rootView.findViewById(R.id.MainLayout);
        linear_content = (LinearLayout) rootView.findViewById(R.id.linear_content);
        relativeLayout_forceLogin = (RelativeLayout) rootView.findViewById(R.id.relativeLayout_forceLogin);
        edt_search.setHint(defaultLang.get16Search());


        if (getTag().equalsIgnoreCase("folder_file")) {
            ((MainActivity) getActivity()).setTitle("");
            ((MainActivity) getActivity()).setDrawerState(false);

        } else {
            ((MainActivity) getActivity()).setTitle("");
            ((MainActivity) getActivity()).setDrawerState(false);

        }

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (GlobalData.isNetworkAvailable(getActivity())) {
                        searchData(edt_search.getText().toString());
                        sessionManager.keyboradHidden(edt_search);
                    } else {
                        sessionManager.keyboradHidden(edt_search);
                        if (docArrayList.size() > 0) {
                            documentAdapter.getFilter().filter(edt_search.getText().toString());
                        }

                    }
                    return true;
                }

                return false;
            }

        });


        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 0) {

                    if (GlobalData.isNetworkAvailable(getActivity())) {
//                        endlessScrollListener.setpriTotalCount(0);
                        getDocumentList();
                        sessionManager.keyboradHidden(edt_search);
                    } else {
                        sessionManager.keyboradHidden(edt_search);
                        if (docArrayList.size() > 0) {

                            documentAdapter.getFilter().filter(editable);
                        }
                    }
                }
            }
        });

        if (sessionManager.isLogin()) {

            relativeLayout_forceLogin.setVisibility(View.GONE);
            relativeLayout_Data.setVisibility(View.VISIBLE);
            getDocumentList();
        } else {
            if (sessionManager.getIsForceLogin().equalsIgnoreCase("1")) {
                relativeLayout_forceLogin.setVisibility(View.VISIBLE);
                relativeLayout_Data.setVisibility(View.GONE);
            } else {
                relativeLayout_forceLogin.setVisibility(View.GONE);
                relativeLayout_Data.setVisibility(View.VISIBLE);
                getDocumentList();
            }
        }
        getAdvertiesment();
        return rootView;
    }


    private void searchData(String keyword) {
        new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_documentList, Param.getDocumentList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), keyword, sessionManager.getFolder_id()), 4, false, this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void pagewiseClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.pageUserClick, Param.pagewiseClick(sessionManager.getEventId(), sessionManager.getUserId(), "", "", "", "OT", sessionManager.getMenuid()), 6, false, this);
        }
    }

    private void getDocumentList() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            if (getTag().equalsIgnoreCase("folder_file")) {
                docArrayList = new ArrayList<>();
                sessionManager.folder_id("0");
                sessionManager.setDocumentHirarchy("Documents / ");
                txt_doc.setText(sessionManager.getDocumentHirarchy());
                Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        try {
//                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                            JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));

                            loadData(jsonObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_documentList, Param.getDocumentList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", "0"), 0, false, this);
                } else {
                    new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_documentList, Param.getDocumentList(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), "", "0"), 0, false, this);
                }
            } else if (getTag().equalsIgnoreCase("file")) {
                docArrayList = new ArrayList<>();
                txt_doc.setText(sessionManager.getDocumentHirarchy());
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.get_documentList, Param.getDocument_folderView(sessionManager.getToken(), sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getFolder_id(), ""), 1, false, this);
            }
        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAttendeeListingData(sessionManager.getEventId(), sessionManager.getUserId(), tag);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {

                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.ListingData)));
                        loadData(jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                edt_search.setVisibility(View.GONE);
                textViewNoDATA.setVisibility(View.VISIBLE);
            }
        }
    }


    private void loadData(JSONObject jsonData) {
        try {
            JSONArray jarrayfolder = jsonData.getJSONArray("folder_list");
            docArrayList = new ArrayList<>();

            for (int i = 0; i < jarrayfolder.length(); i++) {
                JSONObject jsonfile = jarrayfolder.getJSONObject(i);
                docArrayList.add(new Document(jsonfile.getString("doc_id"), jsonfile.getString("doc_type"), jsonfile.getString("title"), jsonfile.getString("document_file"), jsonfile.getString("docicon"), jsonfile.getString("count")));
            }
            if (docArrayList.size() == 0) {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_viewDocument.setVisibility(View.GONE);
            } else {
                edt_search.setVisibility(View.VISIBLE);
                textViewNoDATA.setVisibility(View.GONE);
                rv_viewDocument.setVisibility(View.VISIBLE);
                documentAdapter = new DocumentAdapter(docArrayList, getActivity());
                rv_viewDocument.setLayoutManager(new LinearLayoutManager(getContext()));
                rv_viewDocument.setItemAnimator(new DefaultItemAnimator());
                rv_viewDocument.setAdapter(documentAdapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject mainObject = new JSONObject(volleyResponse.output);

                    if (mainObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = mainObject.getJSONObject("data");
                        if (sqLiteDatabaseHandler.isAttendeeListExist(sessionManager.getEventId(), sessionManager.getUserId(), tag)) {
                            sqLiteDatabaseHandler.deleteListingData(sessionManager.getEventId(), tag, sessionManager.getUserId());
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                        } else {
                            sqLiteDatabaseHandler.insertAttendeeListing(sessionManager.getEventId(), sessionManager.getUserId(), jsonData.toString(), tag);
                        }
                        loadData(jsonData);

                    } else {
                        ToastC.show(getActivity(), mainObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    JSONObject mainObject = new JSONObject(volleyResponse.output);

                    if (mainObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = mainObject.getJSONObject("data");

                        JSONArray jarrayfile = jsonData.getJSONArray("folder_list");


                        for (int i = 0; i < jarrayfile.length(); i++) {
                            JSONObject jsonfile = jarrayfile.getJSONObject(i);
                            docArrayList.add(new Document(jsonfile.getString("doc_id"), jsonfile.getString("doc_type"), jsonfile.getString("title"), jsonfile.getString("document_file"), jsonfile.getString("docicon"), ""));
                        }

                        if (docArrayList.size() != 0) {
                            textViewNoDATA.setVisibility(View.GONE);
                            rv_viewDocument.setVisibility(View.VISIBLE);
                            documentAdapter = new DocumentAdapter(docArrayList, getActivity());
                            rv_viewDocument.setLayoutManager(new LinearLayoutManager(getContext()));
                            rv_viewDocument.setItemAnimator(new DefaultItemAnimator());
                            rv_viewDocument.setAdapter(documentAdapter);
                        } else {
                            textViewNoDATA.setVisibility(View.VISIBLE);
                            rv_viewDocument.setVisibility(View.GONE);
                        }
                    } else {
                        ToastC.show(getActivity(), mainObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                try {
                    JSONObject jbjectNoti = new JSONObject(volleyResponse.output);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                try {
                    JSONObject mainObject = new JSONObject(volleyResponse.output);

                    if (mainObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = mainObject.getJSONObject("data");
                        loadData(jsonData);

                    } else {
                        ToastC.show(getActivity(), mainObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    pagewiseClick();
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) ;
                    {


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

    private void getAdvertiesment() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getAdvertising, Param.getAdvertisment(sessionManager.getEventId(), sessionManager.getMenuid()), 5, false, this);

        } else {
            Cursor cursor = sqLiteDatabaseHandler.getAdvertiesMentData(sessionManager.getEventId(), sessionManager.getMenuid());

            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cursor.getString(cursor.getColumnIndex(sqLiteDatabaseHandler.adverties_Data)));

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
                sessionManager.footerView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "0", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            } else {
                sessionManager.footerView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, bottomAdverViewArrayList, getActivity());
                sessionManager.HeaderView(getContext(), "1", MainLayout, relativeLayout_Data, linear_content, topAdverViewArrayList, getActivity());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
