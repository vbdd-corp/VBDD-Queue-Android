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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.corp.vbdd.vbdd_queueandroid.R;
import com.corp.vbdd.vbdd_queueandroid.main.models.MyAdapter;
import com.corp.vbdd.vbdd_queueandroid.main.models.Queue;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button nextButton;
    Button prevButton;
    Button connexionButton;
    Button nextPersonBecauseAFK;
    Button logOutBtn;

    TextView mainInformationText;
    TextView queueConnectedText;
    TextView lblRemaining;
    TextView remainConstant;
    EditText queueIdEditText;

    LinearLayout connectedLayout;
    LinearLayout notConnectedLayout;

    RadioButton strategy1Radio;
    RadioButton strategy2Radio;

    RecyclerView recyclerView;

    int queueId;
    int strategyId;

    private MqttAndroidClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.nextBtn);
        prevButton = findViewById(R.id.previousBtn);
        connexionButton = findViewById(R.id.connexionBtn);
        nextPersonBecauseAFK = findViewById(R.id.nextPersonBecauseAFK);
        logOutBtn = findViewById(R.id.logOutBtn);

        strategy1Radio = findViewById(R.id.strategy1);
        strategy2Radio = findViewById(R.id.strategy2);

        queueIdEditText = findViewById(R.id.queueId);
        mainInformationText = findViewById(R.id.mainInformation);
        queueConnectedText = findViewById(R.id.queueConnectedText);
        lblRemaining = findViewById(R.id.lblRemaining);
        remainConstant = findViewById(R.id.textView);
        connectedLayout = findViewById(R.id.connectedLayout);
        notConnectedLayout = findViewById(R.id.notConnectedLayout);

        logOutBtn.setOnClickListener(click -> logOut());

        strategy1Radio.setOnClickListener(click -> this.strategyId = 1);
        strategy2Radio.setOnClickListener(click -> this.strategyId = 2);

        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // restHandler.getQueuesList(); // will call fillRecyclerView after back's response

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!client.isConnected())
            MQTTConnect();
    }

    public void fillRecyclerView(List<Queue> queueArray) {
        MyAdapter adapter = new MyAdapter(queueArray);
        recyclerView.setAdapter(adapter);
    }

    private void MQTTConnect() {

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://localhost:1883", clientId);
        System.out.println("INITIALIZE");
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    try {
                        subscribeSuccess();
                        client.subscribe("/log-in-queue", 2);
                        client.subscribe("/display-visitor", 2);
                        client.subscribe("/display-remaining-visitor", 2);
                        client.subscribe("/display-queue-list", 2);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                    client.setCallback(new MqttCallback() {
                        @Override
                        public void connectionLost(Throwable throwable) {
                            System.out.println("CONNECTION LOST");
                        }

                        @Override
                        public void messageArrived(String topic, MqttMessage mqttMessage) {
                            if(topic.equals("success")){
                                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                                System.out.println("received : " + mqttMessage.toString());
                            }
                            else if(topic.equals("/log-in-queue")){
                                Toast.makeText(getApplicationContext(), "Log in", Toast.LENGTH_SHORT).show();
                                logInQueue(Integer.parseInt(mqttMessage.toString()));
                            }
                            else if(topic.equals("/display-visitor")){
                                Toast.makeText(getApplicationContext(), "Display visitor", Toast.LENGTH_SHORT).show();
                                if(mqttMessage.toString().isEmpty()){
                                    mainInformationText.setText("No student left in queue !");
                                }
                                else {
                                    mainInformationText.setText(mqttMessage.toString());
                                }
                            }
                            else if(topic.equals("/display-remaining-visitor")){
                                setRemainingPersons(Integer.parseInt(mqttMessage.toString()));
                            }
                            else if(topic.equals("/display-queue-list")){
                                Toast.makeText(getApplicationContext(), "display queue list", Toast.LENGTH_SHORT).show();
                                List<Queue> queues = getQueueListFromJSON(mqttMessage.toString());
                                fillRecyclerView(queues);
                            }
                        }

                        //TODO : griser bouton next lorsque plus d'étudiant et griser bouton previous quand personne avant.

                        @Override
                        public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                        }
                    });

                    System.out.println("CONNECTION SUCCESS");
                    Toast.makeText(getApplicationContext(), "CONNECTED TO BROKER !", Toast.LENGTH_SHORT).show();

                    connexionButton.setOnClickListener(e -> {
                        sendMqttRequest("/login", "{ \"queueId\" : \""+queueIdEditText.getText().toString()+"\" }");
                    });
                    nextButton.setOnClickListener(e -> {
                        sendMqttRequest("/queues/next-visitor", "{ \"queueId\" : \""+queueId+"\" , " +
                                " \"strategyId\" : \""+strategyId+"\"}");
                    });
                    prevButton.setOnClickListener(e -> {
                        sendMqttRequest("/queues/previous-visitor", "{ \"queueId\" : \""+queueId+"\"}");
                    });
                    nextPersonBecauseAFK.setOnClickListener(e -> {
                        sendMqttRequest("/queues/absent-visitor", "{ \"queueId\" : \""+queueId+"\"}");
                    });

                    sendMqttRequest("/queues/refresh-queue-list", "");

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getApplicationContext(), "DEAD DEAD", Toast.LENGTH_SHORT).show();
                    System.out.println("CONNECTION FAILURE");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private List<Queue> getQueueListFromJSON(String str) {
        List<Queue> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(str);
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    Queue queue = new Queue();
                    JSONObject object = new JSONObject(jsonArray.get(i).toString());
                    queue.setId(object.getInt("id"));
                    queue.setCurrentIndex(object.getInt("currentIndex"));

                    List<Integer> visitorsIds = getVisitorsIdsListFromJSONObject(object);
                    queue.setVisitorsIds(visitorsIds);

                    list.add(queue);
                }
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return list;
    }

    private List<Integer> getVisitorsIdsListFromJSONObject(JSONObject object) throws JSONException {
        JSONArray visitorsIdsJSON = object.getJSONArray("visitorsIds");
        List<Integer> visitorsIds = new ArrayList<>();
        if (visitorsIdsJSON != null) {
            int visitorsIdsLen = visitorsIdsJSON.length();
            for (int j=0;j<visitorsIdsLen;j++){
                visitorsIds.add(Integer.parseInt(visitorsIdsJSON.get(j).toString()));
            }
        }
        return visitorsIds;
    }

    public void sendMqttRequest(String topic, String msg){
        try {
            client.publish(topic, new MqttMessage(msg.getBytes()));
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        connexionButton.setVisibility(View.VISIBLE);
        connectedLayout.setVisibility(View.INVISIBLE);

        strategy1Radio.setChecked(true);
        strategyId = 1;

        remainConstant.setVisibility(View.INVISIBLE);
        lblRemaining.setVisibility(View.INVISIBLE);


        nextButton.setEnabled(false);
        prevButton.setEnabled(false);
        nextPersonBecauseAFK.setEnabled(false);
        // this.restHandler.updateRemainingPersons(queueId);

        MQTTConnect();

    }

    public void subscribeSuccess() throws MqttException {
        IMqttToken token = client.subscribe("success", 2);
        token.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                System.out.println("Subscribe Successfully " + "success");
            }
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                System.out.println("Subscribe Failed " + "success");
            }
        });
    }

    public void subscribeLogToQueue() throws MqttException {
        client.subscribe("success", 2);
    }


    public void logInQueue(int queueId) {
        this.queueId = queueId;

        connectedLayout.setVisibility(View.VISIBLE);
        notConnectedLayout.setVisibility(View.INVISIBLE);

        queueConnectedText.setText(getResources().getString(R.string.queue_connected, queueId));

        nextButton.setEnabled(true);
        prevButton.setEnabled(true);
        nextPersonBecauseAFK.setEnabled(true);
        remainConstant.setVisibility(View.VISIBLE);
        lblRemaining.setVisibility(View.VISIBLE);
        // hide keyboard
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    private void logOut() {
        connectedLayout.setVisibility(View.INVISIBLE);
        notConnectedLayout.setVisibility(View.VISIBLE);
        remainConstant.setVisibility(View.INVISIBLE);
        lblRemaining.setVisibility(View.INVISIBLE);
        nextButton.setEnabled(false);
        prevButton.setEnabled(false);

        nextPersonBecauseAFK.setEnabled(false);
    }

    public void setRemainingPersons(Integer body) {
        lblRemaining.setText(String.valueOf(body));
    }
}
