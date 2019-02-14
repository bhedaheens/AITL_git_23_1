package com.allintheloop.Bean;

/**
 * Created by nteam on 19/5/16.
 */
public class CountryList {
    String id, name;
    String tag;

    public CountryList(String id, String name, String tag) {
        this.id = id;
        this.name = name;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
