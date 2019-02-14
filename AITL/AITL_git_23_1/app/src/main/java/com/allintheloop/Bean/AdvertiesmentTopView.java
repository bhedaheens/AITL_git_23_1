package com.allintheloop.Bean;

/**
 * Created by nteam on 17/11/17.
 */

public class AdvertiesmentTopView {
    String image, url, advertiesmentId;

    public AdvertiesmentTopView(String image, String url, String advertiesmentId) {
        this.image = image;
        this.url = url;
        this.advertiesmentId = advertiesmentId;
    }

    public String getAdvertiesmentId() {
        return advertiesmentId;
    }

    public void setAdvertiesmentId(String advertiesmentId) {
        this.advertiesmentId = advertiesmentId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
