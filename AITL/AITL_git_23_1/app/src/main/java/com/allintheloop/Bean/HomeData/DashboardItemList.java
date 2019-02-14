package com.allintheloop.Bean.HomeData;

/**
 * Created by nteam on 26/4/16.
 */
public class DashboardItemList {


    String pTitle, imgview_status, pMId, pCheck, type, is_icon, icon_path, backColor, isCmS, isforceLogin, categoryId, group_id;
    String imgView;

    public DashboardItemList(String pTitle, String imgview_status, String pMId, String pCheck, String imgView, String is_icon, String icon_path, String backColor, String isCmS, String isforceLogin, String categoryId) {

        this.pTitle = pTitle;
        this.imgview_status = imgview_status;
        this.pMId = pMId;
        this.pCheck = pCheck;
        this.imgView = imgView;
        this.is_icon = is_icon;
        this.icon_path = icon_path;
        this.backColor = backColor;
        this.isCmS = isCmS;
        this.isforceLogin = isforceLogin;
        this.categoryId = categoryId;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getIsforceLogin() {
        return isforceLogin;
    }

    public void setIsforceLogin(String isforceLogin) {
        this.isforceLogin = isforceLogin;
    }

    public String getIsCmS() {
        return isCmS;
    }

    public void setIsCmS(String isCmS) {
        this.isCmS = isCmS;
    }

    public String getImgView() {
        return imgView;
    }

    public void setImgView(String imgView) {
        this.imgView = imgView;
    }

    public String getpTitle() {
        return pTitle;
    }

    public void setpTitle(String pTitle) {
        this.pTitle = pTitle;
    }

    public String getImgview_status() {
        return imgview_status;
    }

    public void setImgview_status(String imgview_status) {
        this.imgview_status = imgview_status;
    }

    public String getpMId() {
        return pMId;
    }

    public void setpMId(String pMId) {
        this.pMId = pMId;
    }

    public String getpCheck() {
        return pCheck;
    }

    public void setpCheck(String pCheck) {
        this.pCheck = pCheck;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_icon() {
        return is_icon;
    }

    public void setIs_icon(String is_icon) {
        this.is_icon = is_icon;
    }

    public String getIcon_path() {
        return icon_path;
    }

    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }
}
