package com.corp.vbdd.vbdd_queueandroid.main.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Queue {

    @SerializedName("id")
    private Integer queueId;

    @SerializedName("visitorsIds")
    private ArrayList<Integer> visitorsIds;


    @SerializedName("currentIndex")
    private Integer currentVisitor;

    public Integer getQueueId() {
        return queueId;
    }

    public Queue setQueueId(Integer queueId) {
        this.queueId = queueId;
        return this;
    }

    public ArrayList<Integer> getVisitorsIds() {
        return visitorsIds;
    }

    public Queue setVisitorsIds(ArrayList<Integer> visitorsIds) {
        this.visitorsIds = visitorsIds;
        return this;
    }

    public Integer getCurrentVisitor() {
        return currentVisitor;
    }

    public Queue setCurrentVisitor(Integer currentVisitor) {
        this.currentVisitor = currentVisitor;
        return this;
    }
}
