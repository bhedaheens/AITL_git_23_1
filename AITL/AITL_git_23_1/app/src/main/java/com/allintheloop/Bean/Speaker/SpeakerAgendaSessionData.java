package com.allintheloop.Bean.Speaker;

/**
 * Created by Aiyaz on 20/3/17.
 */

public class SpeakerAgendaSessionData {
    String Heading, Id, Start_date, Start_time, type, location;

    public SpeakerAgendaSessionData(String id, String heading, String start_date, String start_time, String type, String location) {
        this.Id = id;
        this.Heading = heading;
        this.Start_date = start_date;
        this.Start_time = start_time;
        this.type = type;
        this.location = location;
    }

    public String getHeading() {
        return Heading;
    }

    public void setHeading(String heading) {
        Heading = heading;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getStart_date() {
        return Start_date;
    }

    public void setStart_date(String start_date) {
        Start_date = start_date;
    }

    public String getStart_time() {
        return Start_time;
    }

    public void setStart_time(String start_time) {
        Start_time = start_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
