package com.allintheloop.Fragment.PhotoFilter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.allintheloop.Adapter.AdapterActivity.ActivityCommonAdapter;
import com.allintheloop.Adapter.AdapterPhotoFilter.PhotoFilter_FilterListing_Adapter;
import com.allintheloop.Bean.PhotoFilter.PhotoFilterListBeanClass;
import com.allintheloop.Fragment.ActivityModule.Activtiy_ModuleAllInoneDesign_Fragment;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFilter_filterListing extends Fragment implements VolleyInterface {


    RecyclerView recycler_filterListing;
    LinearLayout linear_bottomView;
    SessionManager sessionManager;
    PhotoFilterListBeanClass photoFilterListBeanClass;
    List<Object> objectList;
    PhotoFilter_FilterListing_Adapter listingAdapter;
    GridLayoutManager mLayoutManager;

    public PhotoFilter_filterListing() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo_filter_filter_listing, container, false);

        initView(rootView);
        getFilterListing();
        return rootView;
    }


    public void getFilterListing() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPhotoFilerImage, Param.getFilterListing(sessionManager.getEventId()), 0, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void initView(View rootView) {
        recycler_filterListing = rootView.findViewById(R.id.recycler_filterListing);
        linear_bottomView = rootView.findViewById(R.id.linear_bottomView);
        sessionManager = new SessionManager(getActivity());


        linear_bottomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentBackStackMaintain();
            }
        });
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Gson gson = new Gson();
                        objectList = new ArrayList<>();
                        photoFilterListBeanClass = gson.fromJson(jsonObject.toString(), PhotoFilterListBeanClass.class);
                        objectList.addAll(photoFilterListBeanClass.getData());

                        listingAdapter = new PhotoFilter_FilterListing_Adapter(objectList, getActivity(), sessionManager);
                        mLayoutManager = new GridLayoutManager(getActivity(), 2);
                        recycler_filterListing.setLayoutManager(mLayoutManager);
                        recycler_filterListing.setItemAnimator(null);
                        recycler_filterListing.setAdapter(listingAdapter);


                        Log.d("Bhavdip", jsonObject.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
