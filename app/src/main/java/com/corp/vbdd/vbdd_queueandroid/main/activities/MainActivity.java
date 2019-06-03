package com.corp.vbdd.vbdd_queueandroid.main.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.corp.vbdd.vbdd_queueandroid.R;
import com.corp.vbdd.vbdd_queueandroid.main.models.RESTHandler;

public class MainActivity extends AppCompatActivity {

    RESTHandler restHandler = new RESTHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button nextButton = findViewById(R.id.nextBtn);
        EditText queueId = findViewById(R.id.queueId);

        nextButton.setOnClickListener(click -> this.restHandler.nextPerson(Integer.valueOf(queueId.getText().toString())));

    }
}
