package com.allintheloop.Bean.AgendaData;

/**
 * Created by Aiyaz on 5/5/17.
 */

public class AgendaSpeaker_SupporterLink {
    String type, value, id;

    public AgendaSpeaker_SupporterLink(String type, String value, String id) {
        this.type = type;
        this.value = value;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
