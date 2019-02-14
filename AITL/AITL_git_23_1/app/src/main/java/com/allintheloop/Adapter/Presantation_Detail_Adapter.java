package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;
import com.allintheloop.Fragment.PresantationModule.PresantationDetail_ViewResultDialog;
import com.allintheloop.Fragment.PresantationModule.Presantation_Detail_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.MyUrls;
import com.allintheloop.Util.Param;
import com.allintheloop.Util.SessionManager;
import com.allintheloop.Util.ToastC;
import com.allintheloop.Volly.VolleyInterface;
import com.allintheloop.Volly.VolleyRequest;
import com.allintheloop.Volly.VolleyRequestResponse;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by nteam on 21/6/16.
 */
public class Presantation_Detail_Adapter extends RecyclerView.Adapter<Presantation_Detail_Adapter.ViewHolder> implements VolleyInterface {

    ArrayList<Exhibitor_DetailImage> imageArrayList;
    Context context;
    boolean txt_isLock = true, is_poll = true;
    SessionManager sessionManager;
    int temp_postion;

    public Presantation_Detail_Adapter(ArrayList<Exhibitor_DetailImage> imageArrayList, Context context) {
        this.imageArrayList = imageArrayList;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_presantation_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        temp_postion = position;
        Exhibitor_DetailImage detailImageObj = imageArrayList.get(position);

        holder.itemView.setFocusable(true);

        if (detailImageObj.getType().equalsIgnoreCase("image")) {
            holder.txt_label.setText("Slide");
            Glide.with(context).load(MyUrls.Imgurl + detailImageObj.getImage_link()).centerCrop().fitCenter().into(holder.array_img);
        } else {


            holder.txt_label.setText("Poll");
            Glide.with(context).load("").centerCrop().fitCenter().placeholder(R.drawable.poll_default_img).into(holder.array_img);
        }


        Presantation_Detail_Fragment.linear_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GlobalData.isNetworkAvailable(context)) {
                    is_poll = false;
                    pushSlide();
                } else {
                    ToastC.show(context, "No Internet Connection");
                }
            }
        });


        Presantation_Detail_Fragment.linear_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Presantation_Detail_Fragment.txt_mode.getText().toString().equalsIgnoreCase("Preview Mode")) {
                    if (GlobalData.isNetworkAvailable(context)) {
                        pushSlide();
                    } else {
                        ToastC.show(context, "No Internet Connection");
                    }


                } else {
                    if (GlobalData.isNetworkAvailable(context)) {
                        liveMode();
                    } else {
                        ToastC.show(context, "No Internet Connection");
                    }

                }
            }
        });

        Presantation_Detail_Fragment.linear_ViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewResult("0");
            }
        });

        Presantation_Detail_Fragment.linear_ViewbarChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                viewResult("1");
            }
        });

        if (detailImageObj.isChanged()) {
            holder.txt_label.setBackgroundColor(context.getResources().getColor(R.color.green));

            if (detailImageObj.getType().equalsIgnoreCase("image")) {
                is_poll = false;

            } else if (detailImageObj.getType().equalsIgnoreCase("survay")) {
                is_poll = true;
            }
        } else {
            holder.txt_label.setBackgroundColor(context.getResources().getColor(R.color.survey_question));
        }
    }


    public void changeColor(int position) {

        for (int i = 0; i < imageArrayList.size(); i++) {

            if (i == position) {

                imageArrayList.get(i).setIsChanged(true);
            } else
                imageArrayList.get(i).setIsChanged(false);

        }
        notifyDataSetChanged();
    }

    private void liveMode() {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.presantationDetailRemovePushSlide, Param.presantation_liveMode(Presantation_Detail_Fragment.presantation_id), 1, true, this);
    }

    private void pushSlide() {
        if (!is_poll) {
            if (!(Presantation_Detail_Fragment.image_name.equalsIgnoreCase(""))) {
                new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.presantationDetailPushSlide, Param.get_Presantation_DetailLockUnlock(sessionManager.getEventId(), Presantation_Detail_Fragment.image_name, Presantation_Detail_Fragment.presantation_id), 1, true, this);
            } else {
                ToastC.show(context, "Please Select Poll Slide");
            }
        } else {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.presantationDetailPushSlide, Param.get_Presantation_DetailLockUnlock(sessionManager.getEventId(), "0", Presantation_Detail_Fragment.presantation_id), 1, true, this);
        }
    }

    private void viewResult(String isbarChart) {

        FragmentActivity activity = (FragmentActivity) (context);
        FragmentManager fm = activity.getSupportFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("image", Presantation_Detail_Fragment.image_name);
        bundle.putString("isbarChart", isbarChart);
        PresantationDetail_ViewResultDialog alertDialog = new PresantationDetail_ViewResultDialog();
        alertDialog.setArguments(bundle);
        alertDialog.show(fm, "fragment_alert");
    }


    @Override
    public int getItemCount() {
        return imageArrayList.size();
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    GlobalData.presantationSendMessage(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    Log.d("AITL PUSH SCREEN", jsonObject.toString());
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        GlobalData.presantationSendMessage(context);
                        if (jsonObject.getString("live_mode").equalsIgnoreCase("1")) {
                            Presantation_Detail_Fragment.txt_mode.setText("Live Mode");
                        } else {
                            Presantation_Detail_Fragment.txt_mode.setText("Preview Mode");
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView array_img;
        TextView txt_label;

        public ViewHolder(View itemView) {
            super(itemView);
            array_img = (ImageView) itemView.findViewById(R.id.array_img);
            txt_label = (TextView) itemView.findViewById(R.id.txt_label);
        }
    }
}