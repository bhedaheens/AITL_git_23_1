package com.allintheloop.Bean.QAList;

/**
 * Created by Aiyaz on 15/3/17.
 */

public class QaModuleData {
    String Id, Session_name, Event_id, session_date, start_time, endtime, fulltime, total_question;

    public QaModuleData(String id, String session_name, String event_id, String session_date, String start_time, String endtime, String fulltime, String total_question) {
        Id = id;
        Session_name = session_name;
        Event_id = event_id;
        this.session_date = session_date;
        this.start_time = start_time;
        this.endtime = endtime;
        this.fulltime = fulltime;
        this.total_question = total_question;
    }

    public String getTotal_question() {
        return total_question;
    }

    public void setTotal_question(String total_question) {
        this.total_question = total_question;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSession_name() {
        return Session_name;
    }

    public void setSession_name(String session_name) {
        Session_name = session_name;
    }

    public String getEvent_id() {
        return Event_id;
    }

    public void setEvent_id(String event_id) {
        Event_id = event_id;
    }

    public String getSession_date() {
        return session_date;
    }

    public void setSession_date(String session_date) {
        this.session_date = session_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getFulltime() {
        return fulltime;
    }

    public void setFulltime(String fulltime) {
        this.fulltime = fulltime;
    }
}
