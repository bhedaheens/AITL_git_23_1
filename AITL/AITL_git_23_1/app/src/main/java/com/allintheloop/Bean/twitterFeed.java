package com.allintheloop.Bean;

/**
 * Created by nteam on 2/8/16.
 */
public class twitterFeed {
    String id, name, time, screen_name, profile_image, media_url, feed_desc;

    public twitterFeed(String id, String name, String feed_desc, String time, String screen_name, String profile_image, String media_url) {
        this.id = id;
        this.name = name;
        this.feed_desc = feed_desc;
        this.time = time;
        this.screen_name = screen_name;
        this.profile_image = profile_image;
        this.media_url = media_url;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getFeed_desc() {
        return feed_desc;
    }

    public void setFeed_desc(String feed_desc) {
        this.feed_desc = feed_desc;
    }
}
