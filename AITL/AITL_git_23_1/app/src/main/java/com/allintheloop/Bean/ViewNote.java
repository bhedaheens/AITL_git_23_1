package com.allintheloop.Bean;

/**
 * Created by nteam on 2/6/16.
 */
public class ViewNote {
    String id, heading, desc, created_at, organizor_id, title, Menu_id, Module_id, is_custom_title;
    String is_cms;

    public ViewNote(String id, String heading, String desc, String created_at, String organizor_id, String title, String Menu_id, String Module_id, String is_custom_title, String is_cms) {
        this.id = id;
        this.heading = heading;
        this.desc = desc;
        this.created_at = created_at;
        this.organizor_id = organizor_id;
        this.title = title;
        this.Menu_id = Menu_id;
        this.Module_id = Module_id;
        this.is_custom_title = is_custom_title;
        this.is_cms = is_cms;
    }

    public String getIs_cms() {
        return is_cms;
    }

    public void setIs_cms(String is_cms) {
        this.is_cms = is_cms;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getOrganizor_id() {
        return organizor_id;
    }

    public void setOrganizor_id(String organizor_id) {
        this.organizor_id = organizor_id;
    }

    public String getMenu_id() {
        return Menu_id;
    }

    public void setMenu_id(String menu_id) {
        Menu_id = menu_id;
    }

    public String getModule_id() {
        return Module_id;
    }

    public void setModule_id(String module_id) {
        Module_id = module_id;
    }

    public String getIs_custom_title() {
        return is_custom_title;
    }

    public void setIs_custom_title(String is_custom_title) {
        this.is_custom_title = is_custom_title;
    }
}