package com.allintheloop.Bean.AgendaData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by nteam on 4/12/17.
 */

public class AgendaData {

    @SerializedName("agenda_list")
    @Expose
    ArrayList<Agenda> agenda_list;

    @SerializedName("category")
    @Expose
    ArrayList<AgendaCategory> category;

    @SerializedName("category_relation")
    @Expose
    ArrayList<AgendaCategoryRelation> category_relation;

    @SerializedName("types")
    @Expose
    ArrayList<AgendaType> types;

    public ArrayList<Agenda> getAgenda_list() {
        return agenda_list;
    }

    public void setAgenda_list(ArrayList<Agenda> agenda_list) {
        this.agenda_list = agenda_list;
    }

    public ArrayList<AgendaCategory> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<AgendaCategory> category) {
        this.category = category;
    }

    public ArrayList<AgendaCategoryRelation> getCategory_relation() {
        return category_relation;
    }

    public void setCategory_relation(ArrayList<AgendaCategoryRelation> category_relation) {
        this.category_relation = category_relation;
    }

    public ArrayList<AgendaType> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<AgendaType> types) {
        this.types = types;
    }
}
