package com.allintheloop.Bean.PhotoFilter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoFilterListBeanClass {
    @SerializedName("data")
    @Expose
    private List<PhotoFilterListing> data;

    public PhotoFilterListBeanClass() {
        data = null;
    }

    public List<PhotoFilterListing> getData() {
        return data;
    }

    public void setData(List<PhotoFilterListing> data) {
        this.data = data;
    }
}
