package com.allintheloop.Bean.Speaker;

import com.allintheloop.Bean.SponsorClass.SponsorListNewData;
import com.intrusoft.sectionedrecyclerview.Section;

import java.util.List;

/**
 * Created by nteam on 30/11/17.
 */

public class SpeakerSectionHeader implements Section<SpeakerListClass.SpeakerListNew> {

    List<SpeakerListClass.SpeakerListNew> childList;
    String sectionText;
    String bgColor;
    String typePosition;

    public SpeakerSectionHeader(List<SpeakerListClass.SpeakerListNew> childList, String sectionText, String bgColor, String typePosition) {
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

    public List<SpeakerListClass.SpeakerListNew> getChildList() {
        return childList;
    }

    public void setChildList(List<SpeakerListClass.SpeakerListNew> childList) {
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
    public List<SpeakerListClass.SpeakerListNew> getChildItems() {
        return childList;
    }
}
