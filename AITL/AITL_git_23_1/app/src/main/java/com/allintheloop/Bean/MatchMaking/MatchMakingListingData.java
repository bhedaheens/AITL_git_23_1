package com.allintheloop.Bean.MatchMaking;

public class MatchMakingListingData {
    String strTitle, strSubTitle, strLogo, strId, strExhiPageId, strModuleId, strModuleName;

    public MatchMakingListingData(String strTitle, String strSubTitle, String strLogo, String strId, String strExhiPageId, String strModuleId, String strModuleName) {
        this.strTitle = strTitle;
        this.strSubTitle = strSubTitle;
        this.strLogo = strLogo;
        this.strId = strId;
        this.strExhiPageId = strExhiPageId;
        this.strModuleId = strModuleId;
        this.strModuleName = strModuleName;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrSubTitle() {
        return strSubTitle;
    }

    public void setStrSubTitle(String strSubTitle) {
        this.strSubTitle = strSubTitle;
    }

    public String getStrLogo() {
        return strLogo;
    }

    public void setStrLogo(String strLogo) {
        this.strLogo = strLogo;
    }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getStrExhiPageId() {
        return strExhiPageId;
    }

    public void setStrExhiPageId(String strExhiPageId) {
        this.strExhiPageId = strExhiPageId;
    }

    public String getStrModuleId() {
        return strModuleId;
    }

    public void setStrModuleId(String strModuleId) {
        this.strModuleId = strModuleId;
    }

    public String getStrModuleName() {
        return strModuleName;
    }

    public void setStrModuleName(String strModuleName) {
        this.strModuleName = strModuleName;
    }
}
