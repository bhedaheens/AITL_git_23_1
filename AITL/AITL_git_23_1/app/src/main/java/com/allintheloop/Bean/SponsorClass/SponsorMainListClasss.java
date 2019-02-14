package com.allintheloop.Bean.SponsorClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 30/11/17.
 */

public class SponsorMainListClasss {
    @SerializedName("sponsors_list")
    @Expose
    private ArrayList<SponsorListNewData> sponsorListNewDataArrayList = new ArrayList<>();

    public ArrayList<SponsorListNewData> getSponsorListNewDataArrayList() {
        return sponsorListNewDataArrayList;
    }

    public void setSponsorListNewDataArrayList(ArrayList<SponsorListNewData> sponsorListNewDataArrayList) {
        this.sponsorListNewDataArrayList = sponsorListNewDataArrayList;
    }
}
