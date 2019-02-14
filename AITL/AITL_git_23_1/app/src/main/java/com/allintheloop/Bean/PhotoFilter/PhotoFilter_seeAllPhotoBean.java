package com.allintheloop.Bean.PhotoFilter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoFilter_seeAllPhotoBean {


    @SerializedName("data")
    @Expose
    private List<Listing> data = null;

    public List<Listing> getData() {
        return data;
    }

    public void setData(List<Listing> data) {
        this.data = data;
    }

    public class Listing {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("id")
        @Expose
        private String id;

        boolean isChecked = false;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
