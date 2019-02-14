package com.allintheloop.Bean.Attendee;

import com.allintheloop.Bean.ExhibitorListClass.Exhibitor_DetailImage;

import java.util.ArrayList;

/**
 * Created by nteam on 23/6/16.
 */
public class Attendee_message {
    String res_id, res_message, res_senderName, res_reciverName, res_senderLogo, res_timeStamp, tag, sender_id, str_img, is_clickable, desiredRecevier = "";
    ArrayList<Exhibitor_DetailImage> imageArrayList;
    ArrayList<Attendee_Comment> commentArrayList;

    public Attendee_message(String res_id, String sender_id, String res_message, String res_senderName, String res_reciverName, String res_senderLogo, String res_timeStamp, String str_img, ArrayList<Attendee_Comment> commentArrayList, String tag, String is_clickable) {
        this.res_message = res_message;
        this.res_senderName = res_senderName;
        this.res_reciverName = res_reciverName;
        this.res_senderLogo = res_senderLogo;
        this.res_timeStamp = res_timeStamp;
        this.str_img = str_img;
        this.commentArrayList = commentArrayList;
        this.tag = tag;
        this.res_id = res_id;
        this.sender_id = sender_id;
        this.is_clickable = is_clickable;
    }

    public Attendee_message(String res_id, String sender_id, String res_message, String res_senderName, String res_reciverName, String res_senderLogo, String res_timeStamp, String str_img, ArrayList<Attendee_Comment> commentArrayList, String tag, String is_clickable, String desiredRecevier) {
        this.res_message = res_message;
        this.res_senderName = res_senderName;
        this.res_reciverName = res_reciverName;
        this.res_senderLogo = res_senderLogo;
        this.res_timeStamp = res_timeStamp;
        this.str_img = str_img;
        this.commentArrayList = commentArrayList;
        this.tag = tag;
        this.res_id = res_id;
        this.sender_id = sender_id;
        this.is_clickable = is_clickable;
        this.desiredRecevier = desiredRecevier;
    }


    public String getDesiredRecevier() {
        return desiredRecevier;
    }

    public void setDesiredRecevier(String desiredRecevier) {
        this.desiredRecevier = desiredRecevier;
    }

    public String getStr_img() {
        return str_img;
    }

    public void setStr_img(String str_img) {
        this.str_img = str_img;
    }

    public String getRes_id() {
        return res_id;
    }

    public void setRes_id(String res_id) {
        this.res_id = res_id;
    }

    public String getRes_message() {
        return res_message;
    }

    public void setRes_message(String res_message) {
        this.res_message = res_message;
    }

    public String getRes_senderName() {
        return res_senderName;
    }

    public void setRes_senderName(String res_senderName) {
        this.res_senderName = res_senderName;
    }

    public String getRes_reciverName() {
        return res_reciverName;
    }

    public void setRes_reciverName(String res_reciverName) {
        this.res_reciverName = res_reciverName;
    }

    public String getRes_senderLogo() {
        return res_senderLogo;
    }

    public void setRes_senderLogo(String res_senderLogo) {
        this.res_senderLogo = res_senderLogo;
    }

    public String getRes_timeStamp() {
        return res_timeStamp;
    }

    public void setRes_timeStamp(String res_timeStamp) {
        this.res_timeStamp = res_timeStamp;
    }

    public ArrayList<Exhibitor_DetailImage> getImageArrayList() {
        return imageArrayList;
    }

    public void setImageArrayList(ArrayList<Exhibitor_DetailImage> imageArrayList) {
        this.imageArrayList = imageArrayList;
    }

    public ArrayList<Attendee_Comment> getCommentArrayList() {
        return commentArrayList;
    }

    public void setCommentArrayList(ArrayList<Attendee_Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getIs_clickable() {
        return is_clickable;
    }

    public void setIs_clickable(String is_clickable) {
        this.is_clickable = is_clickable;
    }
}