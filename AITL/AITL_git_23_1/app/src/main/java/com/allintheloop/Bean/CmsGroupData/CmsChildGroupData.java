package com.allintheloop.Bean.CmsGroupData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nteam on 11/12/17.
 */

public class CmsChildGroupData {
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
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;

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

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }
}
