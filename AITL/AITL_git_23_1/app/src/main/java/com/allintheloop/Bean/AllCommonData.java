package com.allintheloop.Bean;

/**
 * Created by Aiyaz on 27/3/17.
 */

public class AllCommonData {
    String Id, Firstname, Lastname, Company_name, Title, Email, Logo, type, fullName, heading;

    public AllCommonData(String id, String firstname, String lastname, String company_name, String title, String email, String logo, String type, String fullName, String heading) {
        this.Id = id;
        this.Firstname = firstname;
        this.Lastname = lastname;
        this.Company_name = company_name;
        this.Title = title;
        this.Email = email;
        this.Logo = logo;
        this.type = type;
        this.fullName = fullName;
        this.heading = heading;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String firstname) {
        Firstname = firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
