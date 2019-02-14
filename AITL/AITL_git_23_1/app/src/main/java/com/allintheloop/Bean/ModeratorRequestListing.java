package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 25/5/17.
 */

public class ModeratorRequestListing {

    String date, time, status, request_id, sender_id, reciver_id, sender_name, reciver_name;

    public ModeratorRequestListing(String date, String time, String status, String request_id, String sender_id, String reciver_id, String sender_name, String reciver_name) {

        this.date = date;
        this.time = time;
        this.status = status;
        this.request_id = request_id;
        this.sender_id = sender_id;
        this.reciver_id = reciver_id;
        this.sender_name = sender_name;
        this.reciver_name = reciver_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReciver_id() {
        return reciver_id;
    }

    public void setReciver_id(String reciver_id) {
        this.reciver_id = reciver_id;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReciver_name() {
        return reciver_name;
    }

    public void setReciver_name(String reciver_name) {
        this.reciver_name = reciver_name;
    }
}
