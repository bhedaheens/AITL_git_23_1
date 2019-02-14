package com.allintheloop.Bean.AgendaData;

/**
 * Created by nteam on 17/8/16.
 */
public class AgendaBookStatus {
    String heading, start_time, book, resion;

    public AgendaBookStatus(String heading, String start_time, String book, String resion) {
        this.heading = heading;
        this.start_time = start_time;
        this.book = book;
        this.resion = resion;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getResion() {
        return resion;
    }

    public void setResion(String resion) {
        this.resion = resion;
    }
}
