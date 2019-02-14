package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 6/6/16.
 */
public class Exhibitor_DetailImage {
    String image_link;
    String id;
    String tag, type;
    boolean isChanged;
    String chart_type;

    public Exhibitor_DetailImage(String image_link) {
        this.image_link = image_link;
    }


    public Exhibitor_DetailImage(String image_link, String id, String tag) {
        this.image_link = image_link;
        this.id = id;
        this.tag = tag;
    }

    public Exhibitor_DetailImage(String image_link, String tag) {
        this.image_link = image_link;
        this.tag = tag;
    }

    public Exhibitor_DetailImage(String image_link, String tag, boolean isChanged, String type, String chart_type) {
        this.type = type;
        this.image_link = image_link;
        this.tag = tag;
        this.isChanged = isChanged;
        this.chart_type = chart_type;
    }


    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setIsChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChart_type() {
        return chart_type;
    }

    public void setChart_type(String chart_type) {
        this.chart_type = chart_type;
    }
}
