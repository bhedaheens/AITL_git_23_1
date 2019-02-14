package com.allintheloop.Fragment.ExhibitorFragment;


import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Adapter.Exhibitor.ExhibitorLead_AttendeeSelectedList_adapter;
import com.allintheloop.Bean.ExhibitorListClass.ExhibitorLeadAttendeeSelectedList;
import com.allintheloop.R;
import com.allintheloop.Util.EndlessScrollListener_temp;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Util.WrapContentLinearLayoutManager;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExhibitorLead_AttendeeSelectList extends DialogFragment implements VolleyInterface {


    RecyclerView rv_AttendeeListing;
    ProgressBar progressBar1;
    NestedScrollView scrollView;
    EditText edt_search;
    ExhibitorLead_AttendeeSelectedList_adapter selectedList_adapter;
    ArrayList<ExhibitorLeadAttendeeSelectedList> objectArrayList;
    ArrayList<ExhibitorLeadAttendeeSelectedList> objectArrayList1;
    WrapContentLinearLayoutManager mLayoutManager;
    int total_pages, current_page = 1;
    Dialog dialog;
    SessionManager sessionManager;
    TextView textViewNoDATA;
    Button btn_cancel, btn_add;
    ArrayList<String> member_ids = new ArrayList<>();
    String keyword = "";
    int search_page_count = 1, search_total_pages;
    String tag_search = "0";
    int limit_attdendee = 0;
    EndlessScrollListener_temp endlessScrollListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        return dialog;

    }

    public ExhibitorLead_AttendeeSelectList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exhibitor_lead__attendee_select_list, container, false);
        progressBar1 = (ProgressBar) rootView.findViewById(R.id.progressBar1);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        scrollView = (NestedScrollView) rootView.findViewById(R.id.scrollView);
        textViewNoDATA = (TextView) rootView.findViewById(R.id.textViewNoDATA);
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btn_add = (Button) rootView.findViewById(R.id.btn_add);
        objectArrayList = new ArrayList<>();
        objectArrayList1 = new ArrayList<>();
        sessionManager = new SessionManager(getActivity());
        rv_AttendeeListing = (RecyclerView) rootView.findViewById(R.id.rv_AttendeeListing);
        rv_AttendeeListing.setNestedScrollingEnabled(false);
        mLayoutManager = new WrapContentLinearLayoutManager(getActivity());

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (GlobalData.isNetworkAvailable(getActivity())) {


                        if (edt_search.getText().length() > 0) {

                            current_page = 1;
                            search_page_count = 1;
                            keyword = edt_search.getText().toString();
                            getDataByKeyword();
                            sessionManager.keyboradHidden(edt_search);
                            Log.d("AITL FillDataCalled", keyword);
                        } else {
//                            endlessScrollListener.setpriTotalCount(0);
                            current_page = 1;
                            search_page_count = 1;
                            keyword = "";
                            getAttendeeList();
                            Log.d("AITL ClearDataACTION", keyword);
                            sessionManager.keyboradHidden(edt_search);
                        }
                    } else {
//                        sessionManager.keyboradHidden(edt_search);
//                        if (attendanceArrayList.size() > 0)
//                        {
//
//                            attandeeAdatpter_temp.getFilter().filter(edt_search.getText().toString());
//                        }
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
                        sessionManager.keyboradHidden(edt_search);
                        current_page = 1;
                        search_page_count = 1;
                        keyword = "";
                        Log.d("AITL ClearDataText", keyword);
                        getAttendeeList();

                    } else {
//                        sessionManager.keyboradHidden(edt_search);
//                        if (attendanceArrayList.size() > 0) {
//
//                            attandeeAdatpter_temp.getFilter().filter(edt_search.getText().toString());
//                        }
                    }
                }
            }
        });


        endlessScrollListener = new EndlessScrollListener_temp(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (tag_search.equalsIgnoreCase("0")) {
                    if (total_pages > page) {
                        current_page = page + 1;
                        getLeadMoreData();
                    }
                } else {
                    Log.d("Alka", "Page : " + page);
                    if (search_total_pages > page) {
                        Log.d("Alka ", "tag_search : " + tag_search);
                        Log.d("Alka ", "TOTalPage : " + search_total_pages);
                        search_page_count = page + 1;
                        buttonLoadGulfoodSearch();
                    }
                }

                Log.d("AITL LoadMore", "LoadMore");
            }
        };
        scrollView.setOnScrollChangeListener(endlessScrollListener);
        getAttendeeList();

        if (sessionManager.getFundrising_status().equalsIgnoreCase("0")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getTopBackColor()));

            btn_cancel.setBackgroundDrawable(drawable);
            btn_cancel.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));

            btn_add.setBackgroundDrawable(drawable);
            btn_add.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));


        } else {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.RECTANGLE);
            drawable.setCornerRadius(13.0f);
            drawable.setColor(Color.parseColor(sessionManager.getFunTopBackColor()));

            btn_cancel.setBackgroundDrawable(drawable);
            btn_cancel.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));

            btn_add.setBackgroundDrawable(drawable);
            btn_add.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                member_ids = new ArrayList<>();
                if (objectArrayList1.isEmpty())
                    ToastC.show(getContext(), "Please add Link Representatives");
                else {

                    for (int i = 0; i < objectArrayList1.size(); i++) {

                        member_ids.add(objectArrayList1.get(i).getId());
                    }
                    Log.d("AITL MemberArray", member_ids.toString());
                    addNewRepresentatives();
                }
            }
        });

        return rootView;
    }


    private void getLeadMoreData() {
        progressBar1.setVisibility(View.VISIBLE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getExhibitor_attendeeList, Param.getExhibitorLeadData(sessionManager.getEventId(), sessionManager.getUserId(), current_page, keyword), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    private void getDataByKeyword() {
        objectArrayList = new ArrayList<>();
        progressBar1.setVisibility(View.VISIBLE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getExhibitor_attendeeList, Param.getExhibitorLeadData(sessionManager.getEventId(), sessionManager.getUserId(), search_page_count, keyword), 2, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void buttonLoadGulfoodSearch() {
        progressBar1.setVisibility(View.GONE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getExhibitor_attendeeList, Param.getExhibitorLeadData(sessionManager.getEventId(), sessionManager.getUserId(), search_page_count, keyword), 3, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    private void getAttendeeList() {
        objectArrayList = new ArrayList<>();
        progressBar1.setVisibility(View.GONE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getExhibitor_attendeeList, Param.getExhibitorLeadData(sessionManager.getEventId(), sessionManager.getUserId(), current_page, keyword), 0, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void addNewRepresentatives() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.addNewExhibitorRepresantatives, Param.addNewAttendeeRepresentatibves(sessionManager.getEventId(), sessionManager.getUserId(), member_ids.toString()), 1, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    public boolean getGroupList(ExhibitorLeadAttendeeSelectedList alcd) {

        if (objectArrayList1.size() < limit_attdendee) {
            if (!objectArrayList1.contains(alcd)) {
                objectArrayList1.add(alcd);
                selectedList_adapter.notifyDataSetChanged();
            }

            return true;
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
            ad.setMessage("Maximum Attendee Size Reached");
            ad.setIcon(getResources().getDrawable(R.drawable.ic_warning));
            ad.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            ad.show();
            return false;
        }
    }

    public void removeGroupList(ExhibitorLeadAttendeeSelectedList alcd) {


        for (int i = 0; i < objectArrayList1.size(); i++) {
            ExhibitorLeadAttendeeSelectedList data = objectArrayList1.get(i);
            if (data.getId().equalsIgnoreCase(alcd.getId())) {
                objectArrayList1.remove(data);
                break;
            }
        }

//        for (int i = 0; i < objectArrayList.size(); i++) {
//            ExhibitorLeadAttendeeSelectedList data = objectArrayList.get(i);
//            if (data.getId().equalsIgnoreCase(alcd.getId())) {
//                objectArrayList.set(i, alcd);
//                break;
//            }
//        }
        selectedList_adapter.notifyDataSetChanged();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        tag_search = "0";
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        total_pages = jsonData.getInt("total_page");
                        limit_attdendee = Integer.parseInt(jsonData.getString("limit"));
                        loadData(jsonData);

                    }
                } catch (Exception e) {

                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        dialog.dismiss();
                        GlobalData.exhibitorLeadSettingTab(getActivity());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        progressBar1.setVisibility(View.GONE);
                        tag_search = "1";
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        search_total_pages = jsonData.getInt("total_page");
                        limit_attdendee = Integer.parseInt(jsonData.getString("limit"));
                        loadData(jsonData);

                    }
                } catch (Exception e) {

                }
                break;

            case 3:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        progressBar1.setVisibility(View.GONE);
                        tag_search = "1";
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        search_total_pages = jsonData.getInt("total_page");
                        limit_attdendee = Integer.parseInt(jsonData.getString("limit"));
                        loadData(jsonData);

                    }
                } catch (Exception e) {

                }
                break;
        }
    }

    private void loadData(JSONObject jsonData) {
        try {
            JSONArray jsonArray = jsonData.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject object = jsonArray.getJSONObject(i);

                objectArrayList.add(new ExhibitorLeadAttendeeSelectedList(object.getString("Id")
                        , object.getString("logo")
                        , object.getString("Firstname")
                        , object.getString("Lastname")
                        , object.getString("Title")
                        , object.getString("Company_name")
                        , 2));
            }


            if (objectArrayList.isEmpty()) {
                textViewNoDATA.setVisibility(View.VISIBLE);
                rv_AttendeeListing.setVisibility(View.GONE);
            } else {
                selectedList_adapter = new ExhibitorLead_AttendeeSelectedList_adapter(objectArrayList, getActivity(), ExhibitorLead_AttendeeSelectList.this);
                textViewNoDATA.setVisibility(View.GONE);
                rv_AttendeeListing.setVisibility(View.VISIBLE);
                rv_AttendeeListing.setAdapter(selectedList_adapter);
                rv_AttendeeListing.setLayoutManager(mLayoutManager);
                rv_AttendeeListing.setItemAnimator(new DefaultItemAnimator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
