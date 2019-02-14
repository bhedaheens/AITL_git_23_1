package com.allintheloop.Fragment.RequestMeetingModule;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.allintheloop.Adapter.RequestMetting.Adapter_RequestMeetingModeratorNew;
import com.allintheloop.Adapter.RequestMetting.Adapter_RequestMettingListNew;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewList;
import com.allintheloop.Bean.RequestMeeting.RequestMeetingNewModeratorList;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestMettingList_loadFragment extends Fragment {

    int position, parentPos;
    RecyclerView rv_viewMenu;
    ArrayList<String> datalist;
    ArrayList<String> stringTypes;


    ArrayList<RequestMeetingNewList> newListArrayList;
    ArrayList<RequestMeetingNewModeratorList> moderatorListArrayList;


    Adapter_RequestMettingListNew adapterRequestMettingListNew;
    Adapter_RequestMeetingModeratorNew adapter_requestMeetingModeratorNew;


    SessionManager sessionManager;
    String dateData = "";
    Dialog dailog;
    boolean isclick = true;
    EditText edt_search;
    boolean isModerator = false;
    TextView txt_noData;

    public RequestMettingList_loadFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance(int position, ArrayList<String> stringTypes, String dateData, ArrayList<RequestMeetingNewList> newListArrayList, int parentPos) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putInt("parentPos", parentPos);
        bundle.putStringArrayList("stringtypes", stringTypes);
        bundle.putParcelableArrayList("data", newListArrayList);
        bundle.putString("dateData", dateData);
        bundle.putBoolean("isModerator", false);
        RequestMettingList_loadFragment tabFragment = new RequestMettingList_loadFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    public static Fragment getInstance(int position, ArrayList<String> stringTypes, String dateData, ArrayList<RequestMeetingNewModeratorList> newListArrayList, int parentPos, boolean isModerator) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putInt("parentPos", parentPos);
        bundle.putStringArrayList("stringtypes", stringTypes);
        bundle.putParcelableArrayList("data", newListArrayList);
        bundle.putString("dateData", dateData);
        bundle.putBoolean("isModerator", isModerator);
        RequestMettingList_loadFragment tabFragment = new RequestMettingList_loadFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        parentPos = getArguments().getInt("parentPos");
        stringTypes = getArguments().getStringArrayList("stringtypes");
        dateData = getArguments().getString("dateData");
        isModerator = getArguments().getBoolean("isModerator");

        if (isModerator) {
            moderatorListArrayList = getArguments().getParcelableArrayList("data");
        } else {
            newListArrayList = getArguments().getParcelableArrayList("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_requestmettinglist, container, false);
        rv_viewMenu = (RecyclerView) rootView.findViewById(R.id.rv_viewMenu);
        edt_search = (EditText) rootView.findViewById(R.id.edt_search);
        txt_noData = (TextView) rootView.findViewById(R.id.txt_noData);
        sessionManager = new SessionManager(getActivity());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rv_viewMenu.setLayoutManager(mLayoutManager);
        rv_viewMenu.setItemAnimator(new DefaultItemAnimator());
        if (isModerator) {

            if (moderatorListArrayList.get(parentPos).getData().size() > 0) {
                rv_viewMenu.setVisibility(View.VISIBLE);
                txt_noData.setVisibility(View.GONE);
                edt_search.setVisibility(View.VISIBLE);
                adapter_requestMeetingModeratorNew = new Adapter_RequestMeetingModeratorNew(moderatorListArrayList.get(parentPos).getData(), getActivity(), stringTypes.get(position).toString(), dateData, RequestMettingList_loadFragment.this);
                rv_viewMenu.setAdapter(adapter_requestMeetingModeratorNew);
            } else {
                rv_viewMenu.setVisibility(View.GONE);
                txt_noData.setVisibility(View.VISIBLE);
                edt_search.setVisibility(View.GONE);

            }
        } else {
            if (newListArrayList.get(parentPos).getData().size() > 0) {
                rv_viewMenu.setVisibility(View.VISIBLE);
                txt_noData.setVisibility(View.GONE);
                edt_search.setVisibility(View.VISIBLE);
                adapterRequestMettingListNew = new Adapter_RequestMettingListNew(newListArrayList.get(parentPos).getData(), getActivity(), stringTypes.get(position).toString(), dateData, RequestMettingList_loadFragment.this);
                rv_viewMenu.setAdapter(adapterRequestMettingListNew);
            } else {
                rv_viewMenu.setVisibility(View.GONE);
                txt_noData.setVisibility(View.VISIBLE);
                edt_search.setVisibility(View.GONE);
            }
        }

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    GlobalData.hideSoftKeyboard(getActivity());
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
                try {
                    if (isModerator) {
                        if (moderatorListArrayList.size() > 0) {
                            sessionManager.setIsLastCategoryName("");
//                            getExhibitorByKeyword();
                            adapter_requestMeetingModeratorNew.getFilter().filter(edt_search.getText().toString());
                        }
                    } else {
                        if (newListArrayList.size() > 0) {
                            sessionManager.setIsLastCategoryName("");
//                            getExhibitorByKeyword();
                            adapterRequestMettingListNew.getFilter().filter(edt_search.getText().toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (isModerator) {
                        if (moderatorListArrayList.size() > 0) {
//                            getExhibitorByKeyword();
                            adapter_requestMeetingModeratorNew.getFilter().filter(edt_search.getText().toString());
                        }
                    } else {
                        if (newListArrayList.size() > 0) {
//                            getExhibitorByKeyword();
                            adapterRequestMettingListNew.getFilter().filter(edt_search.getText().toString());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return rootView;
    }

    public void fragmentRedirect(String meetingId) {

        GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
        GlobalData.CURRENT_FRAG = GlobalData.requestMettingInvteMoreFragment;
        ((MainActivity) getActivity()).loadFragment();
    }

    public void reloadFragment() {
        GlobalData.CURRENT_FRAG = GlobalData.exhibitorRequestMettingFragment;
        ((MainActivity) getActivity()).reloadFragment();
    }

}
