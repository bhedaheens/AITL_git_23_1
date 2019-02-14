package com.allintheloop.Bean.AgendaData;

import java.io.Serializable;

/**
 * Created by nteam on 10/5/16.
 */
public class Agenda_Time implements Serializable {

    String agenda_id = "", heading = "", start_time = "", start_date = "", end_time = "", end_date = "", map_title = "", address_map = "", agenda_timezone = "", placeleft = "", speaker = "", location = "", isAgenda = "", sessionImage = "", sessionType = "", exhi_user_id = "";
    String metting_id = "", exhibiotor_id = "", attendee_id = "", date = "", time = "", status = "", exhiHeading = "", stand_number = "", is_exhi = "";

    public Agenda_Time(String agenda_id, String heading, String start_time, String start_date, String end_time, String end_date, String map_title, String address_map, String agenda_timezone, String placeleft, String speaker, String location, String sessionImage, String sessionType) {
        this.agenda_id = agenda_id;
        this.heading = heading;
        this.start_time = start_time;
        this.start_date = start_date;
        this.end_time = end_time;
        this.end_date = end_date;
        this.map_title = map_title;
        this.address_map = address_map;
        this.agenda_timezone = agenda_timezone;
        this.placeleft = placeleft;
        this.speaker = speaker;
        this.location = location;
        this.sessionImage = sessionImage;
        this.sessionType = sessionType;
    }


    public Agenda_Time(String agenda_id, String heading, String start_time, String start_date, String end_time, String end_date, String map_title, String address_map, String agenda_timezone, String placeleft, String speaker, String location, String isAgenda, String sessionImage, String sessionType) {
        this.agenda_id = agenda_id;
        this.heading = heading;
        this.start_time = start_time;
        this.start_date = start_date;
        this.end_time = end_time;
        this.end_date = end_date;
        this.map_title = map_title;
        this.address_map = address_map;
        this.agenda_timezone = agenda_timezone;
        this.placeleft = placeleft;
        this.speaker = speaker;
        this.location = location;
        this.isAgenda = isAgenda;
        this.sessionImage = sessionImage;
        this.sessionType = sessionType;
    }

    public Agenda_Time(String metting_id, String exhibiotor_id, String attendee_id, String date, String time, String status, String exhiHeading, String stand_number, String isAgenda, String exhi_user_id, String is_exhi) {
        this.metting_id = metting_id;
        this.exhibiotor_id = exhibiotor_id;
        this.attendee_id = attendee_id;
        this.date = date;
        this.time = time;
        this.status = status;
        this.exhiHeading = exhiHeading;
        this.stand_number = stand_number;
        this.isAgenda = isAgenda;
        this.exhi_user_id = exhi_user_id;
        this.is_exhi = is_exhi;
    }


    public String getSessionImage() {
        return sessionImage;
    }

    public void setSessionImage(String sessionImage) {
        this.sessionImage = sessionImage;
    }

    public String getExhi_user_id() {
        return exhi_user_id;
    }

    public void setExhi_user_id(String exhi_user_id) {
        this.exhi_user_id = exhi_user_id;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getExhiHeading() {
        return exhiHeading;
    }

    public void setExhiHeading(String exhiHeading) {
        this.exhiHeading = exhiHeading;
    }

    public String getIsAgenda() {
        return isAgenda;
    }

    public void setIsAgenda(String isAgenda) {
        this.isAgenda = isAgenda;
    }

    public String getAgenda_id() {
        return agenda_id;
    }

    public void setAgenda_id(String agenda_id) {
        this.agenda_id = agenda_id;
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

    public String getMap_title() {
        return map_title;
    }

    public void setMap_title(String map_title) {
        this.map_title = map_title;
    }

    public String getAddress_map() {
        return address_map;
    }

    public void setAddress_map(String address_map) {
        this.address_map = address_map;
    }

    public String getAgenda_timezone() {
        return agenda_timezone;
    }

    public void setAgenda_timezone(String agenda_timezone) {
        this.agenda_timezone = agenda_timezone;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getPlaceleft() {
        return placeleft;
    }

    public void setPlaceleft(String placeleft) {
        this.placeleft = placeleft;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMetting_id() {
        return metting_id;
    }

    public void setMetting_id(String metting_id) {
        this.metting_id = metting_id;
    }

    public String getExhibiotor_id() {
        return exhibiotor_id;
    }

    public void setExhibiotor_id(String exhibiotor_id) {
        this.exhibiotor_id = exhibiotor_id;
    }

    public String getAttendee_id() {
        return attendee_id;
    }

    public void setAttendee_id(String attendee_id) {
        this.attendee_id = attendee_id;
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

    public String getStand_number() {
        return stand_number;
    }

    public void setStand_number(String stand_number) {
        this.stand_number = stand_number;
    }


    public String getIs_exhi() {
        return is_exhi;
    }

    public void setIs_exhi(String is_exhi) {
        this.is_exhi = is_exhi;
    }
}
