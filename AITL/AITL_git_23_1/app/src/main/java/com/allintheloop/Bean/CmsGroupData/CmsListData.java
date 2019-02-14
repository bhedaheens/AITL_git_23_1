package com.allintheloop.Bean.CmsGroupData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nteam on 11/12/17.
 */

public class CmsListData {
    @SerializedName("Id")
    @Expose
    private String id;
    @SerializedName("Menu_name")
    @Expose
    private String menuName;

    @SerializedName("cms_icon")
    @Expose
    private String cms_icon;


    @SerializedName("group_id")
    @Expose
    private String groupId;


    public String getCms_icon() {
        return cms_icon;
    }

    public void setCms_icon(String cms_icon) {
        this.cms_icon = cms_icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

}
