package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 27/12/16.
 */

public class NotificationListingData {
    String id, title, content;

    public NotificationListingData(String title) {
        this.title = title;
    }

    public NotificationListingData(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
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
}
