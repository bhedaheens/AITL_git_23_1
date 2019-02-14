package com.allintheloop.Bean.HomeData;

/**
 * Created by n-team on 8/12/17.
 */

public class BannerImageData {
    String image, url;

    public BannerImageData(String image, String url) {
        this.image = image;
        this.url = url;
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
