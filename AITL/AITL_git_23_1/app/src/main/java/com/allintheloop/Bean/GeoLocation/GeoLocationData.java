package com.allintheloop.Bean.GeoLocation;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoLocationData {

    @SerializedName("noti")
    @Expose
    private List<GeoLocationList> data = null;

    public List<GeoLocationList> getData() {
        return data;
    }

    public void setData(List<GeoLocationList> data) {
        this.data = data;
    }

    public class GeoLocationList {
        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("content")
        @Expose
        private String content;
        @SerializedName("moduleslink")
        @Expose
        private String moduleslink;
        @SerializedName("custommoduleslink")
        @Expose
        private String custommoduleslink;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("long")
        @Expose
        private String _long;
        @SerializedName("radius")
        @Expose
        private String radius;
        @SerializedName("address")
        @Expose
        private String address;

        @SerializedName("event_id")
        @Expose
        private String event_id;

        boolean status = false;

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getModuleslink() {
            return moduleslink;
        }

        public void setModuleslink(String moduleslink) {
            this.moduleslink = moduleslink;
        }

        public String getCustommoduleslink() {
            return custommoduleslink;
        }

        public void setCustommoduleslink(String custommoduleslink) {
            this.custommoduleslink = custommoduleslink;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLong() {
            return _long;
        }

        public void setLong(String _long) {
            this._long = _long;
        }

        public String getRadius() {
            return radius;
        }

        public void setRadius(String radius) {
            this.radius = radius;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public boolean getStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }

}
