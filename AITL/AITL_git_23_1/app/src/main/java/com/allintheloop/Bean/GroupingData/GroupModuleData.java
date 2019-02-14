package com.allintheloop.Bean.GroupingData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nteam on 28/11/17.
 */

public class GroupModuleData {

    @SerializedName("module_group_id")
    @Expose
    private String moduleGroupId;
    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("group_name")
    @Expose
    private String groupName;

    @SerializedName("group_image")
    @Expose
    private String groupImage;

    @SerializedName("sort_order")
    @Expose
    private String sort_order = "";

    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getModuleGroupId() {
        return moduleGroupId;
    }

    public void setModuleGroupId(String moduleGroupId) {
        this.moduleGroupId = moduleGroupId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public String getSort_order() {
        return sort_order;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
