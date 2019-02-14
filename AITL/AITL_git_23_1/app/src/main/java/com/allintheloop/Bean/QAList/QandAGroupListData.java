package com.allintheloop.Bean.QAList;

import com.allintheloop.Bean.GroupingData.GroupModuleData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 1/12/17.
 */

public class QandAGroupListData {
    @SerializedName("group")
    @Expose
    private ArrayList<GroupModuleData> groupModuleData = new ArrayList<>();


    public ArrayList<GroupModuleData> getGroupModuleData() {
        return groupModuleData;
    }

    public void setGroupModuleData(ArrayList<GroupModuleData> groupModuleData) {
        this.groupModuleData = groupModuleData;
    }
}
