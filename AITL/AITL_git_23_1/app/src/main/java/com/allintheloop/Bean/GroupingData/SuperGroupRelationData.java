package com.allintheloop.Bean.GroupingData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nteam on 16/1/18.
 */

public class SuperGroupRelationData {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("super_group_id")
    @Expose
    private String superGroupId;
    @SerializedName("child_group_id")
    @Expose
    private String childGroupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getSuperGroupId() {
        return superGroupId;
    }

    public void setSuperGroupId(String superGroupId) {
        this.superGroupId = superGroupId;
    }

    public String getChildGroupId() {
        return childGroupId;
    }

    public void setChildGroupId(String childGroupId) {
        this.childGroupId = childGroupId;
    }
}
