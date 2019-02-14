package com.allintheloop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.allintheloop.Bean.PrivateSpeakerList;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Aiyaz on 7/12/16.
 */

public class PrivateSpeakerExpandtableList extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> listDataHeader;
    private HashMap<String, ArrayList<PrivateSpeakerList>> listDataChild;
    SessionManager sessionManager;

    public PrivateSpeakerExpandtableList(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<PrivateSpeakerList>> listDataChild) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        sessionManager = new SessionManager(context);
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

        ExpandableListView eLV = (ExpandableListView) parent;
        eLV.expandGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.private_exapantablelist_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        PrivateSpeakerList privateObj = (PrivateSpeakerList) getChild(groupPosition, childPosition);

        if (convertView == null) {

        }
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.private_expandtablelist_item, null);

        TextView txt_name = (TextView) convertView.findViewById(R.id.lblListItem);
        txt_name.setText(privateObj.getName());

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
