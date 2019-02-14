package com.allintheloop.Bean.ExhibitorListClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nteam on 19/5/16.
 */
public class FavoritedExhibitor implements Serializable {

    @SerializedName("data")
    @Expose
    private ArrayList<FavoriteExhibitor> favoriteExhibitors = new ArrayList<>();


    public ArrayList<FavoriteExhibitor> getFavoriteExhibitors() {
        return favoriteExhibitors;
    }

    public void setFavoriteExhibitors(ArrayList<FavoriteExhibitor> favoriteExhibitors) {
        this.favoriteExhibitors = favoriteExhibitors;
    }


    public class FavoriteExhibitor {
        @SerializedName("exhibitor_page_id")
        @Expose
        private String exhibitor_page_id;

        @SerializedName("exhibitor_user_id")
        @Expose
        private String exhibitor_user_id;

        public String getExhibitor_page_id() {
            return exhibitor_page_id;
        }

        public void setExhibitor_page_id(String exhibitor_page_id) {
            this.exhibitor_page_id = exhibitor_page_id;
        }

        public String getExhibitor_user_id() {
            return exhibitor_user_id;
        }

        public void setExhibitor_user_id(String exhibitor_user_id) {
            this.exhibitor_user_id = exhibitor_user_id;
        }
    }

}
