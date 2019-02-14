package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 27/3/17.
 */

public class UnreadPrivateMessageList {
    String Sendername, Sender_id, Senderlogo, unread_count;

    public UnreadPrivateMessageList(String sendername, String sender_id, String senderlogo, String unread_count) {
        this.Sendername = sendername;
        this.Sender_id = sender_id;
        this.Senderlogo = senderlogo;
        this.unread_count = unread_count;
    }

    public String getSendername() {
        return Sendername;
    }

    public void setSendername(String sendername) {
        Sendername = sendername;
    }

    public String getSender_id() {
        return Sender_id;
    }

    public void setSender_id(String sender_id) {
        Sender_id = sender_id;
    }

    public String getSenderlogo() {
        return Senderlogo;
    }

    public void setSenderlogo(String senderlogo) {
        Senderlogo = senderlogo;
    }

    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }
}
