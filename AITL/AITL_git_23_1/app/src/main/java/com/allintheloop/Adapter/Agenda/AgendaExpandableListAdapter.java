package com.allintheloop.Adapter.Agenda;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.allintheloop.Bean.AgendaData.Agenda_Time;
import com.allintheloop.R;
import com.allintheloop.Util.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by nteam on 10/5/16.
 */
public class AgendaExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> listDataHeader;
    private HashMap<String, ArrayList<Agenda_Time>> listDataChild;
    SessionManager sessionManager;
    int cnt = 0;
    Agenda_Time ageObj;

    public AgendaExpandableListAdapter(Context context, ArrayList<String> listDataHeader, HashMap<String, ArrayList<Agenda_Time>> listDataChild) {
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
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.agenda_expandablelist_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setText(headerTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        Agenda_Time ageObj = (Agenda_Time) getChild(groupPosition, childPosition);

        if (convertView == null) {

        }
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.agenda_expandablelist_item, null);

        TextView txt_name = (TextView) convertView.findViewById(R.id.txt_name);
        TextView txt_time = (TextView) convertView.findViewById(R.id.txt_time);
        TextView txt_speaker_name = (TextView) convertView.findViewById(R.id.txt_speaker_name);
        TextView txt_placeLeft = (TextView) convertView.findViewById(R.id.txt_placeLeft);
        TextView txt_locationName = (TextView) convertView.findViewById(R.id.txt_locationName);
        try {
            if (sessionManager.getAgendaSpeakerColumn().equalsIgnoreCase("0")) {
                txt_speaker_name.setVisibility(View.GONE);
            } else {
                txt_speaker_name.setVisibility(View.VISIBLE);
                txt_speaker_name.setText(ageObj.getSpeaker());
            }
            if (sessionManager.getAgendaPlaceLeft().equalsIgnoreCase("0")) {
                txt_placeLeft.setVisibility(View.GONE);
            } else {
                if (ageObj.getPlaceleft().equalsIgnoreCase("")) {
                    txt_placeLeft.setVisibility(View.GONE);
                } else {
                    txt_placeLeft.setVisibility(View.VISIBLE);
                    txt_placeLeft.setText(ageObj.getPlaceleft());
                }
            }

            if (sessionManager.getAgendalocationLeft().equalsIgnoreCase("0")) {
                txt_locationName.setVisibility(View.GONE);
            } else {
                if (ageObj.getLocation().equalsIgnoreCase("")) {
                    txt_locationName.setVisibility(View.GONE);
                } else {
                    txt_locationName.setVisibility(View.VISIBLE);
                    txt_locationName.setText(ageObj.getLocation());
                }
            }

            txt_name.setText(ageObj.getHeading());

            if (ageObj.getAgenda_timezone().equalsIgnoreCase("")) {
                txt_time.setText(ageObj.getStart_time() + " - " + ageObj.getEnd_time());
            } else {
                txt_time.setText(ageObj.getStart_time() + " - " + ageObj.getEnd_time() + " (" + ageObj.getAgenda_timezone() + ")");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
