package com.allintheloop.Bean.ActivityModule;

public class ActivityOptionChecked {
    String option;
    boolean isChecked = false;

    public ActivityOptionChecked() {
    }

    public ActivityOptionChecked(String option, boolean isChecked) {
        this.option = option;
        this.isChecked = isChecked;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
