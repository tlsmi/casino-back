package com.liceu.casino.model;

public class Card {
    private Rank rank;
    private Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getPalo() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " de " + suit;
    }
}
