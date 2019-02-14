package com.allintheloop.Bean.editProfileAbout;

public class EditProfileKeywordUpdateData {
    public final String categoryName, keyword, categoryId, k;
    public final boolean isCheck, isKeywordCountupdate;

    public EditProfileKeywordUpdateData(String categoryName, String keyword, String categoryId, String k, boolean isCheck, boolean isKeywordCountupdate) {
        this.categoryName = categoryName;
        this.keyword = keyword;
        this.categoryId = categoryId;
        this.k = k;
        this.isCheck = isCheck;
        this.isKeywordCountupdate = isKeywordCountupdate;
    }

    public String getK() {
        return k;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public boolean isCheck() {
        return isCheck;
    }


    public boolean isKeywordCountupdate() {
        return isKeywordCountupdate;
    }
}
