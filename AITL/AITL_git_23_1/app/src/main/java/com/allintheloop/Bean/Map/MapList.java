package com.allintheloop.Bean.Map;

/**
 * Created by nteam on 1/6/16.
 */
public class MapList {

    String id, title, latitude, longitude, include_map, img, img_url,
            google_map_icon, floor_plan_icon, check_dwg_files;

    public MapList(String map_id, String map_title, String latitude, String longitude, String include_map, String img, String google_map_icon, String floor_plan_icon, String check_dwg_files) {

        this.id = map_id;
        this.title = map_title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.include_map = include_map;
        this.img = img;
        this.google_map_icon = google_map_icon;
        this.floor_plan_icon = floor_plan_icon;
        this.check_dwg_files = check_dwg_files;
    }

    public String getCheck_dwg_files() {
        return check_dwg_files;
    }

    public void setCheck_dwg_files(String check_dwg_files) {
        this.check_dwg_files = check_dwg_files;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getInclude_map() {
        return include_map;
    }

    public void setInclude_map(String include_map) {
        this.include_map = include_map;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getGoogle_map_icon() {
        return google_map_icon;
    }

    public void setGoogle_map_icon(String google_map_icon) {
        this.google_map_icon = google_map_icon;
    }

    public String getFloor_plan_icon() {
        return floor_plan_icon;
    }

    public void setFloor_plan_icon(String floor_plan_icon) {
        this.floor_plan_icon = floor_plan_icon;
    }
}