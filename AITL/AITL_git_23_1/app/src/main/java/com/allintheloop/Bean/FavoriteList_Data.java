package com.allintheloop.Bean;

import com.allintheloop.Bean.Attendee.Attendance;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 10/3/17.
 */

public class FavoriteList_Data {
    String type, bg_color;
    ArrayList<Attendance> favoriteList;

    public FavoriteList_Data(String type, String bg_color, ArrayList<Attendance> favoriteList) {
        this.type = type;
        this.bg_color = bg_color;
        this.favoriteList = favoriteList;
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

    public ArrayList<Attendance> getFavoriteList() {
        return favoriteList;
    }

    public void setFavoriteList(ArrayList<Attendance> favoriteList) {
        this.favoriteList = favoriteList;
    }
}
