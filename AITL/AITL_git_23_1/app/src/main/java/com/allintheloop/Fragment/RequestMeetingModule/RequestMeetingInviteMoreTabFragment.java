package com.allintheloop.Fragment.RequestMeetingModule;


import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.allintheloop.Adapter.RequestMetting.Adapter_requestMeetingInviteMore_fragment;
import com.allintheloop.Bean.Attendee.AttendeeInviteMoreList;
import com.allintheloop.MainActivity;
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
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestMeetingInviteMoreTabFragment extends Fragment implements VolleyInterface {


    TextView txt_inviteMore;
    SessionManager sessionManager;
    EditText edt_search;
    RecyclerView rv_viewinviteMore;
    int total_pages, page_count = 1;
    int search_page_count = 1, search_total_pages;

    WrapContentLinearLayoutManager mLayoutManager;
    EndlessScrollListener_temp endlessScrollListener;
    NestedScrollView nestedScrollView;
    Adapter_requestMeetingInviteMore_fragment adapterInviteMore;

    ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> moreDataArrayList;
    AttendeeInviteMoreList attendeeInviteMoreList;
    ProgressBar progressBar;
    String tag_search = "0";
    String keyword = "";
    ArrayList<String> idArray;
    TextView txt_noAttendee;

    public RequestMeetingInviteMoreTabFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reques_meeting_invite_more_tab, container, false);

        ((MainActivity) getActivity()).setTitle("");
        ((MainActivity) getActivity()).setDrawerState(false);

        txt_inviteMore = (TextView) rootView.findViewById(R.id.txt_inviteMore);
        txt_noAttendee = (TextView) rootView.findViewById(R.id.txt_noAttendee);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        rv_viewinviteMore = (RecyclerView) rootView.findViewById(R.id.rv_viewinviteMore);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nestedScrollView);

        sessionManager = new SessionManager(getActivity());
        moreDataArrayList = new ArrayList<>();
        idArray = new ArrayList<>();

        if (sessionManager.getFundrising_status().equalsIgnoreCase("1")) {
            txt_inviteMore.setBackgroundColor(Color.parseColor(sessionManager.getFunBackColor()));
            txt_inviteMore.setTextColor(Color.parseColor(sessionManager.getFunTopTextColor()));
        } else {
            txt_inviteMore.setBackgroundColor(Color.parseColor(sessionManager.getTopBackColor()));
            txt_inviteMore.setTextColor(Color.parseColor(sessionManager.getTopTextColor()));
        }


        adapterInviteMore = new Adapter_requestMeetingInviteMore_fragment(moreDataArrayList, getContext());
        rv_viewinviteMore.setAdapter(adapterInviteMore);
        mLayoutManager = new WrapContentLinearLayoutManager(getActivity());
        rv_viewinviteMore.setLayoutManager(mLayoutManager);
        rv_viewinviteMore.setItemAnimator(new DefaultItemAnimator());

        txt_inviteMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                idArray.clear();

                ArrayList<AttendeeInviteMoreList.AttendeeinviteMoreData> selectedList = adapterInviteMore.getSelectedList();
                for (int i = 0; i < selectedList.size(); i++) {
                    idArray.add(selectedList.get(i).getId());

                }

                if (idArray.size() > 0) {
                    inviteMoreClick();
                } else {
                    ToastC.show(getActivity(), "Please select at least one attendee");
                }

            }
        });

        endlessScrollListener = new EndlessScrollListener_temp(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (tag_search.equalsIgnoreCase("0")) {
                    if (total_pages > page) {
                        page_count = page + 1;
                        loadMore();
                    }
                } else {
                    if (search_total_pages > page) {
                        search_page_count = page + 1;
                        buttonLoadGulfoodSearch();
                    }
                }


            }
        };
        nestedScrollView.setOnScrollChangeListener(endlessScrollListener);
        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (GlobalData.isNetworkAvailable(getActivity())) {


                        if (edt_search.getText().length() > 0) {

                            page_count = 1;
                            search_page_count = 1;
                            keyword = edt_search.getText().toString();
                            getAttendeeeByKeyword();
                            sessionManager.keyboradHidden(edt_search);
                            Log.d("AITL FillDataCalled", keyword);
                        } else {
//                            endlessScrollListener.setpriTotalCount(0);
                            page_count = 1;
                            search_page_count = 1;
                            keyword = "";
                            getData();
                            moreDataArrayList.clear();
                            Log.d("AITL ClearDataACTION", keyword);
                            sessionManager.keyboradHidden(edt_search);
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
                        sessionManager.keyboradHidden(edt_search);
                        page_count = 1;
                        search_page_count = 1;
                        keyword = "";
                        Log.d("AITL ClearDataText", keyword);
                        moreDataArrayList.clear();
                        getData();

                    }
                }
            }
        });

        getData();
        return rootView;

    }


    private void inviteMoreClick() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.inviteAttendee, Param.inviteMoreAttendee(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getRequestMeetingId(), sessionManager.getRequestDate(), sessionManager.getRequestTime(), sessionManager.getRequestLocation(), idArray.toString()), 4, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    private void loadMore() {
        progressBar.setVisibility(View.VISIBLE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getinviteMoreData, Param.getInviteMoreData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getRequestMeetingId(), page_count, ""), 0, false, this);
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
        }
    }

    private void getData() {
        progressBar.setVisibility(View.GONE);
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getinviteMoreData, Param.getInviteMoreData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getRequestMeetingId(), page_count, ""), 0, false, this);
        } else {
            ToastC.show(getActivity(), getResources().getString(R.string.noInernet));
        }
    }

    private void getAttendeeeByKeyword() {

        if (GlobalData.isNetworkAvailable(getActivity())) {

            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getinviteMoreData, Param.getInviteMoreData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getRequestMeetingId(), search_page_count, keyword), 1, false, this);
        }
    }

    private void buttonLoadGulfoodSearch() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
