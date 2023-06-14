package com.liceu.casino.model;

public class Card {
    private Rank rank;
    private Suit suit;
    private int score;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
        this.score = rank.getValue();
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getPalo() {
        return suit;
    }

    public int geetScore() {
        return score;
    }

    @Override
    public String toString() {
        return rank + " de " + suit;
    }
}
