package com.corp.vbdd.vbdd_queueandroid.main.models;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.corp.vbdd.vbdd_queueandroid.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RESTHandler {

    private static final String __BACK_URL__ = "http://" + Config.__IP_ADDRESS__ + ":9428";
    private final Context context;
    private QueueService queueService;

    public RESTHandler(Context context) {

        this.context = context;
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
                if (response.code() == 404) {
                    setInformation("Their is nobody left in the queue");
                } else {
                    setInformation("Nexted ! The next person's id is : " + response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Visitor> call, Throwable t) {
                setInformation("Failed to send request... Maybe timeout ?");
            }
        });
    }

    public ArrayList<Queue> getQueuesList() {

        ArrayList<Queue> queues = new ArrayList<>();

        Call<List<Queue>> call = queueService.getQueuesList();
        call.enqueue(new Callback<List<Queue>>() {

            @Override
            public void onResponse(Call<List<Queue>> call, Response<List<Queue>> response) {
                if (response.body() != null) {
                    queues.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Queue>> call, Throwable t) {
            }
        });
        return queues;
    }

    public void previousPerson(Integer queueId) {
        Call<Visitor> call = queueService.previousPerson(queueId);
        call.enqueue(new Callback<Visitor>() {

            @Override
            public void onResponse(Call<Visitor> call, Response<Visitor> response) {
                if (response.code() == 404) {
                    setInformation("Their is nobody left in the queue");
                } else {
                    setInformation("Previoused ! The previous person's id was (and is) : " + response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Visitor> call, Throwable t) {
                setInformation("Failed to send request... Maybe timeout ?");
            }
        });
    }

    private void setInformation(String information) {
        TextView txtView = (TextView) ((Activity) context).findViewById(R.id.mainInformation);
        txtView.setText(information);
    }
}