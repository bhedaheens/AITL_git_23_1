package com.allintheloop.Bean.ExhibitorListClass;

public class ExhibitorEventBusClass {
    public final String tag;

    public ExhibitorEventBusClass(String data) {
        this.tag = data;
    }

    public String getData() {
        return tag;
    }
}
