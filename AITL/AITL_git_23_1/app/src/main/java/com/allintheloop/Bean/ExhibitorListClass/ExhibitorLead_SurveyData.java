package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nteam on 11/9/17.
 */

public class ExhibitorLead_SurveyData {

    @SerializedName("q_id")
    @Expose
    private String qId;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("exibitor_user_id")
    @Expose
    private String exibitorUserId;


    @SerializedName("previous_redirectId")
    @Expose
    private String previous_redirectId = "";


    @SerializedName("Question")
    @Expose
    private String question;
    @SerializedName("Question_type")
    @Expose
    private String questionType;
    @SerializedName("Option")
    @Expose
    private List<String> option = null;


    @SerializedName("survey_que_ans")
    @Expose
    private List<String> survey_que_ans = null;


    @SerializedName("show_commentbox")
    @Expose
    private String showCommentbox;
    @SerializedName("commentbox_display_style")
    @Expose
    private String commentboxDisplayStyle;
    @SerializedName("commentbox_label_text")
    @Expose
    private String commentboxLabelText;
    @SerializedName("sort_order")
    @Expose
    private String sortOrder;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("skip_logic")
    @Expose
    private String skipLogic;
    @SerializedName("redirectids")
    @Expose
    private Redirectids redirectids;
    @SerializedName("a_redirectids")
    @Expose
    private List<ARedirectid> aRedirectids = null;
    @SerializedName("redirectid")
    @Expose
    private String redirectid;

    public String getQId() {
        return qId;
    }

    public void setQId(String qId) {
        this.qId = qId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getExibitorUserId() {
        return exibitorUserId;
    }

    public void setExibitorUserId(String exibitorUserId) {
        this.exibitorUserId = exibitorUserId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public List<String> getOption() {
        return option;
    }

    public void setOption(List<String> option) {
        this.option = option;
    }


    public List<String> getSurvey_que_ans() {
        return survey_que_ans;
    }

    public void setSurvey_que_ans(List<String> survey_que_ans) {
        this.survey_que_ans = survey_que_ans;
    }

    public String getShowCommentbox() {
        return showCommentbox;
    }

    public void setShowCommentbox(String showCommentbox) {
        this.showCommentbox = showCommentbox;
    }

    public String getCommentboxDisplayStyle() {
        return commentboxDisplayStyle;
    }

    public void setCommentboxDisplayStyle(String commentboxDisplayStyle) {
        this.commentboxDisplayStyle = commentboxDisplayStyle;
    }

    public String getCommentboxLabelText() {
        return commentboxLabelText;
    }

    public void setCommentboxLabelText(String commentboxLabelText) {
        this.commentboxLabelText = commentboxLabelText;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSkipLogic() {
        return skipLogic;
    }

    public void setSkipLogic(String skipLogic) {
        this.skipLogic = skipLogic;
    }

    public Redirectids getRedirectids() {
        return redirectids;
    }

    public void setRedirectids(Redirectids redirectids) {
        this.redirectids = redirectids;
    }

    public List<ARedirectid> getARedirectids() {
        return aRedirectids;
    }

    public void setARedirectids(List<ARedirectid> aRedirectids) {
        this.aRedirectids = aRedirectids;
    }

    public String getRedirectid() {
        return redirectid;
    }

    public void setRedirectid(String redirectid) {
        this.redirectid = redirectid;
    }

    public String getPrevious_redirectId() {
        return previous_redirectId;
    }

    public void setPrevious_redirectId(String previous_redirectId) {
        this.previous_redirectId = previous_redirectId;
    }

    public class Redirectids {

        @SerializedName("fine")
        @Expose
        private String fine;
        @SerializedName("bad")
        @Expose
        private String bad;

        public String getFine() {
            return fine;
        }

        public void setFine(String fine) {
            this.fine = fine;
        }

        public String getBad() {
            return bad;
        }

        public void setBad(String bad) {
            this.bad = bad;
        }

    }


    public class ARedirectid {

        @SerializedName("option")
        @Expose
        private String option;
        @SerializedName("redirect_id")
        @Expose
        private String redirectId;

        public String getOption() {
            return option;
        }

        public void setOption(String option) {
            this.option = option;
        }

        public String getRedirectId() {
            return redirectId;
        }

        public void setRedirectId(String redirectId) {
            this.redirectId = redirectId;
        }

    }


}
