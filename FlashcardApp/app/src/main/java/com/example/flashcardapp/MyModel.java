package com.example.flashcardapp;

import android.util.Log;

import java.util.LinkedList;

public class MyModel {

    private LinkedList<Deck> decks;

    public MyModel(LinkedList<Deck> decks) {
        this.decks = decks;
    }

    public MyModel() {
        this.decks = new LinkedList<>();
    }

    public void addDeck(Deck d) {
        decks.add(d);
    }

    public Deck getDeck(String name) {
        for (Deck d : decks) {
            if (d.getDeckName().equals(name))
                return d;
        }
        return null;
    }

    public LinkedList<Deck> getAllDecks() {
        return decks;
    }


    public void printDecks() {

        for (Deck deck : decks) {
            System.out.println(deck.getDeckName());
            Log.d("----------DECK NAME--------------: ", deck.getDeckName());
            LinkedList<Card> cards = deck.getCards();
            for (Card card : cards) {
                System.out.println("Front: " + card.getFront());
                System.out.println("Back: " + card.getBack());
                System.out.println("---------------------------");
                Log.d("----------FRONT------------------: ", card.getFront());
                Log.d("----------BACK------------------: ", card.getBack());
                Log.d("------------------------------------", "");
            }
        }
    }
}
