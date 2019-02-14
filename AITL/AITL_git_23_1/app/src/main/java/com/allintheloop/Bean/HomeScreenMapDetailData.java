package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 29/5/17.
 */

public class HomeScreenMapDetailData {
    String coords, menuid, redirect_url, cmsid, is_force_login, agenda_id, group_id, exhi_id, super_group_id, exhi_sub_cat_id, all_exhi_sub_cat, is_contains_data, is_user_agenda;


    public HomeScreenMapDetailData(String coords, String menuid, String redirect_url, String cmsid, String is_force_login, String agenda_id, String group_id, String exhi_id, String super_group_id, String exhi_sub_cat_id, String all_exhi_sub_cat, String is_contains_data, String is_user_agenda) {
        this.coords = coords;
        this.menuid = menuid;
        this.redirect_url = redirect_url;
        this.cmsid = cmsid;
        this.is_force_login = is_force_login;
        this.agenda_id = agenda_id;
        this.group_id = group_id;
        this.exhi_id = exhi_id;
        this.super_group_id = super_group_id;
        this.exhi_sub_cat_id = exhi_sub_cat_id;
        this.all_exhi_sub_cat = all_exhi_sub_cat;
        this.is_contains_data = is_contains_data;
        this.is_user_agenda = is_user_agenda;

    }

    public String getIs_user_agenda() {
        return is_user_agenda;
    }

    public void setIs_user_agenda(String is_user_agenda) {
        this.is_user_agenda = is_user_agenda;
    }

    public String getIs_contains_data() {
        return is_contains_data;
    }

    public void setIs_contains_data(String is_contains_data) {
        this.is_contains_data = is_contains_data;
    }

    public String getAll_exhi_sub_cat() {
        return all_exhi_sub_cat;
    }

    public void setAll_exhi_sub_cat(String all_exhi_sub_cat) {
        this.all_exhi_sub_cat = all_exhi_sub_cat;
    }

    public String getExhi_sub_cat_id() {
        return exhi_sub_cat_id;
    }

    public void setExhi_sub_cat_id(String exhi_sub_cat_id) {
        this.exhi_sub_cat_id = exhi_sub_cat_id;
    }

    public String getSuper_group_id() {
        return super_group_id;
    }

    public void setSuper_group_id(String super_group_id) {
        this.super_group_id = super_group_id;
    }

    public String getExhi_id() {
        return exhi_id;
    }

    public void setExhi_id(String exhi_id) {
        this.exhi_id = exhi_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getAgenda_id() {
        return agenda_id;
    }

    public void setAgenda_id(String agenda_id) {
        this.agenda_id = agenda_id;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getRedirect_url() {
        return redirect_url;
    }

    public void setRedirect_url(String redirect_url) {
        this.redirect_url = redirect_url;
    }

    public String getCmsid() {
        return cmsid;
    }

    public void setCmsid(String cmsid) {
        this.cmsid = cmsid;
    }

    public String getIs_force_login() {
        return is_force_login;
    }

    public void setIs_force_login(String is_force_login) {
        this.is_force_login = is_force_login;
    }
}
