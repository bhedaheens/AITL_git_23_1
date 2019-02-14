package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 17/8/16.
 */
public class PledgeItemList {
    String name, thumb, pledge_amt, created_date;

    public PledgeItemList(String name, String thumb, String pledge_amt, String created_date) {
        this.name = name;
        this.thumb = thumb;
        this.pledge_amt = pledge_amt;
        this.created_date = created_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPledge_amt() {
        return pledge_amt;
    }

    public void setPledge_amt(String pledge_amt) {
        this.pledge_amt = pledge_amt;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
