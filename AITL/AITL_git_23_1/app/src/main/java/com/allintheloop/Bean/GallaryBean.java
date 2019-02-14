package com.allintheloop.Bean;

/**
 * Created by KRUNAL on 6/1/2016.
 */
public class GallaryBean {

    String images, status, img_status;

    public GallaryBean(String images, String status) {
        this.images = images;
        this.status = status;
    }

    public GallaryBean(String images, String status, String img_status) {
        this.images = images;
        this.status = status;
        this.img_status = img_status;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImg_status() {
        return img_status;
    }

    public void setImg_status(String img_status) {
        this.img_status = img_status;
    }
}