//            endlessScrollListener.setpriTotalCount(0);
            progressBar.setVisibility(View.VISIBLE);
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getinviteMoreData, Param.getInviteMoreData(sessionManager.getEventId(), sessionManager.getUserId(), sessionManager.getRequestMeetingId(), search_page_count, keyword), 2, false, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }


    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        total_pages = jsonObject1.getInt("total_page");
                        attendeeInviteMoreList = gson.fromJson(jsonObject1.toString(), AttendeeInviteMoreList.class);
                        moreDataArrayList.addAll(attendeeInviteMoreList.getAttendeeinviteMoreDataArrayList());
                        tag_search = "0";
                        if (moreDataArrayList.size() != 0) {
//                            txt_nodataFound.setVisibility(View.GONE);
                            txt_noAttendee.setVisibility(View.GONE);
                            rv_viewinviteMore.setVisibility(View.VISIBLE);
                        } else {
//                            txt_nodataFound.setVisibility(View.VISIBLE);
                            txt_noAttendee.setVisibility(View.VISIBLE);
                            rv_viewinviteMore.setVisibility(View.GONE);
                        }

                        new setListview().execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        search_page_count = jsonObject1.getInt("total_page");
                        attendeeInviteMoreList = gson.fromJson(jsonObject1.toString(), AttendeeInviteMoreList.class);
                        moreDataArrayList = new ArrayList<>();
                        moreDataArrayList.addAll(attendeeInviteMoreList.getAttendeeinviteMoreDataArrayList());
                        tag_search = "1";
                        nestedScrollView.setScrollY(0);
                        if (moreDataArrayList.size() != 0) {
//                            txt_nodataFound.setVisibility(View.GONE);
                            txt_noAttendee.setVisibility(View.GONE);
                            rv_viewinviteMore.setVisibility(View.VISIBLE);
                        } else {
//                            txt_nodataFound.setVisibility(View.VISIBLE);
                            txt_noAttendee.setVisibility(View.VISIBLE);
                            rv_viewinviteMore.setVisibility(View.GONE);
                        }
                        new setListview().execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        progressBar.setVisibility(View.GONE);
                        Gson gson = new Gson();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        search_page_count = jsonObject1.getInt("total_page");
                        attendeeInviteMoreList = gson.fromJson(jsonObject1.toString(), AttendeeInviteMoreList.class);
                        moreDataArrayList.addAll(attendeeInviteMoreList.getAttendeeinviteMoreDataArrayList());

                        new setListview().execute();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("Bhavdip JsonObj", jsonObject.toString());
                        ToastC.show(getActivity(), jsonObject.getString("message"));

                        GlobalData.updateRequestMeetingInvitedListMetod(getActivity());
//                        GlobalData.reuestMeetingReloadData(getActivity());
                        setCheckBoxfalse();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }

    private class setListview extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapterInviteMore.updateList(moreDataArrayList);
        }
    }

    private void setCheckBoxfalse() {
        adapterInviteMore.setAllCheckboxfalse();
    }

}
