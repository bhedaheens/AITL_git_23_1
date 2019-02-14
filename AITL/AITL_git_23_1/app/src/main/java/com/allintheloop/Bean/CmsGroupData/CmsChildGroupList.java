package com.allintheloop.Bean.CmsGroupData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 11/12/17.
 */

public class CmsChildGroupList {
    @SerializedName("child_group")
    @Expose
    private ArrayList<CmsChildGroupData> groupModuleData = new ArrayList<>();


    public ArrayList<CmsChildGroupData> getGroupModuleData() {
        return groupModuleData;
    }

    public void setGroupModuleData(ArrayList<CmsChildGroupData> groupModuleData) {
        this.groupModuleData = groupModuleData;
    }
}
