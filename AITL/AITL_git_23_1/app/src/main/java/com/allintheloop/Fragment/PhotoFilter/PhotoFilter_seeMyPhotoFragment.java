package com.allintheloop.Fragment.PhotoFilter;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Adapter.AdapterPhotoFilter.PhotoFilter_AllPhotoAdapter;
import com.allintheloop.Bean.PhotoFilter.PhotoFilterListBeanClass;
import com.allintheloop.Bean.PhotoFilter.PhotoFilter_seeAllPhotoBean;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyCustomProgressDialog;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoFilter_seeMyPhotoFragment extends Fragment implements VolleyInterface {


    RecyclerView ryclview_myPhoto;
    PhotoFilter_seeAllPhotoBean photoFilterListBeanClass;
    PhotoFilter_AllPhotoAdapter allPhotoAdapter;
    List<PhotoFilter_seeAllPhotoBean.Listing> objectList;
    SessionManager sessionManager;
    LinearLayout linear_backCamera, linear_shareSelcted;
    TextView txt_fb, txt_twiiter, txt_linkedin, txt_activityFeed;

    ArrayList<String> stringArrayList;
    MyCustomProgressDialog mProgressDialog;

    public PhotoFilter_seeMyPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_photo_filter_see_my_photo, container, false);
        initView(rootView);
        if (sessionManager.isLogin())
            getAllPhotosoFFilter();
        return rootView;
    }

    private void getAllPhotosoFFilter() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPhotoFilerUserPhotos, Param.getFilterListingUserPhotos(sessionManager.getEventId(), sessionManager.getUserId()), 0, true, this);
        } else {
            ToastC.show(getActivity(), getString(R.string.noInernet));
        }
    }

    private void initView(View rootView) {
        sessionManager = new SessionManager(getActivity());
        ryclview_myPhoto = rootView.findViewById(R.id.ryclview_myPhoto);
        linear_backCamera = rootView.findViewById(R.id.linear_backCamera);
        linear_shareSelcted = rootView.findViewById(R.id.linear_shareSelcted);
        stringArrayList = new ArrayList<>();

        linear_backCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).fragmentBackStackMaintain();
            }
        });

        linear_shareSelcted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (allPhotoAdapter.getSelectedData().size() > 0) {

                    uploadInteralPhoto();
                } else {
                    ToastC.show(getActivity(), "No photos selected Please tap on the photos you would like to share and tap the Share button.");
                }
            }
        });
    }

    private void uploadInteralPhoto() {
        if (GlobalData.isNetworkAvailable(getActivity())) {
            Log.d("Bhavdip data", "" + allPhotoAdapter.getSelectedData());
            JSONArray jsonArray = new JSONArray();


            for (int i = 0; i < allPhotoAdapter.getSelectedData().size(); i++) {
                jsonArray.put(allPhotoAdapter.getSelectedData().get(i));
            }
            new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.saveMultiplePhotoFilterImage, Param.uploadActityFeedDataFromPhotoFilter(sessionManager.getEventId(), sessionManager.getUserId(), jsonArray.toString()), 1, true, this);
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
                        Gson gson = new Gson();
                        photoFilterListBeanClass = gson.fromJson(jsonObject.toString(), PhotoFilter_seeAllPhotoBean.class);
                        objectList = new ArrayList<>();
                        objectList.addAll(photoFilterListBeanClass.getData());
                        allPhotoAdapter = new PhotoFilter_AllPhotoAdapter(objectList, getActivity(), sessionManager, PhotoFilter_seeMyPhotoFragment.this);
                        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4);
                        mLayoutManager.setSmoothScrollbarEnabled(true);
                        ryclview_myPhoto.setLayoutManager(mLayoutManager);
                        ryclview_myPhoto.setAdapter(allPhotoAdapter);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        Log.d("Bhavdip", jsonObject.toString());
                        for (int i = 0; i < objectList.size(); i++) {
                            objectList.get(i).setChecked(false);
                        }
                        allPhotoAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

        }
    }

}
