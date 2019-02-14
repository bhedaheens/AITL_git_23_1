package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 10/6/17.
 */

public class SurveyCategoryListing {
    String category_id, survey_name;

    public SurveyCategoryListing(String category_id, String survey_name) {
        this.category_id = category_id;
        this.survey_name = survey_name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSurvey_name() {
        return survey_name;
    }

    public void setSurvey_name(String survey_name) {
        this.survey_name = survey_name;
    }
}
