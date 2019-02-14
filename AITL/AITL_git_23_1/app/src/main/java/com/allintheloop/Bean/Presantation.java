package com.allintheloop.Bean;

/**
 * Created by nteam on 8/6/16.
 */
public class Presantation {
    String id, slide_name, start_time, start_date, end_time, end_date, presan_filetype, image, place_name;

    public Presantation(String start_time, String slide_name) {
        this.start_time = start_time;
        this.slide_name = slide_name;
    }

    public Presantation(String id, String slide_name, String start_time, String start_date, String end_time, String end_date, String presan_filetype, String image, String place_name) {
        this.id = id;
        this.slide_name = slide_name;
        this.start_time = start_time;
        this.start_date = start_date;
        this.end_time = end_time;
        this.end_date = end_date;
        this.presan_filetype = presan_filetype;
        this.image = image;
        this.place_name = place_name;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlide_name() {
        return slide_name;
    }

    public void setSlide_name(String slide_name) {
        this.slide_name = slide_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPresan_filetype() {
        return presan_filetype;
    }

    public void setPresan_filetype(String presan_filetype) {
        this.presan_filetype = presan_filetype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
