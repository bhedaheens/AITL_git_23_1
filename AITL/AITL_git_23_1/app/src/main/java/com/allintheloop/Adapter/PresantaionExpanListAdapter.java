package com.allintheloop.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.allintheloop.Bean.Presantation;
import com.allintheloop.MainActivity;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by nteam on 8/6/16.
 */
public class PresantaionExpanListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> listDataHeader;
    private HashMap<String, ArrayList<Presantation>> listDataChild;

    public PresantaionExpanListAdapter(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<Presantation>> listDataChild) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
        return this.listDataChild
                .get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.agenda_expandablelist_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Presantation predanObj = (Presantation) getChild(groupPosition, childPosition);

        if (convertView == null) {

        }
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.presantation_expantablelist_item, null);

        TextView txt_slide = convertView.findViewById(R.id.txt_slide);
        TextView txt_statTime = convertView.findViewById(R.id.txt_statTime);

        Button btn_ViewSilde = convertView.findViewById(R.id.btn_ViewSilde);


        try {

//            if (predanObj.getPresan_filetype().equalsIgnoreCase("0")) {
//                Glide.with(context).load(MyUrls.Imgurl + predanObj.getImage()).centerCrop().fitCenter().into(thumbnail);
//
//            } else {
//                Glide.with(context).load(R.drawable.powerpoint_icon).centerCrop().fitCenter().placeholder(R.drawable.powerpoint_icon).into(thumbnail);
//            }

            txt_slide.setText(predanObj.getSlide_name());
            txt_statTime.setText(predanObj.getStart_time());

            btn_ViewSilde.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                Log.d("AITL Presantation", predanObj.getId());
                bundle.putString("presantation_id", predanObj.getId());
                GlobalData.Fragment_Stack.push(GlobalData.CURRENT_FRAG);
                GlobalData.CURRENT_FRAG = GlobalData.Presantation_Detail_Fragment;
                ((MainActivity) context).loadFragment(bundle);

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
