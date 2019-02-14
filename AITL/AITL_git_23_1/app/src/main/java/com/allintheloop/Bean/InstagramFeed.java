package com.allintheloop.Bean;

/**
 * Created by nteam on 29/9/16.
 */
public class InstagramFeed {
    String id, comment_count, likes_count, low_image, high_image, link, user_name, user_profile, caption_text, type;

    public InstagramFeed(String id, String comment_count, String likes_count, String low_image, String high_image, String link, String user_name, String user_profile, String caption_text, String type) {
        this.id = id;
        this.comment_count = comment_count;
        this.likes_count = likes_count;
        this.low_image = low_image;
        this.high_image = high_image;
        this.link = link;
        this.user_name = user_name;
        this.user_profile = user_profile;
        this.caption_text = caption_text;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getLikes_count() {
        return likes_count;
    }

    public void setLikes_count(String likes_count) {
        this.likes_count = likes_count;
    }

    public String getLow_image() {
        return low_image;
    }

    public void setLow_image(String low_image) {
        this.low_image = low_image;
    }

    public String getHigh_image() {
        return high_image;
    }

    public void setHigh_image(String high_image) {
        this.high_image = high_image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getCaption_text() {
        return caption_text;
    }

    public void setCaption_text(String caption_text) {
        this.caption_text = caption_text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
