package com.corp.vbdd.vbdd_queueandroid.main.models;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTHandler {

    private static final String __BACK_URL__ = "https://10.0.2.2:9428";
    private QueueService queueService;

    public RESTHandler() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(__BACK_URL__)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.queueService = retrofit.create(QueueService.class);
    }

    public void nextPerson(Integer queueId) {
        Call<Visitor> call = queueService.nextPerson(queueId);
        call.enqueue(new Callback<Visitor>() {

            @Override
            public void onResponse(Call<Visitor> call, Response<Visitor> response) {
                Log.d("xx", "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<Visitor> call, Throwable t) {
                Log.d("xx", "onFailure: " + t.getMessage());

            }
        });
     }
}