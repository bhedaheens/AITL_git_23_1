package com.allintheloop.Bean.Speaker;

import com.allintheloop.Bean.SponsorClass.SponsorListNewData;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SpeakerListMainClass {
    @SerializedName("speaker_list")
    @Expose
    private ArrayList<SpeakerListClass> speakerListClassArrayList = new ArrayList<>();

    public ArrayList<SpeakerListClass> getSponsorListNewDataArrayList() {
        return speakerListClassArrayList;
    }

    public void setSponsorListNewDataArrayList(ArrayList<SpeakerListClass> speakerListClassArrayList) {
        this.speakerListClassArrayList = speakerListClassArrayList;
    }
}

