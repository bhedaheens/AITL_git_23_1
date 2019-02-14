package com.allintheloop.Bean.ExhibitorListClass;

import com.allintheloop.Bean.Attendee.Attendance;
import com.intrusoft.sectionedrecyclerview.Section;

import java.util.List;

/**
 * Created by nteam on 7/11/17.
 */

public class SectionHeader implements Section<Attendance> {

    List<Attendance> childList;
    String sectionText;
    String bgColor;

    public SectionHeader(List<Attendance> childList, String sectionText, String bgColor) {
        this.childList = childList;
        this.sectionText = sectionText;
        this.bgColor = bgColor;
    }

    @Override
    public List<Attendance> getChildItems() {
        return childList;
    }

    public String getSectionText() {
        return sectionText;
    }

    public String getBgColor() {
        return bgColor;
    }

    public void setBgColor(String bgColor) {
        this.bgColor = bgColor;
    }


}
