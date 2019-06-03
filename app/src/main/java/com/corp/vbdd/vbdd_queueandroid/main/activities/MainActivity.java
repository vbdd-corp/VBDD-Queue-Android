package com.corp.vbdd.vbdd_queueandroid.main.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.corp.vbdd.vbdd_queueandroid.R;
import com.corp.vbdd.vbdd_queueandroid.main.models.RESTHandler;

public class MainActivity extends AppCompatActivity {

    RESTHandler restHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextButton = findViewById(R.id.nextBtn);
        Button prevButton = findViewById(R.id.previousBtn);
        EditText queueId = findViewById(R.id.queueId);

        restHandler = new RESTHandler(getApplicationContext());

        nextButton.setOnClickListener(click -> this.restHandler.nextPerson(Integer.valueOf(queueId.getText().toString())));
    }



}
