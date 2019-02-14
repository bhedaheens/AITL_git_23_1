package com.allintheloop.Bean;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 27/3/17.
 */

public class CommonListTypeData {
    String type, bg_color;
    ArrayList<AllCommonData> commonDataArrayList;

    public CommonListTypeData(String type, String bg_color, ArrayList<AllCommonData> commonDataArrayList) {
        this.type = type;
        this.bg_color = bg_color;
        this.commonDataArrayList = commonDataArrayList;
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

    public ArrayList<AllCommonData> getCommonDataArrayList() {
        return commonDataArrayList;
    }

    public void setCommonDataArrayList(ArrayList<AllCommonData> commonDataArrayList) {
        this.commonDataArrayList = commonDataArrayList;
    }
}
