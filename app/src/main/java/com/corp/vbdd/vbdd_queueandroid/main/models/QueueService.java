package com.corp.vbdd.vbdd_queueandroid.main.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface QueueService {

    @GET("/api/queues")
    Call<List<Queue>> getQueuesList();

    @GET("/api/queues/{queueId}")
    Call<Queue> getQueue(@Path("queueId") Integer queueId);

    @PUT("/api/queues/{queueId}/next-visitor")
    Call<Visitor> nextPerson(@Path("queueId") Integer queueId);

    @PUT("/api/queues/{queueId}/previous-visitor")
    Call<Visitor> previousPerson(@Path("queueId") Integer queueId);


}
