package com.allintheloop.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aiyaz on 1/8/17.
 */

public class Languages implements Serializable {
    @SerializedName("lang_id")
    @Expose
    public String lang_id;

    @SerializedName("lang_name")
    @Expose
    public String lang_name;

    @SerializedName("lang_icon")
    @Expose
    public String lang_icon;

    @SerializedName("lang_default")
    @Expose
    public String lang_default;
}

