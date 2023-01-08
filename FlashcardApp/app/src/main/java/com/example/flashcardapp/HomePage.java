package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");
        DBUserInfo db = new DBUserInfo(this, "USER");
        String name = db.selectQuery("name", "username= \"" + user + "\"");
        TextView view = findViewById(R.id.homepageBanner);
        view.setText("Welcome " + name + "!");

        Button b1 = findViewById(R.id.hpButton1);
        Button b2 = findViewById(R.id.hpButton2);
        Button b3 = findViewById(R.id.hpButton3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(HomePage.this, DeckConfiguration.class);
                dash.putExtra("username", user);
                startActivity(dash);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(HomePage.this, AccountSettings.class);
                dash.putExtra("username", user);
                startActivity(dash);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(HomePage.this, MainActivity.class);
                startActivity(dash);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}