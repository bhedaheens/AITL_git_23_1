package com.allintheloop.Bean.Attendee;

/**
 * Created by nteam on 4/7/16.
 */
public class Attendee_Comment {
    String comment_id, user_name, comment, logo, image, Time, time_stamp, tag, user_id;

    public Attendee_Comment(String comment_id, String user_id, String user_name, String comment, String logo, String image, String time, String time_stamp, String tag) {
        this.comment_id = comment_id;
        this.user_name = user_name;
        this.comment = comment;
        this.logo = logo;
        this.image = image;
        Time = time;
        this.time_stamp = time_stamp;
        this.tag = tag;
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
