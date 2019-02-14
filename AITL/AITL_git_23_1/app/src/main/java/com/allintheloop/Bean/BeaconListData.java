package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 13/4/17.
 */

public class BeaconListData {
    String id, uuid, name;

    public BeaconListData(String id, String uuid, String name) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
