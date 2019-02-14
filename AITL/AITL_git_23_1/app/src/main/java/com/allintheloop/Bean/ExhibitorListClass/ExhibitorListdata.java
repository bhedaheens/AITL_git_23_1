package com.allintheloop.Bean.ExhibitorListClass;

import com.allintheloop.Bean.Attendee.Attendance;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 25/1/17.
 */

public class ExhibitorListdata {
    @SerializedName("type")
    @Expose
    String type;

    @SerializedName("bg_color")
    @Expose
    String bg_color;


    @SerializedName("data")
    @Expose
    ArrayList<Attendance> exhibitorList;

    public ExhibitorListdata(String type, String bg_color, ArrayList<Attendance> exhibitorList) {
        this.type = type;
        this.bg_color = bg_color;
        this.exhibitorList = exhibitorList;
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

    public ArrayList<Attendance> getExhibitorList() {
        return exhibitorList;
    }

    public void setExhibitorList(ArrayList<Attendance> exhibitorList) {
        this.exhibitorList = exhibitorList;
    }


}