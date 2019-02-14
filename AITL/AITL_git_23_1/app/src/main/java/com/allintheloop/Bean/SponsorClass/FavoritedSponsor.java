package com.allintheloop.Bean.SponsorClass;

import com.allintheloop.Bean.ExhibitorListClass.FavoritedExhibitor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 1/12/17.
 */

public class FavoritedSponsor {

    @SerializedName("data")
    @Expose
    private ArrayList<FavoriteSponsor> favoriteSponsors = new ArrayList<>();

    public ArrayList<FavoriteSponsor> getFavoriteSponsors() {
        return favoriteSponsors;
    }

    public void setFavoriteSponsors(ArrayList<FavoriteSponsor> favoriteSponsors) {
        this.favoriteSponsors = favoriteSponsors;
    }

    public class FavoriteSponsor {
        @SerializedName("sponser_id")
        @Expose
        private String sponserId;

        public String getSponserId() {
            return sponserId;
        }

        public void setSponserId(String sponserId) {
            this.sponserId = sponserId;
        }
    }


}
