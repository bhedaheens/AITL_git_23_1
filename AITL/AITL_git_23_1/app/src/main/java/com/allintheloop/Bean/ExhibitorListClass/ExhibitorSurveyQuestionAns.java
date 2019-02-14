package com.allintheloop.Bean.ExhibitorListClass;

/**
 * Created by nteam on 13/9/17.
 */

public class ExhibitorSurveyQuestionAns {
    String Q_id, Q_ans, Q_comments;


    public ExhibitorSurveyQuestionAns(String q_id, String q_ans, String q_comments) {
        this.Q_id = q_id;
        this.Q_ans = q_ans;
        this.Q_comments = q_comments;
    }

    public String getQ_id() {
        return Q_id;
    }

    public void setQ_id(String q_id) {
        Q_id = q_id;
    }

    public String getQ_ans() {
        return Q_ans;
    }

    public void setQ_ans(String q_ans) {
        Q_ans = q_ans;
    }

    public String getQ_comments() {
        return Q_comments;
    }

    public void setQ_comments(String q_comments) {
        Q_comments = q_comments;
    }
}
