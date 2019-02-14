package com.allintheloop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.allintheloop.Activity.EventList_Activity;
import com.allintheloop.Activity.SearchApp_Activity;
import com.allintheloop.Bean.BeaconListData;
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

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Aiyaz on 13/4/17.
 */

public class BeaconListDataAdapter extends RecyclerView.Adapter<BeaconListDataAdapter.ViewHolder> implements VolleyInterface {

    ArrayList<BeaconListData> beaconListDatas;
    Context context;
    SessionManager sessionManager;
    String key = "";

    public BeaconListDataAdapter(ArrayList<BeaconListData> beaconListDatas, Context context) {
        this.beaconListDatas = beaconListDatas;
        this.context = context;
        sessionManager = new SessionManager(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_beaconlistdata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final BeaconListData beaconListDataObj = beaconListDatas.get(position);
        holder.txt_name.setText(beaconListDataObj.getName());
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (GlobalData.isNetworkAvailable(context)) {
                    deleteBeacon(beaconListDataObj.getId());
                    beaconListDatas.remove(position);
                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                } else {
                    ToastC.show(context, context.getResources().getString(R.string.noInernet));
                }
            }
        });

        holder.img_rename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDialog builder = new MaterialDialog.Builder(context)
                        .title(context.getResources().getString(R.string.txtEnterbeaconName))
                        .positiveColor(context.getResources().getColor(R.color.btnBack))
                        .negativeColor(context.getResources().getColor(R.color.btnBack))
                        .backgroundColor(context.getResources().getColor(R.color.white))
                        .titleColor(context.getResources().getColor(R.color.SearchTxtcolor))
                        .widgetColor(context.getResources().getColor(R.color.btnBack))
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input(context.getResources().getString(R.string.txt_beaconName), "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                String key = input.toString();
                                if (key.length() == 0) {
                                    ToastC.show(context, context.getResources().getString(R.string.txt_beaconName));
                                } else {
                                    renameBeacon(beaconListDataObj.getId(), key);
                                }
                            }
                        }).negativeText("Cancel").show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return beaconListDatas.size();
    }


    private void renameBeacon(String id, String name) {
        if (GlobalData.isNetworkAvailable(context)) {
            new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.renameBeacon, Param.renameBeacon(sessionManager.getEventId(), id, name, sessionManager.getUserId()), 0, true, this);
        } else {
            ToastC.show(context, context.getResources().getString(R.string.noInernet));
        }
    }


    private void deleteBeacon(String id) {
        new VolleyRequest((Activity) context, VolleyRequest.Method.POST, MyUrls.deleleBeacon, Param.delelteBeacon(sessionManager.getEventId(), id, sessionManager.getUserId()), 0, true, this);
    }

    @Override
    public void getVolleyRequestResponse(VolleyRequestResponse volleyResponse) {
        switch (volleyResponse.type) {
            case 0:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {

                        ToastC.show(context, jsonObject.getString("message"));
                        GlobalData.CURRENT_FRAG = GlobalData.getAllBeaconList;
                        ((MainActivity) context).reloadFragment();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 1:
                try {
                    JSONObject jsonObject = new JSONObject(volleyResponse.output);
                    if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                        ToastC.show(context, jsonObject.getString("message"));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        ImageView img_delete, img_rename;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            img_delete = (ImageView) itemView.findViewById(R.id.img_delete);
            img_rename = (ImageView) itemView.findViewById(R.id.img_rename);
        }
    }
}
