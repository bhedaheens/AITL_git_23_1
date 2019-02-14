package com.allintheloop.Bean.GroupingData;

/**
 * Created by nteam on 28/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupRelationModuleData {

    @SerializedName("mgr_id")
    @Expose
    private String mgrId;
    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("module_id")
    @Expose
    private String moduleId;

    public String getMgrId() {
        return mgrId;
    }

    public void setMgrId(String mgrId) {
        this.mgrId = mgrId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

}