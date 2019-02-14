package com.allintheloop.Bean;

/**
 * Created by nteam on 16/6/16.
 */
public class Question {
    String q_id, q_type, question, ans;

    public Question(String q_id, String q_type, String question, String ans) {
        this.q_id = q_id;
        this.q_type = q_type;
        this.question = question;
        this.ans = ans;
    }

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getQ_type() {
        return q_type;
    }

    public void setQ_type(String q_type) {
        this.q_type = q_type;
    }

    public String getQuestion() {
        return question;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
