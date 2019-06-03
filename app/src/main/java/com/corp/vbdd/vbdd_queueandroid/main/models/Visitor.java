package com.corp.vbdd.vbdd_queueandroid.main.models;
import com.google.gson.annotations.SerializedName;

public class Visitor  {

    @SerializedName("id")
    private Integer visitorId;

    public Integer getId() {
        return visitorId;
    }

    public void setId(Integer id) {
        this.visitorId = id;
    }
}
