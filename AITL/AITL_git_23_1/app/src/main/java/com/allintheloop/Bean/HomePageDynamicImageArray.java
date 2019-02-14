package com.allintheloop.Bean;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 29/5/17.
 */

public class HomePageDynamicImageArray {
    String ImageId, imageUrl, content;
    int height, width;
    ArrayList<HomeScreenMapDetailData> mapDetailDatas;

    public HomePageDynamicImageArray(String imageId, String imageUrl, String content, int height, int width, ArrayList<HomeScreenMapDetailData> mapDetailDatas) {
        this.ImageId = imageId;
        this.imageUrl = imageUrl;
        this.content = content;
        this.height = height;
        this.width = width;
        this.mapDetailDatas = mapDetailDatas;
    }

    public ArrayList<HomeScreenMapDetailData> getMapDetailDatas() {
        return mapDetailDatas;
    }

    public void setMapDetailDatas(ArrayList<HomeScreenMapDetailData> mapDetailDatas) {
        this.mapDetailDatas = mapDetailDatas;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageId() {
        return ImageId;
    }

    public void setImageId(String imageId) {
        ImageId = imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
