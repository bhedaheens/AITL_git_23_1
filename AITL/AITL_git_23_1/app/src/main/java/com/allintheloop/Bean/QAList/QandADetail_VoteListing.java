package com.allintheloop.Bean.QAList;

/**
 * Created by Aiyaz on 7/6/17.
 */

public class QandADetail_VoteListing {
    String Id, Message, username, Logo, votes, users_vote, is_highest, sessionId, Role_id, userId, android_id, isAccpeted, show_on_web;

    public QandADetail_VoteListing(String id, String message, String username, String logo, String votes, String users_vote, String is_highest, String sessionId, String Role_id, String userId, String android_id, String isAccpeted, String show_on_web) {
        this.Id = id;
        this.Message = message;
        this.username = username;
        this.Logo = logo;
        this.votes = votes;
        this.users_vote = users_vote;
        this.is_highest = is_highest;
        this.sessionId = sessionId;
        this.Role_id = Role_id;
        this.userId = userId;
        this.android_id = android_id;
        this.isAccpeted = isAccpeted;
        this.show_on_web = show_on_web;
    }


    public String getShow_on_web() {
        return show_on_web;
    }

    public void setShow_on_web(String show_on_web) {
        this.show_on_web = show_on_web;
    }

    public String getIsAccpeted() {
        return isAccpeted;
    }

    public void setIsAccpeted(String isAccpeted) {
        this.isAccpeted = isAccpeted;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRole_id() {
        return Role_id;
    }

    public void setRole_id(String role_id) {
        Role_id = role_id;
    }

    public String getIs_highest() {
        return is_highest;
    }

    public void setIs_highest(String is_highest) {
        this.is_highest = is_highest;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public String getUsers_vote() {
        return users_vote;
    }

    public void setUsers_vote(String users_vote) {
        this.users_vote = users_vote;
    }
}
