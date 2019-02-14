package com.allintheloop.Util;

import java.io.Serializable;

/**
 * Created by Aiyaz on 18/4/17.
 */

public class BeaconFoundDailog implements Serializable {
    String uuid = "", tag = "";
    String major = "", minor = "";
    String edystoneInstance = "", edystoneNameSpace = "";


    public BeaconFoundDailog(String uuid, String major, String minor, String tag) {
        this.uuid = uuid;
        this.major = major;
        this.minor = minor;
        this.tag = tag;
    }

    public BeaconFoundDailog(String edystoneNameSpace, String edystoneInstance, String tag) {
        this.edystoneNameSpace = edystoneNameSpace;
        this.edystoneInstance = edystoneInstance;
        this.tag = tag;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getEdystoneInstance() {
        return edystoneInstance;
    }

    public void setEdystoneInstance(String edystoneInstance) {
        this.edystoneInstance = edystoneInstance;
    }

    public String getEdystoneNameSpace() {
        return edystoneNameSpace;
    }

    public void setEdystoneNameSpace(String edystoneNameSpace) {
        this.edystoneNameSpace = edystoneNameSpace;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }

}
