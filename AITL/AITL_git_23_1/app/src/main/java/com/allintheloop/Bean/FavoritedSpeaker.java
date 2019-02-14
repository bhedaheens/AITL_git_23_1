package com.allintheloop.Bean;

import com.allintheloop.Bean.SponsorClass.FavoritedSponsor;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 11/5/18.
 */

public class FavoritedSpeaker {
    @SerializedName("data")
    @Expose
    private ArrayList<FavoriteSpeaker> favoriteSpeakers = new ArrayList<>();

    public ArrayList<FavoriteSpeaker> getFavoriteSpeakers() {
        return favoriteSpeakers;
    }

    public void setFavoriteSpeakers(ArrayList<FavoriteSpeaker> favoriteSpeakers) {
        this.favoriteSpeakers = favoriteSpeakers;
    }

    public class FavoriteSpeaker {
        @SerializedName("speaker_id")
        @Expose
        private String speaker_id;

        public String getSpeakerId() {
            return speaker_id;
        }

        public void setSpeakerId(String speaker_id) {
            this.speaker_id = speaker_id;
        }
    }
}
