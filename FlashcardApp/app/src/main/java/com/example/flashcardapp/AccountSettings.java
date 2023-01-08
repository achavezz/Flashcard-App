package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");
        TextView banner = findViewById(R.id.asBanner);
        banner.setText("Username: " + user);

        DBUserInfo db = new DBUserInfo(this, "USER");
        String name = db.selectQuery("name", "username= \"" +user + "\"");
        String age  = db.selectQuery("age", "username= \"" +user + "\"");
        String gender  = db.selectQuery("gender", "username= \"" +user + "\"");
        TextView banner3 = findViewById(R.id.asBanner3);
        banner3.setText("Name: " + name);
        TextView banner4 = findViewById(R.id.asBanner4);
        banner4.setText("Age: " + age);
        TextView banner5 = findViewById(R.id.asBanner5);
        banner5.setText("Gender: " + gender);


        Button b1 = findViewById(R.id.asButton1);
        Button b2 = findViewById(R.id.asButton2);
        Button b3 = findViewById(R.id.asButton3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(AccountSettings.this, HomePage.class);
                dash.putExtra("username", user);
                startActivity(dash);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new UpdateFragment(), 1);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { loadFragment(new DeleteFragment(), 2);}
        });
    }

    public void loadFragment(Fragment f, int i) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (i == 1) {
            ft.replace(R.id.frmlayout, f);
        } else {
            ft.replace(R.id.frmlayout2, f);
        }
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        return;
    }
}