package com.allintheloop.Bean.SponsorClass;

import com.intrusoft.sectionedrecyclerview.Section;

import java.util.List;

/**
 * Created by nteam on 30/11/17.
 */

public class SponsorSectionHeader implements Section<SponsorListNewData.SponsorNewData> {

    List<SponsorListNewData.SponsorNewData> childList;
    String sectionText;
    String bgColor;
    String typePosition;

    public SponsorSectionHeader(List<SponsorListNewData.SponsorNewData> childList, String sectionText, String bgColor, String typePosition) {
        this.childList = childList;
        this.sectionText = sectionText;
        this.bgColor = bgColor;
        this.typePosition = typePosition;
    }


    public String getTypePostion() {
        return typePosition;
    }

    public void setTypePostion(String typePostion) {
        this.typePosition = typePostion;
    }

    public List<SponsorListNewData.SponsorNewData> getChildList() {
        return childList;
    }

    public void setChildList(List<SponsorListNewData.SponsorNewData> childList) {
        this.childList = childList;
    }

    public String getSectionText() {
        return sectionText;
    }

    public void setSectionText(String sectionText) {
        this.sectionText = sectionText;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }


    @Override
    public List<SponsorListNewData.SponsorNewData> getChildItems() {
        return childList;
    }
}
