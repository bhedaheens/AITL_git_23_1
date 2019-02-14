package com.allintheloop.Bean.RequestMeeting;

/**
 * Created by Aiyaz on 13/1/17.
 */

public class ViewRequestMettigDateTime {
    String id, heading, time, api_date_time;

    public ViewRequestMettigDateTime(String id, String heading, String time, String api_date_time) {
        this.id = id;
        this.heading = heading;
        this.time = time;
        this.api_date_time = api_date_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getApi_date_time() {
        return api_date_time;
    }

    public void setApi_date_time(String api_date_time) {
        this.api_date_time = api_date_time;
    }
}
