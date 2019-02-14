package com.allintheloop.Adapter.Attendee;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allintheloop.Bean.Attendee.AttendeeFilterList;
import com.allintheloop.Bean.Attendee.AttendeeKeywordData;
import com.allintheloop.Fragment.AttandeeFragments.AttendeeFullDirectory_Fragment;
import com.allintheloop.R;
import com.allintheloop.Util.GlobalData;
import com.allintheloop.Util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AttendeeFilterExpantableListAdapter extends BaseExpandableListAdapter {
    AttendeeFullDirectory_Fragment fullDirectory_fragment;
    SessionManager sessionManager;
    private Context context;
    private ArrayList<AttendeeFilterList.Data> listDataHeader;
    private HashMap<AttendeeFilterList.Data, ArrayList<AttendeeKeywordData>> listDataChild;

    public AttendeeFilterExpantableListAdapter(Context context, ArrayList<AttendeeFilterList.Data> listDataHeader, HashMap<AttendeeFilterList.Data, ArrayList<AttendeeKeywordData>> listDataChild, AttendeeFullDirectory_Fragment fullDirectory_fragment, SessionManager sessionManager) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listDataChild;
        this.fullDirectory_fragment = fullDirectory_fragment;
        this.sessionManager = sessionManager;
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
        AttendeeFilterList.Data data = (AttendeeFilterList.Data) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.adapter_attendeefilterlist, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.group_name);
        ImageView img_arrow = (ImageView) convertView.findViewById(R.id.img_arrow);

        if (isExpanded) {
            img_arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.up_arrow));
        } else {
            img_arrow.setImageDrawable(context.getResources().getDrawable(R.drawable.down_arrow));
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            lblListHeader.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
        }
        lblListHeader.setText(data.getColumnName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final AttendeeKeywordData predanObj = (AttendeeKeywordData) getChild(groupPosition, childPosition);

        if (convertView == null) {

        }
        LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.adapter_attendefilter_expandchild, null);

        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        CheckBox ivCheck = (CheckBox) convertView.findViewById(R.id.ivCheck);


        RelativeLayout relative_main = (RelativeLayout) convertView.findViewById(R.id.relative_main);
        try {

            if (predanObj.getKeyword() != null || predanObj.getKeyword().isEmpty()) {
                txtName.setText(predanObj.getKeyword());
                if (predanObj.isCheck()) {
                    ivCheck.setChecked(true);
                } else {
                    ivCheck.setChecked(false);
                }
            }


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                GlobalData.customeCheckboxColorChangeAttendeeFilter(ivCheck, sessionManager);
                relative_main.setBackground(context.getResources().getDrawable(R.drawable.ripple_effect));
            }

            ivCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (predanObj.isCheck()) {
                        ivCheck.setChecked(false);
                        predanObj.setCheck(false);
                    } else {
                        ivCheck.setChecked(true);
                        predanObj.setCheck(true);
                    }
                }
            });

            txtName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (predanObj.isCheck()) {
                        ivCheck.setChecked(false);
                        predanObj.setCheck(false);
                    } else {
                        ivCheck.setChecked(true);
                        predanObj.setCheck(true);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    public JSONArray getSelectableList() {
        HashMap<AttendeeFilterList.Data, ArrayList<AttendeeKeywordData>> dataArrayListHashMap = new HashMap<>();
        Iterator dataIterator = listDataChild.keySet().iterator();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArraykeyword;
        while (dataIterator.hasNext()) {

            for (int i = 0; i < listDataChild.size(); i++) {
                boolean isSelected = false;
                jsonArraykeyword = new JSONArray();
                AttendeeFilterList.Data key = (AttendeeFilterList.Data) dataIterator.next();
                ArrayList<AttendeeKeywordData> data = listDataChild.get(key);
                for (AttendeeKeywordData keywordData : data) {
                    if (keywordData.isCheck()) {
                        isSelected = true;
                        jsonArraykeyword.put(keywordData.getKeyword());
                    }
                }
                try {
                    if (isSelected) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("keywords", jsonArraykeyword);
                        jsonObject.put("id", key.getId());
                        jsonArray.put(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonArray;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}

