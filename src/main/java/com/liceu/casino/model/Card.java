package com.liceu.casino.model;

public class Card {
    private Rank rank;
    private Suit suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " de " + suit;
    }
}
