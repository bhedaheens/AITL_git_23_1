package com.allintheloop.Bean;

import com.allintheloop.Bean.Attendee.Attendee_Comment;
import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;

import java.util.ArrayList;

/**
 * Created by nteam on 9/7/16.
 */
public class photoSection {
    String feed_id, sender_id, time, sender_name, sender_logo, total_like, is_like, time_stamp, total_comment, str_image;
    ArrayList<Attendee_Comment> commentArrayList;
    ArrayList<Exhibitor_DetailImage> imageArrayList;

    public photoSection(String feed_id, String sender_id, String time, String sender_name, String sender_logo, String total_like, String is_like, String time_stamp, String total_comment, ArrayList<Attendee_Comment> commentArrayList, ArrayList<Exhibitor_DetailImage> imageArrayList) {
        this.feed_id = feed_id;
        this.sender_id = sender_id;
        this.time = time;
        this.sender_name = sender_name;
        this.sender_logo = sender_logo;
        this.total_like = total_like;
        this.is_like = is_like;
        this.time_stamp = time_stamp;
        this.total_comment = total_comment;
        this.commentArrayList = commentArrayList;
        this.imageArrayList = imageArrayList;
    }


    public photoSection(String feed_id, String sender_id, String time, String sender_name, String sender_logo, String total_like, String is_like, String time_stamp, String total_comment, ArrayList<Attendee_Comment> commentArrayList, String str_image) {
        this.feed_id = feed_id;
        this.sender_id = sender_id;
        this.time = time;
        this.sender_name = sender_name;
        this.sender_logo = sender_logo;
        this.total_like = total_like;
        this.is_like = is_like;
        this.time_stamp = time_stamp;
        this.total_comment = total_comment;
        this.commentArrayList = commentArrayList;
        this.str_image = str_image;
    }

    public String getStr_image() {
        return str_image;
    }

    public void setStr_image(String str_image) {
        this.str_image = str_image;
    }

    public String getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(String feed_id) {
        this.feed_id = feed_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getSender_logo() {
        return sender_logo;
    }

    public void setSender_logo(String sender_logo) {
        this.sender_logo = sender_logo;
    }

    public String getTotal_like() {
        return total_like;
    }

    public void setTotal_like(String total_like) {
        this.total_like = total_like;
    }

    public String getIs_like() {
        return is_like;
    }

    public void setIs_like(String is_like) {
        this.is_like = is_like;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getTotal_comment() {
        return total_comment;
    }

    public void setTotal_comment(String total_comment) {
        this.total_comment = total_comment;
    }

    public ArrayList<Attendee_Comment> getCommentArrayList() {
        return commentArrayList;
    }

    public void setCommentArrayList(ArrayList<Attendee_Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    public ArrayList<Exhibitor_DetailImage> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(ArrayList<Exhibitor_DetailImage> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }
}
