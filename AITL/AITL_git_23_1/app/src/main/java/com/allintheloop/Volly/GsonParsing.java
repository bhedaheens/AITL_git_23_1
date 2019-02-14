package com.allintheloop.Volly;


import com.google.gson.Gson;

public class GsonParsing<T> {

    public Object parseJsonObject(String output, T objectType) {
        Gson gson = new Gson();
        Object object = gson.fromJson(output.toString(), objectType.getClass());
        return object;
    }
}
