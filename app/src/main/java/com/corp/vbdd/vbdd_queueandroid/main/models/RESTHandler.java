package com.corp.vbdd.vbdd_queueandroid.main.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.corp.vbdd.vbdd_queueandroid.R;
import com.corp.vbdd.vbdd_queueandroid.main.activities.MainActivity;

import java.io.IOException;
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
    private int remaining;

    public RESTHandler(Context context) {

        this.context = context;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(__BACK_URL__)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.queueService = retrofit.create(QueueService.class);
    }

    public void getQueue(Integer queueId) {
        Call<Queue> call = queueService.getQueue(queueId);
        call.enqueue(new Callback<Queue>() {

            @Override
            public void onResponse(Call<Queue> call, Response<Queue> response) {
                if (response.code() == 404) {
                    setInformation("This queue does not exist !");
                } else {
                    setInformation("");
                    ((MainActivity) context).logInSucess();
                }
            }

            @Override
            public void onFailure(Call<Queue> call, Throwable t) {
                setInformation("Failed to send request... Maybe timeout ? (getQueue())");
            }
        });
    }

    public void nextPerson(Integer queueId, Integer strategyId) {
        Call<Visitor> call = queueService.nextPerson(queueId, strategyId);
        call.enqueue(new Callback<Visitor>() {

            @Override
            public void onResponse(Call<Visitor> call, Response<Visitor> response) {
                if (response.code() == 404) {
                    setInformation("There is nobody left in the queue");
                } else {
                    setInformation("Nexted ! The next person's id is : " + response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Visitor> call, Throwable t) {
                setInformation("Failed to send request... Maybe timeout ? (nextPerson())");
            }
        });
    }

    public void getQueuesList() {
        Call<List<Queue>> call = queueService.getQueuesList();
        call.enqueue(new Callback<List<Queue>>() {

            @Override
            public void onResponse(Call<List<Queue>> call, Response<List<Queue>> response) {
                if (response.body() != null) {
                    ((MainActivity) context).fillRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Queue>> call, Throwable t) {
                setInformation("Failed to send request... Maybe timeout ?  (getQueuesList())");
            }
        });
    }

    public void previousPerson(Integer queueId) {
        Call<Visitor> call = queueService.previousPerson(queueId);
        call.enqueue(new Callback<Visitor>() {

            @Override
            public void onResponse(Call<Visitor> call, Response<Visitor> response) {
                if (response.code() == 404) {
                    setInformation("There is nobody left in the queue");
                } else {
                    setInformation("Previoused ! The previous person's id was (and is) : " + response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Visitor> call, Throwable t) {
                setInformation("Failed to send request... Maybe timeout ?  (previousPerson())");
            }
        });
    }

    public void absentPerson(int queueId) {
        Call<Visitor> call = queueService.previousPerson(queueId);
        call.enqueue(new Callback<Visitor>() {

            @Override
            public void onResponse(Call<Visitor> call, Response<Visitor> response) {
                if (response.code() == 404) {
                    setInformation("There is nobody left in the queue");
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

    public int getNumberPersonsRemaining(int queueId) {
        Call<Integer> call = queueService.getRemainingPerson(queueId);
        call.enqueue(new Callback<Integer>() {

            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 404) {
                    remaining = 0;
                } else {
                    remaining = response.body();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                setInformation("Failed to send request... Maybe timeout ?  (previousPerson())");
            }
        });

        return remaining;

    }
}