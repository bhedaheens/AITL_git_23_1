package com.allintheloop.Bean.Map;

import com.allintheloop.Bean.Map.MapImageSession;

import java.util.ArrayList;

/**
 * Created by nteam on 14/6/16.
 */
public class ImageMappingDetail {
    String coords, user_id, heading, stand_number, exid, first_name, last_name, email, companylogo, id, isSession;
    ArrayList<MapImageSession> imageSessions;

    public ImageMappingDetail(String coords, String user_id, String heading, String stand_number, String exid, String first_name, String last_name, String email, String companylogo, String isSession, ArrayList<MapImageSession> imageSessions) {
        this.coords = coords;
        this.user_id = user_id;
        this.heading = heading;
        this.stand_number = stand_number;
        this.exid = exid;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.companylogo = companylogo;
        this.isSession = isSession;
        this.imageSessions = imageSessions;
    }

    public ArrayList<MapImageSession> getImageSessions() {
        return imageSessions;
    }

    public void setImageSessions(ArrayList<MapImageSession> imageSessions) {
        this.imageSessions = imageSessions;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getStand_number() {
        return stand_number;
    }

    public void setStand_number(String stand_number) {
        this.stand_number = stand_number;
    }

    public String getExid() {
        return exid;
    }

    public void setExid(String exid) {
        this.exid = exid;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompanylogo() {
        return companylogo;
    }

    public void setCompanylogo(String companylogo) {
        this.companylogo = companylogo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsSession() {
        return isSession;
    }

    public void setIsSession(String isSession) {
        this.isSession = isSession;
    }
}
