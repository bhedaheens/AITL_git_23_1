package com.allintheloop.Bean.CmsGroupData;

import com.allintheloop.Bean.GroupingData.GroupModuleData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 11/12/17.
 */

public class CmsSuperGroupList {
    @SerializedName("super_group")
    @Expose
    private ArrayList<CmsSuperGroupData> groupModuleData = new ArrayList<>();


    public ArrayList<CmsSuperGroupData> getGroupModuleData() {
        return groupModuleData;
    }

    public void setGroupModuleData(ArrayList<CmsSuperGroupData> groupModuleData) {
        this.groupModuleData = groupModuleData;
    }
}
