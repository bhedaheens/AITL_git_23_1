package com.allintheloop.Bean.ActivityModule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity_SurveyResult {
    @SerializedName("answer_key")
    @Expose
    private String answerKey;
    @SerializedName("answer_value")
    @Expose
    private String answerValue;
    @SerializedName("color")
    @Expose
    private String color;

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
