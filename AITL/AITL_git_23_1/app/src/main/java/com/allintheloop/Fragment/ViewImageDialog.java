package com.allintheloop.Fragment;

/**
 * Created by nteam on 18/5/16.
 */

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.allintheloop.Fragment.PrivateMessage.Private_Message_Fragment;
import com.eftimoff.viewpagertransformers.DrawFromBackTransformer;
import com.allintheloop.Adapter.Exhibitor.Exhibitor_ImageAdapter;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ViewImageDialog extends DialogFragment implements VolleyInterface {

    Dialog dialog;
    ImageView imgclose;
    public static ViewPager viewPager;
    //    public  static CirclePageIndicator indicator;
    ArrayList<Exhibitor_DetailImage> viewpager_arraylist;
    Exhibitor_ImageAdapter imageAdapter;
    SessionManager sessionManager;
    public static RelativeLayout frame_viewpager;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        final RelativeLayout root = new RelativeLayout(getActivity());
//        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        dialog = new Dialog(getActivity());
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(root);
////        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transperent)));
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setWindowAnimations(R.style.dialog_animation);


        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setWindowAnimations(R.style.dialog_animation);
        return dialog;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.view_image_dialog, container, false);
        sessionManager = new SessionManager(getActivity());
        viewpager_arraylist = new ArrayList<>();
        viewPager = (ViewPager) view.findViewById(R.id.dialogviewPager);
//        indicator=(CirclePageIndicator)view.findViewById(R.id.indicator);
        frame_viewpager = (RelativeLayout) view.findViewById(R.id.frame_viewpager);
        imgclose = (ImageView) view.findViewById(R.id.imgclose);
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialog().dismiss();
            }
        });

        if (GlobalData.isNetworkAvailable(getActivity())) {
//            if(sessionManager.isLogin())
//            {
//                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getNotification, Param.getNotification(sessionManager.getEventId(), sessionManager.getMenuid(), sessionManager.getUserId(), sessionManager.getToken()),1, false, this);
//            }


            if (sessionManager.viewImg_tag.equalsIgnoreCase("photoFragment")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getPhotoImageList, Param.getImageList(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), Photo_Fragment.str_feedID), 0, false, this);
            } else if (sessionManager.viewImg_tag.equalsIgnoreCase("privateMessage")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getMessageImageList, Param.getMessageImageList(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), Private_Message_Fragment.str_message_id), 0, false, this);
            } else if (sessionManager.viewImg_tag.equalsIgnoreCase("publicMessage")) {
                new VolleyRequest(getActivity(), VolleyRequest.Method.POST, MyUrls.getMessageImageList, Param.getMessageImageList(sessionManager.getEventId(), sessionManager.getEventType(), sessionManager.getToken(), PublicMessage_Fragment.str_message_id), 0, false, this);
            }

        } else {
            ToastC.show(getActivity(), "No Internet Connection");
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL", jsonObject.toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jArrayImage = jsonData.getJSONArray("images");

                        for (int i = 0; i < jArrayImage.length(); i++) {
                            viewpager_arraylist.add(new Exhibitor_DetailImage(MyUrls.Imgurl + jArrayImage.getString(i).toString(), "imagedialog"));
                        }

                        if (viewpager_arraylist.size() == 0) {
                            frame_viewpager.setVisibility(View.GONE);

                        } else {
                            frame_viewpager.setVisibility(View.VISIBLE);
                            imageAdapter = new Exhibitor_ImageAdapter(getActivity(), viewpager_arraylist);
                            viewPager.setAdapter(imageAdapter);
//                                indicator.setViewPager(viewPager);
                            viewPager.setPageTransformer(true, new DrawFromBackTransformer());
                        }
                    } else {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        ToastC.show(getActivity(), jsonData.getString("msg"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}