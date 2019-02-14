package com.allintheloop.Bean.Attendee;

public class AttendeeKeywordData {
    String keyword;
    boolean isCheck = false;

    public AttendeeKeywordData(String keyword, boolean isCheck) {
        this.keyword = keyword;
        this.isCheck = isCheck;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
