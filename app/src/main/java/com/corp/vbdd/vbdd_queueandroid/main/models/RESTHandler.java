package com.corp.vbdd.vbdd_queueandroid.main.models;

import retrofit2.Retrofit;

public class RESTHandler {

    private static final String __BACK_URL__ = "http://localhost:9428";
    private QueueService queueService;

    public RESTHandler() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(__BACK_URL__)
                .build();
        this.queueService = retrofit.create(QueueService.class);
    }

    public void nextPerson(Integer queueId) {
        this.queueService.nextPerson(queueId);
    }
}
