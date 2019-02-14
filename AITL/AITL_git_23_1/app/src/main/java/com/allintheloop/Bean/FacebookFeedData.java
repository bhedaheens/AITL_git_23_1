package com.allintheloop.Bean;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 23/1/17.
 */

public class FacebookFeedData {
    String id, name, time, message, picture, link, bottom_name, desc, caption, icon, totalLikes, totalComment;
    ArrayList<String> img_array;

    public FacebookFeedData(String id, String name, String time, String message, String picture, String link, String bottom_name, String desc, String caption, String icon, String totalLikes, String totalComment, ArrayList<String> img_array) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.message = message;
        this.picture = picture;
        this.link = link;
        this.bottom_name = bottom_name;
        this.desc = desc;
        this.caption = caption;
        this.icon = icon;
        this.totalLikes = totalLikes;
        this.totalComment = totalComment;
        this.img_array = img_array;
    }

    public ArrayList<String> getImg_array() {
        return img_array;
    }

    public void setImg_array(ArrayList<String> img_array) {
        this.img_array = img_array;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBottom_name() {
        return bottom_name;
    }

    public void setBottom_name(String bottom_name) {
        this.bottom_name = bottom_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTotalLikes() {
        return totalLikes;
    }

    public void setTotalLikes(String totalLikes) {
        this.totalLikes = totalLikes;
    }

    public String getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(String totalComment) {
        this.totalComment = totalComment;
    }
}
