package com.example.flashcardapp;


import java.util.LinkedList;

public class Deck {

    private String deckName;
    private LinkedList<Card> cards;

    public Deck(String name) {
        this.deckName = name;
        this.cards = new LinkedList<>();
    }

    public Deck(String name, LinkedList<Card> cardList) {
        this.deckName = name;
        this.cards = cardList;
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public void removeCard(int index) {
        if (index >= 0 && index < cards.size()) {
            cards.remove(index);
        }
    }

    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }


    public String getDeckName() {
        return deckName;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return deckName;
    }
}
