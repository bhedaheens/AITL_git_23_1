package com.allintheloop.Bean.CmsGroupData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 11/12/17.
 */

public class CMSList {
    @SerializedName("cms_list")
    @Expose
    private ArrayList<CmsListData> groupModuleData = new ArrayList<>();


    public ArrayList<CmsListData> getGroupModuleData() {
        return groupModuleData;
    }

    public void setGroupModuleData(ArrayList<CmsListData> groupModuleData) {
        this.groupModuleData = groupModuleData;
    }
}
