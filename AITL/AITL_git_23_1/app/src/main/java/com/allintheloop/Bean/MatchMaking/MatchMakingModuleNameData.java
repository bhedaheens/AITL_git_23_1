package com.allintheloop.Bean.MatchMaking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MatchMakingModuleNameData implements Serializable {

    @SerializedName("modules_name")
    @Expose
    private List<ModulesName> modulesName = null;

    public List<ModulesName> getModulesName() {
        return modulesName;
    }

    public void setModulesName(List<ModulesName> modulesName) {
        this.modulesName = modulesName;
    }

    public class ModulesName {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("menuname")
        @Expose
        private String menuname;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMenuname() {
            return menuname;
        }

        public void setMenuname(String menuname) {
            this.menuname = menuname;
        }
    }


}
