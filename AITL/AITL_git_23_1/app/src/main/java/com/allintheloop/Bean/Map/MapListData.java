package com.allintheloop.Bean.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 29/11/17.
 */


public class MapListData {
    @SerializedName("map_list")
    @Expose
    public ArrayList<MapNewData> mapNewDataArrayList;

    public ArrayList<MapNewData> getMapNewDataArrayList() {
        return mapNewDataArrayList;
    }

    public void setMapNewDataArrayList(ArrayList<MapNewData> mapNewDataArrayList) {
        this.mapNewDataArrayList = mapNewDataArrayList;
    }

    public class MapNewData {
        @SerializedName("Id")
        @Expose
        private String id;
        @SerializedName("Map_title")
        @Expose
        private String mapTitle;
        @SerializedName("lat_long")
        @Expose
        private String latLong;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Images")
        @Expose
        private String images;
        @SerializedName("include_map")
        @Expose
        private String includeMap;
        @SerializedName("check_dwg_files")
        @Expose
        private String checkDwgFiles;
        @SerializedName("google_map_icon")
        @Expose
        private String googleMapIcon;
        @SerializedName("floor_plan_icon")
        @Expose
        private String floorPlanIcon;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("long")
        @Expose
        private String _long;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMapTitle() {
            return mapTitle;
        }

        public void setMapTitle(String mapTitle) {
            this.mapTitle = mapTitle;
        }

        public String getLatLong() {
            return latLong;
        }

        public void setLatLong(String latLong) {
            this.latLong = latLong;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public String getIncludeMap() {
            return includeMap;
        }

        public void setIncludeMap(String includeMap) {
            this.includeMap = includeMap;
        }

        public String getCheckDwgFiles() {
            return checkDwgFiles;
        }

        public void setCheckDwgFiles(String checkDwgFiles) {
            this.checkDwgFiles = checkDwgFiles;
        }

        public String getGoogleMapIcon() {
            return googleMapIcon;
        }

        public void setGoogleMapIcon(String googleMapIcon) {
            this.googleMapIcon = googleMapIcon;
        }

        public String getFloorPlanIcon() {
            return floorPlanIcon;
        }

        public void setFloorPlanIcon(String floorPlanIcon) {
            this.floorPlanIcon = floorPlanIcon;
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

    }

}
