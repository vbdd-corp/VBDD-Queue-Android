package com.corp.vbdd.vbdd_queueandroid.main.models;

public class Queue {
    private Integer queueId;
    private Integer visitorsIds;
    private Integer nextVisitor;

    public Integer getQueueId() {
        return queueId;
    }

    public Queue setQueueId(Integer queueId) {
        this.queueId = queueId;
        return this;
    }

    public Integer getVisitorsIds() {
        return visitorsIds;
    }

    public Queue setVisitorsIds(Integer visitorsIds) {
        this.visitorsIds = visitorsIds;
        return this;
    }

    public Integer getNextVisitor() {
        return nextVisitor;
    }

    public Queue setNextVisitor(Integer nextVisitor) {
        this.nextVisitor = nextVisitor;
        return this;
    }
}
