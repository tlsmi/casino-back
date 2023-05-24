package com.liceu.casino.model;

public enum Suit {
    PICAS("Picas"),
    DIAMANTES("Diamantes"),
    CORAZONES("Corazones"),
    TREBOLES("Treboles");

    private String name;

    Suit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
