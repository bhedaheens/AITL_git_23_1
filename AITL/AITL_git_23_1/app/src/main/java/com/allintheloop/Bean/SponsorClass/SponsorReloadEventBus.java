package com.allintheloop.Bean.SponsorClass;

public class SponsorReloadEventBus {
    public final String tag;

    public SponsorReloadEventBus(String data) {
        this.tag = data;
    }

    public String getData() {
        return tag;
    }
}
