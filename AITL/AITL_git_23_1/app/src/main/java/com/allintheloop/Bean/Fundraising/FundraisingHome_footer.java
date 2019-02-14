package com.allintheloop.Bean.Fundraising;

/**
 * Created by nteam on 10/6/16.
 */
public class FundraisingHome_footer {
    String first_name, last_name, logo, product_name, amt, tag;

    public FundraisingHome_footer(String first_name, String last_name, String logo, String product_name, String amt, String tag) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.logo = logo;
        this.product_name = product_name;
        this.amt = amt;
        this.tag = tag;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
