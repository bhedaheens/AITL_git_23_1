package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 19/6/17.
 */

public class GamificationPointListing {
    String point, rank;

    public GamificationPointListing(String point, String rank) {
        this.point = point;
        this.rank = rank;
    }


    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
