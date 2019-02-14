package com.allintheloop.Bean.Map;

/**
 * Created by nteam on 20/9/16.
 */
public class MapImageSession {
    String str_sId, str_sHeading, str_sSDate, str_sTime;

    public MapImageSession(String str_sId, String str_sHeading, String str_sSDate, String str_sTime) {
        this.str_sId = str_sId;
        this.str_sHeading = str_sHeading;
        this.str_sSDate = str_sSDate;
        this.str_sTime = str_sTime;
    }

    public String getStr_sId() {
        return str_sId;
    }

    public void setStr_sId(String str_sId) {
        this.str_sId = str_sId;
    }

    public String getStr_sHeading() {
        return str_sHeading;
    }

    public void setStr_sHeading(String str_sHeading) {
        this.str_sHeading = str_sHeading;
    }

    public String getStr_sSDate() {
        return str_sSDate;
    }

    public void setStr_sSDate(String str_sSDate) {
        this.str_sSDate = str_sSDate;
    }

    public String getStr_sTime() {
        return str_sTime;
    }

    public void setStr_sTime(String str_sTime) {
        this.str_sTime = str_sTime;
    }
}
