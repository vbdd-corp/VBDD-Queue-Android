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


    public ArrayList<Integer> getVisitorsIds() {
        return visitorsIds;
    }


    public Integer getCurrentVisitor() {
        return currentVisitor;
    }


    @Override
    public String toString() {
        StringBuilder toReturn = new StringBuilder();
        for (Integer id : visitorsIds) {
            toReturn.append(id).append(", ");
        }

        toReturn = new StringBuilder(toReturn.substring(0, toReturn.length() - 1).substring(0, toReturn.length() - 1));
        return toReturn.toString();
    }
}
