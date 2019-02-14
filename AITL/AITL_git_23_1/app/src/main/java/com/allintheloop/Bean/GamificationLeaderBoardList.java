package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 16/6/17.
 */

public class GamificationLeaderBoardList {
    String id, Sender_name, Company_name, Title, logo, total, Role_id, color, rank, exhibitor_page_id;

    public GamificationLeaderBoardList(String id, String sender_name, String company_name, String title, String logo, String total, String role_id, String color, String rank) {
        this.id = id;
        this.Sender_name = sender_name;
        this.Company_name = company_name;
        this.Title = title;
        this.logo = logo;
        this.total = total;
        this.Role_id = role_id;
        this.color = color;
        this.rank = rank;
    }

    public GamificationLeaderBoardList(String id, String sender_name, String company_name, String title, String logo, String total, String role_id, String color, String rank, String exhibitor_page_id) {
        this.id = id;
        this.Sender_name = sender_name;
        this.Company_name = company_name;
        this.Title = title;
        this.logo = logo;
        this.total = total;
        this.Role_id = role_id;
        this.color = color;
        this.rank = rank;
        this.exhibitor_page_id = exhibitor_page_id;
    }

    public String getExhibitor_page_id() {
        return exhibitor_page_id;
    }

    public void setExhibitor_page_id(String exhibitor_page_id) {
        this.exhibitor_page_id = exhibitor_page_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender_name() {
        return Sender_name;
    }

    public void setSender_name(String sender_name) {
        Sender_name = sender_name;
    }

    public String getCompany_name() {
        return Company_name;
    }

    public void setCompany_name(String company_name) {
        Company_name = company_name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getRole_id() {
        return Role_id;
    }

    public void setRole_id(String role_id) {
        Role_id = role_id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
