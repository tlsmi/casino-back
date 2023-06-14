package com.liceu.casino.model;

public enum Rank {
    AS(1),
    DOS(2),
    TRES(3),
    CUATRO(4),
    CINCO(5),
    SEIS(6),
    SIETE(7),
    OCHO(8),
    NUEVE(9),
    DIEZ(10),
    J(10),
    Q(10),
    K(10);

    private int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
