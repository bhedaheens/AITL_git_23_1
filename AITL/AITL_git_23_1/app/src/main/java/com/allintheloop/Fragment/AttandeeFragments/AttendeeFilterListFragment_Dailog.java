package com.allintheloop.Fragment.AttandeeFragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.allintheloop.Activity.AttendeeCategoryListActivity;
import com.allintheloop.Adapter.Attendee.AttendeeFilterListAdapter;
import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttendeeFilterListFragment_Dailog extends DialogFragment {


    Dialog dialog;
    Activity activity;
    RecyclerView rv_attendeeFilter;
    Button btn_cancel, btn_filter;
    AttendeeFilterListAdapter attendeeFilterListAdapter;
    List<AttendeeFilterList.Data> dataArrayList;
    Bundle bundle;
    ArrayList<String> seclected;
    ArrayList<String> id;


    HashMap<String, ArrayList<String>> listDataChild;

    public AttendeeFilterListFragment_Dailog() {
        // Required empty public constructor
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        activity = getActivity();
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_layout);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.dialog_custom_attendee_filter, container, false);
        rv_attendeeFilter = (RecyclerView) rootView.findViewById(R.id.rv_attendeeFilter);
        btn_cancel = (Button) rootView.findViewById(R.id.btn_cancel);
        btn_filter = (Button) rootView.findViewById(R.id.btn_filter);
        bundle = getArguments();
        dataArrayList = (List<AttendeeFilterList.Data>) bundle.getSerializable("dataArray");
        seclected = new ArrayList<>();
        id = new ArrayList<>();
        listDataChild = new HashMap<String, ArrayList<String>>();
//        attendeeFilterList= new ArrayList<>();
//        attendeeFilterList.add("Job");
//        attendeeFilterList.add("Job");
//        attendeeFilterList.add("Job");
//
//        rv_attendeeFilter.setLayoutManager(new LinearLayoutManager(this.getActivity()));
//        attendeeFilterListAdapter=new AttendeeFilterListAdapter(dataArrayList,activity,AttendeeFilterListFragment_Dailog.this);
//        rv_attendeeFilter.setItemAnimator(new DefaultItemAnimator());
//        rv_attendeeFilter.setAdapter(attendeeFilterListAdapter);

        return rootView;
    }


    public void selectedData(AttendeeFilterList.Data data) {
        Intent intent = new Intent(getActivity(), AttendeeCategoryListActivity.class);
        intent.putExtra("Attendeecategory", data);
        intent.putExtra("selectedData", seclected);
        startActivityForResult(intent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {

                listDataChild.put(data.getExtras().getString("categoryid"), (ArrayList<String>) data.getExtras().getSerializable("selectedName"));
                ArrayList<String> fullArray = (ArrayList<String>) data.getExtras().getSerializable("myData");
                JSONArray jsonArray = new JSONArray();

                Log.d("Bhavdip Data", listDataChild.toString());

//                for (int i=0;i<id.size();i++)
//                {
//                    try {
//                        jsonArray.put(new JSONObject()
//                                .put("keywords",seclected.get(i))
//                                .put("id",id.get(i)));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }


//                attendeeCategoryArrayList.clear();
//                selectedCategory.clear();
//                selectedCategory = (ArrayList<String>) data.getExtras().getSerializable("selected");
//                attendeeCategoryArrayList = (ArrayList<AttendeeCategoryList.AttendeeCategory>) data.getExtras().getSerializable("myData");
//                ArrayList<String> selectedName = (ArrayList<String>) data.getExtras().getSerializable("selectedName");
//                setFilterText(txt_filter, selectedName, selectedCategory);
//
//
//                try {
//                    filterString = "";
//                    filterString = selectedCategory.toString();
//                    filterString = filterString.replaceAll("\\[", "").replaceAll("\\]", "");
//                    Log.d("Bhavdip Keyword", filterString);
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                if (filterString.isEmpty()) {
//
//                } else {
//                    getFilterList();
//                }
            }

        }

    }
}
