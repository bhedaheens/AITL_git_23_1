package com.allintheloop.Bean.Speaker;

public class SpeakerReloadedEventBus {
    public final String tag;

    public SpeakerReloadedEventBus(String data) {
        this.tag = data;
    }

    public String getData() {
        return tag;
    }
}
