package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Calendar;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Study extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private MyModel model;
    private int countCorrect;
    private int countIncorrect;
    private int index;
    private int curIdx;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        Intent intent = getIntent();
        user = intent.getStringExtra("username");
        String myDir = intent.getStringExtra("directory");
        String deck = intent.getStringExtra("deck");

        String[] timeIntervals = {"5", "10", "15"};
        Spinner spinner = findViewById(R.id.timeSpinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(Study.this);
        }

        ArrayAdapter<String> spinnerArrayAdapter =
                new ArrayAdapter<String>(Study.this,android.R.layout.simple_spinner_dropdown_item, timeIntervals);
        spinner.setAdapter(spinnerArrayAdapter);

        Button start = findViewById(R.id.start);
        Button flip = findViewById(R.id.flip);
        Button skip = findViewById(R.id.skip);
        Button correct = findViewById(R.id.correct);
        Button incorrect = findViewById(R.id.incorrect);
        Button end = findViewById(R.id.end);
        Button back = findViewById(R.id.sBack);
        TextView card = findViewById(R.id.card);

        model = DeckConfiguration.readFile(myDir);
        LinkedList<Card> c = model.getDeck(deck).getCards();


        index = 0;
        curIdx = index;
        card.setText(c.get(curIdx).getFront());

        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Card current = c.get(curIdx);
                if (card.getText().toString().equals(current.getFront())){ //if the current displayed card is the same as the back of the current card already
                    card.setText(current.getBack()); //set the front of the current card
                }else{
                    card.setText(current.getFront()); //set the back of the current card
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if(index < c.size()){
                    curIdx = index;
                    card.setText(c.get(index).getFront());
                }else{
                    Toast.makeText(getApplicationContext(), "You've reached the end of the deck.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index < c.size()) {
                    countCorrect++;
                }
                index++;
                if(index < c.size()){
                    curIdx = index;
                    card.setText(c.get(index).getFront());
                }else{
                    Toast.makeText(getApplicationContext(), "You've reached the end of the deck.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        incorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index < c.size()){
                    countIncorrect++;
                }
                index++;
                if(index < c.size()){
                    curIdx = index;
                    card.setText(c.get(index).getFront());
                }else{
                    Toast.makeText(getApplicationContext(), "You've reached the end of the deck.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int interval = Integer.parseInt(spinner.getSelectedItem().toString());
                start_Sensing_new(0, interval*60*1000);
                index = 0;
                curIdx = index;
                card.setText(c.get(index).getFront());
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm(0);
                //loadFragment(new Scoring());
                Intent dash = new Intent(Study.this, FinalScore.class);
                dash.putExtra("username", user);
                dash.putExtra("countCorrect", countCorrect);
                dash.putExtra("countIncorrect", countIncorrect);
                startActivity(dash);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelAlarm(0);
                Intent dash = new Intent(Study.this, DeckConfiguration.class);
                dash.putExtra("username", user);
                startActivity(dash);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String spinner_item = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0){

    }

    public void start_Sensing_new(int code, int time) {
        Calendar calendar = Calendar.getInstance();
        long currentTime = System.currentTimeMillis();
        calendar.setTimeInMillis(currentTime);
        AlarmManager alarmMgr = (AlarmManager)getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), sendNotification.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long sum = calendar.getTimeInMillis() + time;
        //Log.d("==============Get current time=========", ""+ calendar.getTimeInMillis());
        //Log.d("==============Get added time=========", ""+ sum);
        //Log.d("==============Get current time=========", ""+ sum);
        alarmMgr.setExact(AlarmManager.RTC_WAKEUP, sum, alarmIntent);

        Toast.makeText(this, "Sensing alert alarm has been created. This alarm will send to a broadcast start sensing receiver.", Toast.LENGTH_LONG).show();
    }

    public void cancelAlarm(int code) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), sendNotification.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(getApplicationContext(), code, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "Alarm has been cancelled.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}