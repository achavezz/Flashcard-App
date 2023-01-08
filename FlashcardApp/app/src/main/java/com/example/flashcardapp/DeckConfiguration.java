package com.example.flashcardapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;


import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class DeckConfiguration extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private MyModel model;
    private String currentDeckname;
    private String myDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_configuration);

        requestAppPermissions();

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        // Get the appropriate name of JSON file and directory
        String filename = username + "_model.txt";
        String externalDir = Environment.getExternalStorageDirectory() +"/Documents/";
        myDir = externalDir + filename;
        Log.d("--------------PrintDir","====="+myDir);

        boolean isDirectory = Files.isDirectory(Paths.get(externalDir));
        boolean isWritableDir = Files.isWritable(Paths.get(externalDir));
        boolean isWritable = Files.isWritable(Paths.get(myDir));
        boolean isReadable = Files.isReadable(Paths.get(myDir));
        boolean isRegular = Files.isRegularFile(Paths.get(myDir));

        Log.d("----------------isDirectory------", ""+isDirectory);
        Log.d("----------------isWritable (Directory)------", ""+isWritableDir);
        Log.d("----------------isWritable (File)------", ""+isWritable);

        Log.d("----------------isReadable------", ""+isReadable);
        Log.d("----------------isRegular------", ""+isRegular);
        Log.d("----------------has READ PERMISSIONS------", ""+hasReadPermissions());

//        try {
//            Files.deleteIfExists(Paths.get(myDir));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Create file if it doesn't exist yet
        if (!isWritable && !isReadable) {
            writeNewFile(myDir);
        }

        model = readFile(myDir);
        LinkedList<Deck> decks = model.getAllDecks();
        Spinner spinner = findViewById(R.id.spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(DeckConfiguration.this);
        }

        ArrayAdapter<Deck> spinnerArrayAdapter =
                new ArrayAdapter<Deck>(DeckConfiguration.this,android.R.layout.simple_spinner_dropdown_item, decks);
        spinner.setAdapter(spinnerArrayAdapter);


        Button b = findViewById(R.id.nextButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(DeckConfiguration.this, Study.class);
                dash.putExtra("username", username);
                dash.putExtra("directory", myDir);
                dash.putExtra("deck", spinner.getSelectedItem().toString());
                startActivity(dash);
            }
        });

        Button add = findViewById(R.id.addButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentDeckname = spinner.getSelectedItem().toString();
                loadFragment(new AddCardFragment());
            }
        });

        Button remove = findViewById(R.id.removeButton);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new RemoveCardFragment());
            }
        });

        Button back = findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dash = new Intent(DeckConfiguration.this, HomePage.class);
                dash.putExtra("username", username);
                startActivity(dash);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        currentDeckname = adapterView.getItemAtPosition(i).toString();

        displayCards(currentDeckname);

    }

    public void displayCards(String deckName) {
        Deck curDeck = model.getDeck(deckName);
        LinearLayout ll = findViewById(R.id.scrollLayout);
        ll.removeAllViews();

        int index = 1;
        // Display all the cards from the currently selected Deck
        for (Card c : curDeck.getCards()) {

            TextView front = new TextView(this);
            front.setText(index + ". " + c.getFront());
            front.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            front.setWidth(dpToPx(120));
            front.setTextSize(18);

            TextView back = new TextView(this);
            back.setText(c.getBack());
            back.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            back.setWidth(dpToPx(260));
            back.setTextSize(18);

            // A horizontal linear layout to show the front and back together in one line
            LinearLayout cardLayout = new LinearLayout(this);
            cardLayout.addView(front);
            cardLayout.addView(back);
            ll.addView(cardLayout);

            index++;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0){

    }

    private int dpToPx(int dp) {
        float density = this.getResources().getDisplayMetrics().density;

        return Math.round((float) dp * density);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }


    private void requestAppPermissions() {
        if (hasReadPermissions() && hasWritePermissions()) {
//        if (hasWritePermissions()) {

            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 0);
    }

    public void loadFragment(Fragment f) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.replace(R.id.framelayout, f);
        ft.commit();
    }

    public void addCardToCurrentDeck(Card c) {
        model.getDeck(currentDeckname).addCard(c);
        writeFile(myDir);
        displayCards(currentDeckname);
    }

    public void removeCardFromCurrentDeck(int id) {
        model.getDeck(currentDeckname).removeCard(id-1);
        writeFile(myDir);
        displayCards(currentDeckname);
    }

    public static MyModel readFile(String directory) {
        try {
            Gson gson = new Gson();

            Log.d("------------------READING FILE------------------", "");
            Reader reader = Files.newBufferedReader(Paths.get(directory));

            Log.d("------------------MY MODEL------------------", "");
            MyModel model = gson.fromJson(reader, MyModel.class);
            reader.close();

            return model;

        } catch (Exception ex) {
            Log.d("------------------ READ EXCEPTION------------------", ex.toString());
            ex.printStackTrace();
        }

        return null;
    }

    public void writeFile(String directory) {
        try {

            Gson gson = new Gson();
            Writer writer = Files.newBufferedWriter(Paths.get(directory));
            gson.toJson(model, writer);
            writer.close();

        } catch (Exception ex) {
            Log.d("------------------WRITE EXCEPTION------------------", ex.toString());
            ex.printStackTrace();
        }
    }

    public void writeNewFile(String directory) {
        try {

            Deck d1 = new Deck("Animal Set");
            d1.addCard(new Card("cat", "a cat is an animal"));
            d1.addCard(new Card("dog", "a dog is also an animal"));
            d1.addCard(new Card("dolphin", "a dolphin is also an animal"));
            d1.addCard(new Card("bear", "a bear is also an animal"));
            d1.addCard(new Card("wolf", "a wolf is also an animal"));
            d1.addCard(new Card("whale", "a whale is also an animal"));
            d1.addCard(new Card("fox", "a fox is also an animal"));
            d1.addCard(new Card("frog", "a frog is also an animal"));
            d1.addCard(new Card("elephant", "an elephant is also an animal"));
            d1.addCard(new Card("caterpillar", "a caterpillar is also an animal"));
            d1.addCard(new Card("lion", "a lion is also an animal"));
            d1.addCard(new Card("crab", "a crab is also an animal"));


            Deck d2 = new Deck("Vehicle Set");
            d2.addCard(new Card("car", "terrestrial four-wheeled vehicle"));
            d2.addCard(new Card("airplane", "a winged-flying vehicle"));
            d2.addCard(new Card("motorcycle", "a two-wheeled vehicle"));
            d2.addCard(new Card("boat", "a water vehicle"));

            MyModel m = new MyModel();
            m.addDeck(d1);
            m.addDeck(d2);

            Gson gson = new Gson();
            Writer writer = Files.newBufferedWriter(Paths.get(directory));
            gson.toJson(m, writer);
            writer.close();

        } catch (Exception ex) {
            Log.d("------------------WRITE EXCEPTION------------------", ex.toString());
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }
}