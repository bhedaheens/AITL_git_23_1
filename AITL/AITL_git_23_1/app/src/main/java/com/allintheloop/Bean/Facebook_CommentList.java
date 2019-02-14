package com.allintheloop.Bean;

import java.util.ArrayList;

/**
 * Created by Aiyaz on 1/8/17.
 */

public class Facebook_CommentList {
    String str_time, str_name, message;
    ArrayList<String> str_imgUrList;

    public Facebook_CommentList(String str_time, String str_name, String message, ArrayList<String> str_imgUrList) {
        this.str_time = str_time;
        this.str_name = str_name;
        this.message = message;
        this.str_imgUrList = str_imgUrList;
    }

    public String getStr_time() {
        return str_time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getStr_name() {
        return str_name;
    }

    public void setStr_name(String str_name) {
        this.str_name = str_name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<String> getStr_imgUrList() {
        return str_imgUrList;
    }

    public void setStr_imgUrList(ArrayList<String> str_imgUrList) {
        this.str_imgUrList = str_imgUrList;
    }
}
