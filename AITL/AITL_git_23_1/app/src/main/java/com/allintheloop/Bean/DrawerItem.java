package com.allintheloop.Bean;

/**
 * Created by nteam on 25/4/16.
 */
public class DrawerItem {

    public String name, id, type, is_forceLogin, categoryId = "", sup_group_id = "", sub_groupId = "", is_contains_data = "";

    public DrawerItem(String name, String id, String type, String is_forceLogin, String categoryId, String sup_group_id, String sub_groupId, String is_contains_data) {

        this.name = name;
        this.id = id;
        this.type = type;
        this.is_forceLogin = is_forceLogin;
        this.categoryId = categoryId;
        this.sup_group_id = sup_group_id;
        this.sub_groupId = sub_groupId;
        this.is_contains_data = is_contains_data;
    }

    public String getSub_groupId() {
        return sub_groupId;
    }

    public void setSub_groupId(String sub_groupId) {
        this.sub_groupId = sub_groupId;
    }

    public String getSup_group_id() {
        return sup_group_id;
    }

    public void setSup_group_id(String sup_group_id) {
        this.sup_group_id = sup_group_id;
    }

    public DrawerItem(String name, String id, String type, String is_forceLogin) {

        this.name = name;
        this.id = id;
        this.type = type;
        this.is_forceLogin = is_forceLogin;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_contains_data() {
        return is_contains_data;
    }

    public void setIs_contains_data(String is_contains_data) {
        this.is_contains_data = is_contains_data;
    }

    public String getIs_forceLogin() {
        return is_forceLogin;
    }

    public void setIs_forceLogin(String is_forceLogin) {
        this.is_forceLogin = is_forceLogin;
    }
}
