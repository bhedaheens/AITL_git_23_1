package com.allintheloop.Bean.SponsorClass;

import com.allintheloop.Bean.Attendee.Attendance;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 23/12/16.
 */

public class SponsorListData {
    String type, bg_color;
    ArrayList<Attendance> sponsoArrayList;

    public SponsorListData(String type, String bg_color, ArrayList<Attendance> sponsoArrayList) {
        this.type = type;
        this.bg_color = bg_color;
        this.sponsoArrayList = sponsoArrayList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBg_color() {
        return bg_color;
    }

    public void setBg_color(String bg_color) {
        this.bg_color = bg_color;
    }

    public ArrayList<Attendance> getSponsoArrayList() {
        return sponsoArrayList;
    }

    public void setSponsoArrayList(ArrayList<Attendance> sponsoArrayList) {
        this.sponsoArrayList = sponsoArrayList;
    }
}
