package com.allintheloop.Bean.ActivityModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ActivityCommonList {
    @SerializedName("data")
    @Expose
    private ArrayList<ActivityCommonClass> data = new ArrayList<>();

    public ArrayList<ActivityCommonClass> getData() {
        return data;
    }

    public void setData(ArrayList<ActivityCommonClass> data) {
        this.data = data;
    }
}
