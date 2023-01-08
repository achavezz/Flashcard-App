package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FinalScore extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        Intent intent = getIntent();
        String user = intent.getStringExtra("username");
        int countC = intent.getIntExtra("countCorrect", 0);
        int countI = intent.getIntExtra("countIncorrect", 0);

        TextView txtC = findViewById(R.id.fsC);
        TextView txtI = findViewById(R.id.fsI);

        txtC.setText(""+countC);
        txtI.setText(""+countI);

        Button b = findViewById(R.id.fsHome);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(FinalScore.this, HomePage.class);
                dash.putExtra("username", user);
                startActivity(dash);
            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }
}