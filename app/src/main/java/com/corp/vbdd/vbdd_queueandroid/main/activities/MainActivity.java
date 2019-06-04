package com.corp.vbdd.vbdd_queueandroid.main.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.corp.vbdd.vbdd_queueandroid.R;
import com.corp.vbdd.vbdd_queueandroid.main.models.MyAdapter;
import com.corp.vbdd.vbdd_queueandroid.main.models.RESTHandler;

public class MainActivity extends AppCompatActivity {

    RESTHandler restHandler;

    Button nextButton;
    Button prevButton;
    Button connexionButton;
    Button nextPersonBecauseAFK;
    Button logOutBtn;

    TextView mainInformationText;
    TextView queueConnectedText;
    TextView lblRemaining;
    EditText queueIdEditText;

    LinearLayout connectedLayout;

    int queueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.nextBtn);
        prevButton = findViewById(R.id.previousBtn);
        connexionButton = findViewById(R.id.connexionBtn);
        nextPersonBecauseAFK = findViewById(R.id.nextPersonBecauseAFK);
        logOutBtn = findViewById(R.id.logOutBtn);

        queueIdEditText = findViewById(R.id.queueId);
        mainInformationText = findViewById(R.id.mainInformation);
        queueConnectedText = findViewById(R.id.queueConnectedText);
        lblRemaining = findViewById(R.id.lblRemaining);
        connectedLayout = findViewById(R.id.connectedLayout);

        restHandler = new RESTHandler(MainActivity.this);

        nextButton.setOnClickListener(click -> {
            this.restHandler.nextPerson(queueId);
            lblRemaining.setText(String.valueOf(this.restHandler.getNumberPersonsRemaining(queueId)));
        });

        prevButton.setOnClickListener(click -> {
            this.restHandler.previousPerson(queueId);
            lblRemaining.setText(String.valueOf(this.restHandler.getNumberPersonsRemaining(queueId)));
        });

        nextPersonBecauseAFK.setOnClickListener(click -> {
            this.restHandler.nextPersonBecauseAFK(queueId);
            lblRemaining.setText(String.valueOf(this.restHandler.getNumberPersonsRemaining(queueId)));
        });

        connexionButton.setOnClickListener(click -> logIn());
        logOutBtn.setOnClickListener(click -> logOut());

        RecyclerView recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyAdapter adapter = new MyAdapter(restHandler.getQueuesList());
        recyclerView.setAdapter(adapter);

        initialize();
    }

    private void initialize() {
        connexionButton.setVisibility(View.VISIBLE);
        connectedLayout.setVisibility(View.INVISIBLE);

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextPersonBecauseAFK.setEnabled(false);
        lblRemaining.setText(String.valueOf(this.restHandler.getNumberPersonsRemaining(queueId)));
    }

    private void logIn() {

        // TODO: Verify if the queue exist in back-end

        queueId = Integer.valueOf(queueIdEditText.getText().toString());
        this.restHandler.getQueue(queueId);
    }

    public void logInSucess() {
        connectedLayout.setVisibility(View.VISIBLE);
        mainInformationText.setVisibility(View.VISIBLE);
        connexionButton.setVisibility(View.INVISIBLE);
        queueIdEditText.setVisibility(View.INVISIBLE);

        queueConnectedText.setText(getResources().getString(R.string.queue_connected, queueId));

        nextButton.setEnabled(true);
        prevButton.setEnabled(true);
        nextPersonBecauseAFK.setEnabled(true);

        // hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    private void logOut() {
        connectedLayout.setVisibility(View.INVISIBLE);
        mainInformationText.setVisibility(View.INVISIBLE);
        connexionButton.setVisibility(View.VISIBLE);
        queueIdEditText.setVisibility(View.VISIBLE);

        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextPersonBecauseAFK.setEnabled(false);
    }

}
