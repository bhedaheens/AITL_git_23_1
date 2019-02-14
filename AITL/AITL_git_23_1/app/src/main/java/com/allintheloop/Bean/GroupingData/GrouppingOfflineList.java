package com.allintheloop.Bean.GroupingData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 28/11/17.
 */

public class GrouppingOfflineList {


    @SerializedName("group")
    @Expose
    private ArrayList<GroupModuleData> groupModuleData = new ArrayList<>();


    @SerializedName("group_relation")
    @Expose
    private ArrayList<GroupRelationModuleData> groupRelationModuleDataArrayList = new ArrayList<>();


    @SerializedName("super_group_relation")
    @Expose
    private ArrayList<SuperGroupRelationData> superGroupRelationDataArrayList = new ArrayList<>();


    @SerializedName("super_group")
    @Expose
    private ArrayList<SuperGroupData> superGroupDataArrayList = new ArrayList<>();


    public ArrayList<GroupModuleData> getGroupModuleData() {
        return groupModuleData;
    }

    public void setGroupModuleData(ArrayList<GroupModuleData> groupModuleData) {
        this.groupModuleData = groupModuleData;
    }

    public ArrayList<GroupRelationModuleData> getGroupRelationModuleDataArrayList() {
        return groupRelationModuleDataArrayList;
    }

    public void setGroupRelationModuleDataArrayList(ArrayList<GroupRelationModuleData> groupRelationModuleDataArrayList) {
        this.groupRelationModuleDataArrayList = groupRelationModuleDataArrayList;
    }


    public ArrayList<SuperGroupRelationData> getSuperGroupRelationDataArrayList() {
        return superGroupRelationDataArrayList;
    }

    public void setSuperGroupRelationDataArrayList(ArrayList<SuperGroupRelationData> superGroupRelationDataArrayList) {
        this.superGroupRelationDataArrayList = superGroupRelationDataArrayList;
    }

    public ArrayList<SuperGroupData> getSuperGroupDataArrayList() {
        return superGroupDataArrayList;
    }

    public void setSuperGroupDataArrayList(ArrayList<SuperGroupData> superGroupDataArrayList) {
        this.superGroupDataArrayList = superGroupDataArrayList;
    }
}
