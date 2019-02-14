package com.allintheloop.Bean.Attendee;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AttendeeFilterList implements Serializable {
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }


    public class Data implements Serializable {
        @SerializedName("keywords")
        @Expose
        private List<String> keywords = null;
        @SerializedName("column_name")
        @Expose
        private String columnName;
        @SerializedName("category")
        @Expose
        private String category;
        @SerializedName("id")
        @Expose
        private String id;


        @SerializedName("k")
        @Expose
        private String k;

        boolean isCheck = false;


        public String getK() {
            return k;
        }

        public void setK(String k) {
            this.k = k;
        }

        List<AttendeeKeywordData> keywordData;

        public List<AttendeeKeywordData> getKeywordData() {
            return keywordData;
        }

        public void setKeywordData(List<AttendeeKeywordData> keywordData) {
            this.keywordData = keywordData;
        }


        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }
    }
}
